package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import static org.mockito.internal.util.MockUtil.getMockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidateWithSingleMock() {
		MockCandidateFilter nextMockFilter = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.mock(Object.class));

		Field field = Mockito.mock(Field.class);
		Object fieldInstance = Mockito.mock(Object.class);

		Object filterResult = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

		assertNotNull(filterResult);
		assertEquals(mocks.get(0), filterResult);
	}

	@Test
	public void testFilterCandidateWithMultipleMocks() {
		MockCandidateFilter nextMockFilter = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.mock(Object1.class));
		mocks.add(Mockito.mock(Object2.class));

		Field field = Mockito.mock(Field.class);
		Object fieldInstance = Mockito.mock(Object.class);
		String expectedFieldName = getMockName(mocks.get(0)).toString();

		List<Object> expectedMatchingMocks = new ArrayList<>();
		expectedMatchingMocks.add(mocks.get(0));

		MockCandidateFilter resultFilter = Mockito.mock(MockCandidateFilter.class);
		when(nextMockFilter.filterCandidate(eq(expectedMatchingMocks), eq(field), eq(fieldInstance)))
				.thenReturn(resultFilter);

		Object filterResult = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

		assertNotNull(filterResult);
		assertEquals(expectedMatchingMocks.get(0), filterResult);
		verify(nextMockFilter).filterCandidate(eq(expectedMatchingMocks), eq(field), eq(fieldInstance));
	}

	@Test
	public void testFilterCandidateWithMultipleFieldsWithSameName() {
		MockCandidateFilter nextMockFilter = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(nextMockFilter);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.mock(Object1.class));
		mocks.add(Mockito.mock(Object2.class));

		Field field1 = Mockito.mock(Field.class);
		Field field2 = Mockito.mock(Field.class);
		Object fieldInstance = Mockito.mock(Object.class);
		String expectedFieldName = getMockName(mocks.get(0)).toString();

		List<Object> expectedMatchingMocks = new ArrayList<>();
		expectedMatchingMocks.add(mocks.get(0));

		MockCandidateFilter resultFilter = Mockito.mock(MockCandidateFilter.class);
		when(nextMockFilter.filterCandidate(eq(expectedMatchingMocks), eq(field1), eq(fieldInstance)))
				.thenReturn(resultFilter);

		Object filterResult = nameBasedFilter.filterCandidate(mocks, field1, fieldInstance);

		assertNotNull(filterResult);
		assertEquals(expectedMatchingMocks.get(0), filterResult);
		verify(nextMockFilter).filterCandidate(eq(expectedMatchingMocks), eq(field1), eq(fieldInstance));

		mockStatic(NameBasedCandidateFilter.class);
		reset(nextMockFilter);

		Object filterResult2 = nameBasedFilter.filterCandidate(mocks, field2, fieldInstance);

		assertNotNull(filterResult2);
		assertEquals(expectedMatchingMocks.get(0), filterResult2);
		verify(nextMockFilter).filterCandidate(eq(expectedMatchingMocks), eq(field1), eq(fieldInstance));
		verifyNoMoreInteractions(nextMockFilter);
	}

	@Test
	public void testFilterCandidateWhenNextIsNull() {
		NameBasedCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(null);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.mock(Object.class));

		Field field = Mockito.mock(Field.class);
		Object fieldInstance = Mockito.mock(Object.class);

		assertNull(nameBasedFilter.filterCandidate(mocks, field, fieldInstance));
	}
}

class Object1 {
	// Empty class
}

class Object2 {
	// Empty class
}