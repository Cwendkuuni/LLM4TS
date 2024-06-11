
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {
    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void filterCandidate_shouldReturnOngoingInjecterFromNextFilter() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyList(), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_shouldOnlyAddMatchingMocksToList() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getType()).thenReturn(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> mockTypeMatches = new ArrayList<>();
        mockTypeMatches.add(mock1);
        mockTypeMatches.add(mock2);
        verify(next).filterCandidate(eq(mockTypeMatches), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_shouldNotAddNonMatchingMocksToList() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getType()).thenReturn(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance));
    }
}
