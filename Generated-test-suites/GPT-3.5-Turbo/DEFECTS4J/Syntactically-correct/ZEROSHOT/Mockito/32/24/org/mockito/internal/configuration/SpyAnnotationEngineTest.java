import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testProcess() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field field1 = mock(Field.class);
        when(field1.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field1.isAccessible()).thenReturn(true);
        when(field1.get(testClass)).thenReturn(null);
        when(field1.getName()).thenReturn("field1");

        Field field2 = mock(Field.class);
        when(field2.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field2.isAccessible()).thenReturn(true);
        when(field2.get(testClass)).thenReturn(new LinkedList());
        when(field2.getName()).thenReturn("field2");

        Field field3 = mock(Field.class);
        when(field3.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field3.isAccessible()).thenReturn(true);
        when(field3.get(testClass)).thenReturn(mock(MockUtil.class));
        when(field3.getName()).thenReturn("field3");

        Field field4 = mock(Field.class);
        when(field4.isAnnotationPresent(Spy.class)).thenReturn(false);

        Field[] fields = new Field[]{field1, field2, field3, field4};
        when(context.getDeclaredFields()).thenReturn(fields);

        MockitoAnnotations.initMocks(testClass);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);

        verify(field1, times(1)).isAnnotationPresent(Spy.class);
        verify(field1, times(1)).isAccessible();
        verify(field1, times(1)).get(testClass);
        verify(field1, times(1)).getName();

        verify(field2, times(1)).isAnnotationPresent(Spy.class);
        verify(field2, times(1)).isAccessible();
        verify(field2, times(1)).get(testClass);
        verify(field2, times(1)).getName();

        verify(field3, times(1)).isAnnotationPresent(Spy.class);
        verify(field3, times(1)).isAccessible();
        verify(field3, times(1)).get(testClass);
        verify(field3, times(1)).getName();

        verify(field4, times(1)).isAnnotationPresent(Spy.class);

        verifyNoMoreInteractions(field1, field2, field3, field4);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("context");
        Annotation annotation = field.getAnnotation(Spy.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.assertNoAnnotations(annotation.getClass(), field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected MockitoException is not thrown");
        } catch (MockitoException e) {
            assertEquals("Unsupported combination of annotations: Spy, Spy", e.getMessage());
        }
    }

}