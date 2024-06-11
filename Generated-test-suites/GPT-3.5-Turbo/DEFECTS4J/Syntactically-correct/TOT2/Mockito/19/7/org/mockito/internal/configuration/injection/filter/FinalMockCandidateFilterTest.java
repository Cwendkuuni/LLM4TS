import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = mock.getClass().getDeclaredField("mockField");
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, new Object());
        Object injectedObject = ongoingInjecter.thenInject();
        
        assertNotNull(injectedObject);
        assertEquals(mock, injectedObject);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = new Object().getClass().getDeclaredField("mockField");
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, new Object());
        Object injectedObject = ongoingInjecter.thenInject();
        
        assertNull(injectedObject);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_exception() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        Field field = new Object().getClass().getDeclaredField("mockField");
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, new Object());
        ongoingInjecter.thenInject();
    }

}