import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import java.lang.reflect.Field;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = mock(Collection.class);
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return null;
            }
        };

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(mocks, times(1)).add(fieldInstance);
        verifyNoMoreInteractions(mocks);
        verifyNoMoreInteractions(fieldToBeInjected);
        verifyNoMoreInteractions(fieldInstance);
    }

}