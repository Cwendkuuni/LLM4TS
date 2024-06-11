import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import static org.mockito.Mockito.*;


public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    
    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        
        mocks.addAll(Arrays.asList(mock1, mock2, mock3));
        
        when(field.getType().isAssignableFrom(Object.class)).thenReturn(true);
        when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock3.getClass())).thenReturn(true);
        
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);
        
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        verify(field, times(4)).getType();
        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        
        assertEquals(ongoingInjecter, result);
    }
}