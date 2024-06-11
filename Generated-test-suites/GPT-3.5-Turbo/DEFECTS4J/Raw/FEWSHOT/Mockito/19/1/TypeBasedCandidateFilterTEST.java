import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private List<Object> mockTypeMatches;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
        mockTypeMatches = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_FieldTypeMatches() {
        Object mock = new Object();
        mocks.add(mock);
        mockTypeMatches.add(mock);
        when(field.getType().isAssignableFrom(mock.getClass())).thenReturn(true);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(field, times(1)).getType();
        verify(next, times(1)).filterCandidate(mockTypeMatches, field, fieldInstance);

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_FieldTypeDoesNotMatch() {
        Object mock = new Object();
        mocks.add(mock);
        when(field.getType().isAssignableFrom(mock.getClass())).thenReturn(false);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(field, times(1)).getType();
        verify(next, times(1)).filterCandidate(mockTypeMatches, field, fieldInstance);

        assertEquals(ongoingInjecter, result);
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        when(field.getType().isAssignableFrom(any())).thenReturn(false);

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(field, times(1)).getType();
        verify(next, times(1)).filterCandidate(mockTypeMatches, field, fieldInstance);

        assertEquals(ongoingInjecter, result);
    }
}