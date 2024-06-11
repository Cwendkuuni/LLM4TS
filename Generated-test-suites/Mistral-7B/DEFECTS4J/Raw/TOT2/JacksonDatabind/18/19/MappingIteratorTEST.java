import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MappingIteratorTest {
    private MappingIterator<String> stringIterator;
    private MappingIterator<Integer> integerIterator;
    private MappingIterator<Double> doubleIterator;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        stringIterator = createMappingIterator("\"apple\", \"banana\", \"orange\"");
        integerIterator = createMappingIterator("[1, 2, 3]");
        doubleIterator = createMappingIterator("[1.0, 2.5, 3.14]");
    }

    private <T> MappingIterator<T> createMappingIterator(String jsonString) throws IOException {
        Iterator<T> jsonIterator = objectMapper.values(new StringTextReader(jsonString));
        return (MappingIterator<T>) jsonIterator;
    }

    @Test
    public void testHasNext() {
        // Test string iterator
        testHasNext(stringIterator);

        // Test integer iterator
        testHasNext(integerIterator);

        // Test double iterator
        testHasNext(doubleIterator);
    }

    @Test
    public void testNext() {
        // Test string iterator
        testNext(stringIterator);

        // Test integer iterator
        testNext(integerIterator);

        // Test double iterator
        testNext(doubleIterator);
    }

    @Test
    public void testClose() throws IOException {
        // Test string iterator
        testClose(stringIterator);

        // Test integer iterator
        testClose(integerIterator);

        // Test double iterator
        testClose(doubleIterator);
    }

    @Test
    public void testReadAll() throws IOException {
        // Test string iterator
        testReadAll(stringIterator);

        // Test integer iterator
        testReadAll(integerIterator);

        // Test double iterator
        testReadAll(doubleIterator);
    }

    private <T> void testHasNext(MappingIterator<T> iterator) {
        // Test basic functionality
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());

        // Test edge cases and exceptions
        iterator.close();
        assertFalse(iterator.hasNext());
        assertThrows(IllegalStateException.class, iterator::next);

        // Test exception handling
        ObjectMapper exceptionMapper = new ObjectMapper();
        MappingIterator<T> exceptionMappingIterator = (MappingIterator<T>) exceptionMapper.values(new StringTextReader(""));
        assertFalse(exceptionMappingIterator.hasNext());
        assertThrows(JsonProcessingException.class, () -> exceptionMappingIterator.nextValue());
    }

    private <T> void testNext(MappingIterator<T> iterator) {
        // Test basic functionality
        assertEquals("apple", iterator.next().toString());
        assertEquals("banana", iterator.next().toString());
        assertEquals("orange", iterator.next().toString());

        // Test edge cases and exceptions
        iterator.close();
        assertNull(iterator.next());
        assertThrows(IllegalStateException.class, iterator::next);

        // Test exception handling
        ObjectMapper exceptionMapper = new ObjectMapper();
        MappingIterator<T> exceptionMappingIterator = (MappingIterator<T>) exceptionMapper.values(new StringTextReader(""));
        assertNull(exceptionMappingIterator.next());
        assertThrows(JsonProcessingException.class, () -> exceptionMappingIterator.nextValue());
    }

    private <T> void testClose(MappingIterator<T> iterator) throws IOException {
        // Test basic functionality
        iterator.close();
        assertFalse(iterator.hasNext());

        // Test edge cases and exceptions
        iterator.next();
        iterator.next();
        iterator.close();
        assertFalse(iterator.hasNext());
        assertThrows(IllegalStateException.class, iterator::next);

        // Test exception handling
        ObjectMapper exceptionMapper = new ObjectMapper();
        MappingIterator<T> exceptionMappingIterator = (MappingIterator<T>) exceptionMapper.values(new StringTextReader(""));
        exceptionMappingIterator.close();
        assertFalse(exceptionMappingIterator.hasNext());
        assertThrows(JsonProcessingException.class, () -> exceptionMappingIterator.nextValue());
    }

    private <T> void testReadAll(MappingIterator<T> iterator) throws IOException {
        List<T> resultList = iterator.readAll();
        assertEquals(3, resultList.size());
        assertEquals("apple", resultList.get(0).toString());
        assertEquals("banana", resultList.get(1).toString());
        assertEquals("orange", resultList.get(2).toString());
    }
}

//