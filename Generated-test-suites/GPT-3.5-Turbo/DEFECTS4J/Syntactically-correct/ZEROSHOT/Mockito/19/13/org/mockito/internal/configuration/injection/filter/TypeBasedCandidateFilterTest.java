import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(List.class);
        Object mock3 = mock(ArrayList.class);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(false);
        when(field.getType().isAssignableFrom(mock3.getClass())).thenReturn(true);

        List<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock1);
        mockTypeMatches.add(mock3);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mockTypeMatches, field, fieldInstance);
        assertEquals(result, ongoingInjecter);
    }
}