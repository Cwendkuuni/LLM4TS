package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeFactory;

import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testCreateInstance() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("StringType")) {
                    return TypeFactory.defaultInstance().constructType(StringType.class);
                }
                return null;
            }
        };
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = StringType.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializer);

        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializerBase copiedTypeDeserializer = typeDeserializer.copyForProperty(property);
        assertNotNull(copiedTypeDeserializer);
        assertEquals(typeDeserializer._baseType, copiedTypeDeserializer._baseType);
        assertEquals(typeDeserializer._idResolver, copiedTypeDeserializer._idResolver);
        assertEquals(typeDeserializer._typePropertyName, copiedTypeDeserializer._typePropertyName);
        assertEquals(typeDeserializer._typeIdVisible, copiedTypeDeserializer._typeIdVisible);
        assertEquals(typeDeserializer._deserializers, copiedTypeDeserializer._deserializers);
        assertEquals(typeDeserializer._defaultImpl, copiedTypeDeserializer._defaultImpl);
        assertNull(copiedTypeDeserializer._defaultImplDeserializer);
        assertEquals(property, copiedTypeDeserializer._property);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if (typeId.equals("StringType")) {
                    return TypeFactory.defaultInstance().constructType(StringType.class);
                }
                return null;
            }
        };
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = StringType.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, "StringType");
        assertNotNull(deserializer);
        assertInstanceOf(StringTypeDeserializer.class, deserializer);

        deserializer = typeDeserializer._findDeserializer(ctxt, "UnknownTypeId");
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = StringType.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(deserializer);

        typeDeserializer._defaultImplDeserializer = new StringTypeDeserializer();
        deserializer = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
        assertInstanceOf(StringTypeDeserializer.class, deserializer);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = StringType.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestDeserializationContext();
        typeDeserializer._handleUnknownTypeId(ctxt, "UnknownTypeId", idResolver, baseType);
    }

    @Test
    public void testToString() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = StringType.class;

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolverBase]";
        assertEquals(expected, typeDeserializer.toString());
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals("class java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, "typeProperty", false, null);
        assertEquals("typeProperty", typeDeserializer.getPropertyName());
    }

    @Test
    public void testgetTypeIdResolver() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, null);
        assertEquals(null, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, false, String.class);
        assertEquals(String.class, typeDeserializer.getDefaultImpl());
    }

    private static class StringTypeDeserializer extends JsonDeserializer<String> {}

    private static class StringType {}

    private static class TypeIdResolverBase implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
            return null;
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
    }

    private static class TestDeserializationContext extends DeserializationContext {
        @Override
        public BeanProperty findProperty(String name) {
            return null;
        }

        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public Object findValueDeserializer(JavaType type) throws IOException {
            return null;
        }

        @Override
        public Object readValue(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public void handleUnexpectedValue(Object value, DeserializationContext ctxt) throws IOException {
            // Do nothing
        }

        @Override
        public void reportError(MessageBase msg) throws IOException {
            // Do nothing
        }

        @Override
        public void reportError(String msg) throws IOException {
            // Do nothing
        }

        @Override
        public void reportError(MessageBase msg, Object[] args) throws IOException {
            // Do nothing
        }

        @Override
        public void close() throws IOException {
            // Do nothing
        }

        @Override
        public void setContextualHandler(JsonDeserializer.Contextual deserializer, Object bean) throws IOException {
            // Do nothing
        }

        @Override
        public void setBeanProperty(BeanProperty property, Object value) throws IOException {
            // Do nothing
        }

        @Override
        public void setContextualHandlerInstantiator(DeserializationContext.ContextualHandlerInsteadOfDeserializer instantiator) {
            // Do nothing
        }
    }
}