
import org.junit.Test;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void filterCandidate_withNoMocks_shouldReturnNextFilter() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(nextFilter);
        assertEquals(result, nextFilter);
    }

    @Test
    public void filterCandidate_withMatchingMocks_shouldReturnNextFilter() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Object.class);

        Object fieldInstance = mock(Object.class);

        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(field, times(2)).getType();
        verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, nextFilter);
    }

    @Test
    public void filterCandidate_withNonMatchingMocks_shouldNotReturnNextFilter() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Integer.class);

        Object fieldInstance = mock(Object.class);

        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextFilter);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(field, times(2)).getType();
    }

}

