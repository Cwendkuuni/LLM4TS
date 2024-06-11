import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private final MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidateWithMockName() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                List<Object> mockNameMatches = new ArrayList<Object>();
                for (Object mock : mocks) {
                    if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
                        mockNameMatches.add(mock);
                    }
                }
                return new OngoingInjecter(mockNameMatches, field, fieldInstance);
            }
        };

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        Field field = String.class.getField("name");
        Object fieldInstance = null;

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(1, result.getMocks().size());
        Assert.assertEquals("mock1", result.getMocks().get(0));
    }

    @Test
    public void testFilterCandidateWithoutMockName() throws NoSuchFieldException {
        MockCandidateFilter next = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter(mocks, field, fieldInstance);
            }
        };

        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");

        Field field = String.class.getField("name");
        Object fieldInstance = null;

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertEquals(2, result.getMocks().size());
        Assert.assertEquals("mock1", result.getMocks().get(0));
        Assert.assertEquals("mock2", result.getMocks().get(1));
    }
}