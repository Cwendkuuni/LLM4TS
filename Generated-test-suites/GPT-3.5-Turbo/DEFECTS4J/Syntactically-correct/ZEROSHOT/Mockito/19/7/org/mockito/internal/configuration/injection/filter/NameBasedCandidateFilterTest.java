import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_OneMockWithMatchingName_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new MockUtil();
        
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)))
            .thenReturn(mock(OngoingInjecter.class));
        
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidate_MultipleMocksWithMatchingNames_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(MockCandidateFilter.class);
        
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)))
            .thenReturn(mock(OngoingInjecter.class));
        
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidate_NoMatchingNames_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new MockUtil();
        
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)))
            .thenReturn(mock(OngoingInjecter.class));
        
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidate_OneMockWithMatchingNameNoConflicts_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(MockCandidateFilter.class);
        
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)))
            .thenReturn(mock(OngoingInjecter.class));
        
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }
    
    @Test
    public void testFilterCandidate_OneMockWithMatchingNameConflicts_ReturnsNextFilter() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = mock(OngoingInjecter.class);
        
        MockCandidateFilter nextFilter = mock(MockCandidateFilter.class);
        when(nextFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)))
            .thenReturn(mock(OngoingInjecter.class));
        
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(nextFilter);
        
        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNotNull(result);
    }
}