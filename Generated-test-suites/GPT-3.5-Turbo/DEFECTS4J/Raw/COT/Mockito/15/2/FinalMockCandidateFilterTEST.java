
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_injectException_throwsMockitoException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

}
