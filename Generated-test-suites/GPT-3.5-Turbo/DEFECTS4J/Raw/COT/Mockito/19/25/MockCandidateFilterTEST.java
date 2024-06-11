import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mocks = mock(Collection.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(ongoingInjecter, result);
    }

    /*
     * Potential Edge Cases:
     * - Empty collection of mocks
     * - Null field to be injected
     * - Null field instance
     * - Field instance of different type than field to be injected
     */

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(new Collection<>(), fieldToBeInjected, fieldInstance))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(new Collection<>(), fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(new Collection<>(), fieldToBeInjected, fieldInstance);
        assertSame(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidateWithNullFieldToBeInjected() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, null, fieldInstance))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, null, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, null, fieldInstance);
        assertSame(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidateWithNullFieldInstance() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, null);
        assertSame(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidateWithFieldInstanceOfDifferentType() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(ongoingInjecter, result);
    }

}