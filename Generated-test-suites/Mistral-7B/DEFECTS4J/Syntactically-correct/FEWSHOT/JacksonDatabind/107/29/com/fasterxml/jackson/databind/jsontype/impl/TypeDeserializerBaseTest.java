package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    private static final JavaType BASE_TYPE = mock(JavaType.class);
    private static final TypeIdResolver ID_RESOLVER = mock(TypeIdResolver.class);
    private static final BeanProperty PROPERTY = mock(BeanProperty.class);
    private static final JsonTypeInfo JSON_INFO = mock(JsonTypeInfo.class);
    private static final DeserializationContext CTXT = mock(DeserializationContext.class);

    @SuppressWarnings("unchecked")
    private static final Map<String, JsonDeserializer<Object>> DESERIALIZERS = new HashMap<String, JsonDeserializer<Object>>();

    private static TypeDeserializerBase createTestee(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        return new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers() {
                return DESERIALIZERS;
            }
        };
    }

    @Test
    public void testCreate() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNotNull(testee);
    }

    @Test
    public void testCreateWithProperty() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, "propertyName", false, null);
        assertNotNull(testee);
        assertEquals("propertyName", testee.getPropertyName());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullBaseType() {
        new TypeDeserializerBase(null, ID_RESOLVER, null, false, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullIDResolver() {
        new TypeDeserializerBase(BASE_TYPE, null, null, false, null);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateNullTypePropertyName() {
        new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNegativeTypeIdVisible() {
        new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, -1, null);
    }

    @Test
    public void testTypeIdVisibleTrue() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, true, null);
        assertTrue(testee.isTypeIdVisible());
    }

    @Test
    public void testTypeIdVisibleFalse() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertFalse(testee.isTypeIdVisible());
    }

    @Test
    public void testForPropertyNullProperty() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNull(testee.forProperty(null));
    }

    @Test
    public void testForPropertyNotNull() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        TypeDeserializer result = testee.forProperty(PROPERTY);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusionNull() {
        TypeDeserializerBase testee = createTestee(BASE_Type, ID_RESOLVER, null, false, null);
        assertNull(testee.getTypeInclusion());
    }

    @Test
    public void testGetTypeInclusionInclude() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        TypeDeserializer.As result = testee.getTypeInclusion();
        assertEquals(TypeDeserializer.As.INCLUDE, result);
    }

    @Test
    public void testGetTypeInclusionExclude() {
        TypeDeserializerBase testee = new TypeDeserializerBase(BASE_TYPE, ID_RESOLVER, null, false, null) {
            @Override
            public TypeDeserializer.As getTypeInclusion() {
                return TypeDeserializer.As.EXCLUDE;
            }
        };
        TypeDeserializer.As result = testee.getTypeInclusion();
        assertEquals(TypeDeserializer.As.EXCLUDE, result);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        String result = testee.toString();
        assertTrue(result.contains("[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNull(testee._findDeserializer(null, "typeId"));
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNull(testee._findDeserializer(CTXT, null));
    }

    @Test
    public void testFindDeserializerNoDeserializerRegistered() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        when(ID_RESOLVER.typeFromId(CTXT, "typeId")).thenReturn(null);
        JsonDeserializer<Object> result = testee._findDeserializer(CTXT, "typeId");
        assertNull(result);
    }

    @Test
    public void testFindDeserializerRegistered() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        Converter<String, JsonDeserializer<Object>> deserializerMock = mock(Converter.class);
        when(ID_RESOLVER.typeFromId(CTXT, "typeId")).thenReturn(BASE_TYPE);
        when(ID_RESOLVER.findDeserializerForId(CTXT, "typeId")).thenReturn(deserializerMock);
        JsonDeserializer<Object> result = testee._findDeserializer(CTXT, "typeId");
        assertEquals(deserializerMock, result);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNull(testee._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        assertNull(testee._findDefaultImplDeserializer(CTXT));
    }

    @Test
    public void testFindDefaultImplDeserializerRegistered() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, JsonDeserializer.class);
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(testee._defaultImpl).thenReturn(BASE_TYPE);
        when(CTXT.findContextualValueDeserializer(BASE_TYPE, PROPERTY)).thenReturn(defaultImplDeserializer);
        JsonDeserializer<Object> result = testee._findDefaultImplDeserializer(CTXT);
        assertEquals(defaultImplDeserializer, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        testee._handleUnknownTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleUnknownTypeIdNullTypeId() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        testee._handleUnknownTypeId(CTXT, null);
    }

    @Test
    public void testHandleUnknownTypeIdRegisteredTypeId() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        when(ID_RESOLVER.typeFromId(CTXT, "typeId")).thenReturn(null);
        testee._handleUnknownTypeId(CTXT, "extraDesc");
    }

    @Test
    public void testHandleMissingTypeIdNullContext() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        testee._handleMissingTypeId(null, "extraDesc");
    }

    @Test
    public void testHandleMissingTypeIdRegisteredTypeId() throws IOException {
        TypeDeserializerBase testee = createTestee(BASE_TYPE, ID_RESOLVER, null, false, null);
        when(ID_RESOLVER.typeFromId(CTXT, "typeId")).thenThrow(new IOException("Missing type id"));
        testee._handleMissingTypeId(CTXT, "extraDesc");
    }

}