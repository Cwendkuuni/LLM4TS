import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import java.util.Arrays;
import java.util.Collections;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return null;
            }
        };

        Assert.assertNull(filter.filterCandidate(null, new Field(MockCandidateFilterTest.class, "testField"), new Object()));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Assert.assertNull(filter.filterCandidate(Collections.emptyList(), new Field(MockCandidateFilterTest.class, "testField"), new Object()));
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Object mock = new Object();
        Collection<Object> mocks = Arrays.asList(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, new Field(MockCandidateFilterTest.class, "testField"), new Object());
        Assert.assertNotNull(result);
        Assert.assertEquals(mock, result.getCandidate());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        filter.filterCandidate(Collections.emptyList(), null, new Object());
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };

        Assert.assertNull(filter.filterCandidate(Collections.emptyList(), new Field(MockCandidateFilterTest.class, "testField"), null));
    }

}