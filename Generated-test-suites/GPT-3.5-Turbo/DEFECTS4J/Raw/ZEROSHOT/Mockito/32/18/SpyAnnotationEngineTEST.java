import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        List<Field> fields = new LinkedList<>();
        fields.add(Mockito.mock(Field.class));
        when(context.getDeclaredFields()).thenReturn(fields.toArray(new Field[0]));

        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (Exception e) {
            fail("Error occurred during processing: " + e.getMessage());
        }
    }

    @Test
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        List<Field> fields = new LinkedList<>();
        Field field = Mockito.mock(Field.class);
        fields.add(field);
        when(context.getDeclaredFields()).thenReturn(fields.toArray(new Field[0]));

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Exception not thrown when instance is null");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("Cannot create a @Spy for '"));
        }
    }

    @Test
    public void testProcess_withMockInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        List<Field> fields = new LinkedList<>();
        Field field = Mockito.mock(Field.class);
        Object instance = Mockito.mock(Object.class);
        fields.add(field);
        when(context.getDeclaredFields()).thenReturn(fields.toArray(new Field[0]));
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        try {
            spyAnnotationEngine.process(context, testClass);
            verify(Mockito.times(1)).reset(instance);
        } catch (Exception e) {
            fail("Error occurred during processing: " + e.getMessage());
        }
    }

    @Test
    public void testProcess_withNonMockInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        List<Field> fields = new LinkedList<>();
        Field field = Mockito.mock(Field.class);
        Object instance = new LinkedList<>();
        fields.add(field);
        when(context.getDeclaredFields()).thenReturn(fields.toArray(new Field[0]));
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        try {
            spyAnnotationEngine.process(context, testClass);
            verify(Mockito.times(1)).spy(instance);
        } catch (Exception e) {
            fail("Error occurred during processing: " + e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> annotation = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);
        Class[] undesiredAnnotations = {Mockito.mock(Class.class)};

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
        verifyZeroInteractions(annotation);
        verifyZeroInteractions(field);
        verifyZeroInteractions(undesiredAnnotations);
    }
}