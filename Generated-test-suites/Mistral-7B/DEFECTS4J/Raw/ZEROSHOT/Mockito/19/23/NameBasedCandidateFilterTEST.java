package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockInfo;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import org.mockito.plugins.MockMakerUtil;
import org.mockito.plugins.MockMakerUtilProperties;

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
	private MockUtil mockUtil;
	private MockMaker mockMaker;
	private MockCandidateFilter nextMock;

	@Before
	public void setUp() {
		mockUtil = spy(new MockUtil());
		mockMaker = new MockMakerImpl();
		nextMock = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextMock);
	}

	@Test
	public void testFilterCandidateWithMultipleMocksAndMatchingFieldName() {
		// Given
		String mockName1 = "mockName1";
		String mockName2 = "mockName1";
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, MockCreationSettings.defaultSettings()
				.mockName(mockName1)));
		mocks.add(mock(Object.class, MockCreationSettings.defaultSettings()
				.mockName(mockName2)));
		Field field = new Field("field", Object.class, null);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		verify(mocks.get(0)).equals(result.getMock());
	}

	@Test
	public void testFilterCandidateWithMultipleMocksAndDifferingFieldNames() {
		// Given
		String mockName1 = "mockName1";
		String mockName2 = "mockName2";
		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock(Object.class, MockCreationSettings.defaultSettings()
				.mockName(mockName1)));
		mocks.add(mock(Object.class, MockCreationSettings.defaultSettings()
				.mockName(mockName2)));
		Field field1 = new Field("field1", Object.class, null);
		Field field2 = new Field("field2", Object.class, null);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field2, fieldInstance);
		verify(mocks.get(1)).equals(result.getMock());
	}

	@Test
	public void testFilterCandidateWithSingleMock() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Object mock = mock(Object.class, MockCreationSettings.defaultSettings());
		mocks.add(mock);
		Field field = new Field("field", Object.class, null);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		verify(mock).equals(result.getMock());
	}

	@Test
	public void testFilterCandidateWithNoMocks() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field("field", Object.class, null);
		Object fieldInstance = new Object();

		// When
		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

		// Then
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
		assertNull(result);
	}

	@Test
	public void testFilterCandidateWithNullFields() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field field = null;
		Object fieldInstance = new Object();

		// When & Then
		assertNull(filter.filterCandidate(mocks, field, fieldInstance));
		verify(nextMock, never()).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidateWithNullMockCollection() {
		// Given
		Field field = new Field("field", Object.class, null);
		Object fieldInstance = new Object();

		// When & Then
		assertNull(filter.filterCandidate(null, field, fieldInstance));
		verify(nextMock, never()).filterCandidate(null, field, fieldInstance);
	}

	@Test
	public void testFilterCandidateWithNullMock() {
		// Given
		Collection<Object> mocks = new ArrayList<>();
		Field field = new Field("field", Object.class, null);
		Object fieldInstance = new Object();
		Object nullMock = null;

		// When & Then
		assertNull(filter.filterCandidate(mocks, field, fieldInstance));
		verify(nextMock, never()).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testGetNextMockCandidateFilter() {
		// Given

		// When
		MockCandidateFilter result = filter.getNextMockCandidateFilter();

		// Then
		assertEquals(nextMock, result);
	}
}