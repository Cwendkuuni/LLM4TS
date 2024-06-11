package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockConstructor;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockUtil mockUtil;
	private Stubber stubber;
	private MockConstructor<Object> mockConstructor;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Before
	public void setup() {
		initMocks(this);
		filter = new NameBasedCandidateFilter(null);
		mockUtil = spy(MockUtil.class);
		stubber = mockitoRule.stubber();
		mockConstructor = mockConstructor(Object.class);
	}

	// Testing constructor
	@Test
	public void testNameBasedCandidateFilterConstruction() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		assert (filter != null);
	}

	// Testing filterCandidate method with empty collection
	@Test
	public void testFilterCandidateWithEmptyCollection() {
		Collection<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(result, nextFilter(mocks, field, fieldInstance));
	}

	// Testing filterCandidate method with single mock
	@Test
	public void testFilterCandidateWithSingleMock() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mockName"));
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		String mockName = "mockName";
		stubber.when(mockUtil.getMockName(mocks.get(0))).thenReturn(mockName);
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(result.getMock(), mocks.get(0));
		assertEquals(result.getField(), field);
		assertEquals(result.getFieldInstance(), fieldInstance);
	}

	// Testing filterCandidate method with multiple mocks having same name
	@Test
	public void testFilterCandidateWithMultipleMocksHavingSameName() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mockName"));
		mocks.add(mock(Object.class, "mockName"));
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(result.getMock(), mocks.get(0));
		assertEquals(result.getField(), field);
		assertEquals(result.getFieldInstance(), fieldInstance);
	}

	// Testing filterCandidate method with multiple mocks having different names
	@Test
	public void testFilterCandidateWithMultipleMocksHavingDifferentNames() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, "mockName1"));
		mocks.add(mock(Object.class, "mockName2"));
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		OngoingInjecter result1 = nextFilter(mocks, field, fieldInstance);
		OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(result1.getMock(), mocks.get(1));
		assertEquals(result1.getField(), field);
		assertEquals(result1.getFieldInstance(), fieldInstance);
		assertEquals(result2.getMock(), mocks.get(0));
		assertEquals(result2.getField(), field);
		assertEquals(result2.getFieldInstance(), fieldInstance);
	}

	// Testing next filter
	private OngoingInjecter nextFilter(Collection<Object> mocks, Field field, Object fieldInstance) {
		NameBasedCandidateFilter next = mock(NameBasedCandidateFilter.class);
		return next.filterCandidate(mocks, field, fieldInstance);
	}

	// Testing getMockName method of MockUtil
	@Test(expected = IllegalStateException.class)
	public void testGetMockNameWithNullMock() {
		stubber.when(mockUtil.getMockName(null)).thenThrow(new IllegalStateException());
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		filter.filterCandidate(mocks, field, fieldInstance);
	}

	// Testing filterCandidate method with null collection
	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullCollection() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = null;
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		filter.filterCandidate(mocks, field, fieldInstance);
	}

	// Testing filterCandidate method with null field
	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullField() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		Field field = null;
		Object fieldInstance = mock(Object.class);
		filter.filterCandidate(mocks, field, fieldInstance);
	}

	// Testing filterCandidate method with null field instance
	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullFieldInstance() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = null;
		filter.filterCandidate(mocks, field, fieldInstance);
	}

	// Testing filterCandidate method with empty field name
	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidateWithEmptyFieldName() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		Field field = mock(Field.class);
		Object fieldInstance = mock(Object.class);
		stubber.when(mockUtil.getMockName(mocks.get(0))).thenReturn("");
		filter.filterCandidate(mocks, field, fieldInstance);
	}
}