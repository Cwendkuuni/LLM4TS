import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Collections3;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = Collections3.emptyList();
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = Collections3.list(mock(Object.class));
        Field field = mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_EmptyMockCollection() {
        Collection<Object> mocks = Collections3.emptyList();
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_SingleMatchingMock() {
        Collection<Object> mocks = Collections3.list(mock(Object.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(mock(String.class).getClass());

        Object mock = mock(String.class);
        when(mocks.iterator().next()).thenReturn(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.getCandidate());
        assertEquals(nextMock, result.getFilter());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockCollection() {
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockType() {
        Collection<Object> mocks = Collections3.list(mock(Object.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = Collections3.list(mock(Object.class));
        Field field = mock(Field.class);

        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_MultipleMatchingMocks() {
        Collection<Object> mocks = Collections3.list(mock(String.class), mock(String.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(mock(String.class).getClass());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mocks.get(0), result.getCandidate());
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        Collection<Object> mocks = Collections3.list(mock(Integer.class), mock(Integer.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(mock(String.class).getClass());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidate_FilterNextCalled() {
        Collection<Object> mocks = Collections3.list(mock(String.class));
        Object fieldInstance = mock(Object.class);
        Field field = mock(Field.class);

        when(nextMock.filterCandidate(eq(mocks), eq(field), eq(fieldInstance))).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(nextMock.filterCandidate(mocks, field, fieldInstance), result);
    }
}