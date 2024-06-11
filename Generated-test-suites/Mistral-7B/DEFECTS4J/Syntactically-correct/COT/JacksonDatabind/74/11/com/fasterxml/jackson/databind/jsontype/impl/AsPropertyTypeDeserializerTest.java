package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testConstructorWithDefaultImpl() throws Exception {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY);
        assertNotNull(deserializer);
    }

    @Test
    public void testConstructorWithTypeIdResolver() throws Exception {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testConstructorWithTypeIdResolverAndInclusion() throws Exception {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY, As.INCLUDE_AS_PROPERTY);
        assertNotNull(deserializer);
    }

    @Test
    public void testConstructorWithTypeIdResolverAndExclusion() throws Exception {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY, As.EXCLUDE_AS_PROPERTY);
        assertNotNull(deserializer);
    }

    @Test
    public void testConstructorWithProperty() throws Exception {
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY, null, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testForProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        BeanProperty property = new BeanProperty("propertyName", javaType);
        AsPropertyTypeDeserializer deserializerForProperty = deserializer.forProperty(property);
        assertEquals(deserializer, deserializerForProperty);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"typePropertyName\":\"String\",\"data\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeTypedFromObject(jsonParser, null);
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithoutTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"data\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{\"data\":\"test\"}")), null);
        // null or some default implementation should be returned
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{\"data\":\"test\"}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithPropertyNameMismatch() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typeOtherPropertyName", false, null, As.PROPERTY);
        String json = "{\"typePropertyName\":\"String\",\"data\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        try {
            deserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        JsonParser jsonParser = mapper.getFactory().createParser("{}");
        try {
            deserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyJsonAndDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{}")), null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"@type\":\"java.lang.String\",\"data\":\"test\"}";
        JsonParser jsonParser = mapper.getFactory().createParser(json);
        Object result = deserializer.deserializeWithNativeTypeId(jsonParser, null, "java.lang.String");
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"@type\":\"\"}"; // empty native type id
        try {
            deserializer.deserializeWithNativeTypeId(new JsonParser(new StringReader(json)), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingNativeTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"data\":\"test\"}";
        try {
            deserializer.deserializeWithNativeTypeId(new JsonParser(new StringReader(json)), null, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingNativeTypeIdAndDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{\"data\":\"test\"}")), null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{\"data\":\"test\"}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingNativeTypeIdAndEmptyJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{}")), null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingNativeTypeIdAndEmptyJsonAndDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{}")), null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithEmptyJsonAndDefaultImplWithCustomDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        StdDeserializer customDeserializer = new StdDeserializer();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY, customDeserializer);
        Object result1 = deserializer.deserializeTypedFromObject(new JsonParser(new StringReader("{}")), null);
        Object result2 = deserializer.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        String json = "{\"invalidJson\":\"test\"}";
        try {
            deserializer.deserializeTypedFromObject(new JsonParser(new StringReader(json)), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithInvalidJsonAndDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(new JsonParser(new StringReader("{\"invalidJson\":\"test\"}")), null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(new JsonParser(new StringReader("{\"invalidJson\":\"test\"}")), null, null);
        assertEquals(result1, result2);
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        JsonParser jsonParser = mapper.getFactory().createParser("{}");
        try {
            deserializer.deserializeTypedFromObject(jsonParser, null);
            fail("Expected JsonProcessingException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeTypedFromObjectWithMissingJsonAndDefaultImpl() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver typeIdResolver = mapper.getTypeIdResolver();
        AsPropertyTypeDeserializer deserializer1 = new AsPropertyTypeDeserializer(javaType, typeIdResolver, "typePropertyName", false, null, As.PROPERTY);
        Object result1 = deserializer1.deserializeTypedFromObject(null, null);
        Object result2 = deserializer1.deserializeTypedUsingDefaultImpl(null, null, null);
        assertEquals(result1, result2);
    }
}