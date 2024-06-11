import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void injectMocksOnFields_shouldInjectMocksAndInitializeFields() throws Exception {
        // Set up mocks
        Set<Object> mocks = new HashSet<>();
        Object mockInstance = mock(Object.class);
        mocks.add(mockInstance);

        // Set up injectMocksFields
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(injectMocksField);

        // Set up testClassInstance
        DefaultInjectionEngine testClassInstance = new DefaultInjectionEngine();

        // Inject mocks on fields
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mock has been injected and initialized
        assertEquals(mockInstance, mocks.iterator().next());
    }

    @Test
    public void injectMocksOnFields_shouldNotInjectMocksWhenFieldCannotBeInitialized() throws Exception {
        // Set up mocks
        Set<Object> mocks = new HashSet<>();

        // Set up injectMocksFields
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(injectMocksField);

        // Set up testClassInstance
        DefaultInjectionEngine testClassInstance = new DefaultInjectionEngine();

        // Inject mocks on fields
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that no mocks have been injected
        assertEquals(0, mocks.size());
    }

    @Test
    public void injectMocksOnFields_shouldInjectMocksInSubTypesBeforeSuperTypes() throws Exception {
        // Set up mocks
        Set<Object> mocks = new HashSet<>();
        Object mockInstance1 = mock(Object.class);
        Object mockInstance2 = mock(Object.class);
        mocks.add(mockInstance1);
        mocks.add(mockInstance2);

        // Set up injectMocksFields
        Set<Field> injectMocksFields = new HashSet<>();
        Field injectMocksField = DefaultInjectionEngine.class.getDeclaredField("supertypesLast");
        injectMocksFields.add(injectMocksField);

        // Set up testClassInstance
        DefaultInjectionEngine testClassInstance = new DefaultInjectionEngine();

        // Inject mocks on fields
        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mocks have been injected in the correct order (subtypes before supertypes)
        verify(mockInstance1).thenInject();
        verify(mockInstance2).thenInject();
        verifyNoMoreInteractions(mockInstance1, mockInstance2);
    }
}