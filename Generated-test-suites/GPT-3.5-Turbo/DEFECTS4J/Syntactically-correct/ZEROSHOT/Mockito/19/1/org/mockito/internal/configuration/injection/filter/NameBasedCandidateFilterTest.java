import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        // Create mocks
        Object mock = mock(Object.class);

        // Create test object
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        // Invoke method under test
        filter.filterCandidate(List.of(mock), null, null);

        // Verify
        verify(nextFilter).filterCandidate(List.of(mock), null, null);
    }

    @Test
    public void testFilterCandidate_multipleMocksMatchingName() throws Exception {
        // Create mocks
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // Set up mock names
        when(mock1.toString()).thenReturn("mockName");
        when(mock2.toString()).thenReturn("mockName");

        // Create test object
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        // Invoke method under test
        filter.filterCandidate(List.of(mock1, mock2), null, null);

        // Verify
        verify(nextFilter).filterCandidate(List.of(mock1, mock2), null, null);
    }

    @Test
    public void testFilterCandidate_multipleMocksNonMatchingName() throws Exception {
        // Create mocks
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        // Set up mock names
        when(mock1.toString()).thenReturn("mockName1");
        when(mock2.toString()).thenReturn("mockName2");

        // Create test object
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);

        // Invoke method under test
        filter.filterCandidate(List.of(mock1, mock2), null, null);

        // Verify
        verify(nextFilter).filterCandidate(List.of(mock1, mock2), null, null);
    }
}