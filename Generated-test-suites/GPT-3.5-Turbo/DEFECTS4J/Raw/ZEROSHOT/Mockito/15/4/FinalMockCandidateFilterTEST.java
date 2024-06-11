import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setup() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock_successfullyInjects() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();
        assertTrue(result);

        verify(fieldInstance, times(1)).set(any());
    }

    @Test
    public void testFilterCandidate_multipleMocks_unableToInject() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();
        assertFalse(result);

        verify(fieldInstance, never()).set(any());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_singleMock_exceptionThrown() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        doThrow(new Exception()).when(fieldInstance).set(any());

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        // MockitoException should be thrown
    }
}