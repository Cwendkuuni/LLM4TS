package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() {
        // Create an instance of TypeDeserializerBase with valid arguments
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);
        assertNotNull(typeDeserializerBase);
    }

    @Test
    public void testForProperty() {
        // Create a mock BeanProperty
        BeanProperty property = mock(BeanProperty.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test forProperty method
        TypeDeserializer result = typeDeserializerBase.forProperty(property);
        assertInstanceOf(TypeDeserializer.class, result);
    }

    @Test
    public void testGotTypeInclusion() {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test getTypeInclusion method
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ROOT_VALUE, result);
    }

    @Test
    public void testBaseTypeName() {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test baseTypeName method
        String result = typeDeserializerBase.baseTypeName();
        assertEquals("java.lang.String", result);
    }

    @Test
    public void testGetPropertyName() {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test getPropertyName method
        String result = typeDeserializerBase.getPropertyName();
        assertEquals("_typePropertyName", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test getTypeIdResolver method
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertEquals(idResolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        // Create an instance of TypeDeserializerBase with null defaultImpl
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test getDefaultImpl method with null defaultImpl
        assertNull(typeDeserializerBase.getDefaultImpl());

        // Create an instance of TypeDeserializerBase with a defaultImpl
        JavaType baseType2 = new SimpleType(Map.class);
        TypeIdResolver idResolver2 = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType2, idResolver2, "_typePropertyName", true, HashMap.class);

        // Test getDefaultImpl method with a defaultImpl
        assertNotNull(typeDeserializerBase2.getDefaultImpl());
        assertEquals(HashMap.class, typeDeserializerBase2.getDefaultImpl().getRawClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyAfterConstruction() {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test forProperty method after construction
        typeDeserializerBase.forProperty(mock(BeanProperty.class));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test findDeserializer method with null typeId
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(ctxt, null);
        assertNull(result);

        // Test findDeserializer method with a known typeId
        String typeId = "com.example.MyType";
        JavaType type = new SimpleType(getClass().forName("com.example." + typeId));
        when(ctxt.typeFromId(anyString())).thenReturn(type);
        result = typeDeserializerBase._findDeserializer(ctxt, typeId);
        assertNotNull(result);
        assertInstanceOf(JsonDeserializer.class, result);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeIdResolver() throws IOException {
        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, "_typePropertyName", true, null);

        // Test findDeserializer method with a null typeIdResolver
        String typeId = "com.example.MyType";
        when(ctxt.typeFromId(anyString())).thenReturn(new SimpleType(getClass().forName("com.example." + typeId)));
        typeDeserializerBase._findDeserializer(ctxt, typeId);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnresolvableTypeId() throws IOException {
        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test findDeserializer method with an unresolvable typeId
        String typeId = "com.example.MyType";
        when(ctxt.typeFromId(anyString())).thenReturn(null);
        typeDeserializerBase._findDeserializer(ctxt, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test findDefaultImplDeserializer method
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNull(result);

        // Create an instance of TypeDeserializerBase with a defaultImplDeserializer
        JavaType baseType2 = new SimpleType(String.class);
        TypeIdResolver idResolver2 = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType2, idResolver2, "_typePropertyName", true, null);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(eq(baseType2), any(BeanProperty.class))).thenReturn(defaultImplDeserializer);

        result = typeDeserializerBase2._findDefaultImplDeserializer(ctxt);
        assertNotNull(result);
        assertSame(defaultImplDeserializer, result);
    }

    @Test(expected = IOException.class)
    public void testFindDefaultImplDeserializerWithNullContext() throws IOException {
        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test findDefaultImplDeserializer method with a null context
        typeDeserializerBase._findDefaultImplDeserializer(null);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        // Create a mock JsonParser
        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn("com.example.MyType");

        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test deserializeWithNativeTypeId method
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        assertNull(result);

        // Create an instance of TypeDeserializerBase with a deserializer for MyType
        String typeId = "com.example.MyType";
        JavaType type = new SimpleType(getClass().forName("com.example." + typeId));
        when(ctxt.findContextualValueDeserializer(eq(type), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));
        result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
        assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullContext() throws IOException {
        // Create a mock JsonParser
        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn("com.example.MyType");

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, "_typePropertyName", true, null);

        // Test deserializeWithNativeTypeId method with a null context
        typeDeserializerBase._deserializeWithNativeTypeId(jp, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithUnresolvableTypeId() throws IOException {
        // Create a mock JsonParser
        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn("com.example.MyType");

        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test deserializeWithNativeTypeId method with an unresolvable typeId
        when(ctxt.findContextualValueDeserializer(eq(baseType), any(BeanProperty.class))).thenReturn(null);
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "com.example.MyType");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        // Create a mock DeserializationContext
        DeserializationContext ctxt = mock(DeserializationContext.class);

        // Create an instance of TypeDeserializerBase
        JavaType baseType = new SimpleType(String.class);
        TypeIdResolver idResolver = new TypeIdResolverBase();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "_typePropertyName", true, null);

        // Test handleUnknownTypeId method
        String typeId = "com.example.MyType";
        String extraDesc = "extra description";
        when(ctxt.getTypeFromId(anyString())).thenReturn(null);
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unknown type id: " + typeId, e.getMessage());
            assertEquals(extraDesc, e.getDescription(null));
        }
    }
}