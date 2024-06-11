import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        Field fieldToBeInjected = new Field();
        Object fieldInstance = new Object();

        Collection<Object> mocks = mock(Collection.class);
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(ongoingInjecter, result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }
}