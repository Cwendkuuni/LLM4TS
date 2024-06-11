import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = ExtendedCollections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        Collection<Object> mocks = ExtendedCollections.listOf(new Object(), new String(), new Number());
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_SingleMatchingMock() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(result.getCandidate(), mocks.iterator().next());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_ManyMatchingMocks() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Object.class), mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(result.getCandidate(), mocks.iterator().next());
    }

    @Test
    public void testFilterCandidate_NextFilterCalled() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(result.getCandidate(), mocks.iterator().next());
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}