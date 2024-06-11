import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;

public class MappingIteratorTest {
    
    private MappingIterator<Object> emptyIterator;

    @Before
    public void setUp() throws Exception {
        emptyIterator = MappingIterator.emptyIterator();
    }

    @Test
    public void testHasNextValue_EmptyIterator() throws IOException {
        assertFalse(emptyIterator.hasNextValue());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextValue_EmptyIterator() throws IOException {
        emptyIterator.nextValue();
    }

    @Test
    public void testHasNext_EmptyIterator() throws IOException {
        assertFalse(emptyIterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext_EmptyIterator() throws IOException {
        emptyIterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_EmptyIterator() throws IOException {
        emptyIterator.remove();
    }

    private MappingIterator<String> createIterator(String... values) throws IOException {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        JavaType type = null;
        Object valueToUpdate = null;
        boolean managedParser = true;
        MappingIterator<String> iterator = new MappingIterator<>(type, parser, context, deserializer, managedParser, valueToUpdate);
        
        for (String value : values) {
            parser.nextToken();
            parser.setValue(value);
            parser.nextToken();
        }
        
        return iterator;
    }

    @Test
    public void testHasNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator("a", "b", "c");
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertTrue(iterator.hasNextValue());
        iterator.nextValue();
        assertFalse(iterator.hasNextValue());
    }

    @Test
    public void testNextValue() throws IOException {
        MappingIterator<String> iterator = createIterator("a", "b", "c");
        assertEquals("a", iterator.nextValue());
        assertEquals("b", iterator.nextValue());
        assertEquals("c", iterator.nextValue());
    }

    @Test
    public void testReadAll() throws IOException {
        MappingIterator<String> iterator = createIterator("1", "2", "3");
        List<String> resultList = new ArrayList<>();
        iterator.readAll(resultList);
        assertEquals("1", resultList.get(0));
        assertEquals("2", resultList.get(1));
        assertEquals("3", resultList.get(2));
    }

    @Test
    public void testReadAll_EmptyList() throws IOException {
        MappingIterator<String> iterator = createIterator("1", "2", "3");
        List<String> resultList = Collections.emptyList();
        List<String> result = iterator.readAll(resultList);
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }

    @Test
    public void testReadAll_Collection() throws IOException {
        MappingIterator<String> iterator = createIterator("1", "2", "3");
        List<String> resultList = new ArrayList<>();
        resultList.add("0");
        List<String> result = iterator.readAll(resultList);
        assertEquals("0", result.get(0));
        assertEquals("1", result.get(1));
        assertEquals("2", result.get(2));
        assertEquals("3", result.get(3));
    }

    @Test
    public void testGetParser() throws IOException {
        MappingIterator<String> iterator = createIterator("a");
        JsonParser parser = iterator.getParser();
        assertNotNull(parser);
    }

    @Test
    public void testGetParserSchema() throws IOException {
        MappingIterator<String> iterator = createIterator("a");
        assertNull(iterator.getParserSchema());
    }

    @Test
    public void testGetCurrentLocation() throws IOException {
        MappingIterator<String> iterator = createIterator("a");
        JsonLocation location = iterator.getCurrentLocation();
        assertNotNull(location);
    }

    @Test
    public void testThrowNoSuchElement() {
        try {
            emptyIterator._throwNoSuchElement();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            assertEquals("No such element", e.getMessage());
        }
    }

    @Test
    public void testHandleMappingException() {
        try {
            emptyIterator._handleMappingException(new JsonMappingException("Mapping failed"));
            fail("Expected RuntimeJsonMappingException");
        } catch (RuntimeJsonMappingException e) {
            assertEquals("Mapping failed", e.getMessage());
        }
    }

    @Test
    public void testHandleIOException() {
        try {
            emptyIterator._handleIOException(new IOException("IO exception"));
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("IO exception", e.getMessage());
        }
    }
}