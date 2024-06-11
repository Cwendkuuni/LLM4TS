package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.newMock;
import static org.mockito.internal.util.MockUtil.newMockWithName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate_SingleMock_NoConflict() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(newMockWithName("mock1"));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		Object expected = newMockWithName("mock1");

		assertEquals(expected, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_MultipleMocks_NoConflict() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(newMockWithName("mock1"));
		mocks.add(newMockWithName("mock2"));

		Object mock1 = newMockWithName("mock1");
		Object mock2 = newMockWithName("mock2");

		List<Object> mockNameMatches = new ArrayList<>();
		mockNameMatches.add(mock1);

		when(nextMock.filterCandidate(mockNameMatches, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		Object expected = mock1;

		assertEquals(expected, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_MultipleMocks_Conflict() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(newMockWithName("mock1"));
		mocks.add(newMockWithName("mock2"));

		Object mock1 = newMockWithName("mock1");
		Object mock2 = newMockWithName("mock1"); // same name as the first mock

		List<Object> mockNameMatches = new ArrayList<>();
		mockNameMatches.add(mock1);

		when(nextMock.filterCandidate(mockNameMatches, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		Object expected = mock2; // the mock with the same name as the field name

		assertEquals(expected, result.getMock());
		assertEquals(field, result.getField());
		assertEquals(fieldInstance, result.getFieldInstance());
	}

	@Test
	public void testFilterCandidate_MultipleMocks_NoNameMatches() {
		MockCandidateFilter nextMock = mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(newMockWithName("mock1"));
		mocks.add(newMockWithName("mock2"));

		when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertNull(result);
	}
}