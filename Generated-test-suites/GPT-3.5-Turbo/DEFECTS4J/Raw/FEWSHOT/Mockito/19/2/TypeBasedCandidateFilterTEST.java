import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {
    @Test
    public void testFilterCandidate() {
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

        List<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        when(nextFilter.filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(field).getType();
        verify(mock1).getClass();
        verify(mock2).getClass();
        verify(nextFilter).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
        assertEquals(result, ongoingInjecter);
    }
}