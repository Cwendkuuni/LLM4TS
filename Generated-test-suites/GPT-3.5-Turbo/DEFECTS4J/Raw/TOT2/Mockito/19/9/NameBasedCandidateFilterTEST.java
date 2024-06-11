import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockFilter);
        
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add("mock1");
        mocks.add("mock2");
        
        Field field = new Field();
        Object fieldInstance = "fieldInstance";
        
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidateWithMatchingName() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockFilter);
        
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add("fieldInstance");
        
        Field field = new Field();
        field.setName("fieldInstance");
        Object fieldInstance = "fieldInstance";
        
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidateWithConflictingNames() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockFilter);
        
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add("mock1");
        mocks.add("mock1");
        
        Field field = new Field();
        field.setName("mock1");
        Object fieldInstance = "fieldInstance";
        
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        Assert.assertNotNull(result);
    }
}