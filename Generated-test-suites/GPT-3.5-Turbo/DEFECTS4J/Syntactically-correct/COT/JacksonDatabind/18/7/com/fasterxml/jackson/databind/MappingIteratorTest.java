import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private MappingIterator<Object> mappingIterator;

    @Before
    public void setup() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> objects = Arrays.asList(1, 2, 3);
        mappingIterator = mapper.readerFor(Object.class).readValues(objects);
    }

    @Test
    public void testHasNext() throws IOException {
        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        assertTrue(mappingIterator.hasNext());
        mappingIterator.next();
        assertFalse(mappingIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext() throws IOException {
        assertEquals(1, mappingIterator.next());
        assertEquals(2, mappingIterator.next());
        assertEquals(3, mappingIterator.next());
        mappingIterator.next(); // should throw NoSuchElementException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        mappingIterator.remove(); // should throw UnsupportedOperationException
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
        assertFalse(mappingIterator.getParser().isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE));
    }

    @Test
    public void testHasNextValue() throws IOException {
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.next();
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.next();
        assertTrue(mappingIterator.hasNextValue());
        mappingIterator.next();
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValue() throws IOException {
        assertEquals(1, mappingIterator.nextValue());
        assertEquals(2, mappingIterator.nextValue());
        assertEquals(3, mappingIterator.nextValue());
        mappingIterator.nextValue(); // should throw NoSuchElementException
    }

    @Test
    public void testReadAll() throws IOException {
        List<Object> resultList = mappingIterator.readAll();
        assertTrue(resultList.containsAll(Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testGetParser() {
        assertNotNull(mappingIterator.getParser());
    }

    @Test
    public void testGetParserSchema() {
        assertNull(mappingIterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNull(mappingIterator.getCurrentLocation());
    }
}