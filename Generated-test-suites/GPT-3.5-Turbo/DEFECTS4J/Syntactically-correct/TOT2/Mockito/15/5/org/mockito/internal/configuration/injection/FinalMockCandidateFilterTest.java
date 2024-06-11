import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        // Pass a null Field instance to force an exception
        filter.filterCandidate(mocks, null, fieldInstance);
    }
}