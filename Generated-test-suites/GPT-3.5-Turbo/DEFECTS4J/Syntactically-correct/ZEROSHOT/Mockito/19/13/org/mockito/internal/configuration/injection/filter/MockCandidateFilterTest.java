import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);

        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter expected = mock(OngoingInjecter.class);

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(expected);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(expected, result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }
}