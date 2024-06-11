import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {
    private MockCandidateFilter mockCandidateFilter;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        fieldToBeInjected = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = new ArrayList<>();

        OngoingInjecter injecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(injecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter, times(2)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));
    }
}