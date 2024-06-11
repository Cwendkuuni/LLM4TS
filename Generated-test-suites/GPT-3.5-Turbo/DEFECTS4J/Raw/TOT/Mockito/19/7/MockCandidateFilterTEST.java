import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter candidateFilter = mock(MockCandidateFilter.class);

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        when(candidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(ongoingInjecter, result);
        verify(candidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}