import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    // Mock objects
    private MockCandidateFilter mockCandidateFilterMock;
    private Field fieldMock;
    private Object fieldInstanceMock;
    private Object mock1;
    private Object mock2;

    // Class under test
    private NameBasedCandidateFilter nameBasedCandidateFilter;

    @Test
    public void testFilterCandidate_withMultipleMocksAndMatchingName_shouldReturnMatchingMocks() {
        // Arrange
        List<Object> mockList = new ArrayList<>();
        mockList.add(mock1);
        mockList.add(mock2);

        when(fieldMock.getName()).thenReturn("mock1");

        MockUtil mockUtilMock = mock(MockUtil.class);
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock2)).thenReturn("mock2");

        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mockList, fieldMock, fieldInstanceMock);

        // Assert
        assertEquals(1, result.size());
        verify(mock1, never()).doSomething();
        verify(mock2, never()).doSomething();
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndNonMatchingName_shouldReturnEmptyList() {
        // Arrange
        List<Object> mockList = new ArrayList<>();
        mockList.add(mock1);
        mockList.add(mock2);

        when(fieldMock.getName()).thenReturn("nonMatchingName");

        MockUtil mockUtilMock = mock(MockUtil.class);
        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock2)).thenReturn("mock2");

        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mockList, fieldMock, fieldInstanceMock);

        // Assert
        assertEquals(0, result.size());
        verify(mock1, never()).doSomething();
        verify(mock2, never()).doSomething();
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnOriginalList() {
        // Arrange
        List<Object> mockList = new ArrayList<>();
        mockList.add(mock1);

        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mockList, fieldMock, fieldInstanceMock);

        // Assert
        assertEquals(mockList.size(), result.size());
        verify(mock1, never()).doSomething();
    }

    @Test
    public void testFilterCandidate_withNoMocks_shouldReturnEmptyList() {
        // Arrange
        List<Object> mockList = new ArrayList<>();

        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mockList, fieldMock, fieldInstanceMock);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testFilterCandidate_withMultipleMocksAndMatchingName_shouldReturnMatchingMocks_whenFieldIsNull() {
        // Arrange
        List<Object> mockList = new ArrayList<>();
        mockList.add(mock1);
        mockList.add(mock2);

        when(mockUtilMock.getMockName(mock1)).thenReturn("mock1");
        when(mockUtilMock.getMockName(mock2)).thenReturn("mock2");

        nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilterMock);

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mockList, null, fieldInstanceMock);

        // Assert
        assertEquals(2, result.size());
        verify(mock1, never()).doSomething();
        verify(mock2, never()).doSomething();
    }
}