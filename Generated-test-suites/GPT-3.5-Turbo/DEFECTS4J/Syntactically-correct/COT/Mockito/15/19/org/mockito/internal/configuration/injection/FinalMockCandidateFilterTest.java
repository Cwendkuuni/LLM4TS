import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Setup
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = getField();
        Object fieldInstance = mock(Object.class);

        // Test
        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        // Verify
        assertTrue(result);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Setup
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = getField();
        Object fieldInstance = mock(Object.class);

        // Test
        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        // Verify
        assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionOnInjection() {
        // Setup
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = getField();
        Object fieldInstance = mock(Object.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);

        // Stub
        doThrow(new Exception()).when(fieldSetter).set(any(Object.class));

        // Test
        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("fieldName");
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Failed to get field");
        }
    }
}