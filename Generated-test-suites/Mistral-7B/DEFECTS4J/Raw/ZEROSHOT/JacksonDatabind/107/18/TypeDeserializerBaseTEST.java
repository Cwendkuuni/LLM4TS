package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;

public class TypeDeserializerBaseTest {

    private TypeIdResolver typeIdResolver;
    private JavaType baseType;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        typeIdResolver = mock(TypeIdResolver.class);
        baseType = Object.class;
        when(typeIdResolver.getIdResolverInstance()).thenReturn(typeIdResolver);
        when(typeIdResolver.typeFromId(Mockito.any(DeserializationContext.class), Mockito.anyString())).thenReturn(null);

        typeDeserializer = new TypeDeserializerBase(baseType, typeIdResolver, "_typePropertyName", true, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };
    }

    @Test
    public void testDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        JsonParser jp = mock(JsonParser.class);

        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "typeId";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findContextualValueDeserializer(Mockito.eq(_typePropertyName), Mockito.any(JavaType.class))).thenReturn(mock(JsonDeserializer.class));

        JsonParser jp = mock(JsonParser.class);
        when(jp.getTypeId()).thenReturn(typeId);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(ctxt, typeId);
        assertEquals(deserializer.getClass(), JsonDeserializer.class);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);

        typeDeserializer._handleUnknownTypeId(ctxt, "typeId");
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("[" + TypeDeserializerBase.class.getName() + "; base-type:java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolverImpl]", typeDeserializer.toString());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("_typePropertyName", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetBaseType() {
        assertEquals(baseType, typeDeserializer.baseType());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findContextualValueDeserializer(Mockito.any(JavaType.class), Mockito.any(BeanProperty.class))).thenReturn(mock(JsonDeserializer.class));

        Object result = typeDeserializer._findDefaultImplDeserializer(ctxt);
        assertNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testGetDeserializersMapIsEmpty() {
        assertEquals(0, typeDeserializer._deserializers.size());
    }

    @Test
    public void testGetDeserializerFromMap() throws IOException {
        String typeId = "typeId";
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(ctxt.findContextualValueDeserializer(Mockito.eq(_typePropertyName), Mockito.any(JavaType.class))).thenReturn(deserializer);

        typeDeserializer._deserializers.put(typeId, deserializer);

        JsonDeserializer<Object> result = typeDeserializer._findDeserializer(ctxt, typeId);
        assertEquals(deserializer, result);
    }
}