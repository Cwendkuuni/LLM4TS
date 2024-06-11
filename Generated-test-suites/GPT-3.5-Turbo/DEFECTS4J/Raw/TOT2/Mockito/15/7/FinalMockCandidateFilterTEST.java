import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(ongoingInjecter.thenInject());

    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = new Object() {
            public void customMethod() {
                // Custom method for creating an exception
            }
        };
        filter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}