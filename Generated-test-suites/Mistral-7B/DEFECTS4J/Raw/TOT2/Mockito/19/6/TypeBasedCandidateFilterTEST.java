package tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    TypeBasedCandidateFilter filter;
    MockCandidateFilter mockFilter;
    Collection<Object> mocks;
    Field field;
    Object fieldInstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockFilter = Mockito.spy(mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        field = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
        filter = new TypeBasedCandidateFilter(mockFilter);
    }

    @Test
    public void testConstructor() {
        assertNotNull(filter);
        assertEquals(mockFilter, filter.next);
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsNull() {
        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NoMatchingMocks_ReturnsNext() {
        Mockito.when(field.getType().isAssignableFrom(mocks.get(0).getClass())).thenReturn(false);
        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertSame(mockFilter, candidate);
    }

    @Test
    public void testFilterCandidate_MatchingMock_ReturnsMock() {
        Mockito.when(field.getType().isAssignableFrom(mocks.get(0).getClass())).thenReturn(true);
        Object mock = mocks.get(0);
        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertSame(mock, candidate);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_MockWithSameTypeButDifferentClass_ReturnsNext() {
        Mockito.when(field.getType().isAssignableFrom(mocks.get(0).getClass())).thenReturn(true);
        Mockito.when(mocks.get(0).getClass().isAssignableFrom(mocks.get(1).getClass())).thenReturn(false);
        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertSame(mockFilter, candidate);
    }

    @Test
    public void testFilterCandidate_MockWithSameTypeAndEqualHashCode_ReturnsMock() {
        Mockito.when(field.getType().isAssignableFrom(mocks.get(0).getClass())).thenReturn(true);
        Object mockA = Mockito.mock(Object.class);
        Object mockB = mockA;
        mocks.add(mockA);
        mocks.add(mockB);
        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertSame(mockA, candidate);
    }

    @Test
    public void testFilterCandidate_MockWithSameTypeButDifferentInstance_ReturnsNext() {
        Mockito.when(field.getType().isAssignableFrom(mocks.get(0).getClass())).thenReturn(true);
        Object mockA = Mockito.mock(Object.class);
        Object mockB = Mockito.mock(Object.class);
        mocks.add(mockA);
        mocks.add(mockB);
        Object candidate = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(candidate);
        assertSame(mockFilter, candidate);
    }
}