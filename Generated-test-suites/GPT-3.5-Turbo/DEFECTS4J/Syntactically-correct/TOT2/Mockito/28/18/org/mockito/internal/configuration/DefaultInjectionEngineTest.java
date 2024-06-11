import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    
    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() {
        // create mocks
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // create test class instance
        TestClass testInstance = new TestClass();

        // set up test fields
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(TestClass.class.getDeclaredField("field1"));
        injectMocksFields.add(TestClass.class.getDeclaredField("field2"));

        Set<Object> mocks = new HashSet<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // perform injection
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

        // assert mocks injected correctly
        assertEquals(mock1, testInstance.getField1());
        assertEquals(mock2, testInstance.getField2());
    }

    // other test cases for public methods can be added here

    private static class TestClass {
        private Object field1;
        private Object field2;

        public Object getField1() {
            return field1;
        }

        public Object getField2() {
            return field2;
        }
    }

}