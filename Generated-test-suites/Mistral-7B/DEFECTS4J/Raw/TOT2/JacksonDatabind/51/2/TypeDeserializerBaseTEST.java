package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test class for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    private static final TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
    private static final String TYPE_ID = "testTypeId";
    private static final String TYPE_ID_STR = "'" + TYPE_ID + "'";
    private static final String JSON_STRING = "{\"typeId\":" + TYPE_ID_STR + "}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @SuppressWarnings("unchecked")
    private static final JsonDeserializer<Object> NULLIFYING_DESERIALIZER = NullifyingDeserializer.instance;

    /**
     * Test method for {@link TypeDeserializerBase#TypeDeserializerBase(JavaType, TypeIdResolver, String, boolean, JavaType)} constructor.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testTypeDeserializerBaseConstructor() throws JsonProcessingException {
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        JavaType baseType = JavaType.constructRawType(TypeDeserializerBase.class);
        String typePropertyName = "typePropertyName";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructRawType(String.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
        assertEquals(baseType, typeDeserializerBase.getBaseType());
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
        assertEquals(typeIdVisible, typeDeserializerBase.isTypeIdVisible());
        assertEquals(defaultImpl, typeDeserializerBase.getDefaultImpl());
    }

    /**
     * Test method for {@link TypeDeserializerBase#forProperty(BeanProperty)} method.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testForProperty() throws JsonProcessingException {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(typeDeserializerBase.getClass(), typeDeserializer.getClass());
    }

    /**
     * Test method for {@link TypeDeserializerBase#getTypeInclusion()} method.
     */
    @org.junit.Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonTypeInfo.As getTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_ONLY, getTypeInclusion);
    }

    /**
     * Test method for {@link TypeDeserializerBase#baseTypeName()} method.
     */
    @org.junit.Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals(TypeDeserializerBase.class.getName(), baseTypeName);
    }

    /**
     * Test method for {@link TypeDeserializerBase#toString()} method.
     */
    @org.junit.Test
    public void testToString() {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        String toString = typeDeserializerBase.toString();
        String expectedToString = "[TypeDeserializerBase; base-type:class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; id-resolver:null]";
        assertEquals(expectedToString, toString);
    }

    /**
     * Test method for {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testFindDeserializer() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));
        when(deserializationContext.getTypeFactory().constructSpecializedType(any(JavaType.class), eq(String.class))).thenReturn(JavaType.constructRawType(String.class));
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDeserializer(deserializationContext, TYPE_ID);
        assertEquals(mock(JsonDeserializer.class), jsonDeserializer);
    }

    /**
     * Test method for {@link TypeDeserializerBase#_findDefaultImplDeserializer(DeserializationContext)} method.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.isEnabled(eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE))).thenReturn(false);
        when(deserializationContext.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(NULLIFYING_DESERIALIZER);
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JsonDeserializer<Object> jsonDeserializer = typeDeserializerBase._findDefaultImplDeserializer(deserializationContext);
        assertEquals(NULLIFYING_DESERIALIZER, jsonDeserializer);
    }

    /**
     * Test method for {@link TypeDeserializerBase#_deserializeWithNativeTypeId(JsonParser, DeserializationContext)} method.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.findContextualValueDeserializer(eq(String.class), any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));
        JsonParser jsonParser = OBJECT_MAPPER.getFactory().createParser(JSON_STRING);
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        assertEquals(null, result);
    }

    /**
     * Test method for {@link TypeDeserializerBase#_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)} method.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testHandleUnknownTypeId() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.handleUnknownTypeId(any(JavaType.class), anyString(), any(TypeIdResolver.class), anyString())).thenReturn(JavaType.constructRawType(String.class));
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(deserializationContext, TYPE_ID, mock(TypeIdResolver.class), JavaType.constructRawType(Object.class));
        assertEquals(JavaType.constructRawType(String.class), result);
    }

    /**
     * Test edge case for {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method when no deserializer is found.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test(expected = JsonMappingException.class)
    public void testFindDeserializerNoDeserializerFound() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        when(deserializationContext.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(null);
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        typeDeserializerBase._findDeserializer(deserializationContext, TYPE_ID);
    }

    /**
     * Test edge case for {@link TypeDeserializerBase#_findDeserializer(DeserializationContext, String)} method with null deserialization context.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testFindDeserializerNullDeserializationContext() throws JsonProcessingException {
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        assertNull(typeDeserializerBase._findDeserializer(null, TYPE_ID));
    }

    /**
     * Test edge case for {@link TypeDeserializerBase#_handleUnknownTypeId(DeserializationContext, String, TypeIdResolver, JavaType)} method with null type id.
     *
     * @throws JsonProcessingException if any errors occur during deserialization
     */
    @org.junit.Test
    public void testHandleUnknownTypeIdNullTypeId() throws JsonProcessingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeDeserializerBase typeDeserializerBase = new TestTypeDeserializerBase();
        JavaType result = typeDeserializerBase._handleUnknownTypeId(deserializationContext, null, mock(TypeIdResolver.class), JavaType.constructRawType(Object.class));
        assertNull(result);
    }
}

class TestTypeDeserializerBase extends TypeDeserializerBase {

    public TestTypeDeserializerBase() {
        super(JavaType.constructRawType(Object.class), mock(TypeIdResolver.class), "typePropertyName", true, JavaType.constructRawType(String.class));
    }

}