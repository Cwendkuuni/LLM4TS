package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializer;
    private DeserializationContext context;
    private JsonParser parser;
    private JsonType idResolver;

    @Before
    public void setUp() {
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
        idResolver = mock(JsonType.class);
        when(context.getTypeFactory()).thenReturn(new StandardSerializer.BaseSerializerFactory());
        typeDeserializer = new TypeDeserializerBase(
                JavaType.construct(Integer.class),
                idResolver,
                "typeProperty",
                true,
                JavaType.construct(Double.class)) {
                    @Override
                    public TypeDeserializer forProperty(BeanProperty prop) {
                        return this;
                    }
                };
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(Double.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(idResolver.getTypeId()).thenReturn("intTypeId");
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(typeDeserializer._baseType, _property)).thenReturn(deserializer);

        assertEquals(deserializer, typeDeserializer._findDeserializer(context, "intTypeId"));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        when(context.findContextualValueDeserializer(JavaType.construct(String.class), _property)).thenReturn(mock(JsonDeserializer.class));
        assertEquals(mock(JsonDeserializer.class), typeDeserializer._findDefaultImplDeserializer(context));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(context.handleUnknownTypeId(JavaType.construct(Integer.class), null, idResolver, null)).thenReturn(JavaType.construct(Long.class));

        assertEquals(JavaType.construct(Long.class), typeDeserializer._handleUnknownTypeId(context, null, idResolver, JavaType.construct(Integer.class)));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithNullIdResolver() throws IOException {
        when(context.handleUnknownTypeId(JavaType.construct(Integer.class), null, null, null)).thenThrow(IOException.class);

        typeDeserializer._handleUnknownTypeId(context, null, null, JavaType.construct(Integer.class));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, context, "intTypeId");
        assertEquals(Integer.class, Class.forName("java.lang.Integer").cast(result.getClass()));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(JavaType.construct(String.class), _property)).thenReturn(deserializer);

        Object result = typeDeserializer._deserializeWithNativeTypeId(parser, context, null);
        assertEquals(String.class, result.getClass());
    }
}