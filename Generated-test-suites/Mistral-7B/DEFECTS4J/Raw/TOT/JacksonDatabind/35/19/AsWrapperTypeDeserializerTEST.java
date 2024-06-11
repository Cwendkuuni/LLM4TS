package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TestBase;
import org.testng.annotations.*;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertInstanceOf;

public class AsWrapperTypeDeserializerTest extends TestBase {

    @Test
    public void testDeserializationWithWrapperObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(javaType, As.WRAPPER_OBJECT);

        String json = "{\"type\":\"com.TestClass\",\"value\":{\"name\":\"Test\",\"age\":30}}";
        Object readValue = mapper.readValue(json, javaType);

        assertInstanceOf(TestClass.class, readValue);
        TestClass testClass = (TestClass) readValue;
        assertEquals("Test", testClass.getName());
        assertEquals(30, testClass.getAge());
    }

    @Test
    public void testDeserializationWithMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"value\":{\"name\":\"Test\",\"age\":30}}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.ClassIdMismatch.class, e.getCause().getClass());
        }
    }

    @Test
    public void testDeserializationWithMissingValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"type\":\"com.TestClass\"}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.WrongTokenException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testDeserializationWithEmptyTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"type\":\"\",\"value\":{\"name\":\"Test\",\"age\":30}}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.WrongTokenException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testDeserializationWithInvalidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"type\":\"com.InvalidClass\",\"value\":{\"name\":\"Test\",\"age\":30}}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.ClassNotFoundException.class, e.getCause().getCause().getClass());
        }
    }

    @Test
    public void testDeserializationWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"type\":null,\"value\":{\"name\":\"Test\",\"age\":30}}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.WrongTokenException.class, e.getCause().getClass());
        }
    }

    @Test
    public void testDeserializationWithInvalidValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType javaType = typeFactory.constructType(AsWrapperTypeDeserializer.class);

        String json = "{\"type\":\"com.TestClass\",\"value\":123}";
        try {
            mapper.readValue(json, javaType);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            assertEquals(JsonMappingException.WrongTokenException.class, e.getCause().getClass());
        }
    }
}