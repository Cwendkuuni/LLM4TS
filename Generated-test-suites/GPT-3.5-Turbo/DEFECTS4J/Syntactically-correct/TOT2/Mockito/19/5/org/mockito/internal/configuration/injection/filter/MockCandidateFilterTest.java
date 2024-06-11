import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter(new ArrayList<>());
            }
        };
        
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field();
        Object fieldInstance = new Object();
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertNotNull(result);
    }

}