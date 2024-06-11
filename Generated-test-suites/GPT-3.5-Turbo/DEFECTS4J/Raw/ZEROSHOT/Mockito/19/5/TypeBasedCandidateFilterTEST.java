import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Setup
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        field.setAccessible(true);
        Field field = this.getClass().getDeclaredField("mocks");
        Object fieldInstance = null;

        // Test
        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    }
}