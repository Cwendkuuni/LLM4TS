import org.junit.Before;
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

import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<Object>();
        testClassInstance = new Object();
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        // Arrange
        Set<Field> injectMocksFields = new HashSet<Field>();
        Field field1 = TestClass.class.getDeclaredField("field1");
        injectMocksFields.add(field1);
        Field field2 = TestClass.class.getDeclaredField("field2");
        injectMocksFields.add(field2);

        // Act
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Assert
        // Verify that the mock candidates are being injected correctly
        verify(mocks, times(2)).add(anyObject());

        // Verify that the mock candidates are being filtered correctly
        verify(field1, times(1)).getType();
        verify(field2, times(1)).getType();
        verify(field1, times(1)).isAssignableFrom(field2.getType());
        verify(field2, times(1)).isAssignableFrom(field1.getType());
    }

    public static class TestClass {
        private Object field1;
        private Object field2;
    }
}