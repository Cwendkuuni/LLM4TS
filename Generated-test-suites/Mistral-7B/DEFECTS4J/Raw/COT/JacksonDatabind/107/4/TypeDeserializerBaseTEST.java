package com.fasterxml.jackson.databind.jsontype.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {
    

    @org.junit.Before
    public void setup() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(mock(DeserializationContext.class), anyString())).thenReturn(null);
        when(idResolver.getDescForKnownTypeIds()).thenReturn("known type ids = Object, String");

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ctxt.setIdResolver(idResolver);
        ctxt.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);

        TypeDeserializerBase testee = new TypeDeserializerBase(JavaType.constructType(int.class), idResolver, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.WRAPPER_ARRAY;
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return NullifyingDeserializer.instance;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc) throws IOException {
                return null;
            }
        };
        this.testee = testee;
    }

    @org.junit.Test
    public void testForProperty() throws JsonProcessingException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase testee = new TypeDeserializerBase(JavaType.constructType(int.class), null, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
        TypeDeserializer actual = testee.forProperty(property);
        assertEquals(testee.getClass(), actual.getClass());
    }

    @org.junit.Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, testee.getTypeInclusion());
    }

    @org.junit.Test
    public void testBaseTypeName() {
        assertEquals("int", testee.baseTypeName());
    }

    @org.junit.Test
    public void testPropertyName() {
        assertEquals(null, testee.getPropertyName());
    }

    @org.junit.Test
    public void testTypeIdResolverGetTypeFromId() throws IOException {
        when(idResolver.typeFromId(ctxt, anyString())).thenReturn(JavaType.constructType(String.class));
        JavaType actual = testee._findDeserializer(ctxt, "String");
        assertEquals(JavaType.constructType(String.class), actual);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(ctxt.findContextualValueDeserializer(eq(Void.TYPE), any(BeanProperty.class))).thenReturn(deserializer);
        JsonDeserializer<Object> actual = testee._findDefaultImplDeserializer(ctxt);
        assertEquals(deserializer, actual);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        when(parser.getValueAsInt()).thenReturn(1);
        when(parser.getText()).thenReturn("\"String\"");

        Object actual = testee._deserializeWithNativeTypeId(parser, ctxt);
        assertNull(actual);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        when(idResolver.getTypeFromId(ctxt, anyString())).thenThrow(new IOException("Type Id not found"));

        JavaType actual = testee._handleUnknownTypeId(ctxt, "TypeId");
        assertNull(actual);
    }

    @org.junit.Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws IOException {
        when(idResolver.getTypeFromId(ctxt, anyString())).thenReturn(null);

        JavaType actual = testee._handleMissingTypeId(ctxt, "MissingTypeId");
        assertNull(actual);
    }

    
}