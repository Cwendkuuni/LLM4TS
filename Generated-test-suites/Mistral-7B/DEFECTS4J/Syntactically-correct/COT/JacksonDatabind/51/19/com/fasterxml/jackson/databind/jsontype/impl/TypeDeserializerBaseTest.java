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

/**
 * Test cases for TypeDeserializerBase class.
 */
public class TypeDeserializerBaseTest {

    @Test
    public void testConstructor() throws Exception {
        JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertSame(baseType, typeDeserializerBase._baseType);
        assertSame(idResolver, typeDeserializerBase._idResolver);
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
        assertTrue(typeDeserializerBase._typeIdVisible);
        assertEquals(defaultImpl, typeDeserializerBase._defaultImpl);
        assertNull(typeDeserializerBase._property);
    }

    @Test
    public void testForProperty() throws Exception {
        // Implement the forProperty method in a subclass and test it here.
        // For example:
        //
        // public class MyTypeDeserializerBase extends TypeDeserializerBase {
        //     @Override
        //     protected TypeDeserializer forProperty(BeanProperty prop) {
        //         // Your implementation here
        //     }
        // }
        //
        // TypeDeserializerBaseTest.java:
        //
        // @Test
        // public void testForProperty() throws Exception {
        //     MyTypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(...);
        //     BeanProperty property = mock(BeanProperty.class);
        //     TypeDeserializer result = typeDeserializerBase.forProperty(property);
        //     assertEquals(expectedTypeDeserializer, result);
        // }
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        // Implement the getTypeInclusion method in a subclass and test it here.
        // For example:
        //
        // public class MyTypeDeserializerBase extends TypeDeserializerBase {
        //     @Override
        //     public JsonTypeInfo.As getTypeInclusion() {
        //         // Your implementation here
        //     }
        // }
        //
        // TypeDeserializerBaseTest.java:
        //
        // @Test
        // public void testGetTypeInclusion() throws Exception {
        //     MyTypeDeserializerBase typeDeserializerBase = new MyTypeDeserializerBase(...);
        //     JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        //     assertEquals(expectedTypeInclusion, result);
        // }
    }

    @Test
    public void testFindDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(Object.class, null, null, false, null);
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "com.example.MyClass";
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MyClass.class);

        when(context.getTypeFactory()).thenReturn(ObjectMapper.getDefaultInstance().getTypeFactory());
        when(context.findContextualValueDeserializer(type, any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertSame(mock(JsonDeserializer.class), result);

        verify(context).findContextualValueDeserializer(type, any(BeanProperty.class));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(Object.class, null, null, false, null);
        DeserializationContext context = mock(DeserializationContext.class);

        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(NullifyingDeserializer.instance);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertSame(NullifyingDeserializer.instance, result);

        verify(context).findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class));
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(Object.class, null, null, false, null);
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "com.example.MyClass";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = Object.class;

        when(context.handleUnknownTypeId(baseType, typeId, idResolver, anyString())).thenThrow(JsonMappingException.class);

        try {
            typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        verify(context).handleUnknownTypeId(baseType, typeId, idResolver, anyString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_NullTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(Object.class, null, null, false, null);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        typeDeserializerBase._deserializeWithNativeTypeId(jp, context, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_UnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(Object.class, null, null, false, null);
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "com.example.MyClass";

        when(jp.getTypeId()).thenReturn(typeId);

        typeDeserializerBase._deserializeWithNativeTypeId(jp, context, null);
    }
}