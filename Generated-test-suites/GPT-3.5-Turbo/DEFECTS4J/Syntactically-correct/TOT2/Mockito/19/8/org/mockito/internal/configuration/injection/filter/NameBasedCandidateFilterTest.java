import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
  
  private NameBasedCandidateFilter mockCandidateFilter;
  private MockCandidateFilter nextMockCandidateFilter;
  
  @Before
  public void setUp() {
    nextMockCandidateFilter = mock(MockCandidateFilter.class);
    mockCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
  }
  
  @Test
  public void testFilterCandidate() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Field field = String.class.getDeclaredField("CASE_INSENSITIVE_ORDER");
    Object fieldInstance = "Test";
    
    OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    // Verify that the next filterCandidate method is called with the correct parameters
    verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
  }
  
  // Edge case: Testing filterCandidate method with multiple mocks and matching names
  @Test
  public void testFilterCandidateWithMultipleMocksAndMatchingNames() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);
    mocks.add(mock1);
    mocks.add(mock2);
    Field field = String.class.getDeclaredField("CASE_INSENSITIVE_ORDER");
    Object fieldInstance = "CASE_INSENSITIVE_ORDER";
    
    OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    // Verify that the next filterCandidate method is called with only the matching mock
    verify(nextMockCandidateFilter).filterCandidate(List.of(mock2), field, fieldInstance);
  }
  
  // Error scenario: Testing filterCandidate method with no mocks
  @Test
  public void testFilterCandidateWithNoMocks() throws Exception {
    Collection<Object> mocks = new ArrayList<>();
    Field field = String.class.getDeclaredField("CASE_INSENSITIVE_ORDER");
    Object fieldInstance = "Test";
    
    OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    
    // Verify that the next filterCandidate method is called with the same parameters
    verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
  }
}