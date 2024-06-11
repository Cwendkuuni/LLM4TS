import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.fasterxml.jackson.core.JsonToken.*;

public class MappingIteratorTest {
    private static final ObjectMapper mapper = new ObjectMapper();
    private JsonFactory factory = mapper.getFactory();
    private JsonParser parser;
    private DeserializationContext context;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setup() throws Exception {
        String json = "[\"apple\", \"banana\", \"orange\"]";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        parser = factory.createParser(is);
        context = mapper.getDeserializationContext();
        mappingIterator = new MappingIterator<>(factory.constructType(String.class), parser, context, null, true, null);
    }

    

    @Test
    public void testHasNext_InitialState() {
        assertFalse(mappingIterator.hasNext());
    }

    @Test
    public void testNext_InitialState() {
        assertNull(mappingIterator.next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        mappingIterator.remove();
    }

    @Test
    public void testClose() throws IOException {
        mappingIterator.close();
        assertFalse(mappingIterator.hasNext());
        assertNull(mappingIterator.next());
    }

    @Test
    public void testHasNextValue_InitialState() throws IOException {
        assertFalse(mappingIterator.hasNextValue());
    }

    @Test
    public void testNextValue_InitialState() throws IOException {
        assertNull(mappingIterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        List<String> resultList = mappingIterator.readAll();
        assertEquals(0, resultList.size());
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
    public void testgetCurrentLocation() {
        JsonLocation location = mappingIterator.getCurrentLocation();
        assertNotNull(location);
    }

    @Test
    public void testHasNext_WhenParserHasNextToken() throws IOException {
        parser.nextToken();
        assertTrue(mappingIterator.hasNext());
    }

    @Test
    public void testNext_WhenParserHasNextToken() throws IOException {
        parser.nextToken();
        String value = mappingIterator.next();
        assertEquals("apple", value);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_WhenParserHasNoNextToken() throws IOException {
        parser.nextToken(); // consume START_ARRAY token
        parser.nextToken(); // consume STRING token for "apple"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_ARRAY token for ["banana", "orange"]
        parser.nextToken(); // consume START_OBJECT token for "banana"
        parser.nextToken(); // consume STRING token for "banana"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_OBJECT token for "orange"
        parser.nextToken(); // consume STRING token for "orange"
        parser.nextToken(); // consume END_OBJECT token
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume END_ARRAY token
        String value = mappingIterator.next();
        assertNull(value);
    }

    @Test
    public void testRemove_Exception() {
        try {
            mappingIterator.remove();
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test(expected = IOException.class)
    public void testNextValue_ParserHasInvalidInput() throws IOException {
        parser.nextToken(); // consume START_ARRAY token
        parser.nextToken(); // consume STRING token for "apple"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume END_OBJECT token
        parser.nextToken(); // consume START_ARRAY token for ["banana"]
        parser.nextToken(); // consume START_OBJECT token for "banana"
        parser.nextToken(); // consume BOOLEAN token for false
        parser.nextToken(); // consume END_OBJECT token
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume END_ARRAY token
        parser.nextToken(); // consume END_ARRAY token
        parser.nextToken(); // consume END_DOCUMENT token
        try {
            mappingIterator.nextValue();
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testNextValue_DeserializerThrowsMappingException() throws IOException {
        parser.nextToken(); // consume START_ARRAY token
        parser.nextToken(); // consume STRING token for "apple"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_ARRAY token for ["banana"]
        parser.nextToken(); // consume START_OBJECT token for "banana"
        parser.nextToken(); // consume STRING token for "banana"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_OBJECT token for "orange"
        parser.nextToken(); // consume STRING token for "orange"
        JsonDeserializer<String> deserializer = new StringDeserializerWithException();
        mappingIterator = new MappingIterator<>(factory.constructType(String.class), parser, context, deserializer, true, null);
        try {
            mappingIterator.nextValue();
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test(expected = RuntimeException.class)
    public void testNextValue_DeserializerThrowsIOException() throws IOException {
        parser.nextToken(); // consume START_ARRAY token
        parser.nextToken(); // consume STRING token for "apple"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_ARRAY token for ["banana"]
        parser.nextToken(); // consume START_OBJECT token for "banana"
        parser.nextToken(); // consume STRING token for "banana"
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume START_OBJECT token for "orange"
        parser.nextToken(); // consume STRING token for "orange"
        parser.nextToken(); // consume END_OBJECT token
        parser.nextToken(); // consume COMMA token
        parser.nextToken(); // consume END_ARRAY token
        parser.nextToken(); // consume END_ARRAY token
        parser.nextToken(); // consume END_DOCUMENT token
        JsonDeserializer<String> deserializer = new StringDeserializerWithIOException();
        mappingIterator = new MappingIterator<>(factory.constructType(String.class), parser, context, deserializer, true, null);
        try {
            mappingIterator.nextValue();
            fail();
        } catch (RuntimeException e) {
            // Expected
        }
    }

    @Test
    public void testReadAll_ReadAllEntries() throws IOException {
        List<String> resultList = mappingIterator.readAll();
        assertEquals(3, resultList.size());
        assertEquals("apple", resultList.get(0));
        assertEquals("banana", resultList.get(1));
        assertEquals("orange", resultList.get(2));
    }

    @Test
    public void testReadAll_EmptyIterator() throws IOException {
        mappingIterator = new MappingIterator<>(factory.constructType(String.class), null, context, null, true, null);
        List<String> resultList = mappingIterator.readAll();
        assertEquals(0, resultList.size());
    }

    @Test
    public void testReadAll_NullIterator() throws IOException {
        mappingIterator = null;
        try {
            List<String> resultList = mappingIterator.readAll();
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testReadAll_NullInputList() throws IOException {
        mappingIterator = new MappingIterator<>(factory.constructType(String.class), parser, context, null, true, null);
        mappingIterator.readAll((List<String>) null);
    }

    @Test
    public void testReadAll_CollectionTypeMismatch() throws IOException {
        mappingIterator = new MappingIterator<>(factory.constructType(Integer.class), parser, context, null, true, null);
        try {
            mappingIterator.readAll((Collection<String>) Collections.singletonList("test"));
            fail();
        } catch (ClassCastException e) {
            // Expected
        }
    }

    
}

class StringDeserializerWithException extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        throw new JsonMappingException("Deserializer Threw Mapping Exception");
    }
}

class StringDeserializerWithIOException extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        throw new IOException("Deserializer Threw IOException");
    }
}