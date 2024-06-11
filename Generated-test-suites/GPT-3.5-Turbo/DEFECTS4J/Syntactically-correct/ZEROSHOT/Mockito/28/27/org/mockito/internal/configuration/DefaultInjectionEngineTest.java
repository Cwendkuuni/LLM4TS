import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = new Object();
    }

    @Test
    public void testInjectMocksOnFields() {
        Field field1 = getField("field1");
        Field field2 = getField("field2");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        assertNotNull(field1.get(testClassInstance));
        assertNotNull(field2.get(testClassInstance));
        assertEquals(mock1, field1.get(testClassInstance));
        assertEquals(mock2, field2.get(testClassInstance));
    }

    private Field getField(String fieldName) {
        try {
            return testClassInstance.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

}