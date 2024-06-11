package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBaseTestImpl();
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl; base-type:null; id-resolver: null]", typeDeserializerBase.toString());
    }
}

class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {

    public TypeDeserializerBaseTestImpl() {
        super(null, null, "_typePropertyName", false, null);
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
    protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
        return null;
    }

    @Override
    protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
        return null;
    }

    @Override
    protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
        return null;
    }

}