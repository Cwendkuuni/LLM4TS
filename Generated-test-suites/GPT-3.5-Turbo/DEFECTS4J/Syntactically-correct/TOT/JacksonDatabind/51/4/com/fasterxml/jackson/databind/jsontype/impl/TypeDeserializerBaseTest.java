package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, null);
        assertNull(typeDeserializer.forProperty(null));
    }
    
    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, null);
        assertNull(typeDeserializer.getTypeInclusion());
    }
    
    @Test
    public void testBaseTypeName() {
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(baseType, null, null, false, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.JavaTypeImpl", typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, "type", false, null);
        assertEquals("type", typeDeserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, idResolver, null, false, null);
        assertSame(idResolver, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, defaultImpl);
        assertSame(JavaTypeImpl.class, typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testToString() {
        JavaType baseType = new JavaTypeImpl();
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, null, false, null);
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseImpl; base-type:com.fasterxml.jackson.databind.jsontype.impl.JavaTypeImpl; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverImpl]", typeDeserializer.toString());
    }
    
    @Test
    public void testFindDeserializer() {
        DeserializationContext ctxt = new DeserializationContextImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, null);
        assertNull(typeDeserializer._findDeserializer(ctxt, "type"));
    }
    
    @Test
    public void testFindDefaultImplDeserializer() {
        DeserializationContext ctxt = new DeserializationContextImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, new JavaTypeImpl());
        assertNull(typeDeserializer._findDefaultImplDeserializer(ctxt));
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() {
        DeserializationContext ctxt = new DeserializationContextImpl();
        JsonParser jp = new JsonParserImpl();
        Object deserializedObject = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, "type");
        assertNull(deserializedObject);
    }
    
    @Test(expected=JsonMappingException.class)
    public void testHandleUnknownTypeId() {
        DeserializationContext ctxt = new DeserializationContextImpl();
        TypeIdResolver idResolver = new TypeIdResolverImpl();
        JavaType baseType = new JavaTypeImpl();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, null, false, null);
        typeDeserializer._handleUnknownTypeId(ctxt, "type", idResolver, baseType);
    }
}