package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilMock;

	@Before
	public void setup() {
		nextMock = mock(MockCandidateFilter.class);
		mockUtilMock = spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextMock);
		when(mockUtilMock.getMockName(nextMock)).thenAnswer(new Answer<String>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return "nextMock";
			}
		});
	}

	@Test
	public void testFilterCandidate_SingleMock_ReturnsNextFilter() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		Object fieldInstance = mock(Object.class);
		Field field = mock(Field.class);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(nextMock, result);
	}

	@Test
	public void testFilterCandidate_MultipleMocks_ReturnsMatchingMock() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mock1"));
		mocks.add(mock(Object.class, "mock2"));
		mocks.add(mock(Object.class, "mock3"));
		Object fieldInstance = mock(Object.class);
		Field field = mock(Field.class);

		when(mockUtilMock.getMockName(mocks.get(1))).thenReturn("mockName1");

		Object result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mocks.get(1), result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_EmptyMockCollection() {
		Collection<Object> mocks = new ArrayList<>();
		Object fieldInstance = mock(Object.class);
		Field field = mock(Field.class);

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullMockCollection() {
		Collection<Object> mocks = null;
		Object fieldInstance = mock(Object.class);
		Field field = mock(Field.class);

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullField() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mock1"));
		Object fieldInstance = mock(Object.class);
		Field field = null;

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullFieldInstance() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mock1"));
		Object fieldInstance = null;
		Field field = mock(Field.class);

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_MultipleFieldsWithSameName() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mock1"));
		mocks.add(mock(Object.class, "mock2"));
		Object fieldInstance = mock(Object.class);
		Field field1 = mock(Field.class, "field1");
		Field field2 = mock(Field.class, "field1");

		when(mockUtilMock.getMockName(mocks.get(1))).thenReturn("mockName1");

		Object result = filter.filterCandidate(mocks, field1, fieldInstance);

		assertEquals(mocks.get(1), result);
	}
}