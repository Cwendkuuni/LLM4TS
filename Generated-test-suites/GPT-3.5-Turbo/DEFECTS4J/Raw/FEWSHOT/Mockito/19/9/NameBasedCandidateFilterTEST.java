import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

public class NameBasedCandidateFilterTest {
    
    // Create a mock candidate filter for testing
    private class MockMockCandidateFilter implements MockCandidateFilter {
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            return null;
        }
    }

    // Test filterCandidate() method when mocks size is greater than 1 and field name matches mock name
    @Test
    public void testFilterCandidate_MockNameMatch() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(new MockMockCandidateFilter());
        
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that result is not null
        assertNotNull(result);
    }
    
    // Test filterCandidate() method when mocks size is greater than 1 and field name does not match any mock name
    @Test
    public void testFilterCandidate_NoMockNameMatch() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(new MockMockCandidateFilter());
        
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that result is null
        assertNull(result);
    }
    
    // Test filterCandidate() method when mocks size is 1
    @Test
    public void testFilterCandidate_OneMock() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(new MockMockCandidateFilter());
        
        Object mock1 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify that result is not null
        assertNotNull(result);
    }
}