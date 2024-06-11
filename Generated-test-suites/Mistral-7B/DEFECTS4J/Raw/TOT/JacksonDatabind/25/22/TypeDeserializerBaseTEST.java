package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() throws IOException {
        // Test with a simple base class
        JavaType baseType = TypeFactory.defaultInstance().constructType(SimpleClass.class);
        TypeIdResolver idResolver = new StdTypeIdResolver();
        TypeDeserializer typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", false, null);
        BeanProperty property = new BeanProperty.StandardProperty("propertyName", SimpleClass.class);
        TypeDeserializer forProperty = typeDeserializer.forProperty(property);
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
                .use(JsonTypeInfo.Id.NAME)
                .build();

        // Test deserialization with valid type id
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Json.Include.NON_NULL);
        mapper.setDefaultTyping(DefaultTyping.NON_FINAL);
        mapper.setSerializationFeatures(mapper.getDefaultSerializationFeatures());
        mapper.setDeserializationFeatures(mapper.getDefaultDeserializationFeatures());
        SimpleClass obj = mapper.readValue(
                new StringReader("{\"propertyName\": {\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.SimpleClass\", \"type\": \"SimpleClassTypeId\"}}"),
                SimpleClass.class);
        assertEquals("Expected valid object deserialization", new SimpleClass(), obj);

        // Test deserialization with missing type id
        String jsonWithMissingTypeId = "{\"propertyName\": null}";
        try {
            obj = mapper.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
            fail("Expected exception when type id is missing");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test deserialization with unknown type id
        String jsonWithUnknownTypeId = "{\"propertyName\": {\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.SimpleClass\", \"type\": \"UnknownTypeId\"}}";
        try {
            obj = mapper.readValue(new StringReader(jsonWithUnknownTypeId), SimpleClass.class);
            fail("Expected exception when type id is unknown");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("Expected specific error message", 
                    "Unknown type id [UnknownTypeId]", e.getMessage());
        }

        // Test deserialization with null value
        jsonWithMissingTypeId = "{\"propertyName\": null}";
        obj = mapper.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
        assertNull("Expected null object when type id is missing", obj);

        // Test deserialization with default implementation
        JavaType defaultImplType = TypeFactory.defaultInstance().constructType(DefaultImplementationClass.class);
        TypeDeserializerBase typeDeserializerDefaultImpl = new TypeDeserializerBase(baseType, idResolver, 
                "typePropertyName", false, DefaultImplementationClass.class);
        TypeDeserializer forPropertyDefaultImpl = typeDeserializerDefaultImpl.forProperty(property);

        // Test deserialization with valid type id and default implementation in the map
        SimpleClass objWithDefaultImpl = mapper.readValue(
                new StringReader("{\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", " +
                        "\"type\": \"SimpleClassTypeId\"}"), SimpleClass.class);
        assertEquals("Expected valid object deserialization", new SimpleClass(), objWithDefaultImpl);

        // Test deserialization with missing type id and default implementation in the map
        jsonWithMissingTypeId = "{\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", " +
                "\"propertyName\": null}";
        objWithDefaultImpl = mapper.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
        assertNull("Expected null object when type id is missing", objWithDefaultImpl);

        // Test deserialization with unknown type id and default implementation in the map
        jsonWithUnknownTypeId = "{" +
                "\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\"," +
                "\"type\": \"UnknownTypeId\"}";
        try {
            objWithDefaultImpl = mapper.readValue(new StringReader(jsonWithUnknownTypeId), SimpleClass.class);
            fail("Expected exception when type id is unknown");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("Expected specific error message", 
                    "Unknown type id [UnknownTypeId]", e.getMessage());
        }

        // Test deserialization with null value and default implementation in the map
        jsonWithMissingTypeId = "{\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", " +
                "\"propertyName\": null}";
        objWithDefaultImpl = mapper.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
        assertNull("Expected null object when type id is missing", objWithDefaultImpl);

        // Test deserialization with null value and default implementation in the map and nullifying deserializer
        ObjectMapper mapperNullifyingDeserializer = new ObjectMapper();
        mapperNullifyingDeserializer.setSerializationInclusion(Json.Include.NON_NULL);
        mapperNullifyingDeserializer.setDefaultTyping(DefaultTyping.NULL);
        mapperNullifyingDeserializer.setSerializationFeatures(mapperNullifyingDeserializer.getDefaultSerializationFeatures());
        mapperNullifyingDeserializer.setDeserializationFeatures(mapperNullifyingDeserializer.getDefaultDeserializationFeatures());
        try {
            objWithDefaultImpl = mapperNullifyingDeserializer.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
            fail("Expected null object when type id is missing");
        } catch (JsonProcessingException e) {
            // Expected, but this is a different exception type
        }
        assertNull("Expected null object when type id is missing and nullifying deserializer is used", objWithDefaultImpl);

        // Test deserialization with missing type id and nullifying deserializer
        jsonWithMissingTypeId = "{\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", " +
                "\"propertyName\": null}";
        objWithDefaultImpl = mapperNullifyingDeserializer.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
        assertNull("Expected null object when type id is missing and nullifying deserializer is used", objWithDefaultImpl);

        // Test deserialization with unknown type id and nullifying deserializer
        jsonWithUnknownTypeId = "{" +
                "\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\"," +
                "\"type\": \"UnknownTypeId\"}";
        try {
            objWithDefaultImpl = mapperNullifyingDeserializer.readValue(new StringReader(jsonWithUnknownTypeId), SimpleClass.class);
            fail("Expected exception when type id is unknown");
        } catch (JsonMappingException e) {
            // Expected
            assertEquals("Expected specific error message", 
                    "Unknown type id [UnknownTypeId]", e.getMessage());
        }

        // Test deserialization with null value and nullifying deserializer
        jsonWithMissingTypeId = "{\"class\": \"com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase\", " +
                "\"propertyName\": null}";
        objWithDefaultImpl = mapperNullifyingDeserializer.readValue(new StringReader(jsonWithMissingTypeId), SimpleClass.class);
        assertNull("Expected null object when type id is missing and nullifying deserializer is used", objWithDefaultImpl);
    }

    static class SimpleClass {
        // Empty class
    }

    static class DefaultImplementationClass {
        // Empty class
    }
}