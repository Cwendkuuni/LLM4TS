
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess_withNullInstance() {
        Class<?> context = getClass();
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.getName()).thenReturn("mockList");
        when(field.get(testClass)).thenReturn(null);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockList' field because the *instance* is missing\n" +
                                "The instance must be created *before* initMocks();\n" +
                                "Example of correct usage of @Spy:\n" +
                                "   @Spy List mock = new LinkedList();\n" +
                                "   //also, don't forget about MockitoAnnotations.initMocks();",
                         e.getMessage());
        }
    }

    @Test
    public void testProcess_withMockInstance() {
        Class<?> context = getClass();
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        List<Object> mockList = mock(List.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.getName()).thenReturn("mockList");
        when(field.get(testClass)).thenReturn(mockList);
        when(new MockUtil().isMock(mockList)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(mockList).reset();
    }

    @Test
    public void testProcess_withNonMockInstance() {
        Class<?> context = getClass();
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        List<Object> instance = new LinkedList<>();
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.getName()).thenReturn("mockList");
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        verify(field).set(testClass, Mockito.spy(instance));
    }

    @Test
    public void testAssertNoAnnotations() {
        Class<Annotation> annotation = Annotation.class;
        Field field = mock(Field.class);
        Class<Mock> undesiredAnnotation = Mock.class;

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);

        verifyZeroInteractions(field);
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_withAnnotationPresent() {
        Class<Annotation> annotation = Annotation.class;
        Field field = mock(Field.class);
        Class<Mock> undesiredAnnotation = Mock.class;
        when(field.isAnnotationPresent(undesiredAnnotation)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);
    }

}
