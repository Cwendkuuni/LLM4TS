import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_EmptyMockList() {
        Collection<Object> mocks = new ArrayList<>();

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullMockList() {
        Collection<Object> mocks = null;

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = Arrays.asList(new StringMock(), new IntegerMock());

        Object fieldInstance = new Object();
        Field field = null;

        assertNull(filter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyMockTypeMatches() {
        Collection<Object> mocks = Arrays.asList(new StringMock(), new IntegerMock());

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(mockTypeMatches);
        assertEquals(0, mockTypeMatches.size());
    }

    @Test
    public void testFilterCandidate_SingleMockTypeMatch() {
        Collection<Object> mocks = Arrays.asList(new StringMock(), new IntegerMock(), new StringMock());

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(mockTypeMatches);
        assertEquals(1, mockTypeMatches.size());
        assertEquals(StringMock.class, mockTypeMatches.get(0).getClass());
    }

    @Test
    public void testFilterCandidate_MultipleMockTypeMatches() {
        Collection<Object> mocks = Arrays.asList(new StringMock(), new StringMock(), new IntegerMock(), new IntegerMock());

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        List<Object> mockTypeMatches = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(mockTypeMatches);
        assertEquals(2, mockTypeMatches.size());
        assertTrue(mockTypeMatches.contains(new StringMock()));
        assertTrue(mockTypeMatches.contains(new StringMock()));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockListThrowsException() {
        Collection<Object> mocks = null;

        Object fieldInstance = new Object();
        Field field = new Field("fieldName", String.class, null);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldThrowsException() {
        Collection<Object> mocks = Arrays.asList(new StringMock(), new IntegerMock());

        Object fieldInstance = new Object();
        Field field = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    private static class StringMock {}
    private static class IntegerMock {}
}