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
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilter.class);
        injectionEngine.setMockCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<>();
        Set<Object> mocks = new HashSet<>();
        Object testClassInstance = new Object();

        Field field = createContextField();
        injectMocksFields.add(field);
        mocks.add(createMockObject());

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockCandidateFilter, times(1)).filterCandidate(mocks, field, testClassInstance);
    }

    private Field createContextField() {
        try {
            return DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object createMockObject() {
        return mock(Object.class);
    }
}