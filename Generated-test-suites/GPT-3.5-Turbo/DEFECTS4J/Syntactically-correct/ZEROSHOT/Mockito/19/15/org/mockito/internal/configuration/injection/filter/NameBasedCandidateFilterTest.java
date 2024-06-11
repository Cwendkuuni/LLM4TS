import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockCandidateFilter mockCandidateFilter;
  
    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    }
  
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
      
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
      
        when(field.getName()).thenReturn("mock1");
      
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
      
        verify(mockCandidateFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }
  
    @Test
    public void testFilterCandidateWithNoMocks() {
        Collection<Object> mocks = new ArrayList<>();
      
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
      
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
      
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }
  
    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        mocks.add(mock1);
      
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
      
        when(field.getName()).thenReturn("mock1");
      
        nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
      
        verify(mockCandidateFilter).filterCandidate(mockNameMatches, eq(field), eq(fieldInstance));
    }
}