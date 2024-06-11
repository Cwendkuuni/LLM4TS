package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockUtil;
import org.mockito.internal.MockitoConstants;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockCandidateFilter;
import org.mockito.internal.util.name.MockName;
import org.mockito.internal.util.name.MockNameProvider;
import org.mockito.internal.util.reflection.FieldAccessException;
import org.mockito.internal.util.reflection.FieldAccessUtil;
import org.mockito.internal.util.reflection.ReflectUtils;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.util.name.NameMatchingStrategy.EXACT;

public class NameBasedCandidateFilterTest {

	private static final String MOCK_NAME = "mockName";
	private static final String FIELD_NAME = "fieldName";

	private List<Object> mocks;
	private Object fieldInstance;
	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMockFilter;
	private MockNameProvider mockNameProvider;

	@Before
	public void setUp() {
		mockNameProvider = Whitebox.getInternalState(MockitoConstants.MOCKITO_SYSTEM_PROVIDER, MockNameProvider.class);
		mocks = new ArrayList<>();
		mocks.add(createMock("mock1"));
		mocks.add(createMock("mock2"));
		nextMockFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextMockFilter);
		fieldInstance = new Object();
	}

	@Test
	public void testFilterWithOneMock() {
		assertEquals(fieldInstance, filter.filterCandidate(mocks, createField("field1"), fieldInstance));
	}

	@Test
	public void testFilterWithMatchingMock() {
		Field field = createField("fieldName");
		MockName mockName = mockNameProvider.getMockName(mocks.get(0));
		assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance));
		assertEquals(mockName.toString(), field.getName());
	}

	@Test
	public void testFilterWithMultipleMatchingMocks() {
		MockName mockName1 = mockNameProvider.getMockName(mocks.get(0));
		MockName mockName2 = mockNameProvider.getMockName(mocks.get(1));
		assertEquals(mocks.get(0), filter.filterCandidate(mocks, createField(mockName1.toString()), fieldInstance));
		assertEquals(mocks.get(1), filter.filterCandidate(mocks, createField(mockName2.toString()), fieldInstance));
	}

	@Test
	public void testFilterWithNoMatchingMocks() {
		Field field = createField("NonExistentMockName");
		assertNull(filter.filterCandidate(mocks, field, fieldInstance));
	}

	@Test
	public void testFilterWithConflictingMocks() {
		Field conflictingField = createField("conflictingFieldName");
		MockName mockName1 = mockNameProvider.getMockName(mocks.get(0));
		MockName mockName2 = mockNameProvider.getMockName(mocks.get(1));
		assertEquals(mocks.get(1), filter.filterCandidate(mocks, conflictingField, fieldInstance));
		assertEquals(mockName2.toString(), conflictingField.getName());
	}

	@Test
	public void testNextFilterIsCalledWhenMultipleMocksMatch() {
		Field field = createField("fieldName");
		when(nextMockFilter.filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance)).thenReturn(fieldInstance);
		when(nextMockFilter.filterCandidate(Collections.singletonList(mocks.get(1)), field, fieldInstance)).thenReturn(null);

		assertEquals(fieldInstance, filter.filterCandidate(mocks, field, fieldInstance));
		verify(nextMockFilter).filterCandidate(Collections.singletonList(mocks.get(0)), field, fieldInstance);
		verifyNoMoreInteractions(nextMockFilter);
	}

	@Test
	public void testNextFilterIsCalledWhenNoMocksMatch() {
		Field field = createField("NonExistentMockName");
		when(nextMockFilter.filterCandidate(Collections.emptyList(), field, fieldInstance)).thenReturn(fieldInstance);

		assertEquals(fieldInstance, filter.filterCandidate(mocks, field, fieldInstance));
		verify(nextMockFilter).filterCandidate(Collections.emptyList(), field, fieldInstance);
	}

	private Object createMock(String name) {
		try {
			return ReflectUtils.newProxyInstance(Class.forName("org.mockito.Mockito"), new Class[]{MockName.class},
					new MockNameProvider() {
						@Override
						public MockName getMockName(Object mockObject) {
							return new MockName(Objects.requireNonNull(name), EXACT);
						}
					}, name);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	private Field createField(String name) {
		try {
			return FieldAccessUtil.getField(getClass(), name);
		} catch (FieldAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static class MockNameProvider implements NameMatchingStrategy {
		@Override
		public MockName getMockName(Object mockObject) {
			return new MockName("mockName", EXACT);
		}
	}
}