import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate() {
		// Create mocks
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Object mock3 = mock(Object.class);
		
		// Setup mocks
		when(mock1.toString()).thenReturn("mock1");
		when(mock2.toString()).thenReturn("mock2");
		when(mock3.toString()).thenReturn("mock3");
		
		// Create list of mocks and field
		Collection<Object> mocks = new ArrayList<Object>();
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);
		Field field = null; // Field doesn't matter for this test
		Object fieldInstance = null; // Field instance doesn't matter for this test
		
		// Create NameBasedCandidateFilter
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		// Test filterCandidate method
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Verify
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}
	
	@Test
	public void testFilterCandidateWithMatchingField() {
		// Create mocks
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Object mock3 = mock(Object.class);
		
		// Setup mocks
		when(mock1.toString()).thenReturn("field1");
		when(mock2.toString()).thenReturn("field2");
		when(mock3.toString()).thenReturn("field3");
		
		// Create list of mocks and field
		Collection<Object> mocks = new ArrayList<Object>();
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);
		
		Field field = new Field(); // Field with name "field2"
		Object fieldInstance = null; // Field instance doesn't matter for this test
		
		// Create NameBasedCandidateFilter
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
		
		// Test filterCandidate method
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Verify
		verify(next).filterCandidate(mocks.subList(1, 2), field, fieldInstance);
	}
}