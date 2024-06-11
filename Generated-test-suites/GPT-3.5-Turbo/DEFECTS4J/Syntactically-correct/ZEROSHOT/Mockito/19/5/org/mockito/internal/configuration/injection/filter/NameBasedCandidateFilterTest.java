import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        // Create a mock for the next filter
        next = Mockito.mock(MockCandidateFilter.class);
        
        // Create an instance of the filter
        filter = new NameBasedCandidateFilter(next);
        
        // Create a mock util
        mockUtil = Mockito.mock(MockUtil.class);
    }
    
    @Test
    public void testFilterCandidateWithNoMatches() throws NoSuchFieldException {
        // Create mocks
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        
        // Create a field and field instance
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();
        
        // Mock the behavior of mockUtil.getMockName()
        when(mockUtil.getMockName(mock1)).thenReturn(null);
        when(mockUtil.getMockName(mock2)).thenReturn(null);
        
        // Call the method under test
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify the mockUtil.getMockName calls
        verify(mockUtil, times(1)).getMockName(mock1);
        verify(mockUtil, times(1)).getMockName(mock2);
        
        // Verify the next.filterCandidate call
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        
        // Verify the result
        assertEquals(result, next.filterCandidate(mocks, field, fieldInstance));
    }
    
    @Test
    public void testFilterCandidateWithMatches() throws NoSuchFieldException {
        // Create mocks
        Object mock1 = new Object();
        Object mock2 = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        
        // Create a field and field instance
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();
        
        // Mock the behavior of mockUtil.getMockName() and field.getName()
        when(mockUtil.getMockName(mock1)).thenReturn("next");
        when(mockUtil.getMockName(mock2)).thenReturn("next");
        when(field.getName()).thenReturn("next");
        
        // Call the method under test
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        
        // Verify the mockUtil.getMockName and field.getName() calls
        verify(mockUtil, times(2)).getMockName(any());
        verify(field, times(1)).getName();
        
        // Verify the next.filterCandidate call
        verify(next, times(1)).filterCandidate(any(), eq(field), eq(fieldInstance));
        
        // Verify the result
        assertEquals(result, next.filterCandidate(any(), eq(field), eq(fieldInstance)));
    }
}