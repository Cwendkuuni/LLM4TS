package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for TypeDeserializerBase
 */
public class TypeDeserializerBaseTest {

    

    @Test
    public void testTypeDeserializerBaseConstructor() {
        // Test the constructor with valid arguments
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializerBase);

        // Test the constructor with null arguments
        try {
            new TypeDeserializerBase(null, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new TypeDeserializerBase(new JavaType() {}, null, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new TypeDeserializerBase(null, new TypeIdResolver() {}, null, false, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializer.class, forProperty.getClass());

        // Test with null argument
        try {
            typeDeserializerBase.forProperty(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetTypeInclusion() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInclusion);
        assertTrue(typeInclusion.isUnknown());

        // Test with JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.WRAP_EXISTING_ARRAY_TYPES;
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        typeDeserializerBase.setTypeInclusion(expectedTypeInclusion);
        JsonTypeInfo.As actualTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(expectedTypeInclusion, actualTypeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("typeProperty", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idRes, typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        Class<?> defaultImplClass = typeDeserializerBase.getDefaultImpl();
        assertEquals(Object.class, defaultImplClass);

        // Test with null defaultImpl
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, null);
        defaultImplClass = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImplClass);
    }

    @Test
    public void testToString() {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.startsWith("[TypeDeserializerBase"));
        assertTrue(toString.endsWith("]"));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        String typeId = "typeId";
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(deserializer);

        // Test with null context
        try {
            typeDeserializerBase._findDeserializer(null, typeId);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with null typeId
        try {
            typeDeserializerBase._findDeserializer(context, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with known typeId
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        typeDeserializerBase._deserializers.put(typeId, stringDeserializer);
        deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(stringDeserializer, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        String typeId = "typeId";
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);

        // Test with null context
        try {
            typeDeserializerBase._findDefaultImplDeserializer(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with null defaultImpl
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, null);
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);

        // Test with known defaultImpl
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        typeDeserializerBase._defaultImplDeserializer = stringDeserializer;
        synchronized (typeDeserializerBase._defaultImpl) {
            deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
            assertEquals(stringDeserializer, deserializer);
        }
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        String typeId = "typeId";
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        JsonParser jsonParser = mapper.getFactory().createParser("{}");
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
        assertNull(result);

        // Test with null jsonParser
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(null, mapper.getDeserializationContext());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with null typeId
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test with known typeId
        JsonDeserializer<String> stringDeserializer = mapper.findValueDeserializer(String.class);
        typeDeserializerBase._deserializers.put(typeId, stringDeserializer);
        Object resultObject = new Object();
        jsonParser.setValue(resultObject);
        jsonParser.nextToken();
        jsonParser.setValue(typeId);
        JsonNode typeIdNode = jsonParser.readValueAsTree();
        JsonNode explicitTypeIdNode = JsonNodeFactory.instance.textField("typeId", typeIdNode);
        jsonParser.setValue(explicitTypeIdNode);
        result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, mapper.getDeserializationContext());
        assertEquals(resultObject.getClass(), result.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType baseType = new ClassType(String.class);
        TypeIdResolver idRes = new ClassIdResolver(String.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = false;
        String typeId = "unknownTypeId";
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext context = mapper.getDeserializationContext();
        typeDeserializerBase._handleUnknownTypeId(context, typeId, idRes, baseType);
    }

    
}