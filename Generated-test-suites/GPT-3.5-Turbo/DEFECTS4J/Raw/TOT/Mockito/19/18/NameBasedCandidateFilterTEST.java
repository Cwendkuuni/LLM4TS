import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

	private MockCandidateFilter nextMockCandidateFilter;
	private NameBasedCandidateFilter nameBasedCandidateFilter;

	@Before
	public void setUp() {
		nextMockCandidateFilter = mock(MockCandidateFilter.class);
		nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMockCandidateFilter);
	}

	@Test
	public void testFilterCandidate_NoMocks_NoNameMatches() throws NoSuchFieldException {

		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = nextMockCandidateFilter;
		Collection<Object> mocks = new ArrayList<>();

		OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
		assertEquals(nextMockCandidateFilter, ongoingInjecter.getNext());
	}

	@Test
	public void testFilterCandidate_NoMocks_OneNameMatch() throws NoSuchFieldException {

		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = nextMockCandidateFilter;
		Collection<Object> mocks = new ArrayList<>();
		Object mock = mock(Object.class);
		when(mockUtil.getMockName(mock)).thenReturn("next");

		mocks.add(mock);

		OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
		assertEquals(nextMockCandidateFilter, ongoingInjecter.getNext());
	}

	@Test
	public void testFilterCandidate_NoMocks_MultipleNameMatches() throws NoSuchFieldException {
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = nextMockCandidateFilter;
		Collection<Object> mocks = new ArrayList<>();

		Object mock1 = mock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("next");

		Object mock2 = mock(Object.class);
		when(mockUtil.getMockName(mock2)).thenReturn("next");

		Object mock3 = mock(Object.class);
		when(mockUtil.getMockName(mock3)).thenReturn("next");

		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);

		when(nextMockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMockCandidateFilter).filterCandidate(any(), any(), any());
		assertEquals(nextMockCandidateFilter, ongoingInjecter.getNext());
	}

	@Test
	public void testFilterCandidate_MultipleMocks_OneNameMatch() throws NoSuchFieldException {
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = nextMockCandidateFilter;
		Collection<Object> mocks = new ArrayList<>();

		Object mock1 = mock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("next");

		Object mock2 = mock(Object.class);
		when(mockUtil.getMockName(mock2)).thenReturn("notNext");

		mocks.add(mock1);
		mocks.add(mock2);

		when(nextMockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMockCandidateFilter).filterCandidate(any(), any(), any());
		assertEquals(nextMockCandidateFilter, ongoingInjecter.getNext());
	}

	@Test
	public void testFilterCandidate_MultipleMocks_MultipleNameMatches() throws NoSuchFieldException {
		Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
		Object fieldInstance = nextMockCandidateFilter;
		Collection<Object> mocks = new ArrayList<>();

		Object mock1 = mock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("next");

		Object mock2 = mock(Object.class);
		when(mockUtil.getMockName(mock2)).thenReturn("next");

		Object mock3 = mock(Object.class);
		when(mockUtil.getMockName(mock3)).thenReturn("notNext");

		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);

		when(nextMockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMockCandidateFilter).filterCandidate(any(), any(), any());
		assertEquals(nextMockCandidateFilter, ongoingInjecter.getNext());
	}

}