package com.example.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TypeResolutionContext;
import com.fasterxml.jackson.databind.util.TypeUtil;

public class TypeDeserializerBaseTest {

    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final String DEFAULT_IMPL_TYPE_NAME = "defaultImplTypeName";

    private TypeIdResolver idResolver;
    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setUp() throws Exception {
        idResolver = mock(TypeIdResolver.class);
        objectMapper = new ObjectMapper();
        typeDeserializer = new TypeDeserializerBase(
                objectMapper.getTypeFactory().constructType(Object.class), idResolver,
                TYPE_PROPERTY_NAME, true,
                objectMapper.getTypeFactory().constructType(Class.forName(DEFAULT_IMPL_TYPE_NAME))) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return mock(JsonDeserializer.class);
            }
        };
    }

    @Test
    public void testForProperty() throws IOException, JsonProcessingException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer<?> deserializer = typeDeserializer.forProperty(property);
        assertSame(typeDeserializer.getClass(), deserializer.getClass());
    }

    @Test
    public void testGetTypeInclusion() {
        assertNull(typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializer.baseTypeName();
        assertSame(Object.class.getName(), baseTypeName);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetPropertyNameThrowsException() {
        typeDeserializer.getPropertyName();
    }

    @Test
    public void testgetTypeIdResolver() {
        assertSame(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testgetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializer.getDefaultImpl();
        assertSame(Class.forName(DEFAULT_IMPL_TYPE_NAME), defaultImpl);
    }

    @Test
    public void testToString() throws IOException {
        String toString = typeDeserializer.toString();
        String expectedToString = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver=mock(TypeIdResolver)]";
        assertSame(expectedToString, toString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "typeId";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(mock(DeserializationContext.class), typeId)).thenReturn(
                objectMapper.getTypeFactory().constructType(String.class));
        when(idResolver.typeFromId(mock(DeserializationContext.class), "anotherTypeId"))
                .thenReturn(null);

        deserializer = typeDeserializer._findDeserializer(mock(DeserializationContext.class), typeId);
        assertSame(deserializer, typeDeserializer._deserializers.get(typeId));

        deserializer = typeDeserializer._findDeserializer(mock(DeserializationContext.class), "anotherTypeId");
        assertNull(deserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerThrowsIOException() throws IOException {
        when(idResolver.typeFromId(mock(DeserializationContext.class), "typeId")).thenThrow(new IOException());
        typeDeserializer._findDeserializer(mock(DeserializationContext.class), "typeId");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "typeId";
        JavaType actual = typeDeserializer._handleUnknownTypeId(mock(DeserializationContext.class),
                typeId, idResolver, objectMapper.getTypeFactory().constructType(Object.class));
        assertNull(actual);
    }
}