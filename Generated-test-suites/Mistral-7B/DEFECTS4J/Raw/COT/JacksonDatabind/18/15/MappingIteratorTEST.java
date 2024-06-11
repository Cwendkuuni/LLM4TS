package com.fasterxml.jackson.databind.test; // assume we have a package named 'test' for test classes

import com.fasterxml.jackson.core.JsonMappingException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    

    @Test
    public void testHasNext() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        iterator.remove();
    }

    @Test
    public void testNext() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        String value = iterator.next();
        assertNotNull(value);
        assertEquals("expected value", value);
        iterator.next();
        assertNull(iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElement() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next(); // this one should throw NoSuchElementException
    }

    @Test
    public void testClose() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextValue() throws Exception {
        MappingIterator<String> iterator = createMappingIterator();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        JsonParser parser = createParserWithIOException();
        MappingIterator<String> iterator = new MappingIterator<String>(null, parser, null, null, false, null);
        iterator.nextValue();
        fail("expected IOException");
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException() throws Exception {
        JsonParser parser = createParserWithJsonMappingException();
        MappingIterator<String> iterator = new MappingIterator<String>(null, parser, null, null, false, null);
        iterator.nextValue();
        fail("expected JsonMappingException");
    }

    @Test
    public void testReadAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[\"one\", \"two\", \"three\", \"four\", \"five\"]");
        MappingIterator<String> iterator = new MappingIterator<String>(mapper.getTypeFactory().constructCollectionType(ArrayList.class, String.class), parser, null, null, false, null);
        List<String> result = iterator.readAll();
        assertSize(5, result);
        for (int i = 0; i < result.size(); i++) {
            assertEquals("expected value " + (i + 1), "\"expected value " + (i + 1) + '\"', result.get(i));
        }
    }

    private static MappingIterator<String> createMappingIterator() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\"}");
        return new MappingIterator<String>(mapper.getTypeFactory().constructValueType(String.class, null), parser, null, mapper.findValueDeserializer(String.class, null), false, null);
    }

    private static JsonParser createParserWithIOException() throws Exception {
        return createParserWithException(new IOException("expected IOException"));
    }

    private static JsonParser createParserWithJsonMappingException() throws Exception {
        return createParserWithException(new JsonMappingException(new JsonParseException("expected JsonMappingException"), "expectedJsonMappingException"));
    }

    private static JsonParser createParserWithException(Exception e) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser("{\"name\": \"John\"}");
        parser.setErrorHandler(new ThrowingErrorHandler());
        parser.nextToken();
        parser.getParserConfig().handleUnexpectedToken(parser.getCurrentToken(), null);
        parser.clearCurrentToken();
        parser.setText(null);
        parser.close();
        parser = factory.createParser("{\"class\": \"Exception\", \"message\": \"" + e.getMessage() + "\"}");
        return parser;
    }

    private static class ThrowingErrorHandler extends BaseErrorHandler {
        @Override
        public void reportingError(JsonParser p, JsonMappingException m) throws IOException {
            throw m;
        }
    }

    
}