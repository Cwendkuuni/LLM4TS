import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                List<Object> mockTypeMatches = new ArrayList<Object>();
                for (Object mock : mocks) {
                    mockTypeMatches.add(mock);
                }
                return new OngoingInjecter(mockTypeMatches);
            }
        };

        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        assertEquals(0, ongoingInjecter.getMockTypeMatches().size());
    }
}