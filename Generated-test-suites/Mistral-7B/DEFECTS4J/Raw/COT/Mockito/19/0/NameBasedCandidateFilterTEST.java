import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {
	private NameBasedCandidateFilter filter;
	private MockCandidateFilter nextFilter;
	private MockUtil mockUtil;
	private Stubber stubber;

	@Before
	public void setup() {
		mockUtil = mock(MockUtil.class);
		nextFilter = mock(MockCandidateFilter.class);
		filter = new NameBasedCandidateFilter(nextFilter);
		stubber = mockito3(stubber());
	}

	@Test
	public void testFilterCandidate_SingleMock_NoConflict() {
		Object mock = createMock(Object.class);
		when(mockUtil.getMockName(mock)).thenReturn("mockName");

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock);

		Field field = mockitoClass(Field.class);

		Object fieldInstance = new Object();

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock, result.getMock());
		verifyZeroInteractions(nextFilter);
	}

	@Test
	public void testFilterCandidate_MultipleMocks_NoConflict() {
		Object mock1 = createMock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("mockName1");

		Object mock2 = createMock(Object.class);
		when(mockUtil.getMockName(mock2)).thenReturn("mockName2");

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		Field field = mockitoClass(Field.class);

		Object fieldInstance = new Object();

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock1, result.getMock());
		verifyZeroInteractions(nextFilter);
	}

	@Test
	public void testFilterCandidate_MultipleMocks_Conflict() {
		Object mock1 = createMock(Object.class);
		when(mockUtil.getMockName(mock1)).thenReturn("mockName1");

		Object mock2 = createMock(Object.class);
		when(mockUtil.getMockName(mock2)).thenReturn("mockName1");

		Collection<Object> mocks = new ArrayList<>();
		mocks.add(mock1);
		mocks.add(mock2);

		Field field = mockitoClass(Field.class);

		Object fieldInstance = new Object();

		when(nextFilter.filterCandidate(eq(mocks), any(Field.class), eq(fieldInstance)))
				.thenAnswer(new Answer<OngoingInjecter>() {
					@Override
					public OngoingInjecter answer(InvocationOnMock invocation) {
						return new OngoingInjecter(mock2, invocation.getArgumentAt(0, 0));
					}
				});

		OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
		assertEquals(mock2, result.getMock());
		verify(nextFilter).filterCandidate(eq(mocks), any(Field.class), eq(fieldInstance));
	}
}