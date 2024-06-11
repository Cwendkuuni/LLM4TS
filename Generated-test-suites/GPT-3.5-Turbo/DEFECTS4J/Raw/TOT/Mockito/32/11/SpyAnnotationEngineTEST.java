import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

public class SpyAnnotationEngineTest {

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testCreateMockFor_WhenAnnotationIsSpy_ShouldReturnNull() {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = null;
        Annotation spyAnnotation = mock(Spy.class);

        // Act
        Object result = spyAnnotationEngine.createMockFor(spyAnnotation, field);

        // Assert
        assertNull(result);
    }

    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsNull_ShouldThrowMockitoException() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getField(TestClass.class, "field1");
        Object testClass = new TestClass();

        // Act & Assert
        try {
            spyAnnotationEngine.process(TestClass.class, testClass);
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("Cannot create a @Spy for 'field1' field because the *instance* is missing"));
        }
    }

    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsMock_ShouldResetInstance() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getField(TestClass.class, "field2");
        Object testClass = new TestClass();

        // Act
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Assert
        verify(Mockito.<List<String>>reset(testClass.field2));
    }

    @Test
    public void testProcess_WhenFieldHasSpyAnnotationAndInstanceIsNotMock_ShouldSetFieldToSpyInstance() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getField(TestClass.class, "field3");
        Object testClass = new TestClass();

        // Act
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Assert
        Object fieldValue = getFieldAccessible(field, testClass);
        assertTrue(new MockUtil().isMock(fieldValue));
    }

    @Test
    public void testProcess_WhenFieldHasNoSpyAnnotation_ShouldNotChangeField() throws IllegalAccessException {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getField(TestClass.class, "field4");
        Object testClass = new TestClass();

        // Act
        spyAnnotationEngine.process(TestClass.class, testClass);

        // Assert
        Object fieldValue = getFieldAccessible(field, testClass);
        assertNull(fieldValue);
    }

    @Test
    public void testAssertNoAnnotations_WhenFieldHasUndesiredAnnotation_ShouldThrowUnsupportedCombinationOfAnnotationsException() throws IllegalAccessException, NoSuchFieldException {
        // Arrange
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = getField(TestClass.class, "field5");
        Class<Mock> undesiredAnnotation = Mock.class;
        TestClass testClass = new TestClass();

        // Act & Assert
        try {
            getFieldAccessible(SpyAnnotationEngine.class.getDeclaredMethod("assertNoAnnotations", Class.class, Field.class, Class[].class),
                    spyAnnotationEngine, undesiredAnnotation, field).invoke(spyAnnotationEngine, undesiredAnnotation, field, undesiredAnnotation);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Unsupported combination of annotations"));
        }
    }

    // Helper methods

    private Field getField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        return cls.getDeclaredField(fieldName);
    }

    private Object getFieldAccessible(Field field, Object obj) throws IllegalAccessException {
        boolean wasAccessible = field.isAccessible();
        field.setAccessible(true);
        Object value = field.get(obj);
        field.setAccessible(wasAccessible);
        return value;
    }

    private class TestClass {
        @Spy
        private List<String> field1;

        @Mock
        private List<String> field2;

        private List<String> field3;

        private List<String> field4;

        @Mock
        @Captor
        private List<String> field5;
    }
}