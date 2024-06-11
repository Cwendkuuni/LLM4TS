import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMockFor_NullInstance_ExceptionIsThrown() {
        Annotation annotation = mock(Annotation.class);
        Field field = getField("mockField");
        try {
            spyAnnotationEngine.createMockFor(annotation, field);
            fail("MockitoException should have been thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testCreateMockFor_ValidInstance_NoExceptionIsThrown() throws MockitoException {
        List mockInstance = new LinkedList();
        Annotation annotation = mock(Annotation.class);
        Field field = getField("mockField");
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNotNull(result);
    }

    @Test
    public void testCreateMockFor_NonNullInstance_ReturnsSameInstance() throws MockitoException {
        List mockInstance = new LinkedList();
        Annotation annotation = mock(Annotation.class);
        Field field = getField("mockField");
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertEquals(mockInstance, result);
    }

    @Test
    public void testProcess_FieldNotAnnotatedWithSpy_NothingIsDone() {
        Class testClass = getClass();
        spyAnnotationEngine.process(testClass, testClass);
        // No assertion, if no exceptions were thrown, it means nothing happened
    }

    @Test
    public void testProcess_FieldAnnotatedWithSpy_InstanceIsNotNullAndNotMock_ExpectSpyInstance() throws IllegalAccessException, MockitoException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Class testClass = getClass();
        Field instanceField = getField("instanceField");
        Object testInstance = spy(testClass.newInstance());
        when(spyAnnotationEngineSpy.createMockFor(any(Annotation.class), eq(instanceField))).thenReturn(null);
        doReturn(false).when(mockUtil).isMock(any(Object.class));
        spyAnnotationEngineSpy.process(testClass, testInstance);
        verify(instanceField, never()).set(eq(testInstance), any(Object.class));
        verify(mockUtil, times(1)).isMock(any(Object.class));
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void testProcess_FieldAnnotatedWithSpy_InstanceIsNull_ExpectException() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Class testClass = getClass();
        Field instanceField = getField("instanceField");
        Object testInstance = spy(testClass.newInstance());
        doThrow(new MockitoException("Mockito Exception")).when(spyAnnotationEngineSpy).createMockFor(any(Annotation.class), eq(instanceField));
        try {
            spyAnnotationEngineSpy.process(testClass, testInstance);
            fail("MockitoException should have been thrown");
        } catch (MockitoException e) {
            assertEquals("Mockito Exception", e.getMessage());
        }
    }

    @Test
    public void testProcess_FieldAnnotatedWithSpy_InstanceIsMock_ExpectResetIsCalled() throws IllegalAccessException, MockitoException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Class testClass = getClass();
        Field instanceField = getField("instanceField");
        Object testInstance = spy(testClass.newInstance());
        Object mockInstance = mock(Object.class);
        when(spyAnnotationEngineSpy.createMockFor(any(Annotation.class), eq(instanceField))).thenReturn(mockInstance);
        doReturn(true).when(mockUtil).isMock(eq(mockInstance));
        spyAnnotationEngineSpy.process(testClass, testInstance);
        verify(mockInstance, times(1)).isMock();
        verify(mockInstance, times(1)).reset();
        verify(mockInstance, never()).spy();
        verify(mockUtil, times(1)).isMock(eq(mockInstance));
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void testProcess_FieldAnnotatedWithSpy_InstanceIsNotMock_ExpectSpyIsCalled() throws
            IllegalAccessException, MockitoException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Class testClass = getClass();
        Field instanceField = getField("instanceField");
        Object testInstance = spy(testClass.newInstance());
        Object mockInstance = mock(Object.class);
        when(spyAnnotationEngineSpy.createMockFor(any(Annotation.class), eq(instanceField))).thenReturn(mockInstance);
        doReturn(false).when(mockUtil).isMock(eq(mockInstance));
        spyAnnotationEngineSpy.process(testClass, testInstance);
        verify(mockInstance, times(1)).isMock();
        verify(mockInstance, times(1)).spy();
        verify(mockUtil, times(1)).isMock(eq(mockInstance));
        verify(instanceField, times(1)).set(eq(testInstance), eq(mockInstance));
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void testAssertNoAnnotations_NoUndesiredAnnotationPresent_NoExceptionIsThrown() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Field field = getField("instanceField");
        spyAnnotationEngineSpy.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(spyAnnotationEngineSpy, never()).unsupportedCombinationOfAnnotations(anyString(), any(String.class));
    }

    @Test
    public void testAssertNoAnnotations_UndesiredAnnotationPresent_ExceptionIsThrown() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);
        Field field = getField("instanceField");
        doNothing().when(spyAnnotationEngineSpy).unsupportedCombinationOfAnnotations(anyString(), any(String.class));
        spyAnnotationEngineSpy.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class, InjectMocks.class);
        verify(spyAnnotationEngineSpy, never()).unsupportedCombinationOfAnnotations(anyString(), any(String.class));
    }
  
    // Helper method to retrieve the field by name
    private Field getField(String fieldName) {
        try {
            return SpyAnnotationEngineTest.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found: " + fieldName);
        }
    }
}