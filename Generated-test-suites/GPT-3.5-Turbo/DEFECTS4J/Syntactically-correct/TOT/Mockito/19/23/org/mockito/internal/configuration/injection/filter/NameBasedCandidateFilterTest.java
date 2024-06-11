import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
	
	@Test
	public void testFilterCandidate_withMatchingName_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = Object.class.getDeclaredField("fieldName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
	@Test
	public void testFilterCandidate_withMultipleMocksAndMatchingName_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = Object.class.getDeclaredField("fieldName");
		when(MockUtil.getMockName(mock1)).thenReturn("fieldName");
		when(MockUtil.getMockName(mock2)).thenReturn("fieldName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
	@Test
	public void testFilterCandidate_withMultipleMocksButNoMatchingName_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = Object.class.getDeclaredField("fieldName");
		when(MockUtil.getMockName(mock1)).thenReturn("otherName");
		when(MockUtil.getMockName(mock2)).thenReturn("otherName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
	@Test
	public void testFilterCandidate_withEmptyMocks_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Field field = Object.class.getDeclaredField("fieldName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
	@Test
	public void testFilterCandidate_withSingleMockAndMatchingName_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = Object.class.getDeclaredField("fieldName");
		when(MockUtil.getMockName(mock)).thenReturn("fieldName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
	@Test
	public void testFilterCandidate_withSingleMockButNoMatchingName_returnsNextFilterResult() throws Exception {
		// Arrange
		MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = Object.class.getDeclaredField("fieldName");
		when(MockUtil.getMockName(mock)).thenReturn("otherName");
		Object fieldInstance = new Object();
		
		// Act
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		// Assert
		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}
	
}