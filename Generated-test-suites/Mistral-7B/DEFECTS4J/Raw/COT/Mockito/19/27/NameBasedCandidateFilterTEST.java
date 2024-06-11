import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.reflect.whitebox.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidateWithSingleMock() {
		MockCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock = createMock();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		Field field = createField("testField");
		Object fieldInstance = new Object();

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		Assert.assertEquals(mock, result);
	}

	@Test
	public void testFilterCandidateWithMultipleMocks() {
		MockCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock1 = createMock();
		Object mock2 = createMock();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		Field field = createField("testField");
		Object fieldInstance = new Object();

		Whitebox.setInternalState(filter, "next", new StubFilter());

		List<Object> expectedResult = new ArrayList<>();
		expectedResult.add(mock1);

		List<Object> result = (List<Object>) Whitebox.invokeMethod(filter, "filterCandidate", mocks, field, fieldInstance);
		Assert.assertEquals(expectedResult, result);
	}

	@Test(expected = IllegalStateException.class)
	public void testFilterCandidateWithConflictingNames() {
		MockCandidateFilter filter = new NameBasedCandidateFilter(null);
		Object mock1 = createMock();
		Object mock2 = createMock();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		Field field1 = createField("testField1");
		Object fieldInstance1 = new Object();
		Field field2 = createField("testField1");
		Object fieldInstance2 = new Object();

		Whitebox.setInternalState(filter, "next", new StubFilter());

		List<Object> expectedResult = new ArrayList<>();
		expectedResult.add(mock2);

		List<Object> result = (List<Object>) Whitebox.invokeMethod(filter, "filterCandidate", mocks, field1, fieldInstance1);
		Assert.assertEquals(expectedResult, result);
	}

	@Test
	public void testNextFilter() {
		MockCandidateFilter filter = new NameBasedCandidateFilter(new StubFilter());
		Object mock = createMock();
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		Field field = createField("testField");
		Object fieldInstance = new Object();

		StubFilter nextFilter = new StubFilter();
		Whitebox.setInternalState(nextFilter, "filterCandidate", new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return mock;
			}
		});
		Whitebox.setInternalState(filter, "next", nextFilter);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		Assert.assertEquals(mock, result);
	}

	private Field createField(String name) {
		try {
			Field field = MockUtil.class.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	private Object createMock() {
		Object mock = MockUtil.mock(Object.class);
		Whitebox.setInternalState(mock, "name", "testMock");
		return mock;
	}

	private static class StubFilter implements MockCandidateFilter {
		@Override
		public Object filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
			return null;
		}
	}
}