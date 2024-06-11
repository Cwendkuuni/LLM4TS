import org.junit.Before;
import org.junit.Test;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    // Test data
    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Field> injectMocksFields;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setUp() throws Exception {
        defaultInjectionEngine = new DefaultInjectionEngine();
        injectMocksFields = new HashSet<>();
        mocks = new HashSet<>();
        testClassInstance = new Object();
    }

    @Test
    public void testInjectMocksOnFields() {
        Field field1 = getField("field1", String.class);
        Field field2 = getField("field2", Integer.class);
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mock1, times(1)).toString();
        verify(mock2, times(1)).hashCode();
    }

    @Test
    public void testInjectMocksOnFields_MockitoException() {
        Field field1 = getField("field1", String.class);
        injectMocksFields.add(field1);

        Object mock1 = mock(String.class);
        mocks.add(mock1);

        Object[] parameters = {testClassInstance, field1};
        MockitoException mockitoException = new MockitoException("Cannot initialize for InjectMocks annotation");
        doThrow(mockitoException).when(new Reporter()).cannotInitializeForInjectMocksAnnotation(anyString(), any());
        
        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
        
        verify(new Reporter(), times(1)).cannotInitializeForInjectMocksAnnotation(eq("field1"), eq(mockitoException));
    }

    private Field getField(String fieldName, Class<?> fieldType) throws Exception {
        Field field = DefaultInjectionEngine.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(defaultInjectionEngine, fieldType);
        return field;
    }

}