import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_typical() {
        // Arrange
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(List.class));
        Field field = getField();
        Object fieldInstance = null;

        // Act
        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_edge() {
        // Arrange
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(List.class));
        Field field = getField();
        Object fieldInstance = null;

        // Act
        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_error() {
        // Arrange
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(List.class));
        Field field = getField();
        Object fieldInstance = null;

        // Act
        typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(mockCandidateFilter, times(1)).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    private Field getField() {
        Field field = null;
        try {
            field = TypeBasedCandidateFilter.class.getDeclaredField("next");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }
}