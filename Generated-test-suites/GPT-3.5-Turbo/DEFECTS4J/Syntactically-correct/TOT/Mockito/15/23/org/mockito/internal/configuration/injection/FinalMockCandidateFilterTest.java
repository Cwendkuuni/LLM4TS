import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        field = null;
        fieldInstance = null;
    }

    @Test
    public void testFilterCandidate_OneMock_ReturnsOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        assertTrue(result.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ReturnsOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        assertFalse(result.thenInject());
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
        assertFalse(result.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_ExceptionOnInjectingDependency_ThrowsMockitoException() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        field = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);

        Mockito.when(field.getName()).thenReturn("fieldName");
        Mockito.when(fieldInstance.getClass()).thenThrow(new MockitoException("Exception on injecting dependency"));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.thenInject());
    }
}