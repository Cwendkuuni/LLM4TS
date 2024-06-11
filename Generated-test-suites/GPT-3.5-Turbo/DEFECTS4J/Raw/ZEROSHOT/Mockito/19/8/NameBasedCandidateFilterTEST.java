import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
	private MockUtil mockUtil;
	private NameBasedCandidateFilter nameBasedCandidateFilter;
	private MockCandidateFilter next;

	@Before
	public void setUp() {
		mockUtil = new MockUtil();
		next = mock(MockCandidateFilter.class);
		nameBasedCandidateFilter = new NameBasedCandidateFilter(next);
	}

	@Test
	public void testFilterCandidate_singleMock() throws Exception {
		Collection<Object> mocks = new ArrayList<Object>();
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = new Object();
		Object mock = Mockito.mock(Object.class);
		mocks.add(mock);

		when(field.getName()).thenReturn("mock");

		nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(next).filterCandidate(mocks, field, fieldInstance);
	}

	@Test
	public void testFilterCandidate_multipleMocks() throws Exception {
		Collection<Object> mocks = new ArrayList<Object>();
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = new Object();
		Object mock1 = Mockito.mock(Object.class);
		Object mock2 = Mockito.mock(Object.class);
		Object mock3 = Mockito.mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);

		when(field.getName()).thenReturn("mock");

		nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(next).filterCandidate(anyListOf(Object.class), eq(field), eq(fieldInstance));
	}

	@Test
	public void testFilterCandidate_multipleMocks_noMatch() throws Exception {
		Collection<Object> mocks = new ArrayList<Object>();
		Field field = Mockito.mock(Field.class);
		Object fieldInstance = new Object();
		Object mock1 = Mockito.mock(Object.class);
		Object mock2 = Mockito.mock(Object.class);
		Object mock3 = Mockito.mock(Object.class);
		mocks.add(mock1);
		mocks.add(mock2);
		mocks.add(mock3);

		when(field.getName()).thenReturn("notMock");

		nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

		verify(next).filterCandidate(mocks, field, fieldInstance);
	}
}