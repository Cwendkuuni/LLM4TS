import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Arguments;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field fieldToBeInjected = spy(Field.class);
        Object fieldInstance = spy(Object.class);

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(filter).filterCandidate(Arguments.notNull(mocks), Arguments.notNull(fieldToBeInjected), Arguments.notNull(fieldInstance));
    }
}