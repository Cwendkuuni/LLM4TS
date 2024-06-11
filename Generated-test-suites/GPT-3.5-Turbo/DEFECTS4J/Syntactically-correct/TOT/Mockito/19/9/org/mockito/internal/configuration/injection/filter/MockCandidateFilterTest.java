import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

public class MockCandidateFilterTest {
    
    private MockCandidateFilter mockCandidateFilter;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    private Collection<Object> mocks;
    
    @Before
    public void setUp() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
        mocks = new ArrayList<>();
    }
    
    @Test
    public void testFilterCandidate_Typical() {
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(ongoingInjecter);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(ongoingInjecter, result);
    }
    
    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        when(mockCandidateFilter.filterCandidate(mocks, null, fieldInstance))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, null, fieldInstance);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, null, fieldInstance);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullFieldInstance() {
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, null);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, null);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullMocksCollection() {
        when(mockCandidateFilter.filterCandidate(null, fieldToBeInjected, fieldInstance))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(null, fieldToBeInjected, fieldInstance);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(null, fieldToBeInjected, fieldInstance);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullMocksCollectionAndFieldToBeInjected() {
        when(mockCandidateFilter.filterCandidate(null, null, fieldInstance))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(null, null, fieldInstance);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(null, null, fieldInstance);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullMocksCollectionAndFieldInstance() {
        when(mockCandidateFilter.filterCandidate(null, fieldToBeInjected, null))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(null, fieldToBeInjected, null);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(null, fieldToBeInjected, null);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullFieldToBeInjectedAndFieldInstance() {
        when(mockCandidateFilter.filterCandidate(mocks, null, null))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, null, null);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, null, null);
        assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_NullMocksCollectionAndFieldToBeInjectedAndFieldInstance() {
        when(mockCandidateFilter.filterCandidate(null, null, null))
                .thenReturn(null);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(null, null, null);
        
        verify(mockCandidateFilter, times(2)).filterCandidate(null, null, null);
        assertNull(result);
    }
}