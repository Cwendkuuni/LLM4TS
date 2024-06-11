package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.plugins.ClassFilter;
import org.mockito.plugins.PluginManager;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerMockitoCore;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextMock;
	private MockUtil mockUtilMock;
	private Collection<Object> mocks;
	private Field field;
	private Object fieldInstance;

	@Before
	public void setUp() {
		nextMock = mock(MockCandidateFilter.class);
		mockUtilMock = spy(new MockUtil());
		filter = new NameBasedCandidateFilter(nextMock);
		mocks = new ArrayList<>();
		mocks.add(mock(Object.class));
		mocks.add(mock(Object.class));
		field = Mockito.mock(Field.class);
		fieldInstance = mock(Object.class);
	}

	@Test
	public void testFilterCandidate_MocksWithSameName_ReturnsCorrectMock() {
		String mockName = "mockName";
		Mockito.doReturn(mockName).when(mockUtilMock).getMockName(mocks.get(0));
		Mockito.doReturn(mockName).when(mockUtilMock).getMockName(mocks.get(1));

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mocks.get(0), result);
	}

	@Test
	public void testFilterCandidate_MultipleMocksWithSameName_DelegatesToNextFilter() {
		String mockName = "mockName";
		Mockito.doReturn(mockName).when(mockUtilMock).getMockName(mocks.get(0));
		Mockito.doReturn(mockName).when(mockUtilMock).getMockName(mocks.get(1));

		Mockito.doReturn(mock(Object.class)).when(nextMock).filterCandidate(mocks, field, fieldInstance);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock(Object.class), result);
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_NoConflictingNames_DelegatesToNextFilter() {
		String mockName1 = "mockName1";
		Mockito.doReturn(mockName1).when(mockUtilMock).getMockName(mocks.get(0));
		String mockName2 = "mockName2";
		Mockito.doReturn(mockName2).when(mockUtilMock).getMockName(mocks.get(1));

		Mockito.doReturn(mock(Object.class)).when(nextMock).filterCandidate(mocks, field, fieldInstance);

		Object result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock(Object.class), result);
		verify(nextMock).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_NullMocks_ReturnsNull() {
		assertNull(filter.filterCandidate(null, field, fieldInstance));
	}

	@Test
	public void testFilterCandidate_NullField_ReturnsNull() {
		assertNull(filter.filterCandidate(mocks, null, fieldInstance));
	}

	@Test
	public void testFilterCandidate_NullFieldInstance_ReturnsNull() {
		assertNull(filter.filterCandidate(mocks, field, null));
	}
}