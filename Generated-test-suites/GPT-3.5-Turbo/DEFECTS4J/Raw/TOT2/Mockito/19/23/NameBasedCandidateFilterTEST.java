import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    
    @Test
    public void testFilterCandidate() {
        MockUtil mockUtil = new MockUtil();
        MockCandidateFilter nextMockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextMockCandidateFilter);

        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }
}