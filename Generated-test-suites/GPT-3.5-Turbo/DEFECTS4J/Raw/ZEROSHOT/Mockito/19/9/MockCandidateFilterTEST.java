import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = new MockCandidateFilterImpl();

        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();

        Collection<Object> mocks = new ArrayList<Object>();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Assert.assertNotNull(result);
    }

    class MockCandidateFilterImpl implements MockCandidateFilter {
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return new OngoingInjecterImpl();
        }
    }

    class OngoingInjecterImpl implements OngoingInjecter {
        public OngoingInjecterImpl() {
        }

        public void thenInject(Object value) {

        }
    }
}