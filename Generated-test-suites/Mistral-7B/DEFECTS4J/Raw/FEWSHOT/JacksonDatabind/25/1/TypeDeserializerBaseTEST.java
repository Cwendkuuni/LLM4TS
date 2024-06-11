package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeDeserializerTestBase;
import com.fasterxml.jackson.databind.util.TypeIdTestBuilder;

public class TypeDeserializerBaseTest extends TypeDeserializerTestBase {

    public TypeDeserializerBaseTest() {
        super(TypeDeserializerBase.class.getName());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_findDeserializer() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz);

        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(getJavaType(clazz));
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId), eq(null), eq(null), anyIntArray()))
                .thenReturn(getJavaType(clazz));
        when(deserializer.getClass()).thenReturn(clazz);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertSame(deserializer, result);
    }

    @Test
    public void test_findDeserializer_whenTypeIdNotFound() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, null);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertNull(result);
    }

    @Test
    public void test_findDeserializer_whenNativeTypeId() throws Exception {
        String typeId = "MyType";

        TypeDeserializerBase typeDeserializer = getMockTypeDeserializer();

        JsonDeserializer<Object> result = typeDeserializer._deserializeWithNativeTypeId(getMockJsonParser(), getMockContext(), typeId);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void test_handleUnknownTypeId() throws Exception {
        TypeDeserializerBase typeDeserializer = getMockTypeDeserializer();

        typeDeserializer._handleUnknownTypeId(getMockContext(), "unknownTypeId", getMockTypeIdResolver(), getTypeFactory().constructType(String.class));
    }

    @Test
    public void test_findDefaultImplDeserializerWithDefaultContext() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, true, null);

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(getMockContext());
        assertNull(result);
    }

    @Test
    public void test_findDefaultImplDeserializerWithNonNullContext() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, null, true, null);

        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(getMockContextWithDefaultImpl(getTypeFactory().constructType(Map.class), null));
        assertSame(NullifyingDeserializer.instance, result);
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz);

        BeanProperty property = mock(BeanProperty.class);
        when(property.getName()).thenReturn("propertyName");

        TypeDeserializer result = typeDeserializer.forProperty(property);
        TypeDeserializerBase expected = new TypeDeserializerBase(baseType, idResolver, "propertyName", true, clazz);
        assertSame(expected, result);
    }

    @Test
    public void testToString() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz);

        String expected = String.format("[%s; base-type:%s; id-resolver: %s]", getClass().getName(), baseType.toString(), idResolver.getClass().getName());
        String result = typeDeserializer.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz);

        JsonTypeInfo.As result = typeDeserializer.getTypeInclusion();
        JsonTypeInfo.As expected = JsonTypeInfo.As.EXISTING_PROPERTY;
        assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testForProperty_calledTwice() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz);

        BeanProperty property = mock(BeanProperty.class);
        typeDeserializer.forProperty(property);

        // should throw IllegalStateException since 'property' was already used
        typeDeserializer.forProperty(property);
    }

    @Test
    public void test_deserializerMapIsThreadSafe() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        final TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<String, JsonDeserializer<Object>>(16, 0.75f, 4);
        };

        JsonDeserializer<Object> deserializer1 = mock(JsonDeserializer.class);
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(getJavaType(clazz));
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId), eq(null), eq(null), anyIntArray()))
                .thenReturn(getJavaType(clazz));

        when(deserializer1.getClass()).thenReturn(clazz);
        when(deserializer2.getClass()).thenReturn(clazz);

        typeDeserializer.forProperty(mock(BeanProperty.class));

        JsonDeserializer<Object> deser1 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        JsonDeserializer<Object> deser2 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertEquals(deserializer1, deser1);
        assertEquals(deserializer2, deser2);

        typeDeserializer._deserializers.put(typeId, deserializer2);

        JsonDeserializer<Object> deser3 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertEquals(deserializer2, deser3);
    }

    @Test
    public void test_deserializerMapIsThreadSafeWithConcurrentModifications() throws Exception {
        JavaType baseType = getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typeId = "com.example.MyType";
        Class<?> clazz = getClassUnderTest(typeId, String.class);

        final TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, clazz) {
            @Override
            protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<String, JsonDeserializer<Object>>(16, 0.75f, 4);
        };

        JsonDeserializer<Object> deserializer1 = mock(JsonDeserializer.class);
        JsonDeserializer<Object> deserializer2 = mock(JsonDeserializer.class);

        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId))).thenReturn(getJavaType(clazz));
        when(idResolver.typeFromId(any(DeserializationContext.class), eq(typeId), eq(null), eq(null), anyIntArray()))
                .thenReturn(getJavaType(clazz));

        when(deserializer1.getClass()).thenReturn(clazz);
        when(deserializer2.getClass()).thenReturn(clazz);

        typeDeserializer.forProperty(mock(BeanProperty.class));

        JsonDeserializer<Object> deser1 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        JsonDeserializer<Object> deser2 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertEquals(deserializer1, deser1);
        assertEquals(deserializer2, deser2);

        typeDeserializer._deserializers.putIfAbsent(typeId, deserializer2);

        JsonDeserializer<Object> deser3 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertEquals(deserializer2, deser3);

        typeDeserializer._deserializers.remove(typeId);

        JsonDeserializer<Object> deser4 = typeDeserializer._findDeserializer(getMockContext(), typeId);
        assertEquals(deserializer1, deser4);
    }

    private static Class<?> getClassUnderTest(String typeId, Class<?> baseType) {
        return TypeIdTestBuilder.buildSimpleType(TypeDeserializerBase.class.getPackage().getName(), typeId).getRawClass();
    }

    private static JsonParser getMockJsonParser() {
        return mock(JsonParser.class);
    }

    private static DeserializationContext getMockContext() {
        return mock(DeserializationContext.class);
    }

    private static DeserializationContext getMockContextWithDefaultImpl(JavaType defaultImplType, JavaType defaultImplTypeId) {
        DeserializationContext context = getMockContext();
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class))).thenReturn(getMockDeserializer(defaultImplType, defaultImplTypeId));
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        return context;
    }

    private static JsonDeserializer<Object> getMockDeserializer(JavaType type, JavaType typeId) {
        return mock(JsonDeserializer.class);
    }
}