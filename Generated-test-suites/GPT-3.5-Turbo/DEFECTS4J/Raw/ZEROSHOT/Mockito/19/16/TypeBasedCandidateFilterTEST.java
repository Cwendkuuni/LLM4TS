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
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_withMatchingType_shouldFilterCandidates() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        when(field.getType()).thenReturn(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        verify(next).filterCandidate(Collections.singletonList(mock2), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withNonMatchingType_shouldNotFilterCandidates() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        when(field.getType()).thenReturn(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}