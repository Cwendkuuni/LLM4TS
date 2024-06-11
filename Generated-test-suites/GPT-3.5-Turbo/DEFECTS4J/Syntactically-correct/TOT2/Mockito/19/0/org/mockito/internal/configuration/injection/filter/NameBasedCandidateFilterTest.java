import static org.junit.Assert.*;
import org.junit.Test;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Typical use case
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");
        Field field = new Field();
        Object fieldInstance = new Object();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Edge case: mocks with the same name
        mock1.setName("mock");
        mock2.setName("mock");
        List<Object> mocksSameName = new ArrayList<>();
        mocksSameName.add(mock1);
        mocksSameName.add(mock2);
        OngoingInjecter resultSameName = filter.filterCandidate(mocksSameName, field, fieldInstance);
        assertNull(resultSameName);
        
        // Error scenario: passing empty mocks list
        List<Object> emptyMocks = new ArrayList<>();
        OngoingInjecter resultEmpty = filter.filterCandidate(emptyMocks, field, fieldInstance);
        assertNull(resultEmpty);
    }
}