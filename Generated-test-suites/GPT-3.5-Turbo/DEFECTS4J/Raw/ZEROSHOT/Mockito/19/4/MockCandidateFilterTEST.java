import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);

        OngoingInjecter injecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(injecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(injecter, result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

}