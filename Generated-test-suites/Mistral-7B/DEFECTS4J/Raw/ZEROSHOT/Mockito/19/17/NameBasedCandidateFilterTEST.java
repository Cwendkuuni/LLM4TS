package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockUtil mockUtil;
	private MockCandidateFilter nextMock;
	private Collection<Object> mocks;
	private Object fieldInstance;

	@Before
	public void setUp() {
		mockUtil = mock(MockUtil.class);
		nextMock = spy(new NameBasedCandidateFilter(null));
		when(nextMock.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class)))
				.thenAnswer(new Answer<OngoingInjecter>() {
					@Override
					public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
						return null;
					}
				});

		filter = new NameBasedCandidateFilter(nextMock);
		mocks = new ArrayList<>();
		fieldInstance = new Object();
	}

	@Test
	public void testFilterCandidateWithOneMockAndFieldNameMatch() {
		Object mock = mock(Object.class);
		String mockName = "mockName";
		when(mockUtil.getMockName(mock)).thenReturn(mockName);
		mocks.add(mock);

		Field field = new Field("testField", String.class, Field.ACCESSIBLE, null, null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertThat(result, is(instanceOf(nextMock.getClass())));
		assertThat(result, is(notNullValue()));
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidateWithOneMockAndFieldNameMismatch() {
		Object mock = mock(Object.class);
		String mockName = "notMockName";
		when(mockUtil.getMockName(mock)).thenReturn(mockName);
		mocks.add(mock);

		Field field = new Field("testField", String.class, Field.ACCESSIBLE, null, null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testFilterCandidateWithMultipleMocksAndNameMatch() {
		Object mock1 = mock(Object.class);
		String mockName1 = "mockName1";
		when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
		mocks.add(mock1);

		Object mock2 = mock(Object.class);
		String mockName2 = "mockName1";
		when(mockUtil.getMockName(mock2)).thenReturn(mockName2);
		mocks.add(mock2);

		Field field = new Field("testField", String.class, Field.ACCESSIBLE, null, null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertThat(result, is(instanceOf(nextMock.getClass())));
		assertThat(result, is(notNullValue()));
		verify(nextMock).filterCandidate(mock1, field, fieldInstance);
	}

	@Test
	public void testFilterCandidateWithMultipleMocksAndNameMismatch() {
		Object mock1 = mock(Object.class);
		String mockName1 = "mockName1";
		when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
		mocks.add(mock1);

		Object mock2 = mock(Object.class);
		String mockName2 = "mockName2";
		when(mockUtil.getMockName(mock2)).thenReturn(mockName2);
		mocks.add(mock2);

		Field field = new Field("testField", String.class, Field.ACCESSIBLE, null, null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertThat(result, is(nullValue()));
	}

	@Test
	public void testFilterCandidateWithMultipleMocksAndConflictingName() {
		Object mock1 = mock(Object.class);
		String mockName1 = "mockName1";
		when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
		mocks.add(mock1);

		Object mock2 = mock(Object.class);
		String mockName2 = mockName1;
		when(mockUtil.getMockName(mock2)).thenReturn(mockName2);
		mocks.add(mock2);

		Field field = new Field("testField", String.class, Field.ACCESSIBLE, null, null);

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		assertThat(result, is(instanceOf(nextMock.getClass())));
		assertThat(result, is(notNullValue()));
		verify(nextMock).filterCandidate(mock2, field, fieldInstance);
	}
}