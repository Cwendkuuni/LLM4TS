

package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(TypeDeserializerBaseTest.class);

        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseImpl(baseType, null, null, false, null);
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest";
        assertEquals(expected, deserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        String typePropertyName = "type";
        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseImpl(null, null, typePropertyName, false, null);
        assertEquals(typePropertyName, deserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseImpl(null, idResolver, null, false, null);
        assertEquals(idResolver, deserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = mock(JavaType.class);
        Class<?> defaultImplClass = TypeDeserializerBaseTest.class;

        when(defaultImpl.getRawClass()).thenReturn(defaultImplClass);

        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseImpl(null, null, null, false, defaultImpl);
        assertEquals(defaultImplClass, deserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = mock(JavaType.class);
        TypeDeserializerBase deserializerBase = new TypeDeserializerBaseImpl(baseType, null, null, false, null);

        assertEquals(baseType, deserializerBase.baseType());
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public Object deserializeTypedFromObject(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }

    }
}
