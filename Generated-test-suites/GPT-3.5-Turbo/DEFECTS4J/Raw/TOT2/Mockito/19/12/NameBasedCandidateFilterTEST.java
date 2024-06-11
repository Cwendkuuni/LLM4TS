import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

        Field field = null;
        Object fieldInstance = null;

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFilterCandidateWithNoMatchingName() {
        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);

        Field field = null;
        Object fieldInstance = null;

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFilterCandidateWithConflictingNamingFields() {
        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Field field = new Field("fieldName");
        Object fieldInstance = null;

        Collection<Object> result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}