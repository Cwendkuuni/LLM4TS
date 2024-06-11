import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateNoMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        // Intentionally setting field to null to trigger MockitoException
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        injecter.thenInject();
    }
}