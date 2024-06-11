import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Test class for FinalMockCandidateFilter
 */
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulPropertySetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object() {
            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unused")
            private int someField;

            public void someMethod() {}

            public void setSomeField(int value) {
                this.someField = value;
            }
        };
        mocks.add(mock);
        Field field = MockitoTestUtils.getDeclaredField(mock, "someField");
        Object fieldInstance = mock;

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(actual);
        Assert.assertNotNull(actual.thenInject());
        Assert.assertEquals(mock, actual.thenInject());
    }

    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulFieldSetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object() {
            private static final long serialVersionUID = 1L;

            private int someField;

            public void someMethod() {}
        };
        mocks.add(mock);
        Field field = MockitoTestUtils.getDeclaredField(mock, "someField");
        Object fieldInstance = mock;

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(actual);
        Assert.assertNotNull(actual.thenInject());
        Assert.assertEquals(mock, actual.thenInject());

        try {
            BeanPropertySetter.set(fieldInstance, field, mock);
        } catch (Exception e) {
            // Field setter failed, so the filter should have used field access instead
            Assert.assertNull(actual.thenInject());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithSingleMockAndFailedPropertySetter() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object() {
            private static final long serialVersionUID = 1L;

            public void someMethod() {
                throw new RuntimeException("Mock failed to set property");
            }

            public void setSomeField(int value) {
                this.someField = value;
            }
        };
        mocks.add(mock);
        Field field = MockitoTestUtils.getDeclaredField(mock, "someField");
        Object fieldInstance = mock;

        filter.filterCandidate(mocks, field, fieldInstance);

        // Reporter should have been called with the exception
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndNullReturn() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>(2);
        Object mock1 = new Object() {};
        Object mock2 = new Object() {};
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = MockitoTestUtils.getDeclaredField(mock1, "someField");
        Object fieldInstance = mock1;

        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertNotNull(actual);
        Assert.assertNull(actual.thenInject());
    }
}

class MockitoTestUtils {
    /**
     * Returns the field with the given name from the given object.
     *
     * @param object instance of the class from which the field should be retrieved
     * @param fieldName name of the field to be retrieved
     * @return field instance
     * @throws NoSuchFieldException if the field does not exist
     * @throws IllegalAccessException if the field cannot be accessed
     */
    @SuppressWarnings("unchecked")
    private static <T> Field getDeclaredField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (Field) object.getClass().getDeclaredField(fieldName);
    }
}