import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(String.class));
        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(anyList(), any(Field.class), any())).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyList(), any(Field.class), any());
        assertEquals(ongoingInjecter, result);
    }
}