
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Assert.assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());

        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_exceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();

        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = ongoingInjecter.thenInject();

        Assert.assertNull(result);
    }
}
