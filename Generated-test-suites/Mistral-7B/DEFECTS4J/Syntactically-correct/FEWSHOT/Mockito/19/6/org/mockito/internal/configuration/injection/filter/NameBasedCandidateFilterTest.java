package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextSpy;

	@Before
	public void setUp() {
		nextSpy = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextSpy);
	}

	@Test
	public void testFilterCandidate_EmptyMocks_NullResult() {
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name";
			}
		};
		Object fieldInstance = new Object();
		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertNull(result);
	}

	@Test
	public void testFilterCandidate_SingleMock_ReturnsMock() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		Field field = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name";
			}
		};
		Object fieldInstance = new Object();
		Object mock = mocks.get(0);
		assertEquals(mock, filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test
	public void testFilterCandidate_MultipleMocks_ReturnsCorrectMock() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object1.class));
		mocks.add(mock(Object2.class));
		Field field = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name";
			}
		};
		Object fieldInstance = new Object();
		Object mock1 = mocks.get(0);
		assertEquals(mock1, filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test
	public void testFilterCandidate_MultipleFieldsWithSameName_ReturnsCorrectMock() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(ClassWithTwoFields.class));
		Field field1 = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name";
			}
		};
		Field field2 = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name";
			}
		};
		Object fieldInstance = new Object();
		Object mock = mocks.get(0);
		// Assuming that the first field is the one that matches the name
		assertEquals(mock, filter.filterCandidate(mocks, field1, fieldInstance));
	}

	@Test
	public void testFilterCandidate_MultipleFieldsWithDifferentNames_NextFilterCalled() {
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(ClassWithTwoFields.class));
		Field field1 = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name1";
			}
		};
		Field field2 = new Field() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "name2";
			}
		};
		Object fieldInstance = new Object();
		List<Object> mockNameMatches = new ArrayList<>();
		List<Object> resultMocks = new ArrayList<>();

		nextSpy.expects(once()).probability(1.0)
				.method("filterCandidate").withParameter("mocks", mockNameMatches)
				.withParameter(field1).withParameter(fieldInstance)
				.willReturn(resultMocks.get(0));

		Collection<Object> mockNameMatches1 = new ArrayList<>();
		mockNameMatches1.add(mock(ClassWithTwoFields.class));
		Object resultMock1 = mock(Object.class);
		resultMocks.add(resultMock1);

		filter.filterCandidate(mocks, field1, fieldInstance);
		assertEquals(resultMock1, filter.filterCandidate(mocks, field2, fieldInstance));
		assertEquals(resultMock1, nextSpy.getInvocations().get(0).getArgument(0).get(0));
	}
}

class Object1 {}
class Object2 {}
class ClassWithTwoFields {}