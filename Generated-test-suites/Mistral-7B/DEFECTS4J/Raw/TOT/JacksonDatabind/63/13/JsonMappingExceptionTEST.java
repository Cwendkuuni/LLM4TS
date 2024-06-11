package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link JsonMappingException}.
 */
public class JsonMappingExceptionTest {

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testSerializationWithPath() throws Exception {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("a.b.c", 1);
        map.put("d", new Object());

        try {
            mapper.writeValueAsString(map);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that reference path is correct
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(4, references.size());
            assertEquals("d", references.get(0).getFieldName());
            assertEquals("a", references.get(1).getFieldName());
            assertEquals("b", references.get(2).getFieldName());
            assertEquals("c", references.get(3).getFieldName());

            // Check that description is correct
            assertEquals("Map<java.lang.String, java.lang.Object>(" +
                    "\"a\"={" +
                    "    \"b\"={" +
                    "        \"c\"=1," +
                    "        \"d\"=org.test.TestObject()}})",
                    e.getDescription());
        }

        // Test with array
        Object[] arr = new Object[2];
        arr[0] = new Object();
        arr[1] = new Object();

        try {
            mapper.writeValueAsString(arr);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that reference path is correct
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(3, references.size());
            assertEquals("length=2", references.get(0).getDescription());
            assertEquals("arr[1]", references.get(1).getDescription());
            assertEquals("arr[0]", references.get(2).getDescription());
        }
    }

    @Test
    public void testDeserializationWithPath() throws Exception {
        String json = "{\"a\":{\"b\":{\"c\":1}},\"d\":{},\"e\":[{\"f\":1},{}]}";

        try {
            mapper.readValue(new ByteArrayInputStream(json.getBytes()), Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that reference path is correct
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(6, references.size());
            assertEquals("e[1]", references.get(0).getDescription());
            assertEquals("e[0]", references.get(1).getDescription());
            assertEquals("arrays[0]", references.get(2).getDescription());
            assertEquals("arrays[0].f", references.get(3).getDescription());
            assertEquals("a.b", references.get(4).getDescription());
            assertEquals("c", references.get(5).getDescription());

            // Check that description is correct
            assertEquals("Map<java.lang.String, DefaultSerializableBoolean>(" +
                    "\"e\"=[ArrayType(length=2){" +
                    "    [0]=Map<java.lang.String, DefaultSerializableBoolean>(" +
                    "        \"f\"=1," +
                    "        \"class\"=class com.fasterxml.jackson.core.JsonProcessingException]," +
                    "    [1]=Map<java.lang.String, DefaultSerializableBoolean>()}}," +
                    "\"a\"={" +
                    "    \"b\"={" +
                    "        \"c\"=1," +
                    "        \"class\"=class com.fasterxml.jackson.core.JsonProcessingException}}," +
                    "\"d\"={}",
                    e.getDescription());
        }

        // Test with Map
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("a.b", mapper.readValue(new ByteArrayInputStream(json.getBytes()), Map.class));
        map.put("c", new Object());

        try {
            mapper.writeValueAsString(map);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that reference path is correct
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(5, references.size());
            assertEquals("c", references.get(0).getDescription());
            assertEquals("a.b", references.get(1).getDescription());
            assertEquals("Map<java.lang.String, java.lang.Object>" +
                    "(class=class com.fasterxml.jackson.core.JsonMappingException)",
                    references.get(2).getDescription());
            assertEquals("Map<java.lang.String, DefaultSerializableBoolean>" +
                    "(class=class com.fasterxml.jackson.core.JsonMappingException)",
                    references.get(3).getDescription());
            assertEquals("Map<java.lang.String, DefaultSerializableBoolean>" +
                    "(class=class com.fasterxml.jackson.core.JsonMappingException)[\"f\"]",
                    references.get(4).getDescription());

            // Check that description is correct
            assertEquals("Map<java.lang.String, Map<java.lang.String, Object>>(" +
                    "\"c\"={},\"a\"={" +
                    "    \"b\"={" +
                    "        \"class\"=class com.fasterxml.jackson.core.JsonMappingException," +
                    "        \"Map<java.lang.String, DefaultSerializableBoolean>" +
                    "            (class=class com.fasterxml.jackson.core.JsonMappingException)" +
                    "            [\"f\"]={1}}}}",
                    e.getDescription());
        }

        // Test serialization with cyclic references
        map.put("a.b", map);

        try {
            mapper.writeValueAsString(map);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that reference path is correct
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(4, references.size());
            assertEquals("a.b", references.get(0).getDescription());
            assertEquals("a.b", references.get(1).getDescription());
            assertEquals("Map<java.lang.String, Map<java.lang.String, Object>>" +
                    "(class=class com.fasterxml.jackson.core.JsonMappingException)",
                    references.get(2).getDescription());
            assertEquals("Map<java.lang.String, Map<java.lang.String, Object>>" +
                    "(class=class com.fasterxml.jackson.core.JsonMappingException)",
                    references.get(3).getDescription());

            // Check that description is correct
            assertEquals("Map<java.lang.String, Map<java.lang.String, Object>>(" +
                    "\"a.b\"={" +
                    "    \"class\"=class com.fasterxml.jackson.core.JsonMappingException," +
                    "    \"Map<java.lang.String, Map<java.lang.String, Object>>" +
                    "        (class=class com.fasterxml.jackson.core.JsonMappingException)" +
                    "        [\"a.b\"]={" +
                    "            \"class\"=class com.fasterxml.jackson.core.JsonMappingException}}}",
                    e.getDescription());
        }

        // Test serialization with long reference chains
        for (int i = 0; i < 1000; i++) {
            Map<String, Map<String, Object>> mapLevel = new HashMap<>();
            mapLevel.put("mapLevel" + i, mapper.readValue(new ByteArrayInputStream(json.getBytes()), Map.class));
        }

        try {
            mapper.writeValueAsString(map);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(1001, references.size());
            // The last reference should be "Map<java.lang.String, Map<java.lang.String, Object>>"
            // with a very long description. We can't easily test that, so just check the size.
            assertTrue(references.get(1000).getDescription().length() > 500);
        }

        // Test deserialization with long reference chains
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mapper.writeValue(baos, map);
        String json2 = baos.toString();

        try {
            mapper.readValue(new ByteArrayInputStream(json2.getBytes()), Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            List<JsonMappingException.Reference> references = e.getPath();
            assertEquals(1001, references.size());
            // The last reference should be "Map<java.lang.String, Map<java.lang.String, Object>>"
            // with a very long description. We can't easily test that, so just check the size.
            assertTrue(references.get(1000).getDescription().length() > 500);
        }
    }

    @Test
    public void testSerializerProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"foo\":1}";

        try {
            mapper.readValue(new ByteArrayInputStream(json.getBytes()), String.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Test with null processor
            JsonMappingException e2 = JsonMappingException.from(null, e);
            assertEquals("Unexpected JsonMappingException: " + e.getMessage(),
                    e2.getMessage());

            // Test with JsonParser
            JsonMappingException e3 = JsonMappingException.from(mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes())), e);
            assertEquals("Unexpected JsonMappingException: " + e.getMessage(),
                    e3.getMessage());

            // Test with JsonGenerator
            JsonMappingException e4 = JsonMappingException.from(mapper.getFactory().createGenerator(), e);
            assertEquals("Unexpected JsonMappingException: " + e.getMessage(),
                    e4.getMessage());

            // Test with DeserializationContext
            JsonMappingException e5 = JsonMappingException.from(mapper.getDeserializationContext(), e);
            assertEquals("Unexpected JsonMappingException: " + e.getMessage(),
                    e5.getMessage());

            // Test with SerializerProvider
            JsonMappingException e6 = JsonMappingException.from(mapper.getSerializationConfig().getDefaultSerializerProvider(), e);
            assertEquals("Unexpected JsonMappingException: " + e.getMessage(),
                    e6.getMessage());
        }
    }

