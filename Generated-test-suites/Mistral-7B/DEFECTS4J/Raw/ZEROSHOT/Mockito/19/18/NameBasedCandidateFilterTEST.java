package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldUtils;
import org.mockito.internal.util.reflection.ReflectUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stub.Answer;
import org.mockito.stub.MockConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtilSpy;

	@Rule
	public Answer answer = new Answer() {
		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			Object[] args = invocation.getArguments();
			return args[0];
		}
	};

	@Before
	public void setup() {
		nextFilter = mock(MockCandidateFilter.class);
		mockUtilSpy = spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextFilter);
		mockUtilSpy.getMockName = new MockNameProvider();
	}

	@Test
	public void testFilterCandidate_oneMock_noConflict() {
		Object mock = mock(String.class);
		Field field = ReflectUtils.findField(Object.class, "fieldName");
		Object fieldInstance = new Object();

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock, result.getMock());
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_oneMock_conflict() {
		Object mock1 = mock(String.class, "mock1");
		Object mock2 = mock(String.class, "mock1");
		Field field = ReflectUtils.findField(Object.class, "fieldName");
		Object fieldInstance = new Object();

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(Mockito.mock(OngoingInjecter.class));

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock1, result.getMock());
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multipleMocks_noConflict() {
		Object mock1 = mock(String.class, "mock1");
		Object mock2 = mock(String.class, "mock2");
		Field field = ReflectUtils.findField(Object.class, "fieldName");
		Object fieldInstance = new Object();

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(mock1, result.getMock());
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
	}

	@Test(expected = IllegalStateException.class)
	public void testFilterCandidate_multipleMocks_conflict() {
		Object mock1 = mock(String.class, "mock1");
		Object mock2 = mock(String.class, "mock1");
		Field field = ReflectUtils.findField(Object.class, "fieldName");
		Object fieldInstance = new Object();

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		when(nextFilter.filterCandidate(mocks, field, fieldInstance)).thenThrow(new IllegalStateException("Conflicting mocks!"));

		filter.filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_noMocks() {
		Field field = ReflectUtils.findField(Object.class, "fieldName");
		Object fieldInstance = new Object();

		Collection<Object> mocks = new ArrayList<>();

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertEquals(null, result);
		verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
	}

	private static class MockNameProvider {
		public String getMockName(Object mock) {
			return mock.getClass().getName();
		}
	}
}