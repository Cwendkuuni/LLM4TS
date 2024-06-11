package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.MockUtil.spy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

@RunWith(JUnit4.class)
public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidateWithSingleMatchingMock() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.spy(new Foo()));
		Object fieldInstance = new Object();
		Field field = Mockito.spy(new Field() {
			@Override
			public String getName() {
				return "fooName";
			}
		});

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(0), result.getMock());
	}

	@Test
	public void testFilterCandidateWithMultipleMatchingMocks() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(Mockito.spy(new Foo1()));
		mocks.add(Mockito.spy(new Foo2()));
		Object fieldInstance = new Object();
		Field field = Mockito.spy(new Field() {
			@Override
			public String getName() {
				return "fooName";
			}
		});

		List<Object> expectedResults = new ArrayList<>();
		expectedResults.add(mocks.get(0));
		expectedResults.add(mocks.get(1));

		OngoingInjecter result1 = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(expectedResults.get(0), result1.getMock());

		Collection<Object> remainingMocks = new ArrayList<>();
		remainingMocks.addAll(mocks);
		remainingMocks.remove(result1.getMock());

		List<Object> mockNameMatches = new ArrayList<>();
		for (Object mock : remainingMocks) {
			if (field.getName().equals(getMockName(mock).toString())) {
				mockNameMatches.add(mock);
				break;
			}
		}

		OngoingInjecter result2 = filter.filterCandidate(mockNameMatches, field, fieldInstance);
		assertEquals(expectedResults.get(1), result2.getMock());
	}

	@Test
	public void testFilterCandidateWithNoMatchingMocks() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = new ArrayList<>();
		Object fieldInstance = new Object();
		Field field = Mockito.spy(new Field() {
			@Override
			public String getName() {
				return "barName";
			}
		});

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertNull(result.getMock());
	}

	@Test
	public void testFilterCandidateWithNullArguments() {
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
		Collection<Object> mocks = null;
		Field field = null;
		Object fieldInstance = new Object();

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertNull(result.getMock());
	}

	private static class Foo {}
	private static class Foo1 extends Foo {}
	private static class Foo2 extends Foo {}
}