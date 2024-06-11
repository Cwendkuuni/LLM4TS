
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_OneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertTrue(injecter instanceof OngoingInjecter);
        Assert.assertEquals(mocks.iterator().next(), injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = new Field();
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(injecter);
        Assert.assertTrue(injecter instanceof OngoingInjecter);
        Assert.assertNull(injecter.thenInject());
    }
}
