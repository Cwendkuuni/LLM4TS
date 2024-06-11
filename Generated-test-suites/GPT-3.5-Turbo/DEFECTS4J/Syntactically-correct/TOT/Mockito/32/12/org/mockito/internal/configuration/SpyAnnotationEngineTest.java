import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
import java.util.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
    private Object testClass;

    @Test
    public void createMockForShouldReturnNull() {
        Annotation annotation = new SpyAnnotation();
        Field field = createMockField("testField");

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void processShouldThrowExceptionForMissingInstance() {
        Field field = createMockField("testField");
        testClass = new TestClass();

        assertThrows(MockitoException.class, () -> {
            spyAnnotationEngine.process(TestClass.class, testClass);
        });
    }

    @Test
    public void processShouldThrowExceptionForIllegalAccessException() throws NoSuchFieldException {
        Field field = createPrivateField("testField");
        testClass = new TestClass();

        assertThrows(MockitoException.class, () -> {
            spyAnnotationEngine.process(TestClass.class, testClass);
        });
    }

    @Test
    public void processShouldResetMockInstance() {
        Field field = createSpyField("testField");
        testClass = new TestClass();
        List<String> mockInstance = new ArrayList<String>();
        MockUtil mockUtil = new MockUtil();
        Mockito.mock(mockInstance.getClass());

        spyAnnotationEngine.process(TestClass.class, testClass);

        assertTrue(mockUtil.isMock(mockInstance));
    }

    @Test
    public void processShouldSpyInstance() {
        Field field = createSpyField("testField");
        testClass = new TestClass();
        List<String> spyInstance = new ArrayList<String>();

        spyAnnotationEngine.process(TestClass.class, testClass);

        assertEquals(spyInstance, testClass.testField);
    }

    private Field createMockField(String fieldName) {
        Field field = null;
        try {
            field = TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }

    private Field createSpyField(String fieldName) {
        Field field = null;
        try {
            field = TestClass.class.getDeclaredField(fieldName);
            field.set(testClass, new ArrayList<String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return field;
    }

    private Field createPrivateField(String fieldName) {
        Field field = null;
        try {
            field = TestClass.class.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }

    private class TestClass {
        @Spy
        List<String> testField;
    }

    private class SpyAnnotation implements Annotation {

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}