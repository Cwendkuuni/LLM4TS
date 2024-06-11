import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList("mock1");
        Field field = getField("field");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertTrue(result.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList("mock1", "mock2", "mock3");
        Field field = getField("field");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
        assertFalse(result.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList("mock1");
        Field field = getField("field");
        Object fieldInstance = new Object() {
            @SuppressWarnings("unused")
            public void setField(Object value) throws Exception {
                throw new Exception("Custom exception");
            }
        };

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
           throw new RuntimeException("Field not found: " + name);
        }
    }
}