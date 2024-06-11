package miserable.expert;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testConstructor() {
        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mockFilter);
        assertNotNull(filter);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        List<Object> mockTypeMatches = new ArrayList<>();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEmpty(result);
    }

    @Test
    public void testFilterCandidate_NullField() {
        List<Object> mockTypeMatches = new ArrayList<>();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Object fieldInstance = mock(Object.class);
        Field field = null;
        try {
            List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        List<Object> mockTypeMatches = new ArrayList<>();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;
        try {
            List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testFilterCandidate_MocksWithDifferentType() {
        List<Object> mockTypeMatches = new ArrayList<>();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object1.class));
        mocks.add(mock(Object2.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilterCandidate_MocksWithSameType() {
        List<Object> mockTypeMatches = new ArrayList<>();
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(mock(MockCandidateFilter.class));
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        List<Object> result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(1, result.size());
    }
}