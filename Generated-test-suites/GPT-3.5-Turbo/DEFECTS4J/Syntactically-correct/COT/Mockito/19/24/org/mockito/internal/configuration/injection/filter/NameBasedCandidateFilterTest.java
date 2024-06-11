
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Setup
        List<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        // Execute
        filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}
