import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        assertTrue(iterator.hasNextValue());
    }

    @Test
    public void testNext() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        String value = iterator.next();
        assertEquals("value1", value);
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        String value = iterator.nextValue();
        assertEquals("value1", value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws IOException {
        MappingIterator<String> iterator = getEmptyMappingIterator();
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementValue() throws IOException {
        MappingIterator<String> iterator = getEmptyMappingIterator();
        iterator.nextValue();
    }

    private MappingIterator<String> getMappingIterator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"value1\",\"value2\",\"value3\"]");
        MappingIterator<String> iterator = new MappingIterator<String>(null, parser, null, null, true, null);
        return iterator;
    }

    private MappingIterator<String> getEmptyMappingIterator() throws IOException {
        return MappingIterator.emptyIterator();
    }

    @Test
    public void testRemove() {
        MappingIterator<String> iterator = getMappingIterator();
        try {
            iterator.remove();
            fail("remove() should throw UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    @Test
    public void testClose() {
        MappingIterator<String> iterator = getMappingIterator();
        try {
            iterator.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("close() should not throw IOException");
        }
        assertNull(iterator.getParser());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        List<String> results = iterator.readAll();
        assertEquals(3, results.size());
        assertEquals("value1", results.get(0));
        assertEquals("value2", results.get(1));
        assertEquals("value3", results.get(2));
    }

    @Test
    public void testReadAllWithEmptyList() throws IOException {
        MappingIterator<String> iterator = getEmptyMappingIterator();
        List<String> results = iterator.readAll(new ArrayList<String>());
        assertEquals(0, results.size());
    }

    @Test
    public void testReadAllWithExistingList() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        List<String> existingList = new ArrayList<String>();
        existingList.add("existingValue");
        List<String> results = iterator.readAll(existingList);
        assertEquals(4, results.size());
        assertEquals("existingValue", results.get(0));
        assertEquals("value1", results.get(1));
        assertEquals("value2", results.get(2));
        assertEquals("value3", results.get(3));
    }

    @Test
    public void testReadAllWithSet() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        Set<String> results = iterator.readAll(new HashSet<String>());
        assertEquals(3, results.size());
        assertTrue(results.contains("value1"));
        assertTrue(results.contains("value2"));
        assertTrue(results.contains("value3"));
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        FormatSchema schema = iterator.getParserSchema();
        assertNull(schema);
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = getMappingIterator();
        JsonLocation location = iterator.getCurrentLocation();
        assertNull(location);
    }
}