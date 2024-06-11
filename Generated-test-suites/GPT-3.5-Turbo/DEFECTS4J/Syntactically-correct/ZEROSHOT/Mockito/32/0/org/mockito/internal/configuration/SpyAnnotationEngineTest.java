import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private Object testClass;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        testClass = new Object();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Field field = mock(Field.class);
        field.setAccessible(true);
        when(field.get(testClass)).thenReturn(null);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        verify(field).get(testClass);
        verify(field).getName();
        verify(field).setAccessible(true);
        verify(field).isAccessible();
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testProcess_InstanceIsNull() throws IllegalAccessException {
        Field field = mock(Field.class);
        field.setAccessible(true);
        when(field.get(testClass)).thenReturn(null);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        try {
            spyAnnotationEngine.process(testClass.getClass(), testClass);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }

        verify(field).get(testClass);
        verify(field).getName();
        verify(field).setAccessible(true);
        verify(field).isAccessible();
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testProcess_InstanceIsMock() throws IllegalAccessException {
        Field field = mock(Field.class);
        field.setAccessible(true);
        List<Object> mockList = mock(LinkedList.class);
        when(field.get(testClass)).thenReturn(mockList);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        when(new MockUtil().isMock(mockList)).thenReturn(true);

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        verify(field).get(testClass);
        verify(field).getName();
        verify(field).setAccessible(true);
        verify(mockList).getClass();
        verify(new MockUtil()).isMock(mockList);
        verifyNoMoreInteractions(field, mockList);
    }

    @Test
    public void testProcess_InstanceIsNotMock() throws IllegalAccessException {
        Field field = mock(Field.class);
        field.setAccessible(true);
        List<Object> mockList = mock(LinkedList.class);
        when(field.get(testClass)).thenReturn(mockList);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        when(new MockUtil().isMock(mockList)).thenReturn(false);

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        verify(field).get(testClass);
        verify(field).getName();
        verify(field).setAccessible(true);
        verify(mockList).getClass();
        verify(new MockUtil()).isMock(mockList);
        verify(field).set(testClass, Mockito.spy(mockList));
        verifyNoMoreInteractions(field, mockList);
    }

    @Test
    public void testAssertNoAnnotations() throws IllegalAccessException {
        Field field = mock(Field.class);
        @Mock
        Annotation mockAnnotation = new Mock() {
            @Override
            public String value() {
                return "";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Mock.class;
            }
        };
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        doNothing().when(new Reporter()).unsupportedCombinationOfAnnotations("Spy", "Annotation");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);

        verify(field).isAnnotationPresent(Mock.class);
        verify(new Reporter()).unsupportedCombinationOfAnnotations("Spy", "Annotation");
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations_NoAnnotations() throws IllegalAccessException {
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(false);

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);

        verify(field).isAnnotationPresent(Mock.class);
        verifyNoMoreInteractions(field);
    }
}