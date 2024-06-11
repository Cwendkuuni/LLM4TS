import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<>();
        Field[] fields = DefaultInjectionEngine.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectMocks.class)) {
                injectMocksFields.add(field);
            }
        }

        Set<Object> mocks = new HashSet<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);

        Object testClassInstance = new Object(); // Create instance of test class

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert functionality of injectMocksOnFields method
        // Add assertions here
    }

    // Add more test cases for other public methods of DefaultInjectionEngine class
    
}