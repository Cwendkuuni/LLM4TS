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
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(field, times(2)).getType();
        verify(next, times(1)).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidateNoMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(field, times(2)).getType();
        verify(next, times(1)).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
    }
}