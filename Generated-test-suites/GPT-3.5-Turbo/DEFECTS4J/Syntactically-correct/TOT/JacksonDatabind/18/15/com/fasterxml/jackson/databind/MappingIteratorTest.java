import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;

public class MappingIteratorTest {

    // Test case for hasNext() method
    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = createTestIterator();
        assertTrue(iterator.hasNext());

        iterator.next();

        assertFalse(iterator.hasNext());
    }

    // Test case for next() method
    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        assertEquals("value1", iterator.next());
        assertEquals("value2", iterator.next());

        try {
            iterator.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    // Test case for remove() method
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws IOException {
        MappingIterator<String> iterator = createTestIterator();
        iterator.remove();
    }

    // Test case for close() method
    @Test
    public void testClose() throws IOException {
        MappingIterator<String> iterator = createTestIterator();
        iterator.close();
    }

    // Test case for readAll() method
    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        List<String> resultList = iterator.readAll();
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("value1", resultList.get(0));
        assertEquals("value2", resultList.get(1));
    }

    // Test case for readAll(List) method
    @Test
    public void testReadAllToList() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        List<String> resultList = new ArrayList<String>();
        iterator.readAll(resultList);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("value1", resultList.get(0));
        assertEquals("value2", resultList.get(1));
    }

    // Test case for readAll(Collection) method
    @Test
    public void testReadAllToCollection() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        List<String> resultList = new ArrayList<String>();
        iterator.readAll(resultList);
        assertNotNull(resultList);
        assertEquals(2, resultList.size());
        assertEquals("value1", resultList.get(0));
        assertEquals("value2", resultList.get(1));
    }

    // Test case for getParser() method
    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        assertNotNull(iterator.getParser());
    }

    // Test case for getParserSchema() method
    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        assertNull(iterator.getParserSchema());
    }

    // Test case for getCurrentLocation() method
    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = createTestIterator();

        assertNotNull(iterator.getCurrentLocation());
    }

    // Helper method to create a test iterator
    private MappingIterator<String> createTestIterator() throws IOException {
        List<String> list = new ArrayList<String>();
        list.add("value1");
        list.add("value2");

        return new MappingIterator<String>(null, null, null, null, false, list.iterator()){
            @Override
            public boolean hasNextValue() throws IOException {
                return super.hasNextValue();
            }

            @Override
            public String nextValue() throws IOException {
                return super.nextValue();
            }
        };
    }
}