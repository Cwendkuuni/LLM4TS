import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        assertNull(annotationEngine.createMockFor(null, null));
    }

    @Test
    public void testCreateMockForAnnotationPresentField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = new Class() {};
        Field field = PowerMockito.spy(testClass).getClass().getDeclaredField("spiedField");
        PowerMockito.mockStatic(Field.class);
        PowerMockito.whenNew(Field.class, testClass, "spiedField").thenReturn(field);
        PowerMockito.mockStatic(Mockito.class);
        PowerMockito.when(Mockito.class.spy).thenReturn(PowerMockito.mock(Object.class));

        annotationEngine.process(testClass.getClass(), testClass);
        Object instance = field.get(testClass);
        assertNull(instance); // get should return the spied instance

        field.set(testClass, new Object());
        annotationEngine.process(testClass.getClass(), testClass);
        instance = field.get(testClass);
        assertNotNull(instance); // should return the spied instance, but original was null
    }

    @Test
    public void testProcessNullContext() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        assertThrows(NullPointerException.class, () -> annotationEngine.process(null, null));
    }

    @Test
    public void testProcessNullTestClass() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        assertThrows(NullPointerException.class, () -> annotationEngine.process(new Class() {}, null));
    }

    @Test
    public void testProcessAlreadySpyedField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = new Class() {};
        Field field = PowerMockito.spy(testClass).getClass().getDeclaredField("spiedField");
        PowerMockito.mockStatic(Field.class);
        PowerMockito.whenNew(Field.class, testClass, "spiedField").thenReturn(field);
        PowerMockito.mockStatic(Mockito.class);
        PowerMockito.when(Mockito.class.spy).thenReturn(PowerMockito.mock(Object.class));

        annotationEngine.process(testClass.getClass(), testClass);
        annotationEngine.process(testClass.getClass(), testClass);

        Object instance = field.get(testClass);
        assertNotNull(instance); // should return the same spied instance
    }

    @Test(expected = MockitoException.class)
    public void testProcessMissingInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = new Class() {};
        Field field = PowerMockito.spy(testClass).getClass().getDeclaredField("spiedField");
        PowerMockito.mockStatic(Field.class);
        PowerMockito.whenNew(Field.class, testClass, "spiedField").thenReturn(field);

        annotationEngine.process(testClass.getClass(), testClass);
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcessAccessException() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = new Class() {};
        Field field = PowerMockito.mock(Field.class);
        PowerMockito.mockStatic(Field.class);
        PowerMockito.whenNew(Field.class, testClass, "spiedField").thenReturn(field);
        PowerMockito.when(field.isAccessible()).thenReturn(false);

        annotationEngine.process(testClass.getClass(), testClass);
    }

    @Test
    public void testAssertNoAnnotationsNullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = PowerMockito.mock(Field.class);
        annotationEngine.assertNoAnnotations(null, field, Mockito.Mock.class, Captor.class);
    }

    @Test
    public void testAssertNoAnnotationsAnnotationPresent() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = PowerMockito.mock(Field.class);
        PowerMockito.when(field.isAnnotationPresent(Mockito.Mock.class)).thenReturn(true);

        assertThrows(org.mockito.exceptions.base.MockitoException.class, () -> annotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class));
    }
}