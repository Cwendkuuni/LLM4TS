import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockNext = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter(null);
            }
        };

        NameBasedCandidateFilter candidateFilter = new NameBasedCandidateFilter(mockNext);

        Collection<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }
}