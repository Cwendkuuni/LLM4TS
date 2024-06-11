import org.junit.Test;
import org.junit.Before;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = mock(Collection.class);
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_withOneMock_shouldReturnOngoingInjecter_thenInject() {
        Object mock = mock(Object.class);
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(mock);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(fieldInstance).set(any());
    }

    @Test
    public void testFilterCandidate_withNoMocks_shouldReturnOngoingInjecter_thenNotInject() {
        when(mocks.size()).thenReturn(0);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verifyZeroInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnOngoingInjecter_thenNotInject() {
        when(mocks.size()).thenReturn(2);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verifyZeroInteractions(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException_shouldThrowMockitoException() {
        Object mock = mock(Object.class);
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(mock);
        doThrow(MockitoException.class).when(fieldInstance).set(any());

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}