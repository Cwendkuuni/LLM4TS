import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_noCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = null;

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_singleMock_successfulPropertySetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        Assert.assertEquals(result, mock);
    }

    @Test
    public void testFilterCandidate_singleMock_successfulFieldAccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        // Validating that BeanPropertySetter.set() returns false to allow fallback to FieldSetter.set()
        BeanPropertySetter.setReturnValue(false);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        Assert.assertEquals(result, mock);
    }

    @Test
    public void testFilterCandidate_singleMock_unsuccessfulInjection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        // Validating that BeanPropertySetter.set() throws an exception
        BeanPropertySetter.setThrowsException(true);

        // Validating that FieldSetter.set() throws an exception
        FieldSetter.setThrowsException(true);

        // Validating that Reporter.cannotInjectDependency() is called
        Reporter.setCannotInjectDependencyCalled(false);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        Assert.assertNull(result);
        Assert.assertTrue(Reporter.getCannotInjectDependencyCalled());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        List<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = null;

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        Assert.assertNull(result);
    }

}