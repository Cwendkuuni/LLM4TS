package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * Test class for CollectionDeserializer.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    @Test
    public void testCollectionDeserializerBasic() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(String.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        ObjectNode input = mapper.createObjectNode();
        input.put("items", "[\"apple\", \"banana\", \"orange\"]");

        List<String> result = mapper.readValue(input, Collection.class);

        assertEquals(3, result.size());
        assertEquals("apple", result.get(0));
        assertEquals("banana", result.get(1));
        assertEquals("orange", result.get(2));
    }

    @Test
    public void testCollectionDeserializerNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(String.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        ObjectNode input = mapper.createObjectNode();
        input.put("items", "null");

        List<String> result = mapper.readValue(input, Collection.class);

        assertNull(result);
    }

    @Test
    public void testCollectionDeserializerEmpty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(String.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);

        ObjectNode input = mapper.createObjectNode();
        input.put("items", "[]");

        List<String> result = mapper.readValue(input, Collection.class);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testCollectionDeserializerWithDelegateDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Number.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(Integer.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(Integer.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        JsonDeserializer<Object> delegateDeserializer = mapper.getFactory().createValueDeserializer(Integer.class);
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, null);

        ObjectNode input = mapper.createObjectNode();
        input.put("items", "[1, 2, 3]");

        List<Number> result = mapper.readValue(input, Collection.class);

        assertEquals(3, result.size());
        for (Number number : result) {
            assertEquals(number.intValue(), number.intValue());
        }
    }

    @Test
    public void testCollectionDeserializerWithWrapSingleValueAsArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(String.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);
        ObjectNode input = mapper.createObjectNode();
        input.put("items", "apple");

        List<String> result = mapper.readValue(input, Collection.class);

        assertEquals(1, result.size());
        assertEquals("apple", result.get(0));
    }

    @Test
    public void testCollectionDeserializerWithUnresolvedForwardReference() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = mapper.getFactory().createValueDeserializer(String.class);
        TypeDeserializer valueTypeDeserializer = mapper.getFactory().createTypeDeserializer(String.class);
        ValueInstantiator valueInstantiator = mapper.getFactory().getValueInstantiator();
        CollectionDeserializer collectionDeserializer = new CollectionDeserializer(javaType, valueDeserializer, valueTypeDeserializer, valueInstantiator, null, null);
        ObjectNode input = mapper.createObjectNode();
        String id = "id";
        ObjectNode arrayNode = mapper.createArrayNode();
        arrayNode.add(mapper.valueToTree("value1"));
        arrayNode.add(mapper.valueToTree(id));
        input.set("items", arrayNode);

        List<String> result = mapper.readValue(input, Collection.class);

        assertNotNull(result);
        assertNotNull(result.get(1));
        assertEquals(0, result.get(1).size());

        ObjectNode idNode = mapper.readTree(mapper.readerForUpdating(result.get(1)).readValueAsTree());
        List<Object> ids = mapper.readValue(idNode, Collection.class);
        assertEquals(1, ids.size());
        assertEquals(id, ids.get(0).toString());
    }
}