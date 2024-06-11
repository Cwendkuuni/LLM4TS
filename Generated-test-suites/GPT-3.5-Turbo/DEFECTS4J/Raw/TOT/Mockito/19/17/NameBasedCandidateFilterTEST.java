import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_withMatchingName_shouldReturnNextFilterCandidate() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(nextFilter);
		
		List<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		
		when(mock1.toString()).thenReturn("mock1");
		when(mock2.toString()).thenReturn("mock2");
		when(field.getName()).thenReturn("mock1");
		mocks.add(mock1);
		mocks.add(mock2);
		
		// Act
		candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void testFilterCandidate_withNoMatchingName_shouldReturnNextFilterCandidate() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(nextFilter);
		
		List<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		
		when(mock1.toString()).thenReturn("mock1");
		when(mock2.toString()).thenReturn("mock2");
		when(field.getName()).thenReturn("mock3");
		mocks.add(mock1);
		mocks.add(mock2);
		
		// Act
		candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnNextFilterCandidate() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(nextFilter);
		
		List<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		Object mock1 = mock(Object.class);
		
		when(mock1.toString()).thenReturn("mock1");
		when(field.getName()).thenReturn("mock1");
		mocks.add(mock1);
		
		// Act
		candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void testFilterCandidate_withNoMock_shouldReturnNextFilterCandidate() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(nextFilter);
		
		List<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		
		when(field.getName()).thenReturn("mock1");
		
		// Act
		candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
    
    @Test
    public void testFilterCandidate_withMultipleMocksAndNoMatchingName_shouldReturnFilteredCandidates() {
        // Arrange
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(nextFilter);
		
		List<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Object mock3 = mock(Object.class);
		
		when(mock1.toString()).thenReturn("mock1");
		when(mock2.toString()).thenReturn("mock2");
		when(mock3.toString()).thenReturn("mock3");
		when(field.getName()).thenReturn("mock4");
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);
		
		// Act
		candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(any(Collection.class), eq(field), eq(fieldInstance));
    }
}