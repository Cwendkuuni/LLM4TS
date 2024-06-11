package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockUtil mockUtil;

	@Before
	public void setUp() {
		filter = new NameBasedCandidateFilter(null);
		mockUtil = Mockito.spy(new MockUtil());
	}

	@Test
	public void testFilterCandidate_OneMockWithMatchingName_ReturnsThatMock() throws Exception {
		// Given
		Object mock = mock(Object.class);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);
		Field field = new Field("testField", String.class, Object.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock, result.getMock());
	}

	@Test
	public void testFilterCandidate_OneMockWithNonMatchingName_ReturnsNull() throws Exception {
		// Given
		Object mock = mock(Object.class);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);
		Field field = new Field("notTestField", String.class, Object.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertNull(result);
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithMatchingNames_ReturnsCorrectMock() throws Exception {
		// Given
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);
		Field field = new Field("testField", String.class, Object.class);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock1, result.getMock());

		// When filtering again for the same field
		OngoingInjecter result2 = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock1, result2.getMock());

		// Given filtering with the next MockCandidateFilter
		MockCandidateFilter next = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(next);
		Mockito.when(next.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class))).then(new Answer<OngoingInjecter>() {
			@Override
			public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
				Collection<Object> mockNameMatches = (Collection<Object>) invocation.getArguments()[0];
				Object mock = mockNameMatches.iterator().next();
				return new OngoingInjecter(mock, null);
			}
		});

		// When
		OngoingInjecter result3 = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		assertEquals(mock2, result3.getMock());
	}
}