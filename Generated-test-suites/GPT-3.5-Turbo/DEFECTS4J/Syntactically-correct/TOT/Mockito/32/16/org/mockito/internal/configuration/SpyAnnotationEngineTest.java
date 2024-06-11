import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor_NullInstance_ReturnsNull() {
        Spy spyAnnotation = Mockito.mock(Spy.class);
        Field field = Mockito.mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(spyAnnotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess_SpyFieldWithNullInstance_ThrowsException() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object() {
            @Spy
            List<String> mockList;
        };

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Exception should be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockList' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_SpyFieldWithExistingMock_ResetsMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object() {
            @Spy
            List<String> mockList = Mockito.mock(LinkedList.class);
        };

        spyAnnotationEngine.process(context, testClass);

        verify(mockList).clear();
    }

    @Test
    public void testProcess_SpyFieldWithoutExistingMock_CreatesSpy() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object() {
            @Spy
            List<String> mockList = new LinkedList<>();
        };

        spyAnnotationEngine.process(context, testClass);

        assertTrue(new MockUtil().isMock(mockList));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_IllegalAccessException_ThrowsException() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object() {
            @Spy
            List<String> mockList = new LinkedList<>();
        };

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mockList");
        when(field.isAccessible()).thenReturn(false);
        when(field.get(testClass)).thenReturn(mockList);
        doThrow(new IllegalAccessException()).when(field).setAccessible(true);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations_FieldWithUndesiredAnnotations_ThrowsException() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("mockList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
        // No exception should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotations_FieldWithDesiredAnnotation_ThrowsException() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("mockList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotations_FieldWithMultipleDesiredAnnotations_ThrowsException() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("mockList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class);
    }

    @Test
    public void testAssertNoAnnotations_FieldWithoutUndesiredAnnotations_NoExceptionThrown() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("mockList");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Captor.class);
        // No exception should be thrown
    }

}