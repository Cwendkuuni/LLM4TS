package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Test class for TypeDeserializerBase
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstructor() {
        // Test default constructor
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        assertNotNull(instance);

        // Test constructor with provided arguments
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idRes = new SimpleTypeIdResolver();
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        TypeDeserializerBase instance2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, instance2._baseType);
        assertEquals(idRes, instance2._idResolver);
        assertEquals(typePropertyName, instance2._typePropertyName);
        assertEquals(typeIdVisible, instance2._typeIdVisible);
        assertEquals(defaultImpl, instance2._defaultImpl);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer<Object> result = instance.forProperty(property);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        BeanProperty property = null;
        instance.forProperty(property);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        JsonTypeInfo.As result = instance.getTypeInclusion();
        assertNull(result);
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = new SimpleType(String.class);
        TypeDeserializerBase instance = new TypeDeserializerBase(baseType, null, null, false, null);
        String result = instance.baseTypeName();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        String result = instance.getPropertyName();
        assertNull(result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        TypeIdResolver result = instance.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = new SimpleType(String.class);
        TypeDeserializerBase instance = new TypeDeserializerBase(baseType, null, null, false, null);
        Class<?> result = instance.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void testFindDeserializerNullContext() {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        String typeId = "typeId";
        JsonDeserializer<Object> result = instance._findDeserializer(null, typeId);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        String typeId = null;
        instance._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDeserializerEmptyTypeId() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        String typeId = "";
        JsonDeserializer<Object> result = instance._findDeserializer(context, typeId);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerValidTypeId() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        String typeId = "typeId";
        JsonDeserializer<Object> result = instance._findDeserializer(context, typeId);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullType() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        Object type = null;
        instance._findDeserializer(context, type.toString());
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        instance._findDefaultImplDeserializer(null);
    }

    @Test
    public void testFindDefaultImplDeserializerValidContext() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, new SimpleType(Integer.class));
        instance._findDefaultImplDeserializer(context);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        instance._handleUnknownTypeId(null, "typeId", null, null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullIdResolver() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        instance._handleUnknownTypeId(context, null, null, null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, null, null, false, null);
        instance._handleUnknownTypeId(context, "typeId", null, null);
    }

    @Test
    public void testHandleUnknownTypeIdValidContextAndIdResolver() throws IOException {
        DeserializationContext context = new TestDeserializationContext();
        TypeDeserializerBase instance = new TypeDeserializerBase(null, new SimpleTypeIdResolver(), null, false, null);
        instance._handleUnknownTypeId(context, "typeId", null, null);
    }

    /**
     * Test utility class for {@link DeserializationContext}
     */
    public static class TestDeserializationContext extends DeserializationContextBase {
        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public void failContextual(String msg) throws IOException {
            fail(msg);
        }

        @Override
        public void failContextual(String msg, Throwable t) throws IOException {
            fail(msg, t);
        }

        @Override
        public void findContextualValueDeserializer(JsonTypeDescriptor typeDesc) throws IOException {
            fail("Should not call this method");
        }

        @Override
        public void findContextualValueDeserializer(JavaType type, JsonTypeDescriptor typeDesc) throws IOException {
            fail("Should not call this method");
        }

        @Override
        public void findValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            fail("Should not call this method");
        }

        @Override
        public void findValueDeserializer(JavaType type, JsonProperty property) throws IOException {
            fail("Should not call this method");
        }

        @Override
        public void findValueDeserializer(JavaType type, BeanProperty property, JsonDeserializer<?> deserializer) throws IOException {
            fail("Should not call this method");
        }

        @Override
        public void findValueDeserializer(JavaType type, JsonProperty property, JsonDeserializer<?> deserializer) throws IOException {
            fail("Should not call this method");
        }
    }

    /**
     * Test utility class for {@link TypeIdResolver}
     */
    public static class SimpleTypeIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            return "typeId";
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> valueType) {
            return "typeId";
        }

        @Override
        public String idFromValueAndType(Object value, JavaType valueType, JavaType type) {
            return "typeId";
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> valueType, JsonTypeDescriptor typeDesc) {
            return "typeId";
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, JsonTypeDescriptor typeDesc) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, Class<?> baseType) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, JavaType baseType) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, BeanProperty property) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, JsonProperty property) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, JsonTypeDescriptor typeDesc, BeanProperty property) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId, JsonTypeDescriptor typeDesc, JsonProperty property) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public String idFromValueAndType(Object value, JavaType valueType) {
            return "typeId";
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> valueType, JavaType type) {
            return "typeId";
        }

        @Override
        public String idFromValueAndType(Object value, JavaType valueType, JsonTypeDescriptor typeDesc) {
            return "typeId";
        }

        @Override
        public void registerSubtypes(JsonDeserializer.HandledType[] handlers) {
        }

        @Override
        public void registerSubtypes(JsonDeserializer.HandledType[] handlers, JavaType baseType) {
        }

        @Override
        public void registerSubtypes(Map<String, JsonDeserializer.HandledType> handlers) {
        }

        @Override
        public void registerSubtypes(Map<String, JsonDeserializer.HandledType> handlers, JavaType baseType) {
        }

        @Override
        public void registerSubtypes(DeserializationContext ctxt, Map<String, JsonDeserializer.HandledType> handlers) {
        }

        @Override
        public void registerSubtypes(DeserializationContext ctxt, Map<String, JsonDeserializer.HandledType> handlers, JavaType baseType) {
        }

        @Override
        public JavaType resolveTypeId(DeserializationContext ctxt, String typeId) throws IOException {
            return new SimpleType(Integer.class);
        }

        @Override
        public JavaType resolveTypeId(DeserializationContext ctxt, String typeId, JsonDeserializer<?> deserializer) throws IOException {
            return new SimpleType(Integer.class);
        }
    }
}