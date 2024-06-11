import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filterMock;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filterMock = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_withNoMocks_shouldReturnOngoingInjecterWithEmptyMockList() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filterMock.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.getMocks().isEmpty());
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withMocksOfTypeDifferentFromField_shouldNotAddToMockTypeMatches() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Double.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filterMock.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.getMocks().isEmpty());
        verify(nextMock).filterCandidate(ongoingInjecter.getMocks(), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withMocksOfTypeAssignableFromField_shouldAddToMockTypeMatches() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Number.class);
        Object mock2 = mock(Double.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Number.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filterMock.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(ongoingInjecter);
        assertFalse(ongoingInjecter.getMocks().isEmpty());
        assertEquals(2, ongoingInjecter.getMocks().size());
        verify(nextMock).filterCandidate(ongoingInjecter.getMocks(), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_withNonEmptyTypeBasedCandidateFilter_shouldCallNextMockFilterCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(nextMock.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        // Act
        OngoingInjecter result = filterMock.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(ongoingInjecter, result);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}