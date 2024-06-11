import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.invocation.InvocationOnMock.method;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtil;
	private Stubber stubber;

	@Before
	public void setup() {
		mockUtil = mock(MockUtil.class);
		nextFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
		initMocks(this);
		stubber = stub(mockUtil);
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithSameName_ShouldReturnNextFilter() {
		// given
		String mockName = "mockName";
		Object mockA = mock(Object.class, withName(mockName));
		Object mockB = mock(Object.class, withName(mockName));
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mockA);
		mocks.add(mockB);
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		verify(nextFilter).filterCandidate(argThat(c -> c.size() == 1 && c.contains(mockB)), field, fieldInstance);
		verifyNoMoreInteractions(nextFilter);
		verifyNoMoreInteractions(mockUtil);
		verify(mockUtil, times(mocks.size())).getMockName(any());
	}

	@Test
	public void testFilterCandidate_SingleMock_ShouldPassToNextFilter() {
		// given
		String mockName = "mockName";
		Object mock = mock(Object.class, withName(mockName));
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		verifyNoInteractions(nextFilter);
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
		verifyNoMoreInteractions(mockUtil);
		verify(mockUtil, times(mocks.size())).getMockName(any());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullMocks_ShouldThrowException() {
		// given
		Collection<Object> mocks = null;
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		filter.filterCandidate(mocks, field, fieldInstance);

		// then
		verifyNoInteractions(nextFilter);
		verifyNoInteractions(mockUtil);
		throw new AssertionError("Exception not thrown");
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidate_NullField_ShouldThrowException() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = null;
		Object fieldInstance = mock(Object.class);

		// when
		filter.filterCandidate(mocks, field, fieldInstance);

		// then
		verifyNoInteractions(nextFilter);
		verifyNoInteractions(mockUtil);
		throw new AssertionError("Exception not thrown");
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidate_NullFieldInstance_ShouldThrowException() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		Object mock = mock(Object.class);
		mocks.add(mock);
		Field field = mock(Field.class);
		Object fieldInstance = null;

		// when
		filter.filterCandidate(mocks, field, fieldInstance);

		// then
		verifyNoInteractions(nextFilter);
		verifyNoInteractions(mockUtil);
		throw new AssertionError("Exception not thrown");
	}

	@Test
	public void testGetMockName_ReturnsCorrectMockName() {
		// given
		String mockName = "mockName";
		Object mock = mock(Object.class, withName(mockName));

		// when
		String result = mockUtil.getMockName(mock);

		// then
		assertEquals(mockName, result);
		verifyNoMoreInteractions(mockUtil);
	}

	@Test
	public void testGetMockName_ReturnsDefaultNameIfMockNameIsNotDefined() {
		// given
		Object mock = mock(Object.class);

		// when
		String result = mockUtil.getMockName(mock);

		// then
		assertNotNull(result);
		verify(mockUtil).getMockName(mock, "anonymous_" + System.identityHashCode(mock));
		verifyNoMoreInteractions(mockUtil);
	}
}