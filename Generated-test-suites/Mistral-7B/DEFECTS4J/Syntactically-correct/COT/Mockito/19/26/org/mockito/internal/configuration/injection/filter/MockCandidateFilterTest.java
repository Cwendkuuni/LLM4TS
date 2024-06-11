import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Collections部件;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.*;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_NullMocks() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = null;
        assertNull(filter.filterCandidate(mocks, null, null));
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = null;
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = new Field("testField", String.class, MockitoFilter.class);
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocksAndNullFieldInstance() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", String.class, MockitoFilter.class);
        filter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field fieldToBeInjected = new Field("testField", String.class, MockitoFilter.class);
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, new Object()));
    }

    @Test
    public void testFilterCandidate_NonEmptyMocks() {
        MockCandidateFilter filter = createMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(new Object());
        Field fieldToBeInjected = new Field("testField", String.class, MockitoFilter.class);
        assertNotNull(filter.filterCandidate(mocks, fieldToBeInjected, new Object()));
    }

    private MockCandidateFilter createMockCandidateFilter() {
        return new StubMockCandidateFilter();
    }

    private static class StubMockCandidateFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
            return null;
        }
    }

}