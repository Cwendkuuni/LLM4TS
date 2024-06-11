import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, null);
        assertFalse(ongoingInjecter.thenInject());
    }
}