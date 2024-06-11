import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        Object result = injecter.thenInject();
        assertNotNull(result);
        assertEquals(mocks.get(0), result);
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_exceptionThrown_handleException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                throw new RuntimeException("Custom Exception");
            }
        };

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_fieldNull_handleException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

}