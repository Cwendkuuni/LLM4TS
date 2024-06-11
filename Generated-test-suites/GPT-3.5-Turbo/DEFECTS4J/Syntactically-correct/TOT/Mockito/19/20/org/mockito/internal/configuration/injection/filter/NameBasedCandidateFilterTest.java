import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
	
	private NameBasedCandidateFilter filter;
	private MockCandidateFilter next;
	
	@Before
	public void setup() {
		next = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(next);
	}
	
	@Test
	public void testFilterCandidate_singleMockMatch_returnsNextFilteredCandidate() throws NoSuchFieldException {
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = mock(MockCandidateFilter.class);
		
		when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotNull(result);
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multipleMockMatches_returnsNextFilteredCandidateWithMockNameMatches() throws NoSuchFieldException {
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		when(mockUtil.getMockName(mock2)).thenReturn("mock2");
		mocks.add(mock1);
		mocks.add(mock2);
		
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = mock(MockCandidateFilter.class);
		
		when(next.filterCandidate(anyList(), field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotNull(result);
		verify(next).filterCandidate(anyList(), field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_noMockMatch_returnsNextFilteredCandidate() throws NoSuchFieldException {
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		when(mockUtil.getMockName(mock2)).thenReturn("mock2");
		mocks.add(mock1);
		mocks.add(mock2);
		
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = mock(MockCandidateFilter.class);
		
		when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));
		
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		
		assertNotNull(result);
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}

}