import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {
    
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        List<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = new Field();
        Object fieldInstance = new Object();
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }
    
}