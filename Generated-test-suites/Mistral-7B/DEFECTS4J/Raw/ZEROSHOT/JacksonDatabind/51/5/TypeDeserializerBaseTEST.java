package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * TypeDeserializerBaseTest class to test the functionalities of TypeDeserializerBase class.
 */
public class TypeDeserializerBaseTest extends TestBase {

    @Test
    public void testTypeDeserializerBase() throws IOException {
        // Test the constructor
        JavaType baseType = mapType(String.class, String.class);
        TypeIdResolver idResolver = mapTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mapType(Integer.class, Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName,
                typeIdVisible, defaultImpl);

        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase",
                typeDeserializerBase.getClass().getName());
        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
        assertNull(typeDeserializerBase.getProperty());

        // Test forProperty method
        BeanProperty property = new BeanProperty("name", String.class);
        TypeDeserializerBase typeDeserializerBaseForProperty = typeDeserializerBase.forProperty(property);
        assertEquals(typeDeserializerBaseForProperty.getClass(), TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBaseForProperty.getProperty());
        assertEquals(property, typeDeserializerBaseForProperty.getProperty());

        // Test getTypeInclusion method
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertTrue(typeInclusion.isInclude());

        // Test baseTypeName method
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);

        // Test toString method
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
        assertTrue(toString.contains("base-type:"));
        assertTrue(toString.contains("id-resolver:"));

        // Test _findDeserializer method
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper());
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(deserializer);

        // Test _findDefaultImplDeserializer method
        deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);

        // Test _deserializeWithNativeTypeId method
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(createParser("{}"), ctxt, typeId);
        assertNull(result);

        // Test _handleUnknownTypeId method
        JavaType actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        assertNull(actual);

        // Test for deserialization with custom deserializer
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        typeDeserializerBase._deserializers.put(typeId, stringDeserializer);
        result = typeDeserializerBase._findDeserializer(ctxt, typeId).deserialize(createParser("{}"), ctxt);
        assertEquals("", result);

        // Test for deserialization with default implementation
        typeDeserializerBase._deserializers.clear();
        result = typeDeserializerBase._findDefaultImplDeserializer(ctxt).deserialize(createParser("{}"), ctxt);
        assertNull(result);

        // Test for deserialization with no type id
        JsonParser parser = createParser("{}");
        result = typeDeserializerBase._deserializeWithNativeTypeId(parser, ctxt);
        assertNull(result);

        // Test for deserialization with no context
        result = typeDeserializerBase._deserializeWithNativeTypeId(createParser("{}"), null);
        assertNull(result);
    }

    /**
     * Helper method to create a parser instance.
     *
     * @return JsonParser instance
     */
    private JsonParser createParser(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(content.getBytes()));
    }

    /**
     * Helper method to create a map type.
     *
     * @param <T> type of the key
     * @param <U> type of the value
     * @param keyType key type
     * @param valueType value type
     * @return JavaType instance
     */
    public static <T, U> JavaType mapType(Class<T> keyType, Class<U> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
    }

    /**
     * Helper method to create a type id resolver instance.
     *
     * @return TypeIdResolver instance
     */
    public static TypeIdResolver mapTypeIdResolver() {
        return new StandardTypeIdResolver();
    }

    /**
     * Custom deserializer for string type.
     */
    public static class StringDeserializer extends StdDeserializer<String> {

        public StringDeserializer() {
            this(null);
        }

        public StringDeserializer(Class<?> c) {
            super(c);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }
    }
}