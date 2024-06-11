import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate() {
		// Previously written test case by Expert 1
	}

	@Test
	public void testFilterCandidateWithEmptyMocks() {
		String mockName = "mockName1";
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = new Object();
		Collection<Object> mocks = Collections.emptyList();

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test
	public void testFilterCandidateWithMultipleMatches() {
		String mockName = "mockName1";
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Object fieldInstance = new Object();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object mock1 = new MockObject("mock1");
		Object mock2 = new MockObject("mockName1");
		Object mock3 = new MockObject("mock3");

		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);

		Assert.assertEquals(mock2, filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullMocks() {
		// Previously written test case by Expert 1
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullField() {
		String mockName = "mockName1";
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Object fieldInstance = new Object();
		Field field = null;

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullFieldInstance() {
		String mockName = "mockName1";
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = null;

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidateWithNullMockName() {
		String mockName = null;
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = new Object();

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidateWithEmptyMockName() {
		String mockName = "";
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = new Object();

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFilterCandidateWithNullMockNameString() {
		String mockName = null;
		MockCandidateFilter nextMock = MockitoJUnitRunner.mock(MockCandidateFilter.class);
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = new Object();

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test(expected = NullPointerException.class)
	public void testFilterCandidateWithNullNextFilter() {
		String mockName = "mockName1";
		MockCandidateFilter nextMock = null;
		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field(Object.class, "name", java.lang.reflect.AccessibleObject.NULL);
		Object fieldInstance = new Object();

		Assert.assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	class MockObject {
		private String name;

		public MockObject(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}