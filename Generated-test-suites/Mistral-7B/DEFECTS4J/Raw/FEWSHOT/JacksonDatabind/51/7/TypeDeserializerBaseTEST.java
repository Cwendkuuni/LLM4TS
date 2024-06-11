package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testCreateTypeDeserializerBaseEmpty() {
        TypeDeserializerBase emptyTypeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", emptyTypeDeserializerBase.getClass().getName());
        assertNull(emptyTypeDeserializerBase.getPropertyName());
        assertNull(emptyTypeDeserializerBase.getTypeIdResolver());
        assertNull(emptyTypeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testCreateTypeDeserializerBaseWithProperties() {
        Class<Integer> integerType = Integer.class;
        JavaType intJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(integerType);
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType baseType = intJavaType;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, null);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.getClass().getName());
        assertEquals("typeProperty", typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testCreateTypeDeserializerBaseWithDefaultImpl() {
        Class<String> stringType = String.class;
        JavaType stringJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(stringType);
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType baseType = stringJavaType;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Boolean.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals("class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.getClass().getName());
        assertEquals("typeProperty", typeDeserializerBase.getPropertyName());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(Boolean.class, typeDeserializerBase.getDefaultImpl().getRawClass());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String[].class, new StringArrayDeserializer());
        objectMapper.registerModule(module);
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        JavaType stringArrayJavaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String[].class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), "com.fasterxml.jackson.databind.ser.std.StringArraySerializer$StringArraySerializerContextual");
        assertEquals(StringArrayDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper objectMapper = ObjectMapper.getDefaultInstance();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null);
        TypeIdResolver idResolver = new SimpleTypeIdResolver();
        String typeId = "unknownTypeId";
        try {
            JavaType javaType = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), typeId, idResolver, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static class SimpleTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            if ("StringArray".equals(typeId)) {
                return ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String[].class);
            }
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
            return null;
        }

        @Override
        public String idFromValueAndType(DeserializationContext ctxt, Object value, JavaType valueType) throws IOException {
            return null;
        }

        public String getDescForKnownTypeIds() {
            return "StringArray";
        }
    }

    @Test
    public void testToString() {
        JavaType javaType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(javaType, null, "", false, null);
        String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.lang.String; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }
}

class StringArrayDeserializer extends StdDeserializer<String[]> {
    public StringArrayDeserializer() {
        this(null);
    }

    protected StringArrayDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public StringArray deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new StringArray(jp.readValueAs(String[].class));
    }
}

class StringArray {
    private final String[] strings;

    public StringArray(String[] strings) {
        this.strings = strings;
    }
}