    @Test
    public void testUnexpectedIOException() throws Exception {
        String json = "asdf";
        ByteArrayInputStream input = new ByteArrayInputStream(json.getBytes());
        try {
            mapper.readValue(input, Object.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Check that underlying IOException is correctly wrapped
            IOException io = e.getCause();
            assertEquals("Unexpected IOException: null", io.getMessage());
            assertEquals(IOException.class, io.getClass());

            // Check that JsonMappingException description is correct
            assertEquals("Unexpected IOException(IOException: null)", e.getDescription());
        }

        // Test with JsonProcessingException
        try {
            mapper.readTree(input);
            fail("Expected JsonMappingException");
        } catch (JsonProcessingException e) {
            // Check that underlying IOException is correctly wrapped
            JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(e);
            assertEquals("Unexpected IOException(IOException: null)", jme.getMessage());
            assertEquals(JsonMappingException.class, jme.getClass());

            // Check that JsonMappingException description is correct
            assertEquals("Unexpected IOException(IOException: null)", jme.getDescription());
        }
    }

    @Test
    public void testWrapWithPath() throws Exception {
        String json = "{\"foo\":[1,2]}";
        Object foo = mapper.readValue(new ByteArrayInputStream(json.getBytes()), Object[].class)[0];
        JsonMappingException e = new JsonMappingException(new IOException("Unexpected IOException"));
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e, foo, "foo[0]");

        // Check that reference path is correct
        List<JsonMappingException.Reference> references = e2.getPath();
        assertEquals(2, references.size());
        assertEquals("foo[0]", references.get(0).getDescription());
        assertEquals("Object[]", references.get(1).getDescription());

        // Check that description is correct
        assertEquals("Unexpected IOException(IOException: null)", e2.getMessage());
        assertEquals("Unexpected IOException(IOException: null) (through reference chain: Object[0])", e2.getDescription());
    }

    @Test
    public void testWrapWithPathWithIndex() throws Exception {
        String json = "[1,2]";
        Object[] foo = mapper.readValue(new ByteArrayInputStream(json.getBytes()), Object[].class);
        JsonMappingException e = new JsonMappingException(new IOException("Unexpected IOException"));
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e, foo, 1);

        // Check that reference path is correct
        List<JsonMappingException.Reference> references = e2.getPath();
        assertEquals(3, references.size());
        assertEquals("length=2", references.get(0).getDescription());
        assertEquals("Object[][1]", references.get(1).getDescription());
        assertEquals("Object[]", references.get(2).getDescription());

        // Check that description is correct
        assertEquals("Unexpected IOException(IOException: null)", e2.getMessage());
        assertEquals("Unexpected IOException(IOException: null) (through reference chain: Object[][1])", e2.getDescription());
    }

    @Test
    public void testWrapWithPathWithClass() throws Exception {
        Class<?> clazz = Integer.class;
        JsonMappingException e = new JsonMappingException(new IOException("Unexpected IOException"));
        JsonMappingException e2 = JsonMappingException.wrapWithPath(e, clazz, "Integer");

        // Check that reference path is correct
        List<JsonMappingException.Reference> references = e2.getPath();
        assertEquals(2, references.size());
        assertEquals("java.lang.Class", references.get(0).getDescription());
        assertEquals("Integer", references.get(1).getDescription());

        // Check that description is correct
        assertEquals("Unexpected IOException(IOException: null)", e2.getMessage());
        assertEquals("Unexpected IOException(IOException: null) (through reference chain: java.lang.Class<Integer>)", e2.getDescription());
    }
}