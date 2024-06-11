import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_successfullyInjected() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        assertTrue(result);
        assertEquals(mock, field.get(fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        assertFalse(result);
        assertNull(field.get(fieldInstance));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new SomeClass();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        doThrow(new Exception()).when(mock).toString();
        mocks.add(mock);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }

}