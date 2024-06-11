import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MappingIteratorTest {

    @Test
    public void testHasNext() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertEquals(new Integer(1), iterator.next());
    }

    @Test
    public void testRemove() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        try {
            iterator.remove();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testClose() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        MappingIterator<Object> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertTrue(iterator.hasNextValue());
        iterator.next();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertEquals(new Integer(1), iterator.nextValue());
    }

    @Test
    public void testReadAll_List() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        List<Integer> resultList = iterator.readAll(new ArrayList<Integer>());
        assertEquals(3, resultList.size());
        assertEquals(new Integer(1), resultList.get(0));
        assertEquals(new Integer(2), resultList.get(1));
        assertEquals(new Integer(3), resultList.get(2));
    }

    @Test
    public void testReadAll_Collection() throws IOException {
        JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        List<Integer> resultList = iterator.readAll(new ArrayList<Integer>());
        assertEquals(3, resultList.size());
        assertEquals(new Integer(1), resultList.get(0));
        assertEquals(new Integer(2), resultList.get(1));
        assertEquals(new Integer(3), resultList.get(2));
    }

    @Test
    public void testGetParser() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertEquals(parser, iterator.getParser());
    }

    @Test
    public void testGetParserSchema() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        JsonParser parser = new JsonFactory().createParser("1");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<Integer> deserializer = new JsonDeserializer<>();
        MappingIterator<Integer> iterator = new MappingIterator<>(null, parser, context, deserializer, false, null);
        assertNotNull(iterator.getCurrentLocation());
    }
}