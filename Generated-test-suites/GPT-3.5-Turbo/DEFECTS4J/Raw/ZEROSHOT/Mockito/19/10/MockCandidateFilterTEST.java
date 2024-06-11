import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilterImpl;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilterImpl mockCandidateFilter = new MockCandidateFilterImpl();

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field field = MockCandidateFilterImpl.class.getDeclaredField("mocks");
        Object instance = new Object();
        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        OngoingInjecter filteredOngoingInjecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, field, instance)).thenReturn(ongoingInjecter);
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, instance);
        
        assertEquals(result, ongoingInjecter);
        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, field, instance);
        verifyNoMoreInteractions(mockCandidateFilter);
    }
}