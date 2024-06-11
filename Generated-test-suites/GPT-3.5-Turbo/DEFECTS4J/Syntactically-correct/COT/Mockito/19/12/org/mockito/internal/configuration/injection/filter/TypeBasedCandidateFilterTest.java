import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.util.Collection;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        MockCandidateFilter nextMock = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);

        // Exercise
        filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        verify(nextMock).filterCandidate(new ArrayList<Object>(), field, fieldInstance);
    }
}