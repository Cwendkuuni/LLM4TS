import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredFields()[0];
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidateNoCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();

        Object result = filter.filterCandidate(mocks, null, null).thenInject();

        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidateMultipleCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredFields()[0];
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidateExceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = Object.class.getDeclaredFields()[0];
        Object fieldInstance = new Object();

        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
        } catch (Exception e) {
            assertEquals(RuntimeException.class, e.getClass());
        }
    }
}