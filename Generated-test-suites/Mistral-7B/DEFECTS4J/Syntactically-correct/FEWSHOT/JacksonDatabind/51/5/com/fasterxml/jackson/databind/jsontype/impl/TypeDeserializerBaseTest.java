package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBase() {
        // Create a test instance
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null);
        
        // Test forProperty method
        TypeDeserializer expectedTypeDeserializer = mock(TypeDeserializer.class);
        when(expectedTypeDeserializer.forProperty(any(BeanProperty.class))).thenReturn(expectedTypeDeserializer);
        TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(mock(BeanProperty.class));
        assertEquals(expectedTypeDeserializer, actualTypeDeserializer);

        // Test getTypeInclusion method
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
        JsonTypeInfo.As actualTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(expectedTypeInclusion, actualTypeInclusion);

        // Test baseTypeName method
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());

        // Test getPropertyName method
        assertNull(typeDeserializerBase.getPropertyName());

        // Test getTypeIdResolver method
        assertNull(typeDeserializerBase.getTypeIdResolver());

        // Test getDefaultImpl method
        assertNull(typeDeserializerBase.getDefaultImpl());

        // Test toString method
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:null]";
        String actualToString = typeDeserializerBase.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Create a test instance
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "com.example.MyClass";

        // Test null typeId
        assertNull(typeDeserializerBase._findDeserializer(ctxt, null));

        // Test unknown typeId
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(expectedDeserializer.deserialize(any(JsonParser.class), eq(ctxt)))
                .thenReturn(new Object());
        when(ctxt.findContextualValueDeserializer(any(JavaType.class), eq(_property)))
                .thenReturn(expectedDeserializer);
        when(typeDeserializerBase._handleUnknownTypeId(eq(ctxt), eq("unknownTypeId"), eq(_idResolver), eq(_baseType)))
                .thenReturn(mock(JavaType.class));
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(ctxt, "unknownTypeId");
        assertEquals(expectedDeserializer, actualDeserializer);

        // Test known typeId
        when(typeDeserializerBase._deserializers.get(eq("knownTypeId"))).thenReturn(expectedDeserializer);
        JsonDeserializer<Object> actualDeserializer2 = typeDeserializerBase._findDeserializer(ctxt, "knownTypeId");
        assertEquals(expectedDeserializer, actualDeserializer2);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Create a test instance
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object defaultImpl = new Object();

        // Test null _defaultImpl
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(ctxt));

        // Test _defaultImpl with no contextual deserializer
        when(_defaultImplDeserializer.equals(any()))
                .thenReturn(false);
        when(ctxt.findContextualValueDeserializer(eq(_defaultImpl), eq(_property)))
                .thenReturn(null);
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(expectedDeserializer.deserialize(any(JsonParser.class), eq(ctxt)))
                .thenReturn(defaultImpl);
        when(ctxt.findValueDeserializer(eq(_defaultImpl.getClass()), anyBoolean()))
                .thenReturn(expectedDeserializer);
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(expectedDeserializer, actualDeserializer);

        // Test _defaultImpl with contextual deserializer
        when(_defaultImplDeserializer.equals(any()))
                .thenReturn(true);
        JsonDeserializer<Object> actualDeserializer2 = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(_defaultImplDeserializer, actualDeserializer2);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Create a test instance
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                Object.class.getTypeName(), null, null, false, null);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "unknownTypeId";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        // Test null _idResolver
        Object actual = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, null, null);
        assertNull(actual);

        // Test known type id with null _idResolver
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);
        Object expected = null;
        Object actual2 = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, null);
        assertEquals(expected, actual2);

        // Test known type id with non-null _idResolver
        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(mock(JavaType.class));
        JavaType actualType = mock(JavaType.class);
        when(actualType.getRawClass()).thenReturn(Integer.class);
        when(ctxt.findContextualValueDeserializer(eq(actualType), eq(_property)))
                .thenReturn(mock(JsonDeserializer.class));
        Object expected2 = new Object();
        Object actual3 = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, null);
        assertEquals(expected2, actual3);
    }
}