import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock");
        Field field = String.class.getDeclaredFields()[0];
        Object fieldInstance = new String();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(ongoingInjecter.thenInject());
        // Verify the injection
        Assert.assertEquals("mock", fieldInstance);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock1", "mock2");
        Field field = String.class.getDeclaredFields()[0];
        Object fieldInstance = new String();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList("mock");
        // Get a private field to provoke an exception
        Field field = String.class.getDeclaredFields()[0];
        field.setAccessible(false);
        Object fieldInstance = new String();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}