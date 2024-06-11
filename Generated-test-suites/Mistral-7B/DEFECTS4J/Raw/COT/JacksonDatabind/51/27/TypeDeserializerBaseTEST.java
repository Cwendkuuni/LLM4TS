package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Unit tests for {@link TypeDeserializerBase} class.
 */
@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext context;
    
    @Mock
    private JsonParser parser;
    
    @Mock
    private TypeIdResolver idResolver;
    
    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;
    
    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty(null, "testProperty");
        TypeDeserializer expectedTypeDeserializer = new MockTypeDeserializer();
        assertEquals(expectedTypeDeserializer, typeDeserializerBase.forProperty(beanProperty));
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.PROPERTY;
        assertEquals(expectedTypeInclusion, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = new TypeReference<Object>() {}.getType();
        String expectedBaseTypeName = baseType.getRawClass().getName();
        assertEquals(expectedBaseTypeName, typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() {
        String expectedToString = "[TypeDeserializerBase; base-type:class java.lang.Object; id-resolver:null]";
        assertEquals(expectedToString, typeDeserializerBase.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testForPropertyNullProperty() throws IOException {
        BeanProperty beanProperty = null;
        typeDeserializerBase.forProperty(beanProperty);
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        String typeId = "testTypeId";
        assertNull(typeDeserializerBase._findDeserializer(null, typeId));
    }

    @Test
    public void testFindDeserializerExistingDeserializer() throws IOException {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = new MockJsonDeserializer();
        when(idResolver.typeFromId(context, typeId)).thenReturn(new TypeReference<Object>() {}.getType());
        when(context.findContextualValueDeserializer(eq(new TypeReference<Object>() {}.getType()), any())).thenReturn(deserializer);

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(deserializer, actualDeserializer);
    }

    @Test
    public void testFindDeserializerNoDeserializerFound() throws IOException {
        String typeId = "testTypeId";
        when(idResolver.typeFromId(context, typeId)).thenReturn(new TypeReference<Object>() {}.getType());

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(actualDeserializer);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerExistingDeserializer() throws IOException {
        JavaType defaultImpl = new TypeReference<Object>() {}.getType();
        JsonDeserializer<Object> deserializer = new MockJsonDeserializer();
        when(context.findContextualValueDeserializer(eq(defaultImpl), any())).thenReturn(deserializer);

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(deserializer, actualDeserializer);
    }

    @Test
    public void testHandleUnknownTypeIdNullTypeId() throws IOException {
        // Not testing null context, as it results in NPE
        assertNull(typeDeserializerBase._handleUnknownTypeId(context, null, idResolver, null));
    }

    @Test
    public void testHandleUnknownTypeIdUnknownTypeId() throws IOException {
        String unknownTypeId = "unknownTypeId";
        when(idResolver.typeFromId(context, unknownTypeId)).thenReturn(null);

        JavaType actualType = typeDeserializerBase._handleUnknownTypeId(context, unknownTypeId, idResolver, null);
        assertNull(actualType);
    }

    @Test
    public void testHandleUnknownTypeIdKnownTypeId() throws IOException {
        String knownTypeId = "knownTypeId";
        JavaType knownType = new TypeReference<Object>() {}.getType();
        when(idResolver.typeFromId(context, knownTypeId)).thenReturn(knownType);

        JavaType actualType = typeDeserializerBase._handleUnknownTypeId(context, knownTypeId, idResolver, null);
        assertEquals(knownType, actualType);
    }

    private static class MockJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static class MockTypeDeserializer extends TypeDeserializer {
        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    }
}