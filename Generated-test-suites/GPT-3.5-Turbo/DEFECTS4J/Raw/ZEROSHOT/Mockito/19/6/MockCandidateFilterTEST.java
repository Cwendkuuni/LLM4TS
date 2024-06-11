import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter = new MockCandidateFilter() {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return null;
        }
    };

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = MockCandidateFilterTest.class.getDeclaredField("filter");
        Object fieldInstance = new MockCandidateFilterTest();

        OngoingInjecter injecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(injecter);
    }

}