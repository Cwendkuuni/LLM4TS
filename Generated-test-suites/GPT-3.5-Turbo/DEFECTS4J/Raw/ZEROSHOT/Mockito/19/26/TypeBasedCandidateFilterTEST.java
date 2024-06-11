import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        List<Object> mockTypeMatches = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Field.class);
        mockTypeMatches.add(mock1);
        mockTypeMatches.add(mock2);

        when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(false);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);

        assertEquals(ongoingInjecter, result);
    }

}