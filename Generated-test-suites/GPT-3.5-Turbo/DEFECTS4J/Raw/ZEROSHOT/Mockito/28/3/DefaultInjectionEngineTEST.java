import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

    @Test
    public void testInjectMocksOnFields() {
        DefaultInjectionEngine engine = new DefaultInjectionEngine();

        // Create mocks
        MyClass mock1 = Mockito.mock(MyClass.class);
        MyClass mock2 = Mockito.mock(MyClass.class);

        // Create injectMocksFields
        Set<Field> injectMocksFields = new HashSet<>();
        try {
            Field field = MyClass.class.getDeclaredField("mockField");
            injectMocksFields.add(field);
        } catch (NoSuchFieldException e) {
            Assert.fail("Failed to add injectMocksFields");
        }

        // Create mocks set
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // Create testClassInstance
        MyClass testClassInstance = new MyClass();

        // Run injectMocksOnFields method
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that mockField is injected with mock1
        Assert.assertEquals(mock1, testClassInstance.mockField);
    }
}

class MyClass {
    @InjectMocks
    public MyClass mockField;
}