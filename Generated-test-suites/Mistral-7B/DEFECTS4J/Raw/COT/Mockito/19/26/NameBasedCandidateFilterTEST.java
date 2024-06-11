package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.detection.MockDetector;
import org.mockito.internal.util.reflection.FieldUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private MockCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private Collection<Object> mocks;
	private Object fieldInstance;
	private Field field;

	@Rule
	public TestRule mockitoRule = MockitoAnnotations.initMocks(this);

	@Before
	public void setUp() {
		nextMock = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextMock);
		mocks = new ArrayList<>();
		field = Mockito.spy(Mockito.mock(Field.class));
		fieldInstance = mock(Object.class);
	}

	// Test method: public NameBasedCandidateFilter(MockCandidateFilter next)
	@org.junit.Test
	public void testNameBasedCandidateFilterConstructor() {
		NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(nextMock);
		assert (nameBasedCandidateFilter instanceof NameBasedCandidateFilter);
	}

	// Test method: public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance)
	@org.junit.Test
	public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
		when(field.getName()).thenReturn("mockName");
		mocks.add(mock("Mock1", "mockName1"));
		mocks.add(mock("Mock2", "mockName2"));
		mocks.add(mock("Mock3", "mockName"));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(2)), field, fieldInstance);
	}

	@org.junit.Test
	public void testFilterCandidate_MultipleMocksWithDifferentNames_ReturnsAllMatches() {
		when(field.getName()).thenReturn("mockName1");
		mocks.add(mock("Mock1", "mockName1"));
		mocks.add(mock("Mock2", "mockName2"));
		mocks.add(mock("Mock3", "mockName1"));

		List<Object> result = new ArrayList<>();
		result = filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
		verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(1)), field, fieldInstance);

		assertEquals(2, result.size());
		assertEquals(mocks.get(0), result.get(0));
		assertEquals(mocks.get(1), result.get(1));
	}

	@org.junit.Test
	public void testFilterCandidate_NoMatches_ReturnsEmptyCollection() {
		when(field.getName()).thenReturn("mockName");
		mocks.add(mock("Mock1", "mockName2"));
		mocks.add(mock("Mock2", "mockName3"));

		List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

		verifyZeroInteractions(nextMock);
		assertEquals(0, result.size());
	}

	@org.junit.Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullFieldInstance_ThrowsIllegalArgumentException() {
		filter.filterCandidate(mocks, field, null);
	}

	@org.junit.Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_NullField_ThrowsIllegalArgumentException() {
		when(field.getName()).thenThrow(new IllegalArgumentException());
		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@org.junit.Test(expected = IllegalArgumentException.class)
	public void testFilterCandidate_EmptyCollection_ThrowsIllegalArgumentException() {
		filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
	}
}