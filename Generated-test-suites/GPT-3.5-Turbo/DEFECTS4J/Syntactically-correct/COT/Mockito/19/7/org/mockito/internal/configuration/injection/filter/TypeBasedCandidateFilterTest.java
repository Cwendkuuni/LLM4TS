
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter candidateFilter = new TypeBasedCandidateFilter(nextFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = TypeBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        candidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(nextFilter).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }
}
