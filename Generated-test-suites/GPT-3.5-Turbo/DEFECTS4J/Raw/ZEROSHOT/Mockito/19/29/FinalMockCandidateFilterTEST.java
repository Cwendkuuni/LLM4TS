import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.*;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidateSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        assertNotNull(result);
        assertEquals(mock, result);
    }
    
    @Test
    public void testFilterCandidateMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = null;
        Object fieldInstance = null;
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        assertNull(result);
    }
}