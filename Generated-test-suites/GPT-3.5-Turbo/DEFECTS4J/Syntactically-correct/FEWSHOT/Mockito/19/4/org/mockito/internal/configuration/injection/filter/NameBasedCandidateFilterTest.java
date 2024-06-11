package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {
	private MockUtil mockUtil;
	private MockCandidateFilter nextFilter;
	private NameBasedCandidateFilter candidateFilter;

	@Before
	public void setUp() {
		mockUtil = mock(MockUtil.class);
		nextFilter = mock(MockCandidateFilter.class);
		candidateFilter = new NameBasedCandidateFilter(nextFilter);
		candidateFilter.mockUtil = mockUtil;
	}

	@Test
	public void testFilterCandidate_shouldFilterByName() throws Exception {
		Field field = getClass().getDeclaredField("field");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		when(mockUtil.getMockName(mock2)).thenReturn("mock2");

		List<Object> filteredMocks = new ArrayList<>();
		filteredMocks.add(mock1);

		when(nextFilter.filterCandidate(filteredMocks, field, fieldInstance))
				.thenReturn(new OngoingInjecter());

		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(mockUtil).getMockName(mock1);
		verify(mockUtil).getMockName(mock2);
		verify(nextFilter).filterCandidate(filteredMocks, field, fieldInstance);

		assertEquals(new OngoingInjecter(), result);
	}

	@Test
	public void testFilterCandidate_shouldNotFilter() throws Exception {
		Field field = getClass().getDeclaredField("field");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn("mockName1");
		when(mockUtil.getMockName(mock2)).thenReturn("mockName2");

		when(nextFilter.filterCandidate(mocks, field, fieldInstance))
				.thenReturn(new OngoingInjecter());

		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(mockUtil).getMockName(mock1);
		verify(mockUtil).getMockName(mock2);
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);

		assertEquals(new OngoingInjecter(), result);
	}

	@Test
	public void testFilterCandidate_shouldReturnNextFilterResult() throws Exception {
		Field field = getClass().getDeclaredField("field");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);

		when(mockUtil.getMockName(mock1)).thenReturn("mock1");
		when(mockUtil.getMockName(mock2)).thenReturn("mock2");

		OngoingInjecter ongoingInjecter = new OngoingInjecter();

		when(nextFilter.filterCandidate(mocks, field, fieldInstance))
				.thenReturn(ongoingInjecter);

		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(mockUtil).getMockName(mock1);
		verify(mockUtil).getMockName(mock2);
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);

		assertEquals(ongoingInjecter, result);
	}

	@Test
	public void testFilterCandidate_shouldReturnNextFilterResultWhenMocksSizeIsOne() throws Exception {
		Field field = getClass().getDeclaredField("field");
		Object fieldInstance = new Object();
		Collection<Object> mocks = new ArrayList<>();
		Object mock1 = mock(Object.class);
		mocks.add(mock1);

		when(mockUtil.getMockName(mock1)).thenReturn("mock1");

		OngoingInjecter ongoingInjecter = new OngoingInjecter();

		when(nextFilter.filterCandidate(mocks, field, fieldInstance))
				.thenReturn(ongoingInjecter);

		OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(mockUtil).getMockName(mock1);
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);

		assertEquals(ongoingInjecter, result);
	}
}