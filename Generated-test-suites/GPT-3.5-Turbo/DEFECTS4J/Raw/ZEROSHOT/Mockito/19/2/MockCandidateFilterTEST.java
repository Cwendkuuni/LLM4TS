import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return null;
        }
    };

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = MockCandidateFilter.class.getDeclaredField("mockCandidateFilter");
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(ongoingInjecter);
    }
}