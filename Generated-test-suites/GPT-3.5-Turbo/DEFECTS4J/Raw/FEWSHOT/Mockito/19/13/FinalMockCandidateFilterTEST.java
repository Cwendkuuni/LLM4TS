import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        
        Object result = ongoingInjecter.thenInject();
        
        assertEquals(mock, result);
        
        verify(mockitoReporter, never()).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        
        Object result = ongoingInjecter.thenInject();
        
        assertNull(result);
        
        verify(mockitoReporter, times(1)).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
    }
}