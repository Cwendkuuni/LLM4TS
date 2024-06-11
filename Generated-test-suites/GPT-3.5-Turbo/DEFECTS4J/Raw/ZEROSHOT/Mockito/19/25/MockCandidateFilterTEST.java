import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(ongoingInjecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(ongoingInjecter, result);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }
}