import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_withMatchingFieldType_verifyNextFilterCalled() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_withoutMatchingFieldType_verifyNextFilterNotCalled() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Not a mock");
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, never()).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidate_withNullNextFilter_verifyFilterCandidateReturnsNull() {
        // Arrange
        filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        MockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_withEmptyMockList_verifyNextFilterNotCalled() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, never()).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidate_withInvalidFieldType_verifyNextFilterNotCalled() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField();
        field.setType(String.class);
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, never()).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
    }

    // Helper method to create a Field object with a known type
    private Field getField() {
        try {
            return TypeBasedCandidateFilter.class.getDeclaredField("next");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}