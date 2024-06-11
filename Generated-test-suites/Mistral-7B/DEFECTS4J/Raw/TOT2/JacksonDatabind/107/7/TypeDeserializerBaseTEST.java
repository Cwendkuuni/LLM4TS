package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(String.class, forProperty.getType());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(TypeDeserializerBase.class.getName(), baseTypeName);
    }

    @Test
    public void testToString() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String toString = typeDeserializerBase.toString();
        String expected = "[TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:null]";
        assertEquals(expected, toString);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = new TestingDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, null);
        fail("Expected IOException");
    }

    @Test
    public void testFindDeserializerWithValidTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(new TestingDeserializationContext(), "validTypeId");
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = new TestingDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, typePropertyName, typeIdVisible, defaultImpl);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(null);
        assertNull(deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = new TestingDeserializationContext();
        Object result = typeDeserializerBase._handleUnknownTypeId(ctxt, "unknownTypeId");
        fail("Expected JsonMappingException");
    }

    @Test
    public void testHandleMissingTypeId() throws Exception {
        JavaType baseType = TypeFactory.defaultInstance().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new StandardTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = TypeFactory.defaultInstance().constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        DeserializationContext ctxt = new TestingDeserializationContext();
        Object result = typeDeserializerBase._handleMissingTypeId(ctxt, "extraDesc");
        fail("Expected JsonMappingException");
    }
}