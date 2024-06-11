import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_successfulInjection() throws Exception {
        Collection<Object> mocks = createMockCollection(1);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        Object matchingMock = new Object();
        when(mocks.iterator().next()).thenReturn(matchingMock);

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());

        verify(fieldInstance, times(1)).setField(matchingMock);
    }

    @Test
    public void testFilterCandidate_multipleMocks_unsuccessfulInjection() throws Exception {
        Collection<Object> mocks = createMockCollection(2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());

        verifyZeroInteractions(fieldInstance);
    }

    private Collection<Object> createMockCollection(int size) {
        Collection<Object> mocks = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Object mock = mock(Object.class);
            mocks.add(mock);
        }

        return mocks;
    }
}