import org.junit.Test;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();

        // Test for mocks size = 1
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(ongoingInjecter.thenInject());

        // Test for mocks size != 1
        Collection<Object> mocks2 = new ArrayList<>();
        Field field2 = null;
        Object fieldInstance2 = new Object();
        OngoingInjecter ongoingInjecter2 = finalMockCandidateFilter.filterCandidate(mocks2, field2, fieldInstance2);
        assertFalse(ongoingInjecter2.thenInject());
    }
}