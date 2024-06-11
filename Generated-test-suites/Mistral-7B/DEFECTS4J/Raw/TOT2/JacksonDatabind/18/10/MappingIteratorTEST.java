import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class MappingIteratorTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private MappingIterator<String> mappingIterator;

    @Before
    public void setup() throws IOException {
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("[]");
        mappingIterator = new MappingIterator<>(objectMapper.getTypeFactory().constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
    }

    // Test for basic functionality of hasNext() and next() methods
    @Test
    public void testBasicFunctionality() {
        assertTrue(mappingIterator.hasNext());
        assertEquals("", mappingIterator.next());
        assertTrue(mappingIterator.hasNext());
        assertFalse(mappingIterator.hasNext());
    }

    // Test for handling empty list
    @Test
    public void testEmptyList() {
        mappingIterator = MappingIterator.emptyIterator();
        assertFalse(mappingIterator.hasNext());
    }

    // Test for handling null parser
    @Test(expected = IllegalArgumentException.class)
    public void testNullParser() {
        new MappingIterator<>(null, null, null, null, false, null);
    }

    // Test for handling null type
    @Test(expected = IllegalArgumentException.class)
    public void testNullType() {
        new MappingIterator<>(null, jsonParser, null, null, false, null);
    }

    // Test for handling null context
    @Test(expected = IllegalArgumentException.class)
    public void testNullContext() {
        new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
    }

    // Test for handling null deserializer
    @Test(expected = IllegalArgumentException.class)
    public void testNullDeserializer() {
        new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null, true);
    }

    // Test for handling null updated value
    @Test
    public void testNullUpdatedValue() {
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null, true);
        assertTrue(mappingIterator.hasNextValue());
        String value = mappingIterator.nextValue();
        assertNull(value);
    }

    // Test for handling closed parser
    @Test(expected = IOException.class)
    public void testClosedParser() throws IOException {
        jsonParser.close();
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        mappingIterator.hasNextValue();
    }

    // Test for handling unmatched token
    @Test(expected = JsonMappingException.class)
    public void testUnmatchedToken() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("{");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        mappingIterator.nextValue();
    }

    // Test for reading all elements
    @Test
    public void testReadAll() throws IOException {
        List<String> list = new ArrayList<>();
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, list);
        mappingIterator.readAll();
        assertEquals(Collections.emptyList(), list);

        jsonParser = objectMapper.getFactory().createParser("[\"one\",\"two\",\"three\"]");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        List<String> resultList = mappingIterator.readAll();
        assertEquals(Collections.singletonList("one"), resultList);

        jsonParser = objectMapper.getFactory().createParser("[\"one\",\"two\",\"three\",\"four\"]");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        resultList = mappingIterator.readAll();
        assertEquals(Collections.singletonList(Collections.singletonList("one", "two", "three", "four")), resultList);
    }

    // Test for reading all elements into a Collection
    @Test
    public void testReadAllIntoCollection() throws IOException {
        Collection<String> collection = new ArrayList<>();
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, collection);
        mappingIterator.readAll();
        assertEquals(Collections.emptyList(), collection);

        jsonParser = objectMapper.getFactory().createParser("[\"one\",\"two\",\"three\"]");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, collection);
        mappingIterator.readAll();
        assertEquals(Collections.singletonList("one"), collection);

        jsonParser = objectMapper.getFactory().createParser("[\"one\",\"two\",\"three\",\"four\"]");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, collection);
        mappingIterator.readAll();
        assertEquals(Collections.singletonList(Collections.singletonList("one", "two", "three", "four")), collection);
    }

    // Test for handling custom deserializer
    @Test
    public void testCustomDeserializer() throws IOException {
        JsonDeserializer<String> customDeserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };

        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, customDeserializer, false, null);
        List<String> resultList = mappingIterator.readAll();
        assertEquals(Collections.singletonList(Collections.singletonList("", "", "")), resultList);
    }

    // Test for handling closed parser when reading all elements
    @Test(expected = IOException.class)
    public void testClosedParserWhenReadingAllElements() throws IOException {
        jsonParser.close();
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        mappingIterator.readAll();
    }

    // Test for handling unmatched token when reading all elements
    @Test(expected = JsonMappingException.class)
    public void testUnmatchedTokenWhenReadingAllElements() throws IOException {
        jsonParser = objectMapper.getFactory().createParser("{");
        mappingIterator = new MappingIterator<>(JavaType.constructCollectionType(List.class, String.class), jsonParser, null, null, false, null);
        mappingIterator.readAll();
    }
}