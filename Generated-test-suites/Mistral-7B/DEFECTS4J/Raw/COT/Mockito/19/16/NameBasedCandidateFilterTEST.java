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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtil;
	private Stubber stubber;

	@Before
	public void setUp() {
		mockUtil = mock(MockUtil.class);
		nextFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
		stubber = mockito3(stubber -> {});
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
		// Given
		String mockName = "mockName";
		Object mock1 = createMockWithName(mockName);
		Object mock2 = createMockWithName(mockName);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = mockitoClass(Field.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextFilter, times(1)).filterCandidate(argThat(contains(mock1)), field, fieldInstance);
		assertThat(result, is(notNullValue()));
		assertThat(getInternalState(result, "candidate"), is(mock1));
	}

	@Test
	public void testFilterCandidate_SingleMock_ReturnsNextFilter() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field field = mockitoClass(Field.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
		assertThat(result, is(notNullValue()));
		assertThat(getInternalState(result, "candidate"), is(nullValue()));
	}

	@Test(expected = AssertionError.class)
	public void testFilterCandidate_NullMocks_ThrowsAssertionError() {
		// Given
		Collection<Object> mocks = null;
		Field field = mockitoClass(Field.class);
		Object fieldInstance = new Object();

		// When
		filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		fail("FilterCandidate should have thrown an AssertionError when provided with null mocks.");
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidate_NullField_ThrowsNullPointerException() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field nullField = null;
		Object fieldInstance = new Object();

		// When
		filter.filterCandidate(mocks, nullField, fieldInstance);

		// Then
		fail("FilterCandidate should have thrown a NullPointerException when provided with a null field.");
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidate_NullFieldInstance_ThrowsNullPointerException() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field field = mockitoClass(Field.class);
		Object nullFieldInstance = null;

		// When
		filter.filterCandidate(mocks, field, nullFieldInstance);

		// Then
		fail("FilterCandidate should have thrown a NullPointerException when provided with a null field instance.");
	}

	@Test
	public void testFilterCandidate_NoMatchingMocks_ReturnsNextFilter() {
		// Given
		String mockName = "mockName";
		Object mock = createMockWithName(mockName);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);
		Field field = mockitoClass(Field.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextFilter, times(1)).filterCandidate(mocks, field, fieldInstance);
		assertThat(result, is(notNullValue()));
		assertThat(getInternalState(result, "candidate"), is(nullValue()));
	}

	private Object createMockWithName(String name) {
		Object mock = mock(Object.class);
		when(mockUtil.getMockName(mock)).thenReturn(name);
		return mock;
	}

	private Field mockitoClass(Class<?> clazz) {
		return spy(clazz, stubber);
	}

	private <T> Collection<T> contains(T element) {
		return new ArrayList<T>() {{
			add(element);
		}};
	}
}