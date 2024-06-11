import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Test with empty mocks collection
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);
        
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify that next filterCandidate() method is invoked with empty mockTypeMatches
        assertEquals(mocks, result.mocks);
        assertEquals(field, result.field);
        assertEquals(fieldInstance, result.fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMatchingMocks() throws Exception {
        // Test with mocks that have a matching type
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        // Perform the filtering
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify that next filterCandidate() method is invoked with filtered mockTypeMatches
        assertEquals(mocks, result.mocks);
        assertEquals(field, result.field);
        assertEquals(fieldInstance, result.fieldInstance);
    }

    @Test
    public void testFilterCandidateWithNoMatchingMocks() throws Exception {
        // Test with mocks that do not have a matching type
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(next);

        // Perform the filtering
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify that next filterCandidate() method is invoked with an empty mockTypeMatches
        assertEquals(new ArrayList<>(), result.mocks);
        assertEquals(field, result.field);
        assertEquals(fieldInstance, result.fieldInstance);
    }

}