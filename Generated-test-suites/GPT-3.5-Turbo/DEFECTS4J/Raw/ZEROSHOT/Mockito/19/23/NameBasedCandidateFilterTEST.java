import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));

        Field field = Object.class.getDeclaredField("fieldName");
        Object fieldInstance = Mockito.mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> mockNameMatches = new ArrayList<Object>();
        for (Object mock : mocks) {
            if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
                mockNameMatches.add(mock);
            }
        }

        Mockito.verify(next).filterCandidate(mockNameMatches, field, fieldInstance);
        Mockito.verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}