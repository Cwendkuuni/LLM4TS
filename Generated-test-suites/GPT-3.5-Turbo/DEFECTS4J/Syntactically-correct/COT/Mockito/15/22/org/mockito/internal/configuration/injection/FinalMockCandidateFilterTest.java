import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock");
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock1");
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_exception() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock");
        Field field = null;
        Object fieldInstance = null;

        try {
            OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
            ongoingInjecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in null", e.getMessage());
        }
    }

}
