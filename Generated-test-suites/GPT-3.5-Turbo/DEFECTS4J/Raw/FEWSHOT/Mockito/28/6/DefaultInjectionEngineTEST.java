import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.internal.configuration.injection.MockCandidateFilterMockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine injectionEngine;
    private MockCandidateFilterMockito mockCandidateFilter;
    private Reporter reporter;
    private FieldInitializer fieldInitializer;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mockCandidateFilter = mock(MockCandidateFilterMockito.class);
        reporter = mock(Reporter.class);
        fieldInitializer = mock(FieldInitializer.class);
        injectionEngine.setMockCandidateFilter(mockCandidateFilter);
        injectionEngine.setReporter(reporter);
        injectionEngine.setFieldInitializer(fieldInitializer);
    }

    @Test
    public void testInjectMocksOnFields() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Field field1 = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        Field field2 = DefaultInjectionEngine.class.getDeclaredField("reporter");
        injectMocksFields.add(field1);
        injectMocksFields.add(field2);

        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockCandidateFilter.class));
        mocks.add(mock(Reporter.class));

        Object testClassInstance = new DefaultInjectionEngine();

        mockStatic(DefaultInjectionEngine.class);
        when(DefaultInjectionEngine.getDeclaredFields()).thenReturn(new Field[]{field1, field2});
        when(mockCandidateFilter.filterCandidate(anySet(), any(Field.class), anyObject())).thenReturn(mock(MockCandidateFilterMockito.class));

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        verify(fieldInitializer, times(1)).initialize();
        verify(mockCandidateFilter, times(2)).filterCandidate(anySet(), any(Field.class), anyObject());
        verify(mockCandidateFilter, times(2)).thenInject();
    }

    @Test
    public void testInjectMocksOnFields_exception() throws Exception {
        Set<Field> injectMocksFields = new HashSet<>();
        Field field = DefaultInjectionEngine.class.getDeclaredField("mockCandidateFilter");
        injectMocksFields.add(field);

        Set<Object> mocks = new HashSet<>();
        mocks.add(mock(MockCandidateFilterMockito.class));

        Object testClassInstance = new DefaultInjectionEngine();

        mockStatic(DefaultInjectionEngine.class);
        when(DefaultInjectionEngine.getDeclaredFields()).thenReturn(new Field[]{field});
        when(fieldInitializer.initialize()).thenThrow(new MockitoException());
        when(reporter.cannotInitializeForInjectMocksAnnotation(eq(field.getName()), any(MockitoException.class)))
                .thenThrow(new MockitoException());

        Assert.assertThrows(MockitoException.class,
                () -> injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance));

        verify(fieldInitializer, times(1)).initialize();
        verify(reporter, times(1)).cannotInitializeForInjectMocksAnnotation(eq(field.getName()), any(MockitoException.class));
        verify(mockCandidateFilter, never()).filterCandidate(anySet(), any(Field.class), anyObject());
        verify(mockCandidateFilter, never()).thenInject();
    }
}