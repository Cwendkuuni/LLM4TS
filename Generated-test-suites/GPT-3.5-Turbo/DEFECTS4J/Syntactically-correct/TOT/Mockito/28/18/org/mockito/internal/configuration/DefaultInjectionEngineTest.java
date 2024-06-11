import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
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

    private DefaultInjectionEngine engine;

    private MockCandidateFilter mockCandidateFilter;
    private FieldInitializer fieldInitializer;
    private Reporter reporter;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        fieldInitializer = mock(FieldInitializer.class);
        reporter = mock(Reporter.class);

        engine = new DefaultInjectionEngine();
        engine.setMockCandidateFilter(mockCandidateFilter);
        engine.setFieldInitializer(fieldInitializer);
        engine.setReporter(reporter);
    }

    @Test
    public void injectMocksOnFields_InjectsMocksForFieldsAnnotatedByInjectMocks() throws MockitoException {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter"));
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("fieldInitializer"));
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockCandidateFilter.class));
        mocks.add(mock(FieldInitializer.class));
        Object testClassInstance = new DefaultInjectionEngine();

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(fieldInitializer, times(2)).initialize();
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, any(Field.class), any(Object.class));
    }

    @Test
    public void injectMocksOnFields_ThrowsExceptionWhenFieldInitializationFails() throws NoSuchFieldException {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter"));
        Object testClassInstance = new DefaultInjectionEngine();

        MockitoException e = new MockitoException("Mockito Exception");
        when(fieldInitializer.initialize()).thenThrow(e);

        engine.injectMocksOnFields(injectMocksFields, new HashSet<>(), testClassInstance);

        verify(reporter).cannotInitializeForInjectMocksAnnotation("mockCandidateFilter", e);
    }

    @Test
    public void injectMocksOnFields_InjectsMocksInClassHierarchy() throws NoSuchFieldException {
        Set<Field> injectMocksFields = new HashSet<>();
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter"));
        injectMocksFields.add(DefaultInjectionEngine.class.getDeclaredField("fieldInitializer"));
        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockCandidateFilter.class));
        mocks.add(mock(FieldInitializer.class));
        Object testClassInstance = new DefaultInjectionEngine();

        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(mockCandidateFilter, times(4)).filterCandidate(mocks, any(Field.class), any(Object.class));
    }

    @Test
    public void orderedInstanceFieldsFrom_SortsFieldsFromSubTypeToSuperType() throws NoSuchFieldException {
        Field field1 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Field field2 = DefaultInjectionEngine.class.getDeclaredField("fieldInitializer");
        Field field3 = DefaultInjectionEngine.class.getDeclaredField("reporter");

        Field[] declaredFields = {field2, field3, field1};
        when(mockCandidateFilter.filterCandidate(any(Set.class), eq(field1), any(Object.class))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(any(Set.class), eq(field2), any(Object.class))).thenReturn(mockCandidateFilter);
        when(mockCandidateFilter.filterCandidate(any(Set.class), eq(field3), any(Object.class))).thenReturn(mockCandidateFilter);

        Field[] orderedFields = engine.orderedInstanceFieldsFrom(DefaultInjectionEngine.class);
        
        assertArrayEquals(declaredFields, orderedFields);
        verify(mockCandidateFilter, times(3)).filterCandidate(any(Set.class), any(Field.class), any(Object.class));
    }
}