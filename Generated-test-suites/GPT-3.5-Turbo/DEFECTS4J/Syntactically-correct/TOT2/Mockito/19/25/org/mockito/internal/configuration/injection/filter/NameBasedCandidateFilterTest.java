import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock1");

        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(1, result.size());
        assertEquals(mock1, result.get(0));
    }
  
    @Test
    public void testFilterCandidateWithNoMatches() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock2");

        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(0, result.size());
    }
    
    @Test
    public void testFilterCandidateWithMultipleMatches() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        mocks.add(mock1);

        Field field = mock(Field.class);
        when(field.getName()).thenReturn("mock1");

        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(2, result.size());
    }

}