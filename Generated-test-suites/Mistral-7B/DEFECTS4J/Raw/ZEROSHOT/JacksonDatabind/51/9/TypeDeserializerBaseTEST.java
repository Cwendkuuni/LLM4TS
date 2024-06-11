package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Unit tests for {@link TypeDeserializerBase}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private JsonDeserializer<Object> defaultImplDeserializer;
    @Mock
    private TypeDeserializerBase typeDeserializerBase;
    @Mock
    private BeanProperty property;
    @Mock
    private DeserializationContext context;
    @Mock
    private JsonParser parser;
    @Mock
    private JavaType javaType;
    @Mock
    private TypeIdResolver typeIdResolver;

    @InjectMocks
    private TypeDeserializerBase testee;

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "testTypeId";
        JavaType javaType = Mockito.mock(JavaType.class);

        // Test with deserializer found in map
        Mockito.when(typeDeserializerBase._deserializers.get(typeId)).thenReturn(defaultImplDeserializer);
        assertEquals(defaultImplDeserializer, testee._findDeserializer(context, typeId));

        // Test with deserializer not found in map
        Mockito.when(typeDeserializerBase._deserializers.get(typeId)).thenReturn(null);
        Mockito.when(typeDeserializerBase._handleUnknownTypeId(context, typeId, typeIdResolver, javaType)).thenReturn(javaType);
        assertEquals(javaType, testee._findDeserializer(context, typeId));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        // Test with default impl deserializer found
        Mockito.when(context.findContextualValueDeserializer(Mockito.eq(testee._defaultImpl), Mockito.eq(property))).thenReturn(defaultImplDeserializer);
        assertEquals(defaultImplDeserializer, testee._findDefaultImplDeserializer(context));

        // Test with default impl deserializer not found
        Mockito.when(context.findContextualValueDeserializer(Mockito.eq(testee._defaultImpl), Mockito.eq(property))).thenReturn(null);
        Mockito.when(testee._isEnabled(Mockito.eq(context), Mockito.eq(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE))).thenReturn(false);
        assertNull(testee._findDefaultImplDeserializer(context));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "testTypeId";
        JavaType javaType = Mockito.mock(JavaType.class);
        Mockito.when(typeIdResolver.typeFromId(context, typeId)).thenReturn(javaType);

        assertEquals(javaType, testee._handleUnknownTypeId(context, typeId, typeIdResolver, testee._defaultImpl));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithError() throws IOException {
        String typeId = "testTypeId";
        JavaType javaType = Mockito.mock(JavaType.class);

        Mockito.when(typeIdResolver.typeFromId(context, typeId)).thenThrow(new IOException());

        testee._handleUnknownTypeId(context, typeId, typeIdResolver, testee._defaultImpl);
    }
}