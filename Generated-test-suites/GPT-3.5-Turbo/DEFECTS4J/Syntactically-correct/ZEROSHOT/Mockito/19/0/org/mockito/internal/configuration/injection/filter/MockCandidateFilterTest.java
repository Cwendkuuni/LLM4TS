import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;
import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);

        when(mockCandidateFilter.filterCandidate(any(), any(), any())).thenReturn(ongoingInjecter);
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(mockCandidateFilter);

        assertSame(ongoingInjecter, result);
    }
}