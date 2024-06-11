package com.example.test;

import com.fasterxml.jackson.core.io.SerializationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializerBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        BeanProperty property = new BeanProperty(null, "testProperty");
        TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(property);
        assertEquals(TypeDeserializerBase.class, newTypeDeserializerBase.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@256c5a22]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = objectMapper.getTypeFactory().constructValueType(TypeDeserializerBase.class);
        DeserializationContext context = objectMapper.getDeserializationContext();
        assertNull(typeDeserializerBase._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerUnknownTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = objectMapper.getTypeFactory().constructValueType(TypeDeserializerBase.class);
        DeserializationContext context = objectMapper.getDeserializationContext();
        String unknownTypeId = "com.unknown.type";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, unknownTypeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerKnownTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = objectMapper.getTypeFactory().constructValueType(TypeDeserializerBase.class);
        DeserializationContext context = objectMapper.getDeserializationContext();
        String knownTypeId = TypeDeserializerBase.class.getName();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, knownTypeId);
        assertNotNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        String typeId = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(null, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        DeserializationContext context = null;
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNotNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        DeserializationContext context = objectMapper.getDeserializationContext();
        String unknownTypeId = "com.unknown.type";
        JavaType type = typeDeserializerBase._handleUnknownTypeId(context, unknownTypeId);
        assertNull(type);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        DeserializationContext context = objectMapper.getDeserializationContext();
        JavaType expectedType = objectMapper.getTypeFactory().constructType(java.util.Map.class);
        JavaType type = typeDeserializerBase._handleMissingTypeId(context, "Expected missing type id");
        assertEquals(expectedType, type);
    }

    public static class TypeDeserializerBaseTestTypeDeserializer extends TypeDeserializerBase {
        public TypeDeserializerBaseTestTypeDeserializer(JavaType baseType, TypeIdResolver idRes,
                                                        String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }
    }
}