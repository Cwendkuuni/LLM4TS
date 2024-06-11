import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Test class for TypeBasedCandidateFilter.
 */
public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_CollectionContainsTypeMatch_ReturnsFilteredMock() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Field.class), mock(TypeBasedCandidateFilter.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mock(Field.class).getClass(), filteredMock.getClass());
    }

    @Test
    public void testFilterCandidate_CollectionDoesNotContainTypeMatch_ReturnsNull() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(null, filteredMock);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_FieldIsNull_ThrowsNPE() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Field.class), mock(TypeBasedCandidateFilter.class));
        Object fieldInstance = mock(Object.class);

        // When
        filter.filterCandidate(null, mock(Field.class), fieldInstance);

        // Then - exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_MockCollectionIsNull_ThrowsNPE() {
        // Given
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        // When
        filter.filterCandidate(null, field, fieldInstance);

        // Then - exception is thrown
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_FieldInstanceIsNull_ThrowsNPE() {
        // Given
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Field.class), mock(TypeBasedCandidateFilter.class));
        Field field = mock(Field.class);

        // When
        filter.filterCandidate(mocks, null, mock(Object.class));

        // Then - exception is thrown
    }
}