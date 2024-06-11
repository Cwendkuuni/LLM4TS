
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        JavaType baseType = mock(JavaType.class);
        JavaType defaultImpl = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idResolver, "", true, defaultImpl);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.util.Map", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNotNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        assertNotNull(typeDeserializerBase.baseType());
    }

    @Test
    public void testToString() {
        assertNotNull(typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);

        assertNotNull(typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext));
    }

    private static class TypeDeserializerBaseMock extends TypeDeserializerBase {

        public TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public As getTypeInclusion() {
            return null;
        }
    }
}
