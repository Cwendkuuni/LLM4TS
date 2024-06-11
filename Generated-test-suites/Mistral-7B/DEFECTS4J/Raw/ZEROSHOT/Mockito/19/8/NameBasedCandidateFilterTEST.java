package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilMock;

	@Before
	public void setup() {
		nextMock = Mockito.mock(MockCandidateFilter.class);
		mockUtilMock = Mockito.mock(MockUtil.class);
		filter = new NameBasedCandidateFilter(nextMock);
		Mockito.when(mockUtilMock.getMockName(Mockito.any()))
				.thenAnswer(i -> "MockName" + i.hashCode());
	}

	@Test
	public void testFilterCandidate_noConflictingFields_nextFilterUsed() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		mocks.add(mock(Object.class));
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		Object result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(1), result);
	}

	@Test
	public void testFilterCandidate_multipleMocksWithSameName_correctMockUsed() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "MockName1"));
		mocks.add(mock(Object.class, "MockName1"));
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		Object result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		Mockito.verify(nextMock, Mockito.never()).filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(0), result);
	}

	@Test
	public void testFilterCandidate_multipleMocksWithSameName_nextFilterUsed() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "MockName1"));
		mocks.add(mock(Object.class, "MockName2"));
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		Object result = filter.filterCandidate(mocks, field, fieldInstance);

		// then
		Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(1), result);
	}

	@Test(expected = IllegalStateException.class)
	public void testFilterCandidate_noMocksFound() {
		// given
		Collection<Object> mocks = new ArrayList<>();
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = mock(Object.class);

		// when
		filter.filterCandidate(mocks, field, fieldInstance);

		// then
		Mockito.verifyNoInteractions(nextMock);
		throw new AssertionError("Expected an IllegalStateException");
	}
}