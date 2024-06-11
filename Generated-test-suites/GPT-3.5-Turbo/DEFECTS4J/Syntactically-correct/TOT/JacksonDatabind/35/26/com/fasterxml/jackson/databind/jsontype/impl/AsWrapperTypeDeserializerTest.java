
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer.As;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject_WhenTypeFieldPresent_SuccessfulDeserialization() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\": \"someType\", \"propertyName\": \"propertyValue\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JsonNode);
        JsonNode jsonNode = (JsonNode) result;
        assertEquals("someType", jsonNode.get("type").asText());
        assertEquals("propertyValue", jsonNode.get("propertyName").asText());
    }

    @Test
    public void testDeserializeTypedFromArray_WhenTypeFieldPresent_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"type\": \"someType\", \"propertyName\": \"propertyValue\"}]";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromArray(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromScalar_WhenTypeFieldPresent_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "\"someType\"";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromScalar(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromAny_WhenTypeFieldPresent_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\": \"someType\", \"propertyName\": \"propertyValue\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromAny(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObject_WhenIncorrectToken_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"type\": \"someType\", \"propertyName\": \"propertyValue\"}]";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromObject(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObject_WhenFieldMissing_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"propertyName\": \"propertyValue\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromObject(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromObject_WhenTypeIdNull_SuccessfulDeserialization() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"propertyName\": \"propertyValue\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.setFeature(JsonParser.Feature.READ_TYPE_IDS, true);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JsonNode);
        JsonNode jsonNode = (JsonNode) result;
        assertNull(jsonNode.get("type"));
        assertEquals("propertyValue", jsonNode.get("propertyName").asText());
    }

    @Test
    public void testDeserializeTypedFromArray_WhenTypeIdNull_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"propertyName\": \"propertyValue\"}]";
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.setFeature(JsonParser.Feature.READ_TYPE_IDS, true);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromArray(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromScalar_WhenTypeIdNull_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "\"someValue\"";
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.setFeature(JsonParser.Feature.READ_TYPE_IDS, true);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromScalar(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromAny_WhenTypeIdNull_ThrowsWrongTokenException() throws Exception {
        // Arrange
        JavaType baseType = TypeFactory.defaultInstance().constructType(Class.forName("com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer"));

        TypeIdResolver idResolver = new TypeIdResolverMock();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null;
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"propertyName\": \"propertyValue\"}";
        JsonParser jp = mapper.getFactory().createParser(json);
        jp.setFeature(JsonParser.Feature.READ_TYPE_IDS, true);
        DeserializationContext ctxt = mapper.getDeserializationContext();

        // Act and assert
        assertThrows(WrongTokenException.class, () -> deserializer.deserializeTypedFromAny(jp, ctxt));
    }

    private static class TypeIdResolverMock implements TypeIdResolver {
        @Override
        public void init(JavaType arg0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String idFromValue(Object arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String idFromValueAndType(Object arg0, Class<?> arg1) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String idFromBaseType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext arg0, String arg1) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
