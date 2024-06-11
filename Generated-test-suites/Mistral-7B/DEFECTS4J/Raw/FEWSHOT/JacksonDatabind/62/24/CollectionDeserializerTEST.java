package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.CollectionReferringAccumulator;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

/**
 * Test cases for {@link CollectionDeserializer} class.
 */
public class CollectionDeserializerTest {

    @Test
    public void testEmptyArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("[]");
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(0, result.size());
    }

    @Test
    public void testSingleElementArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("['foo']");
        Collection<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(1, result.size());
        assertEquals("foo", result.get(0));
    }

    @Test
    public void testMultipleElementsArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer<List<String>> deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("['foo', 'bar', 'baz']");
        List<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(3, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));
        assertEquals("baz", result.get(2));
    }

    @Test
    public void testDelegateDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<Integer> integerDeserializer = new StandardDeserializer<Integer>(Integer.class);
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, Integer.class);
        deserializer = deserializer.withResolved(integerDeserializer, null, null);
        JsonNode node = mapper.readTree("[1, 2, 3]");
        List<Integer> result = mapper.readValue(node, deserializer.getType());
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }

    @Test
    public void testUnresolvedForwardReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null);
        JsonNode node = mapper.readTree("[['foo'], 'bar']");
        List<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(2, result.size());
        assertEquals("foo", result.get(0));
        assertEquals("bar", result.get(1));

        CollectionDeserializer deserializer2 = deserializer.withResolved(stringDeserializer, null, null);
        JsonNode node2 = mapper.readTree("[['foo'], ['foo']]");
        List<String> result2 = mapper.readValue(node2, deserializer2.getType());
        assertEquals(2, result2.size());
        assertEquals("foo", result2.get(0));
        assertEquals("foo", result2.get(1));

        deserializer2 = deserializer2.withResolved(stringDeserializer, null, null);
        JsonNode node3 = mapper.readTree("[['foo'], ['bar']]");
        List<String> result3 = mapper.readValue(node3, deserializer2.getType());
        assertEquals(2, result3.size());
        assertEquals("foo", result3.get(0));
        assertEquals("bar", result3.get(1));

        deserializer2 = deserializer2.withResolved(stringDeserializer, null, null);
        JsonNode node4 = mapper.readTree("[['foo'], ['foo'], ['bar']]");
        List<String> result4 = mapper.readValue(node4, deserializer2.getType());
        assertEquals(3, result4.size());
        assertEquals("foo", result4.get(0));
        assertEquals("foo", result4.get(1));
        assertEquals("bar", result4.get(2));
    }

    @Test
    public void testContentConverter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null);
        JsonDeserializer<List<Integer>> intListDeserializer =
                new ListDeserializer(Integer.class, mapper.getFactory().findValueDeserializer(Integer.class), false);
        deserializer = deserializer.withValueDeserializer(intListDeserializer);
        JsonNode node = mapper.readTree("[1, 2, 3]");
        List<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }

    @Test
    public void testPolymorphicDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
        CollectionDeserializer<List<Object>> deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, Object.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null);
        deserializer = deserializer.withResolved(intDeserializer, null, null);
        JsonNode node = mapper.readTree("[1, 'foo', 3.0, 'bar']");
        List<Object> result = mapper.readValue(node, deserializer.getType());
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals("foo", result.get(1));
        assertEquals(3.0, result.get(2).doubleValue());
        assertEquals("bar", result.get(3));
    }

    @Test
    public void testEmptyCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("null");
        Collection<Object> result = mapper.readValue(node, new CollectionDeserializer(null, null, null, null, null, null)
                .getType());
        assertEquals(0, result.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testNonArrayException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{'foo': 'bar'}");
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        deserializer = deserializer.withResolved(null, null, null);
        deserializer.deserialize(mapper.getFactory().createParser(node), null);
    }

    @Test
    public void testNullValueDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("[null]");
        List<String> result = mapper.readValue(node, deserializer.getType());
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    public void testNullCollectionDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(null);
        Collection<Object> result = mapper.readValue(node, new CollectionDeserializer(null, null, null, null, null, null)
                .getType());
        assertEquals(0, result.size());
    }

    @Test(expected = JsonMappingException.class)
    public void testFailedDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CollectionDeserializer deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, String.class);
        JsonNode node = mapper.readTree("['foo']");
        JsonParser p = mapper.getFactory().createParser(node);
        deserializer.deserialize(p, null);
    }

    @Test
    public void testPolymorphicTypeDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> stringDeserializer = new StandardDeserializer<String>(String.class);
        JsonDeserializer<Integer> intDeserializer = new StandardDeserializer<Integer>(Integer.class);
        CollectionDeserializer<List<Object>> deserializer = mapper.getTypeFactory()
                .constructCollectionType(List.class, Object.class);
        deserializer = deserializer.withResolved(stringDeserializer, null, null);
        deserializer = deserializer.withResolved(intDeserializer, new IntegerTypeDeserializer(mapper.getFactory()), null);
        JsonNode node = mapper.readTree("[1, 'foo']");
        List<Object> result = mapper.readValue(node, deserializer.getType());
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals("foo", result.get(1));
    }

    @JsonDeserializer(as = Integer.class)
    static class IntegerTypeDeserializer
            extends JsonDeserializer<Object> {
        final JsonDeserializer<Integer> _intDeserializer;

        public IntegerTypeDeserializer(JsonDeserializer.MapperFactory factory) {
            _intDeserializer = factory.findValueDeserializer(Integer.class);
        }

        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            return _intDeserializer.deserialize(jsonParser, deserializationContext);
        }
    }
}