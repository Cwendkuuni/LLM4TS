import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        Assert.assertNull(result);
    }

    @Test
    public void testProcess() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = new Field[1];
        fields[0] = Mockito.mock(Field.class);
        field.set(context, fields);

        Field annotatedField = fields[0];
        Mockito.when(annotatedField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(annotatedField.isAccessible()).thenReturn(true);
        Mockito.when(annotatedField.getName()).thenReturn("testField");
        Object instance = new LinkedList<>();
        annotatedField.set(testClass, instance);
        Mockito.when(annotatedField.get(testClass)).thenReturn(instance);

        Field mockUtilField = context.getDeclaredField("mockUtil");
        mockUtilField.setAccessible(true);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        mockUtilField.set(context, mockUtil);
        Mockito.when(mockUtil.isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(annotatedField, Mockito.times(1)).isAnnotationPresent(Spy.class);
        Mockito.verify(annotatedField, Mockito.times(1)).isAccessible();
        Mockito.verify(annotatedField, Mockito.times(1)).getName();
        Mockito.verify(annotatedField, Mockito.times(1)).setAccessible(true);
        Mockito.verify(annotatedField, Mockito.times(1)).get(testClass);
        Mockito.verify(mockUtil, Mockito.times(1)).isMock(instance);
        Mockito.verify(annotatedField, Mockito.times(1)).set(testClass, Mockito.spy(instance));
    }

    @Test
    public void testProcess_NullInstance() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = new Field[1];
        fields[0] = Mockito.mock(Field.class);
        field.set(context, fields);

        Field annotatedField = fields[0];
        Mockito.when(annotatedField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(annotatedField.isAccessible()).thenReturn(true);
        Mockito.when(annotatedField.getName()).thenReturn("testField");
        annotatedField.set(testClass, null);

        try {
            spyAnnotationEngine.process(context, testClass);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'testField' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }

        Mockito.verify(annotatedField, Mockito.times(1)).isAnnotationPresent(Spy.class);
        Mockito.verify(annotatedField, Mockito.times(1)).isAccessible();
        Mockito.verify(annotatedField, Mockito.times(1)).getName();
        Mockito.verify(annotatedField, Mockito.times(1)).setAccessible(true);
        Mockito.verify(annotatedField, Mockito.times(1)).get(testClass);
    }

    @Test
    public void testProcess_InstanceIsMock() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = new Field[1];
        fields[0] = Mockito.mock(Field.class);
        field.set(context, fields);

        Field annotatedField = fields[0];
        Mockito.when(annotatedField.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(annotatedField.isAccessible()).thenReturn(true);
        Mockito.when(annotatedField.getName()).thenReturn("testField");
        Object instance = Mockito.mock(List.class);
        annotatedField.set(testClass, instance);
        Mockito.when(annotatedField.get(testClass)).thenReturn(instance);

        Field mockUtilField = context.getDeclaredField("mockUtil");
        mockUtilField.setAccessible(true);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        mockUtilField.set(context, mockUtil);
        Mockito.when(mockUtil.isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(annotatedField, Mockito.times(1)).isAnnotationPresent(Spy.class);
        Mockito.verify(annotatedField, Mockito.times(1)).isAccessible();
        Mockito.verify(annotatedField, Mockito.times(1)).getName();
        Mockito.verify(annotatedField, Mockito.times(1)).setAccessible(true);
        Mockito.verify(annotatedField, Mockito.times(1)).get(testClass);
        Mockito.verify(mockUtil, Mockito.times(1)).isMock(instance);
        Mockito.verify(instance, Mockito.times(1)).reset();
    }

    @Test
    public void testAssertNoAnnotations() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class annotation = Spy.class;
        Field field = Mockito.mock(Field.class);

        Field[] undesiredAnnotations = new Field[1];
        undesiredAnnotations[0] = Mockito.mock(Field.class);

        Mockito.when(field.isAnnotationPresent(Mockito.any(Class.class))).thenReturn(true);
        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
        Mockito.verify(field, Mockito.times(1)).isAnnotationPresent(Mockito.any(Class.class));
        Mockito.verify(new Reporter(), Mockito.times(1)).unsupportedCombinationOfAnnotations(Mockito.anyString(), Mockito.anyString());
    }
}