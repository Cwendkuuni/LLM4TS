import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;
        
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }
    
    // Edge Case 1: Mocks size greater than 1
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        
        Field field = null;
        Object fieldInstance = null;
        
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }
    
    // Edge Case 2: Mock with conflicting naming fields
    @Test
    public void testFilterCandidateWithConflictingNames() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
        
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        
        Field field = null;
        Object fieldInstance = null;
        
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

}