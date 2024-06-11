
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    // Test for filterCandidate() when there is only one mock
    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = List.of(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mock");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        verify(new BeanPropertySetter(fieldInstance, field)).set(any());
        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoInteractions(new FieldSetter(fieldInstance, field));
        verifyNoInteractions(new Reporter());
        assertEquals(result, mocks.iterator().next());
    }

    // Test for filterCandidate() when there are multiple mocks
    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = List.of(mock(Object.class), mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mock");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        verifyNoInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoInteractions(new FieldSetter(fieldInstance, field));
        verifyNoInteractions(new Reporter());
        assertNull(result);
    }

}
