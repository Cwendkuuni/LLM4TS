import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        Assert.assertTrue(result);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        Assert.assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = getField();
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);
        boolean result = injecter.thenInject();

        Assert.fail("Exception should have been thrown.");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_withNullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Object fieldInstance = getFieldInstance();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);
        boolean result = injecter.thenInject();

        Assert.fail("NullPointerException should have been thrown.");
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_withNullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = getField();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);
        boolean result = injecter.thenInject();

        Assert.fail("NullPointerException should have been thrown.");
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getFieldInstance() {
        return new Object();
    }
}



// Generated JUnit 4 test cases go here

