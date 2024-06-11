import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MappingIteratorTest {

    @Test
    public void testHasNext() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            assertTrue(iterator.hasNext());
            iterator.next();
            assertTrue(iterator.hasNext());
            iterator.next();
            assertTrue(iterator.hasNext());
            iterator.next();
            assertFalse(iterator.hasNext());
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testNext() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            assertEquals(new Integer(1), iterator.next());
            assertEquals(new Integer(2), iterator.next());
            assertEquals(new Integer(3), iterator.next());
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testRemove() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            iterator.next();
            try {
                iterator.remove();
                fail("UnsupportedOperationException not thrown");
            } catch(UnsupportedOperationException e) {
                // Expected exception
            }
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testClose() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            iterator.close();
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadAllWithList() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            List<Integer> result = iterator.readAll();
            assertEquals(list, result);
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadAllWithCollection() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        try {
            List<Integer> result = new ArrayList<>();
            iterator.readAll(result);
            assertEquals(list, result);
        } catch(IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testGetParser() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        assertNotNull(iterator.getParser());
        assertTrue(iterator.getParser() instanceof JsonParser);
    }

    @Test
    public void testGetParserSchema() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        MappingIterator<Integer> iterator = objectMapper.reader().forType(Integer.class).readValues(list);
        assertNotNull(iterator.getCurrentLocation());
        assertTrue(iterator.getCurrentLocation() instanceof JsonLocation);
    }
}