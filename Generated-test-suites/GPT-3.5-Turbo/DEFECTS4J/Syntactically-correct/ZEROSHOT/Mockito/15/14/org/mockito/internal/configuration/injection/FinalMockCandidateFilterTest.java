import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        verify(fieldInstance, times(1)).set(any());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = mock(Collection.class);
        when(mocks.size()).thenReturn(2);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        verifyZeroInteractions(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() throws Exception {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        doThrow(MockitoException.class).when(fieldInstance).set(any());

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }
}