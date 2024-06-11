package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilSpy;
	private Stubber stubber;
	private Object fieldInstance;
	private Field field;
	private Collection<Object> mocks;

	@Rule
	public MockitoTestRule mockitoRule = new MockitoTestRule();

	@Before
	public void setUp() {
		nextMock = mock(MockCandidateFilter.class);
		mockUtilSpy = spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextMock);
		field = mock(Field.class);
		mocks = new ArrayList<>();
		stubber = mockitoRule.stubber();
		fieldInstance = mock(Object.class);
	}

	@Test
	public void testFilterCandidate_singleMock_nextFilterCalled() {
		when(mockUtilSpy.getMockName(mocks.get(0))).thenReturn("mockName");
		Answer<OngoingInjecter> answer = stubber.answers(new Answer<OngoingInjecter>() {
			@Override
			public OngoingInjecter answer(InvocationOnMock invocation) {
				return null;
			}
		});
		stubber.doAnswer(answer).when(nextMock).filterCandidate(mocks, field, fieldInstance);

		filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multiMocks_nextFilterCalledWithCorrectMock() {
		when(mockUtilSpy.getMockName(mocks.get(0))).thenReturn("mockName1");
		when(mockUtilSpy.getMockName(mocks.get(1))).thenReturn("mockName2");

		mocks.add(mock(Object.class));

		Answer<OngoingInjecter> answer = stubber.answers(new Answer<OngoingInjecter>() {
			@Override
			public OngoingInjecter answer(InvocationOnMock invocation) {
				return null;
			}
		});
		stubber.doAnswer(answer).when(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));

		filter.filterCandidate(mocks, field, fieldInstance);

		verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
		verify(nextMock).filterCandidate(contains(mocks.get(1)), eq(field), eq(fieldInstance));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFilterCandidate_noMocks_throwException() {
		filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
	}
}