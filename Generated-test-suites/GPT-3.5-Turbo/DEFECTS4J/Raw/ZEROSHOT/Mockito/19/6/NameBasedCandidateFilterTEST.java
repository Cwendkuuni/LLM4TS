import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtil;

	@Before
	public void setup() {
		nextFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
		mockUtil = mock(MockUtil.class);
	}

	@Test
	public void filterCandidate_withMultipleMocksAndMatchingName_shouldReturnFilteredCandidates() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
		Object fieldInstance = null;
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn("mockUtil");
		when(mockUtil.getMockName(mock2)).thenReturn("otherMock");

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}

	@Test
	public void filterCandidate_withMultipleMocksAndNoMatchingName_shouldReturnAllMocks() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
		Object fieldInstance = null;
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn("otherMock");
		when(mockUtil.getMockName(mock2)).thenReturn("anotherMock");

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
	}

	@Test
	public void filterCandidate_withSingleMockAndMatchingName_shouldReturnFilteredCandidate() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
		Object fieldInstance = null;
		Object mock = mock(Object.class);
		mocks.add(mock);

		when(mockUtil.getMockName(mock)).thenReturn("mockUtil");

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
	}

	@Test
	public void filterCandidate_withSingleMockAndNoMatchingName_shouldReturnSingleMock() throws Exception {
		Collection<Object> mocks = new ArrayList<>();
		Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
		Object fieldInstance = null;
		Object mock = mock(Object.class);
		mocks.add(mock);

		when(mockUtil.getMockName(mock)).thenReturn("otherMock");

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextFilter).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
	}
}