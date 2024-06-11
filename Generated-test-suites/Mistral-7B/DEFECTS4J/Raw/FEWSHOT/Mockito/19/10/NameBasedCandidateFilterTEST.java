package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.createMock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

	@Test
	public void testFilterCandidate_OneMock_NoConflictingFields() {
		MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(createMock(Object.class));

		Field field = MockUtil.getField(Object.class, "field1");
		Object fieldInstance = new Object();

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(0), result);
	}

	@Test
	public void testFilterCandidate_OneMock_ConflictingFields() {
		MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(createMock(ObjectA.class));
		mocks.add(createMock(ObjectB.class));

		Field field = MockUtil.getField(ObjectA.class, "field1");
		Object fieldInstanceA = new ObjectA();

		Field conflictingField = MockUtil.getField(ObjectB.class, "field1");
		Object fieldInstanceB = new ObjectB();

		assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstanceA));
		assertEquals(mocks.get(1), filter.filterCandidate(mocks, conflictingField, fieldInstanceB));
	}

	@Test
	public void testFilterCandidate_MultipleMocks_NoConflictingFields() {
		MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(createMock(ObjectA.class));
		mocks.add(createMock(ObjectB.class));

		Field field = MockUtil.getField(ObjectA.class, "field1");
		Object fieldInstanceA = new ObjectA();

		Field fieldInNext = MockUtil.getField(ObjectB.class, "field2");
		Object fieldInstanceB = new ObjectB();

		assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstanceA));
		assertEquals(mocks.get(1), filter.filterCandidate(mocks, fieldInNext, fieldInstanceB));
	}

	@Test
	public void testFilterCandidate_MultipleMocks_ConflictingFields() {
		MockCandidateFilter nextMock = Mockito.mock(MockCandidateFilter.class);

		NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(createMock(ObjectA.class));
		mocks.add(createMock(ObjectB.class));

		Field fieldA = MockUtil.getField(ObjectA.class, "field1");
		Object fieldInstanceA = new ObjectA();

		Field fieldB = MockUtil.getField(ObjectB.class, "field1");
		Object fieldInstanceB = new ObjectB();

		Field conflictingField = MockUtil.getField(ObjectB.class, "field1");
		Object conflictingFieldInstance = new ObjectB();

		assertEquals(mocks.get(0), filter.filterCandidate(mocks, fieldA, fieldInstanceA));
		assertEquals(mocks.get(1), filter.filterCandidate(mocks, conflictingField, fieldInstanceB));
	}
}

class ObjectA {
	private String field1;

	public String getField1() {
		return field1;
	}
}

class ObjectB {
	private String field1;

	public String getField1() {
		return field1;
	}
}