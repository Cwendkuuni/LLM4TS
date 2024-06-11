
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyList(), same(field), same(fieldInstance));
    }

    @Test
    public void testFilterCandidateWithTypeMatch() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);

        when(field.getType().isAssignableFrom(Object.class)).thenReturn(true);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyList(), same(field), same(fieldInstance));
    }

    @Test
    public void testFilterCandidateWithoutTypeMatch() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object mock = mock(Object.class);

        mocks.add(mock);

        when(field.getType().isAssignableFrom(String.class)).thenReturn(false);

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyList(), same(field), same(fieldInstance));
    }
}

