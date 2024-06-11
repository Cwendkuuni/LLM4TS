import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter nameBasedCandidateFilter;
    
    @Before
    public void setUp() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);
    }
    
    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();
        Object fieldInstance = new Object();
        
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(result);
    }
}