import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        field = mock(Field.class);
        fieldInstance = new Object();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_SingleMatch() {
        Object mock = mock(Object.class);
        String fieldName = "mock";
        when(field.getName()).thenReturn(fieldName);
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_NoMatch() {
        Object mock = mock(Object.class);
        String fieldName = "mock";
        when(field.getName()).thenReturn(fieldName);
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_MultipleMatches() {
        String fieldName = "mock";
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn(fieldName);
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(mock1);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertEquals(mock1, result);
    }

    @Test
    public void testFilterCandidate_MultipleMatchesDifferentNames() {
        String fieldName = "mock";
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn("differentName");
        when(next.filterCandidate(anyCollection(), any(Field.class), any(Object.class))).thenReturn(mock1);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertEquals(mock1, result);
    }
}