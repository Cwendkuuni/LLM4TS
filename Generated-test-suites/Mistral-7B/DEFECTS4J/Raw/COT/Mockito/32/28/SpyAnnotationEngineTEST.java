import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotationAndField() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Assert.assertNull(engine.createMockFor(null, null));
    }

    @Test
    public void testCreateMockForAnnotationAndNullField() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Assert.assertNull(engine.createMockFor(mock(Annotation.class), null));
    }

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Mockito.class);
        Assert.assertNull(engine.createMockFor(null, field));
    }

    @Test
    public void testCreateMockForAnnotationAndField() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Mockito.class);
        Assert.assertNull(engine.createMockFor(mock(Annotation.class), field));
    }

    @Test
    public void testProcessNullContext() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Assert.assertDoesNotThrow(() -> engine.process(null, Mockito.mock(Object.class)));
    }

    @Test
    public void testProcessNullTestClass() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Assert.assertDoesNotThrow(() -> engine.process(context, null));
    }

    @Test
    public void testProcessEmptyFields() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Field[] fields = new Field[0];
        engine.process(context, testClass);

        // No fields, no need to check access
    }

    @Test
    public void testProcessFieldsWithNoSpyAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Field field1 = new Field("field1", String.class, Object.class);
        Field field2 = new Field("field2", Integer.class, Object.class);
        Field[] fields = new Field[]{field1, field2};

        engine.process(context, testClass);

        // Check that fields were not modified
        PowerMockito.verifyNoFieldAccessed(testClass, any(Field.class));
    }

    @Test
    public void testProcessFieldsWithSpyAnnotationAndNullInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Field field = new Field("testField", String.class, Object.class);
        field.setAccessible(true);
        field.set(testClass, null);

        Field[] fields = new Field[]{field};

        engine.process(context, testClass);

        // Instance is null, should not be spied
        PowerMockito.verifyNoFieldAccessed(testClass, any(Field.class));
    }

    @Test
    public void testProcessFieldsWithSpyAnnotationAndMockInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Object mockInstance = mock(String.class);

        Field field = new Field("testField", String.class, Object.class);
        field.setAccessible(true);
        field.set(testClass, mockInstance);

        Field[] fields = new Field[]{field};

        engine.process(context, testClass);

        // Instance is a mock, should be reset
        verify(mockInstance).reset();
    }

    @Test
    public void testProcessFieldsWithSpyAnnotationAndSpyedInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Object spyInstance = spy(mock(String.class));

        Field field = new Field("testField", String.class, Object.class);
        field.setAccessible(true);
        field.set(testClass, spyInstance);

        Field[] fields = new Field[]{field};

        engine.process(context, testClass);

        // Instance is spied, no need to reset
        verifyNoInteractions(spyInstance);
    }

    @Test(expected = MockitoException.class)
    public void testProcessFieldsWithSpyAnnotationAndMissingInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Field field = new Field("testField", String.class, Object.class);

        Field[] fields = new Field[]{field};

        engine.process(context, testClass);

        // Missing instance, should throw exception
        field.set(testClass, null);
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcessFieldsWithSpyAnnotationAndInaccessibleInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        Object testClass = mock(Object.class);

        Field field = new Field("testField", String.class, Object.class);
        field.setAccessible(false);
        field.set(testClass, mock(String.class));

        Field[] fields = new Field[]{field};

        engine.process(context, testClass);

        // Inaccessible instance, should throw exception
    }

    @Test
    public void testAssertNoAnnotationsNullAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Object.class);

        engine.assertNoAnnotations(null, field, Mockito.Mock.class, Captor.class);
    }

    @Test
    public void testAssertNoAnnotationsSpyAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Object.class);
        field.setAnnotation(new Spy());

        try {
            engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class);
            Assert.fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testAssertNoAnnotationsMockAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Object.class);
        field.setAnnotation(Mockito.Mock.class);

        try {
            engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class);
            Assert.fail();
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testAssertNoAnnotationsCaptorAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = new Field("testField", String.class, Object.class);
        field.setAnnotation(Captor.class);

        try {
            engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class);
            Assert.fail();
        } catch (AssertionError e) {
            // Expected
        }
    }
}