import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnProperOngoingInjecter() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());

        verify(fieldInstance, times(1)).field = mock1;
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnEmptyOngoingInjecter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());

        verifyZeroInteractions(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException_shouldThrowMockitoException() {
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        doThrow(MockitoException.class).when(fieldSetter).set(any());

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }
}