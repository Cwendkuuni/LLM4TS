import static org.mockito.Mockito.*;
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

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private MockCandidateFilter mockCandidateFilter;
    private Comparator<Field> supertypesLast;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        supertypesLast = mock(Comparator.class);
        defaultInjectionEngine = new DefaultInjectionEngine(mockCandidateFilter, supertypesLast);
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = new Object();

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the mockCandidateFilter is called with the correct parameters
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), any(Field.class), eq(testClassInstance));
    }

    @Test
    public void testInjectMocksOnFields_MockitoException() {
        Set<Field> injectMocksFields = new HashSet<Field>();
        Set<Object> mocks = new HashSet<Object>();
        Object testClassInstance = new Object();

        // Throw a MockitoException when calling initialize()
        whenNew(FieldInitializer.class).withArguments(eq(testClassInstance), any(Field.class)).thenThrow(MockitoException.class);

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Verify that the Reporter is called with the correct parameters
        verifyNew(Reporter.class).withNoArguments();
        verify(reporterMock, times(1)).cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = Object.class;
        Set<Object> mocks = new HashSet<Object>();
        Object fieldInstance = new Object();

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

        // Verify that the mockCandidateFilter is called with the correct parameters
        verify(mockCandidateFilter, times(1)).filterCandidate(eq(mocks), any(Field.class), eq(fieldInstance));
    }

    @Test
    public void testOrderedInstanceFieldsFrom() {
        Class<?> awaitingInjectionClazz = Object.class;

        defaultInjectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);

        // Verify that the declared fields are sorted using the supertypesLast comparator
        verifyNew(Arrays.class).withNoArguments();
        verify(arraysMock, times(1)).sort(any(Field[].class), eq(supertypesLast));
    }
}