package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

	private final MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
	private final NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMock);
	private static final String MOCK_NAME = "mockName";
	private static final String FIELD_NAME = "fieldName";
	private static final Object FIELD_INSTANCE = new Object();
	private static final Object MOCK_1 = mock(Object.class);
	private static final Object MOCK_2 = mock(Object.class);
	private static final Object MOCK_3 = mock(Object.class);
	private static final Object CONFLICTING_MOCK = mock(Object.class);

	@Rule
	public Answer2<Object, Field> answer = new Answer2<Object, Field>() {
		@Override
		public Object answer(InvocationOnMock invocation) throws Throwable {
			return invocation.getArgument(1);
		}

		@Override
		public Field answer(InvocationOnMock invocation) throws Throwable {
			return (Field) invocation.getArgument(0);
		}
	};

	@Test
	public void testEmptyCollection() {
		assertEquals(nextMock, filter.filterCandidate(new ArrayList<Object>(), Mockito.spy(new Field("", FIELD_NAME, null, null, null, null)), FIELD_INSTANCE));
	}

	@Test
	public void testFilterCandidate_noMatch_nextFilter() {
		when(nextMock.filterCandidate(Collections.singletonList(MOCK_1), Mockito.spy(new Field(FIELD_NAME, null, null, null, null, null)), FIELD_INSTANCE)).thenReturn(MOCK_1);

		assertEquals(MOCK_1, filter.filterCandidate(Collections.singletonList(MOCK_1), new Field(FIELD_NAME, null, null, null, null, null), FIELD_INSTANCE));
	}

	@Test
	public void testFilterCandidate_matchingName_returnMatchingMock() {
		when(nextMock.filterCandidate(new ArrayList<Object>(), Mockito.spy(new Field(FIELD_NAME, null, null, null, null, null)), FIELD_INSTANCE)).thenReturn(MOCK_2);

		when(mockUtil.getMockName(MOCK_1)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(MOCK_2)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(MOCK_3)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(CONFLICTING_MOCK)).thenReturn("conflictingMockName");

		assertEquals(MOCK_1, filter.filterCandidate(Arrays.asList(MOCK_1, MOCK_2, MOCK_3, CONFLICTING_MOCK), new Field(FIELD_NAME, null, null, null, null, null), FIELD_INSTANCE));
	}

	@Test
	public void testFilterCandidate_conflictingName_nextFilter() {
		when(nextMock.filterCandidate(new ArrayList<Object>(), Mockito.spy(new Field(FIELD_NAME, null, null, null, null, null)), FIELD_INSTANCE)).thenReturn(MOCK_3);

		when(mockUtil.getMockName(MOCK_1)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(MOCK_2)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(MOCK_3)).thenReturn(MOCK_NAME);
		when(mockUtil.getMockName(CONFLICTING_MOCK)).thenReturn("conflictingMockName");

		assertEquals(MOCK_3, filter.filterCandidate(Arrays.asList(MOCK_1, MOCK_2, MOCK_3, CONFLICTING_MOCK), new Field(FIELD_NAME, null, null, null, null, null), FIELD_INSTANCE));
	}
}