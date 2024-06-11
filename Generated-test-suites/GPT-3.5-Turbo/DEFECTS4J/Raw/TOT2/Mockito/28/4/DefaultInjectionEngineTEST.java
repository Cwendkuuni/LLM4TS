import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.util.reflection.FieldInitializer;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields_NullInjectMocksFields_InjectMocksFieldsNullPointerException() {
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();
        
        try {
            engine.injectMocksOnFields(null, mocks, testClassInstance);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Fields annotated by @InjectMocks cannot be null", e.getMessage());
        }
    }

    @Test
    public void testInjectMocksOnFields_NullMocks_MocksNullPointerException() {
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        Object testClassInstance = new Object();
        
        try {
            engine.injectMocksOnFields(injectMocksFields, null, testClassInstance);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Mocks cannot be null", e.getMessage());
        }
    }

    @Test
    public void testInjectMocksOnFields_NullTestClassInstance_TestClassInstanceNullPointerException() {
        DefaultInjectionEngine engine = new DefaultInjectionEngine();
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        
        try {
            engine.injectMocksOnFields(injectMocksFields, mocks, null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("Test class instance cannot be null", e.getMessage());
        }
    }

    // Additional test cases for other public methods in DefaultInjectionEngine class can be added here

}