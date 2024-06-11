import org.junit.Test;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = mock(OngoingInjecter.class);
        when(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance)).thenReturn(injecter);

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(injecter, result);
        verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
    }

}