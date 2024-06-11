import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {
	private MockCandidateFilter next;
	private NameBasedCandidateFilter nameBasedCandidateFilter;
	private Field field;
	private Object fieldInstance;
	private MockUtil mockUtil;

	@Before
	public void setUp() {
		next = mock(MockCandidateFilter.class);
		field = mock(Field.class);
		fieldInstance = new Object();
		mockUtil = mock(MockUtil.class);
		nameBasedCandidateFilter = new NameBasedCandidateFilter(next);
		nameBasedCandidateFilter.mockUtil = mockUtil;
	}

	@Test
	public void testFilterCandidate_WithMatchingMockName() {
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		
		when(field.getName()).thenReturn("mockName");
		when(mockUtil.getMockName(mock1)).thenReturn(new StringBuilder("mockName"));
		when(mockUtil.getMockName(mock2)).thenReturn(new StringBuilder("otherName"));
		
		OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		verify(next).filterCandidate(Mockito.anyCollection(), Mockito.eq(field), Mockito.eq(fieldInstance));
	}

	@Test
	public void testFilterCandidate_WithNoMatchingMockName() {
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		Object mock2 = mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		
		when(field.getName()).thenReturn("mockName");
		when(mockUtil.getMockName(mock1)).thenReturn(new StringBuilder("otherName"));
		when(mockUtil.getMockName(mock2)).thenReturn(new StringBuilder("otherName"));
		
		OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_WithSingleMock() {
		Collection<Object> mocks = new ArrayList<Object>();
		Object mock1 = mock(Object.class);
		mocks.add(mock1);
		
		when(field.getName()).thenReturn("mockName");
		when(mockUtil.getMockName(mock1)).thenReturn(new StringBuilder("mockName"));
		
		OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
		
		verify(next).filterCandidate(mocks, field, fieldInstance);
	}
}