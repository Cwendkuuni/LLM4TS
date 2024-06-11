import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(ongoingInjecter);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, new Object());

        assertEquals(ongoingInjecter, result);
    }
}