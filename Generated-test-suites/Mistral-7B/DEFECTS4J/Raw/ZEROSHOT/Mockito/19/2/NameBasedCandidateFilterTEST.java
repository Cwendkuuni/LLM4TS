package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.testAnswer;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilSpy;
	private Object fieldInstance;
	private Field field;
	private Collection<Object> mocks;

	@Rule
	public Answer<Object> answer = testAnswer(new Object() {});

	@Before
	public void setUp() {
		nextMock = Mockito.mock(MockCandidateFilter.class);
		mockUtilSpy = Mockito.spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextMock);
		field = Mockito.mock(Field.class);
		mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		mocks.add(mock(Object.class));
		fieldInstance = mock(Object.class);
	}

	@Test
	public void testFilterCandidateWithSingleMock() {
		// Given
		String mockName = "mockName";
		Mockito.doReturn(mockName).when(mockUtilSpy).getMockName(Mockito.any(Object.class));

		// When
		OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		verify(answer).answers(Mockito.any());
		assertNull(actual);
	}

	@Test
	public void testFilterCandidateWithExactNameMatch() {
		// Given
		String mockName = "mockName";
		Mockito.doReturn(mockName).when(mockUtilSpy).getMockName(Mockito.any(Object.class));

		// When
		Object mockWithMatchingName = mock(Object.class);
		mocks.set(0, mockWithMatchingName);
		OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(Collections.singletonList(mockWithMatchingName), field, fieldInstance);
		verify(answer).answers(Mockito.any());
		assertEquals(mockWithMatchningName, actual.getMock());
	}

	@Test
	public void testFilterCandidateWithMultipleMocksWithSameName() {
		// Given
		String mockName = "mockName";
		Mockito.doReturn(mockName).when(mockUtilSpy).getMockName(Mockito.any(Object.class));

		// When
		OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		verify(answer).answers(Mockito.any());
		assertNull(actual);
	}

	@Test
	public void testFilterCandidateWithMultipleMocksWithDifferentNames() {
		// Given
		String mockName1 = "mockName1";
		String mockName2 = "mockName2";
		Mockito.doReturn(mockName1).when(mockUtilSpy).getMockName(Mockito.any(Object.class));
		Mockito.doReturn(mockName2).when(mockUtilSpy).getMockName(Mockito.eq(mocks.get(1)));

		// When
		OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(Collections.singletonList(mocks.get(1)), field, fieldInstance);
		verify(answer).answers(Mockito.any());
		assertEquals(mocks.get(1), actual.getMock());
	}
}