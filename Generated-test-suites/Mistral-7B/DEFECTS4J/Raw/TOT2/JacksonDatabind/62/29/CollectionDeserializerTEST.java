import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CollectionDeserializerTest {

    // Test method for basic functionality of CollectionDeserializer
    @Test
    public void testBasicFunctionality() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, String.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<String> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test method for handling null values
    @Test
    public void testHandlingNullValues() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[null]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Object.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Object> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertTrue(result.contains(null));
        assertEquals(1, result.size());
    }

    // Test method for handling empty arrays
    @Test
    public void testHandlingEmptyArrays() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Integer> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test method for handling an array with single element
    @Test
    public void testHandlingSingleElementArrays() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Integer> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).intValue());
    }

    // Test method for handling an array with multiple elements
    @Test
    public void testHandlingMultipleElementsArrays() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, 2, 3]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Integer.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Integer> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals(2, result.get(1).intValue());
        assertEquals(3, result.get(2).intValue());
    }

    // Test method for handling arrays with different element types
    @Test
    public void testHandlingArraysWithDifferentElementTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, \"two\", 3.0]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Object> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals("two", result.get(1));
        assertEquals(3.0, result.get(2).doubleValue());
    }

    // Test method for handling an array with polymorphic types
    @Test
    public void testHandlingArraysWithPolymorphicTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[1, \"two\", 3.0, new Date()]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(Date.class, null);
        CollectionDeserializer contextualDeserializer = deserializer.withResolved(null, null, typeDeserializer, null);
        List<Object> result = contextualDeserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).intValue());
        assertEquals("two", result.get(1));
        assertEquals(3.0, result.get(2).doubleValue());
        assertTrue(result.get(3) instanceof Date);
    }

    // Test method for handling a null type descriptor
    @Test(expected = IllegalArgumentException.class)
    public void testHandlingNullTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        TypeDeserializer typeDeserializer = null;
        CollectionDeserializer contextualDeserializer = deserializer.withResolved(null, null, typeDeserializer, null);
        contextualDeserializer.deserialize(mapper.getFactory().createParser("[1]"), null);
    }

    // Test method for handling an array with a non-supported type
    @Test(expected = JsonMappingException.class)
    public void testHandlingArraysWithNonSupportedTypes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[true]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Boolean.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Boolean> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertEquals(1, result.size());
        // This should throw an exception
        result.get(0);
    }

    // Test method for handling an array with a circular reference
    @Test(expected = JsonMappingException.class)
    public void testHandlingArraysWithCircularReferences() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("[{id: 1, nested: {id: 0}}]");
        JavaType javaType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
        CollectionDeserializer deserializer = mapper.findValueDeserializer(javaType);
        List<Object> result = deserializer.deserialize(mapper.treeToValues(jsonNode), null);
        assertNotNull(result);
        // This should throw an exception
        result.get(0).getClass().getDeclaredMethod("getId").invoke(result.get(0));
    }
}