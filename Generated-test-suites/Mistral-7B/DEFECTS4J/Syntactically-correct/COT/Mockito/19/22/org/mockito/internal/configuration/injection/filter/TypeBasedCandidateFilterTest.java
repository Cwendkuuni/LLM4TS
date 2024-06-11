import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ExtendedCollections;

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
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_CollectionContainsMocksWithMatchingType_ReturnsNextFilter() {
        Collection<Object> mocks = ExtendedCollections.newSet(mock(Object.class), mock(Field.class), mock(TypeBasedCandidateFilter.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Object expectedResult = nextMock.filterCandidate(ExtendedCollections.newList(mock(Object.class), mock(Field.class), mock(TypeBasedCandidateFilter.class)), field, fieldInstance);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testFilterCandidate_CollectionDoesNotContainMocksWithMatchingType_ReturnsEmptyList() {
        Collection<Object> mocks = ExtendedCollections.newSet(mock(Object.class), mock(String.class), mock(TypeBasedCandidateFilter.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        List<Object> result = ExtendedCollections.newList(filter.filterCandidate(mocks, field, fieldInstance));

        assertEquals(0, result.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_FieldIsNull_ThrowsNullPointerException() {
        Collection<Object> mocks = ExtendedCollections.newSet(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_FieldInstanceIsNull_ThrowsNullPointerException() {
        Collection<Object> mocks = ExtendedCollections.newSet(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_MocksCollectionIsEmpty_ThrowsIllegalArgumentException() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(ExtendedCollections.newSet(), field, fieldInstance);
    }
}