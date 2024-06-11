import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        mocks.add(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }

    @Test(expected=MockitoException.class)
    public void testFilterCandidate_exception() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        mocks.add(new Object());
        mocks.add(new Object());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }
}