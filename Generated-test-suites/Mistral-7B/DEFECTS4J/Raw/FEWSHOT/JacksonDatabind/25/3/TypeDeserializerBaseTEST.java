package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, null);
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNull(deserializer);

        typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, MyClass.class);
        deserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(deserializer);
        assertEquals(MyClassDeserializer.class, deserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));

        typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, Void.class);
        assertNotNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));
        assertEquals(NullifyingDeserializer.instance.getClass(), typeDeserializerBase._findDefaultImplDeserializer(ctxt).getClass());
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new TestContext();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, null);
        String typeId = "unknown";
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, Object.class);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown JSON type ID: unknown", e.getMessage());
        }

        typeDeserializerBase = new BaseTypeDeserializer(Object.class, null, null, false, MyClass.class);
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, Object.class);
            fail();
        } catch (JsonMappingException e) {
            assertEquals("Unknown JSON type ID: unknown for class [simple type, class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase]", e.getMessage());
        }
    }

    static class BaseTypeDeserializer extends TypeDeserializerBase {
        public BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        BaseTypeDeserializer(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new BaseTypeDeserializer(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXISTING_PROPERTY;
        }

        static class MyClassDeserializer extends JsonDeserializer<MyClass> {
            @Override
            public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new MyClass();
            }
        }
    }

    static class MyClass {}

    static class TestContext extends DeserializationContext {
        @Override
        public Object findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return null;
        }

        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }
    }
}