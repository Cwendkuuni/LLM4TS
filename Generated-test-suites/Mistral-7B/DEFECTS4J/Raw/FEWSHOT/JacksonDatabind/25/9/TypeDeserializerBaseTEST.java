package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithEmptyStringTypeId() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, "");
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerWithTypeId() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, null);
        String typeId = "com.example.MyClass";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        // Replace this with the actual deserializer for MyClass
        JsonDeserializer<Object> expectedDeserializer = new StdScalarDeserializer(String.class);
        assertEquals(expectedDeserializer, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullDefaultImpl() {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithBogusClassDefaultImpl() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, Object.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(NullifyingDeserializer.instance, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerWithClassDefaultImpl() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, MyClass.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        // Replace this with the actual deserializer for MyClass
        JsonDeserializer<Object> expectedDeserializer = new StdScalarDeserializer(String.class);
        assertEquals(expectedDeserializer, deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, null, null, false, null);
        String typeId = "com.unknown.MyClass";
        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, null, null);
            fail("Expected an exception");
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    @Test
    public void testHandleUnknownTypeIdWithTypeIdResolverBase() throws IOException {
        DeserializationContext context = new DeserializationContext(new StandardDeserializerProvider());
        TypeIdResolverBase typeIdResolverBase = new CustomTypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new BaseTypeDeserializerBase(null, typeIdResolverBase, null, false, null);
        String typeId = "com.unknown.MyClass";
        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, typeIdResolverBase, null);
            fail("Expected an exception");
        } catch (JsonMappingException e) {
            // Expected exception
            String extraDesc = e.getMessage();
            String expectedExtraDesc = "known type ids are not statically known";
            assertEquals(expectedExtraDesc, extraDesc);
        }
    }
}

class BaseTypeDeserializerBase extends TypeDeserializerBase {
    public BaseTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName,
            boolean typeIdVisible, Class<?> defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    public BaseTypeDeserializerBase(TypeDeserializerBase src, BeanProperty property) {
        super(src, property);
    }

    @Override
    public TypeDeserializer forProperty(BeanProperty prop) {
        return new BaseTypeDeserializerBase(this, prop);
    }
}

class CustomTypeIdResolverBase extends TypeIdResolverBase {
    @Override
    public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
        // Return null to simulate an unresolvable type id
        return null;
    }
}

class MyClass {
    // Replace this with the actual implementation of your MyClass
}