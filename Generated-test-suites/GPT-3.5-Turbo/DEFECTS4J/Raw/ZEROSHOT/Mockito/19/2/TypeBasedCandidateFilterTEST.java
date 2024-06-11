import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        List<Object> mocks = new ArrayList<Object>();
        Field field = new Field();
        Object fieldInstance = new Object();
        
        MockCandidateFilter nextMockCandidateFilter = new MockCandidateFilter() {
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(nextMockCandidateFilter);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(result);
    }
}