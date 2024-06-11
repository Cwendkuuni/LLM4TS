import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidateWithSingleMock() throws Exception {
        Collection<Object> mocks = new LinkedList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
        verifyPrivate(fieldInstance).set(mock);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() throws Exception {
        Collection<Object> mocks = new LinkedList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        verifyZeroInteractions(fieldInstance);
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() throws Exception {
        Collection<Object> mocks = new LinkedList<>();
        object mock = mock(Object.class);
        mocks.add(mock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        doThrow(new Exception()).when(mock);
        new FieldSetter(fieldInstance, field).set(mock);

        filter.filterCandidate(mocks, field, fieldInstance);
    }
}