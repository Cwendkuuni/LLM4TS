
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new MockCandidateFilterImpl();
    }

    @Test
    public void testFilterCandidate() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = MockCandidateFilterTest.class.getDeclaredField("mockCandidateFilter");
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

}
