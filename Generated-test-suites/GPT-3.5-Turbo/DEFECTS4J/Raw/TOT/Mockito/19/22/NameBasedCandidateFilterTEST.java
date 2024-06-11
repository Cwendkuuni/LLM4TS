import org.junit.Test;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {
  
  // Test for filterCandidate method
  @Test
  public void testFilterCandidate() {
    // Typical use case
    MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
    NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    Collection<Object> mocks = new ArrayList<Object>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    Object nonMock = new Object();
    Field field = null;
    Object fieldInstance = null;
  
    mocks.add(mock1);
    mocks.add(mock2);
    mocks.add(nonMock);
    
    nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    verify(mockCandidateFilter, times(1)).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    
    
    // Edge case with only one mock
    mocks.clear();
    mocks.add(mock1);
    
    nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    verify(mockCandidateFilter, times(1)).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    
    
    // Error scenario with no mocks
    mocks.clear();
    
    nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    verify(mockCandidateFilter, times(1)).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
  }

}