import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;

    @Test
    public void testFilterCandidate() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        filter = new NameBasedCandidateFilter(next);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;

        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidateWithMockNameMatches() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return null;
            }
        };
        filter = new NameBasedCandidateFilter(next);

        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;

        List<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        MockUtil mockUtil = new MockUtil();
        mockUtil.mockName("mock1");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNull(result);
    }
}