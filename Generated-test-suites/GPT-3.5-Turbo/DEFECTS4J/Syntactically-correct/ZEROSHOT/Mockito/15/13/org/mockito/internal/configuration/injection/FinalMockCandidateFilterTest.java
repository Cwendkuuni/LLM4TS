import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_singleCandidate() {
        Object mock = new Object();
        mocks.add(mock);

        field = getField("exampleField");
        fieldInstance = new MockClass();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(injecter.thenInject());
        Assert.assertEquals(mock, fieldInstance.exampleField);
    }

    @Test
    public void testFilterCandidate_multipleCandidates() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.addAll(Arrays.asList(mock1, mock2));

        field = getField("exampleField");
        fieldInstance = new MockClass();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(injecter.thenInject());
        Assert.assertNull(fieldInstance.exampleField);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        mocks.add(new Object());

        field = getField("exampleField");
        fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();
    }

    private Field getField(String fieldName) {
        try {
            return MockClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class MockClass {
        private Object exampleField;
    }
}