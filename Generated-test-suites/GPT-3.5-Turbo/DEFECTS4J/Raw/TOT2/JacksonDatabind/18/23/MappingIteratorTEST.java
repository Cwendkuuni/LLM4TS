import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private MappingIterator<String> mappingIterator;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        mappingIterator = mapper.readValues(mapper.writeValueAsBytes(list), String.class);
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

    @Test
    public void testNext() throws IOException {
        assertEquals("one", mappingIterator.next());
        assertEquals("two", mappingIterator.next());
        assertEquals("three", mappingIterator.next());
        assertThrows(NoSuchElementException.class, mappingIterator::next);
    }

    @Test
    public void testNextValue() throws IOException {
        assertEquals("one", mappingIterator.nextValue());
        assertEquals("two", mappingIterator.nextValue());
        assertEquals("three", mappingIterator.nextValue());
        assertThrows(NoSuchElementException.class, mappingIterator::nextValue);
    }

    @Test
    public void testRemove() {
        assertThrows(UnsupportedOperationException.class, mappingIterator::remove);
    }

    @Test
    public void testClose() {
        mappingIterator.close();
        assertThrows(IOException.class, mappingIterator::hasNext);
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("one", resultList.get(0));
        assertEquals("two", resultList.get(1));
        assertEquals("three", resultList.get(2));
    }

    @Test
    public void testReadAllWithCollection() throws IOException {
        List<String> resultList = new ArrayList<>();
        mappingIterator.readAll(resultList);
        assertEquals(3, resultList.size());
        assertEquals("one", resultList.get(0));
        assertEquals("two", resultList.get(1));
        assertEquals("three", resultList.get(2));
    }

    @Test
    public void testGetParser() {
        assertNotNull(mappingIterator.getParser());
    }

    @Test
    public void testGetCurrentLocation() {
        assertNotNull(mappingIterator.getCurrentLocation());
    }
}