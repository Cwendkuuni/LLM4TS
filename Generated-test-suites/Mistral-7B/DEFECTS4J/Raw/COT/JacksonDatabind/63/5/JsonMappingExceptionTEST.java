package com.fasterxml.jackson.databind.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class JsonMappingExceptionTest {

    
    
    @Test
    public void testJsonMappingException_basic() {
        String message = "Test JsonMappingException";
        JsonMappingException exception = new JsonMappingException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testJsonMappingException_with_location() {
        JsonMappingException exception = new JsonMappingException("Test JsonMappingException", null, 1, 1, "TestFile.java:123");
        assertEquals("Test JsonMappingException", exception.getMessage());
        assertEquals("TestFile.java:123", exception.getLocalizedMessage());
    }

    @Test
    public void testJsonMappingException_with_rootCause() {
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = new JsonMappingException("Test JsonMappingException", null, rootCause);
        assertEquals("Test JsonMappingException", exception.getMessage());
        assertNotNull(exception.getRootCause());
        assertEquals("Test IOException", exception.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingException_with_processor() {
        JsonGenerator generator = new JsonFactory().createGenerator(new StringWriter());
        JsonMappingException exception = new JsonMappingException(generator, "Test JsonMappingException");
        assertEquals("Test JsonMappingException", exception.getMessage());
        assertNotNull(exception.getProcessor());
        assertEquals(generator, exception.getProcessor());
    }

    @Test
    public void testJsonMappingException_fromJsonParser() {
        String json = "{\"name\":\"John\", \"age\":5}";
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createParser(json));
        assertEquals("Unexpected token (EXPECTING_VALUE_SEPARATOR): name<EOF>", exception.getMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonParser_with_location() {
        String json = "{\"name\":\"John\", \"age\":5}";
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createParser(json), 1, 1, "TestFile.java:123");
        assertEquals("Unexpected token (EXPECTING_VALUE_SEPARATOR): name<EOF>", exception.getMessage());
        assertEquals("TestFile.java:123", exception.getLocalizedMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonParser_with_rootCause() {
        String json = "{\"name\":\"John\", \"age\":5}";
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createParser(json), rootCause);
        assertEquals("Unexpected token (EXPECTING_VALUE_SEPARATOR): name<EOF>", exception.getMessage());
        assertNotNull(exception.getRootCause());
        assertEquals("Test IOException", exception.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonParser_with_path() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"name\":{}, \"age\":5}";
        JsonMappingException exception = JsonMappingException.from(mapper.readValue(json, Person.class));
        assertEquals(1, exception.getPath().size());
        Reference ref = exception.getPath().get(0);
        assertEquals(Person.class, ref.getFrom().getClass());
        assertEquals("name", ref.getFieldName());
    }

    @Test
    public void testJsonMappingException_fromJsonParser_with_path_and_rootCause() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"name\":{}, \"age\":5}";
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(mapper.readValue(json, Person.class), rootCause);
        assertEquals(1, exception.getPath().size());
        Reference ref = exception.getPath().get(0);
        assertEquals(Person.class, ref.getFrom().getClass());
        assertEquals("name", ref.getFieldName());
        assertNotNull(exception.getRootCause());
        assertEquals("Test IOException", exception.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonGenerator() {
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createGenerator(new StringWriter()), "Test JsonMappingException");
        assertEquals("Test JsonMappingException", exception.getMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonGenerator_with_location() {
        StringWriter writer = new StringWriter();
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createGenerator(writer), 1, 1, "TestFile.java:123");
        assertEquals("Test JsonMappingException", exception.getMessage());
        assertEquals("TestFile.java:123", exception.getLocalizedMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonGenerator_with_rootCause() {
        StringWriter writer = new StringWriter();
        IOException rootCause = new IOException("Test IOException");
        JsonMappingException exception = JsonMappingException.from(new JsonFactory().createGenerator(writer), rootCause);
        assertEquals("Test JsonMappingException", exception.getMessage());
        assertNotNull(exception.getRootCause());
        assertEquals("Test IOException", exception.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingException_fromJsonGenerator_with_path() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Person person = new Person();
        mapper.writeValueAsStrings(generator, person);
        JsonMappingException exception = JsonMappingException.from(generator);
        assertEquals(1, exception.getPath().size());
        Reference ref = exception.getPath().get(0);
        assertEquals(StringWriter.class, ref.getFrom().getClass());
        assertEquals("JsonGenerator", ref.getFieldName());
    }

    @Test
    public void testJsonMappingException_fromJsonGenerator_with_path_and_rootCause() {
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Person person = new Person();
        IOException rootCause = new IOException("Test IOException");
        mapper.writeValueAsStrings(generator, person);
        JsonMappingException exception = JsonMappingException.from(generator, rootCause);
        assertEquals(1, exception.getPath().size());
        Reference ref = exception.getPath().get(0);
        assertEquals(StringWriter.class, ref.getFrom().getClass());
        assertEquals("JsonGenerator", ref.getFieldName());
        assertNotNull(exception.getRootCause());
        assertEquals("Test IOException", exception.getRootCause().getMessage());
    }

    @Test
    public void testJsonMappingException_wrapWithPath() {
        IOException ioException = new IOException("Test IOException");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        Person person = new Person();
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, person);
        assertEquals(1, jme.getPath().size());
        Reference ref = jme.getPath().get(0);
        assertEquals(Person.class, ref.getFrom().getClass());
        assertEquals("Person", ref.getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException_wrapWithPath_with_class() {
        IOException ioException = new IOException("Test IOException");
        Class<?> clazz = Person.class;
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, clazz);
        fail("Expected JsonMappingException to be thrown but it was not.");
    }

    @Test
    public void testJsonMappingException_wrapWithPath_with_map() {
        IOException ioException = new IOException("Test IOException");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "John");
        map.put("age", 5);
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, map, "age");
        assertEquals(1, jme.getPath().size());
        Reference ref = jme.getPath().get(0);
        assertEquals(Map.class, ref.getFrom().getClass());
        assertEquals("age", ref.getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException_wrapWithPath_with_map_no_field() {
        IOException ioException = new IOException("Test IOException");
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", "John");
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, map, null);
        fail("Expected JsonMappingException to be thrown but it was not.");
    }

    @Test
    public void testJsonMappingException_wrapWithPath_with_array() {
        IOException ioException = new IOException("Test IOException");
        Object[] array = new Object[] { new Person(), "John" };
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, array, 1);
        assertEquals(1, jme.getPath().size());
        Reference ref = jme.getPath().get(0);
        assertEquals(Object[].class, ref.getFrom().getClass());
        assertEquals("1", ref.getFieldName());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonMappingException_wrapWithPath_with_array_no_index() {
        IOException ioException = new IOException("Test IOException");
        Object[] array = new Object[] { new Person(), "John" };
        JsonMappingException jme = JsonMappingException.wrapWithPath(ioException, array, null);
        fail("Expected JsonMappingException to be thrown but it was not.");
    }

    @Test
    public void testJsonMappingException_fromUnexpectedIOE() {
        IOException ioException = new IOException("Test IOException");
        JsonMappingException jme = JsonMappingException.fromUnexpectedIOE(ioException);
        assertEquals(1, jme.getPath().size());
        Reference ref = jme.getPath().get(0);
        assertEquals(IOException.class, ref.getFrom().getClass());
        assertEquals("IOException", ref.getFieldName());
    }

    static class Person {
        @JsonProperty("name")
        private String name;
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    
}