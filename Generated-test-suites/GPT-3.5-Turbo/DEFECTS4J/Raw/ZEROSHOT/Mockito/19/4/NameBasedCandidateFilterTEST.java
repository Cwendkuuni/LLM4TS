import org.junit.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate_withMatchingName_shouldReturnOngoingInjecter() {
		// Setup
		Collection<Object> mocks = new ArrayList<Object>();
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(next);
		Field field = mock(Field.class);
		Object fieldInstance = new Object();
		
		// Add mock with matching name
		Object mock = mock(Object.class);
		when(field.getName()).thenReturn("mock");
		when(mockUtil.getMockName(mock)).thenReturn("mock");
		mocks.add(mock);
		
		// Mock behavior of next.filterCandidate()
		OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
		when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);
		
		// Execute
		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Verify
		verify(next).filterCandidate(mockNameMatches, field, fieldInstance);
		Assert.assertEquals(result, ongoingInjecter);
	}

	@Test
	public void testFilterCandidate_withoutMatchingName_shouldReturnOngoingInjecter() {
		// Setup
		Collection<Object> mocks = new ArrayList<Object>();
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(next);
		Field field = mock(Field.class);
		Object fieldInstance = new Object();
		
		// Add mock without matching name
		Object mock = mock(Object.class);
		when(field.getName()).thenReturn("mock1");
		when(mockUtil.getMockName(mock)).thenReturn("mock2");
		mocks.add(mock);
		
		// Mock behavior of next.filterCandidate()
		OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
		when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);
		
		// Execute
		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Verify
		verify(next).filterCandidate(mocks, field, fieldInstance);
		Assert.assertEquals(result, ongoingInjecter);
	}

	@Test
	public void testFilterCandidate_withSameTypeButDifferentName_shouldReturnOngoingInjecter() {
		// Setup
		Collection<Object> mocks = new ArrayList<Object>();
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(next);
		Field field = mock(Field.class);
		Object fieldInstance = new Object();
		
		// Add mock with different name but same type
		Object mock1 = mock(Object.class);
		when(field.getName()).thenReturn("mock1");
		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		mocks.add(mock1);
		
		// Add mock with matching name but different type
		Object mock2 = mock(Integer.class);
		when(field.getName()).thenReturn("mock1");
		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		mocks.add(mock2);
		
		// Mock behavior of next.filterCandidate()
		OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
		when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);
		
		// Execute
		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		// Verify
		verify(next).filterCandidate(mocks, field, fieldInstance);
		Assert.assertEquals(result, ongoingInjecter);
	}

}