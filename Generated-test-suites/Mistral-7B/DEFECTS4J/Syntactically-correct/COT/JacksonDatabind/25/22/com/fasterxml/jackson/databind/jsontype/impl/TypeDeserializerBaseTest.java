package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    private static final String BASE_TYPE_NAME = "java.lang.String";
    private static final String TYPE_PROPERTY_NAME = "typeProperty";
    private static final String TYPE_ID_PROPERTY_NAME = "typeIdProperty";
    private static final String TYPE_ID = "typeId";
    private static final String TYPE_ID_STR = "typeIdStr";
    private static final String UNKNOWN_TYPE_ID = "unknownTypeId";
    private static final TypeIdResolver TYPE_ID_RESOLVER = mock(TypeIdResolver.class);
    private static final DeserializationContext DESERIALIZATION_CONTEXT = mock(DeserializationContext.class);
    private static final JsonParser JSON_PARSER = mock(JsonParser.class);

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        when(DESERIALIZATION_CONTEXT.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));

        when(JSON_PARSER.getTypeId())
                .thenReturn(TYPE_ID);
        when(JSON_PARSER.getText())
                .thenReturn("testValue");

        JavaType baseType = TypeFactory.defaultInstance().constructType(BASE_TYPE_NAME);
        typeDeserializerBase = new TypeDeserializerBase(baseType, TYPE_ID_RESOLVER, TYPE_PROPERTY_NAME, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return mock(TypeDeserializer.class);
            }
        };
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.forProperty(any(BeanProperty.class))).thenReturn(typeDeserializer);

        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertEquals(typeDeserializer, forProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals(BASE_TYPE_NAME, typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals(TYPE_PROPERTY_NAME, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(TYPE_ID_RESOLVER, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }

    @Test
    public void testToString() throws IOException {
        String toString = typeDeserializerBase.toString();
        assertTrue(toString.contains(BASE_TYPE_NAME));
        assertTrue(toString.contains("id-resolver:" + TYPE_ID_RESOLVER));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(DESERIALIZATION_CONTEXT.findContextualValueDeserializer(eq(_baseType), any(BeanProperty.class)))
                .thenReturn(deserializer);

        JsonDeserializer<Object> deserializerFromFindDeserializer = typeDeserializerBase._findDeserializer(DESERIALIZATION_CONTEXT, typeId);
        assertEquals(deserializer, deserializerFromFindDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        typeDeserializerBase._findDeserializer(DESERIALIZATION_CONTEXT, null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeId() throws IOException {
        when(TYPE_ID_RESOLVER.typeFromId(DESERIALIZATION_CONTEXT, eq(TYPE_ID)))
                .thenReturn(null);

        typeDeserializerBase._findDeserializer(DESERIALIZATION_CONTEXT, TYPE_ID);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerUnknownTypeIdWithTypeIdResolver() throws IOException {
        when(TYPE_ID_RESOLVER.typeFromId(DESERIALIZATION_CONTEXT, eq(UNKNOWN_TYPE_ID)))
                .thenReturn(null);

        typeDeserializerBase._handleUnknownTypeId(DESERIALIZATION_CONTEXT, UNKNOWN_TYPE_ID, TYPE_ID_RESOLVER, _baseType);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithDefaultImpl() throws IOException {
        when(DESERIALIZATION_CONTEXT.isEnabled(eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)))
                .thenReturn(false);
        when(DESERIALIZATION_CONTEXT.findContextualValueDeserializer(eq(_baseType), any(BeanProperty.class)))
                .thenReturn(null);
        when(DESERIALIZATION_CONTEXT.findValueDeserializer(eq(JSON_PARSER), eq(_property)))
                .thenReturn(NullifyingDeserializer.instance);

        typeDeserializerBase._findDeserializer(DESERIALIZATION_CONTEXT, TYPE_ID);
    }
}