

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = FinalMockCandidateFilter.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
        assertEquals(mocks.iterator().next(), field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = FinalMockCandidateFilter.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
        assertNull(field.get(fieldInstance));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = FinalMockCandidateFilter.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        field.setAccessible(false); // Make the field inaccessible to cause an exception

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }
}

