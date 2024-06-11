import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter candidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        candidateFilter = new FinalMockCandidateFilter();
        mocks = mock(Collection.class);
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnOngoingInjecter() throws Exception {
        Object mock = mock(Object.class);
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(mock);
        doNothing().when(field).setAccessible(true);

        OngoingInjecter ongoingInjecter = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(true, ongoingInjecter.thenInject());
        verify(field).setAccessible(true);
        verify(field, times(1)).set(fieldInstance, mock);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnOngoingInjecter() throws Exception {
        when(mocks.size()).thenReturn(2);

        OngoingInjecter ongoingInjecter = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(false, ongoingInjecter.thenInject());
        verify(field, never()).setAccessible(true);
        verify(field, never()).set(fieldInstance, any());
    }
}