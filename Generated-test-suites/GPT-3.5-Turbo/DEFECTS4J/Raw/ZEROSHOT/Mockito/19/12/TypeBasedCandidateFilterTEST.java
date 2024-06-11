import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
    }

    @Test
    public void filterCandidate_shouldReturnNextFilterResult() {
        // Arrange
        List<Object> mockTypeMatches = new ArrayList<>();
        when(mockCandidateFilter.filterCandidate(anyList(), any(Field.class), any(Object.class))).thenReturn(mock(OngoingInjecter.class));
        when(field.getType()).thenReturn(Object.class);
        mocks.add("mock1");
        mocks.add("mock2");

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(mockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);
    }

    @Test
    public void filterCandidate_shouldAddMatchingMocksToList() {
        // Arrange
        List<Object> mockTypeMatches = new ArrayList<>();
        when(mockCandidateFilter.filterCandidate(anyList(), any(Field.class), any(Object.class))).thenReturn(mock(OngoingInjecter.class));
        when(field.getType()).thenReturn(String.class);
        mocks.add("mock1");
        mocks.add(1);

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(mockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);
        assertEquals("Mock type matches should contain 1 element", 1, mockTypeMatches.size());
        assertEquals("First element in mock type matches should be 'mock1'", "mock1", mockTypeMatches.get(0));
    }

    @Test
    public void filterCandidate_shouldReturnEmptyListIfNoMatchingMocks() {
        // Arrange
        List<Object> mockTypeMatches = new ArrayList<>();
        when(mockCandidateFilter.filterCandidate(anyList(), any(Field.class), any(Object.class))).thenReturn(mock(OngoingInjecter.class));
        when(field.getType()).thenReturn(Integer.class);
        mocks.add("mock1");
        mocks.add(1);

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull("Result should not be null", result);
        verify(mockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);
        assertTrue("Mock type matches should be empty", mockTypeMatches.isEmpty());
    }
}