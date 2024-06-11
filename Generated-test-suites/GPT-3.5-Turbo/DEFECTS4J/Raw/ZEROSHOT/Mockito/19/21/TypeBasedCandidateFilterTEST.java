import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Create objects for testing
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        List<Object> mocks = new ArrayList<Object>();
        Field field = new Field();
        Object fieldInstance = new Object();

        // Test when no mocks are present
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);

        // Test when a mock matches the field type
        Object mockTypeMatch = new Object();
        mocks.add(mockTypeMatch);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(1, result.getMocks().size());
        assertTrue(result.getMocks().contains(mockTypeMatch));

        // Test when multiple mocks match the field type
        Object mockTypeMatch2 = new Object();
        mocks.add(mockTypeMatch2);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(2, result.getMocks().size());
        assertTrue(result.getMocks().contains(mockTypeMatch));
        assertTrue(result.getMocks().contains(mockTypeMatch2));

        // Test when no mocks match the field type
        Object mockNoMatch = new Object();
        mocks.add(mockNoMatch);
        result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        assertEquals(field, result.getField());
        assertEquals(fieldInstance, result.getFieldInstance());
        assertEquals(0, result.getMocks().size());
    }
}