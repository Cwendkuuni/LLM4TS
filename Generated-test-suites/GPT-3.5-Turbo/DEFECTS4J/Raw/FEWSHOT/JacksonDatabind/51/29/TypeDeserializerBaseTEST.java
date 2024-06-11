package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private JavaType baseType;

    @Mock
    private BeanProperty property;

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBaseTestImpl(baseType, idResolver, "", true, null);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(null, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Class", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl; base-type:com.fasterxml.jackson.databind.JavaType; id-resolver: com.fasterxml.jackson.databind.jsontype.TypeIdResolverImpl]", typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId_WithTypeNull() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = null;
        typeDeserializerBase._handleUnknownTypeId(ctxt, "test", idResolver, baseType);
    }

    private class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

        private static final long serialVersionUID = 1L;

        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public org.apache.axis2.jaxws.spi.handler.HandlerResolverImpl.TypeDeserializerBase forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public org.apache.axis2.jaxws.spi.handler.HandlerResolverImpl.JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

    }

}