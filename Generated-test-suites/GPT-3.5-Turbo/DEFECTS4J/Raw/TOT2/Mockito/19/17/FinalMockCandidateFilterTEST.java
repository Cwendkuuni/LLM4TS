import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_multipleCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_exceptionInInjection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        BeanPropertySetter setter = mock(BeanPropertySetter.class);
        when(setter.set(any())).thenThrow(new RuntimeException("Error"));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertNull(result);
        verify(setter, times(1)).set(any());
        verifyNoMoreInteractions(setter);
    }

    @Test
    public void testFilterCandidate_noCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertNull(result);
    }
}