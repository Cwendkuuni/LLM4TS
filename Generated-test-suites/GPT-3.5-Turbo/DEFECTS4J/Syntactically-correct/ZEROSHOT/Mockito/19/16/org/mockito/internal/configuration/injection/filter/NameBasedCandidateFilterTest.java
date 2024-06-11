import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        MockCandidateFilter nextFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        filter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_withMatchingName() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_withNoNameMatches() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("mockUtil");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_withMultipleNameMatches() throws NoSuchFieldException {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        Object mock3 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }
}