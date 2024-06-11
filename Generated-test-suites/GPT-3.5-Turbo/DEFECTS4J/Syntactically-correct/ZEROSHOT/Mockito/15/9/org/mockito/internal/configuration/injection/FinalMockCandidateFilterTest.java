import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.*;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_SingleMock_thenInject() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(field, times(1)).set(eq(fieldInstance), eq(mock));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_thenNotInject() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verify(field, never()).set(any(), any());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_ExceptionThrown_thenCaptureAndThrowMockitoException() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        final Exception exception = new Exception("Injection Failed");
        doThrow(exception).when(field).set(any(), any());

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}