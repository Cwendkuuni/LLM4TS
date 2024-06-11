import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        Collection<Object> mocks = ExtendedCollections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(String.class), mock(Integer.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_MatchingMocks() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Field.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        Collection<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = ExtendedCollections.emptyList();
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = ExtendedCollections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_IllegalCollectionType() {
        Collection<Integer> mocks = ExtendedCollections.listOf(1, 2, 3);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_NextFilterCalled() {
        Collection<Object> mocks = ExtendedCollections.listOf(mock(Field.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter nextResult = mock(OngoingInjecter.class);

        when(next.filterCandidate(mocks, field, fieldInstance)).thenReturn(nextResult);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(nextResult, result);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}