package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {
    private static final String TEST_TYPE_ID = "TestTypeId";

    @Test
    public void testFindDeserializer() throws IOException {
        TypeDeserializerBase td = createTestTypeDeserializerBase();
        DeserializationContext ctxt = createTestContext();

        JsonDeserializer<Object> deser = td._findDeserializer(ctxt, TEST_TYPE_ID);
        assertNull(deser);

        JavaType type = createMockTypeIdResolver().typeFromId(ctxt, TEST_TYPE_ID);
        deser = td._findDeserializer(ctxt, TEST_TYPE_ID);
        assertNull(deser);

        when(type.getRawClass()).thenReturn(Object.class);
        deser = td._findDeserializer(ctxt, TEST_TYPE_ID);
        assertNull(deser);

        when(type.getRawClass()).thenReturn(String.class);
        deser = td._findDeserializer(ctxt, TEST_TYPE_ID);
        assertNull(deser);

        when(type.getRawClass()).thenReturn(MyTestClass.class);
        JsonDeserializer<MyTestClass> deserializer = mock(JsonDeserializer.class);
        when(td._deserializers.get(TEST_TYPE_ID)).thenReturn(deserializer);
        deser = td._findDeserializer(ctxt, TEST_TYPE_ID);
        assertEquals(deserializer, deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase td = createTestTypeDeserializerBase();
        DeserializationContext ctxt = createTestContext();

        assertEquals(NullifyingDeserializer.instance, td._findDefaultImplDeserializer(ctxt));

        when(td._defaultImplDeserializer).thenReturn(mock(JsonDeserializer.class));
        assertEquals(td._defaultImplDeserializer, td._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        TypeDeserializerBase td = createTestTypeDeserializerBase();
        JsonParser jp = createTestJsonParser();

        Object result = td._deserializeWithNativeTypeId(jp, createTestDeserializationContext(), null);
        assertNull(result);

        when(jp.getTypeId()).thenReturn(TEST_TYPE_ID);
        result = td._deserializeWithNativeTypeId(jp, createTestDeserializationContext(), null);
        assertNull(result);

        when(jp.getTypeId()).thenReturn(TEST_TYPE_ID);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(td._findDeserializer(createTestDeserializationContext(), TEST_TYPE_ID)).thenReturn(deserializer);
        result = td._deserializeWithNativeTypeId(jp, createTestDeserializationContext(), TEST_TYPE_ID);
        assertEquals(deserializer, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase td = createTestTypeDeserializerBase();
        DeserializationContext ctxt = createTestDeserializationContext();

        td._handleUnknownTypeId(ctxt, TEST_TYPE_ID, createMockTypeIdResolver(), createTestJavaType());
    }

    private TypeDeserializerBase createTestTypeDeserializerBase() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(createTestJavaType());
        return new TypeDeserializerBase(createTestJavaType(), idResolver, "", true, Object.class) {
            @Override
            public Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>();
            @Override
            public JsonDeserializer<Object> _defaultImplDeserializer;
        };
    }

    private static TypeIdResolver createMockTypeIdResolver() {
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(typeIdResolver.getTypeIdResolverBaseClass()).thenReturn(TypeIdResolverBase.class);
        return typeIdResolver;
    }

    private static JavaType createTestJavaType() {
        return createTypeRef("com.test.MyTestClass", new Class[0]);
    }

    private static DeserializationContext createTestDeserializationContext() {
        return createContextBuilder().build();
    }
}