package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyname() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl();
        assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl();
        assertEquals(JsonTypeInfo.As.PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl();
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestImpl();
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    private static class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {
        private static final long serialVersionUID = 1L;

        public TypeDeserializerBaseTestImpl() {
            super(JavaType.construct(Object.class), null, "_typePropertyName", true, null);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }    
}