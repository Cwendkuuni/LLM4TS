package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DefaultParser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertNotNull(typeDeserializerBase);
        assertEquals(baseType, typeDeserializerBase._baseType);
        assertEquals(idResolver, typeDeserializerBase._idResolver);
        assertEquals(null, typeDeserializerBase._property);
        assertEquals(null, typeDeserializerBase._defaultImpl);
        assertEquals(null, typeDeserializerBase._typePropertyName);
        assertEquals(false, typeDeserializerBase._typeIdVisible);
        assertEquals(new ConcurrentHashMap<>(16, 0.75f, 4), typeDeserializerBase._deserializers);
        assertNull(typeDeserializerBase._defaultImplDeserializer);
    }

    @Test
    public void testTypeDeserializerBaseCopy() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase src = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
        TypeDeserializerBase dest = new TypeDeserializerBase(src, property);
        assertNotNull(dest);
        assertEquals(baseType, dest._baseType);
        assertEquals(idResolver, dest._idResolver);
        assertEquals(property, dest._property);
        assertEquals(null, dest._defaultImpl);
        assertEquals(null, dest._typePropertyName);
        assertEquals(false, dest._typeIdVisible);
        assertEquals(src._deserializers, dest._deserializers);
        assertNull(dest._defaultImplDeserializer);
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertNull(typeDeserializerBase._findDeserializer(null, ""));
    }

    @Test
    public void testFindDeserializerNoTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonParser parser = new DefaultParser();
        parser.setContext(context);
        assertNull(typeDeserializerBase._findDeserializer(context, ""));
    }

    @Test
    public void testFindDeserializerNullTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonParser parser = new DefaultParser();
        parser.setContext(context);
        assertNull(typeDeserializerBase._findDeserializer(context, null));
    }

    @Test
    public void testFindDeserializerNoDeserializerFound() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonParser parser = new DefaultParser();
        parser.setContext(context);
        when(typeDeserializerBase._idResolver.typeFromId(context, "test")).thenReturn(null);
        assertNull(typeDeserializerBase._findDeserializer(context, "test"));
    }

    @Test
    public void testFindDeserializerDeserializerFound() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonParser parser = new DefaultParser();
        parser.setContext(context);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._idResolver.typeFromId(context, "test")).thenReturn(mock(JavaType.class));
        when(typeDeserializerBase._deserializers.get("test")).thenReturn(deserializer);
        when(deserializer.deserialize(parser, context)).thenReturn("test");
        assertEquals("test", typeDeserializerBase._findDeserializer(context, "test").deserialize(parser, context));
    }

    @Test
    public void testDefaultImplDeserializerNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testDefaultImplDeserializerNoDefaultImpl() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDefaultImplDeserializerNullProperty() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        synchronized (typeDeserializerBase) {
            typeDeserializerBase._defaultImplDeserializer = mock(JsonDeserializer.class);
            typeDeserializerBase._property = null;
        }
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testDefaultImplDeserializerDeserializerFound() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        Class<?> defaultImpl = Mockito.mock(Class.class);
        JavaType javaType = mock(JavaType.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._idResolver.typeFromId(context, "test")).thenReturn(null);
        when(typeDeserializerBase._getDefaultImpl()).thenReturn(javaType);
        when(javaType.getRawClass()).thenReturn(defaultImpl);
        when(typeDeserializerBase._deserializers.get(defaultImpl.getName())).thenReturn(deserializer);
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test");
        synchronized (typeDeserializerBase) {
            typeDeserializerBase._defaultImplDeserializer = deserializer;
        }
        assertEquals("test", typeDeserializerBase._findDefaultImplDeserializer(context).deserialize(parser, context));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return null;
            }
        };
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null, null));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return null;
            }
        };
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, null, null));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeIdUnresolvableTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return _findDeserializer(ctxt, (String) typeId).deserialize(jp, ctxt);
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        JsonParser parser = new DefaultParser();
        DeserializationContext context = parser.getCodec().getDeserializationContext();
        ObjectMapper objectMapper = new ObjectMapper();
        context.setContextualHandler(TypeDeserializer.class, typeDeserializerBase);
        parser.setContext(context);
        parser.nextToken();
    }

    @Test
    public void testDeserializeWithNativeTypeIdResolvableTypeId() throws IOException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return _findDeserializer(ctxt, (String) typeId).deserialize(jp, ctxt);
            }

            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                JavaType javaType = ctxt.getTypeFactory().constructType(String.class);
                return ctxt.findContextualValueDeserializer(javaType, null);
            }
        };
        JsonParser parser = new DefaultParser();
        DeserializationContext context = parser.getCodec().getDeserializationContext();
        ObjectMapper objectMapper = new ObjectMapper();
        context.setContextualHandler(TypeDeserializer.class, typeDeserializerBase);
        parser.setContext(context);
        parser.nextToken();
        assertEquals("test", parser.readValueAs(String.class));
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(null, "test", null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdNullIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(null, null, null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(null, "test", null, new JavaType() {
            @Override
            public JavaType narrowBy(Class<?> cls) {
                return null;
            }
        });
    }

    @Test
    public void testHandleUnknownTypeIdDeserializerFound() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        DeserializationContext context = objectMapper.getDeserializationContext();
        JsonParser parser = new DefaultParser();
        parser.setContext(context);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        when(typeDeserializerBase._idResolver.typeFromId(context, "test")).thenReturn(null);
        when(typeDeserializerBase._handleUnknownTypeId(context, "test", typeDeserializerBase._idResolver, null)).thenReturn(deserializer);
        when(deserializer.deserialize(parser, context)).thenReturn("test");
        assertEquals("test", typeDeserializerBase._handleUnknownTypeId(context, "test", null, null).deserialize(parser, context));
    }

    @Test
    public void testToString() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertEquals("[TypeDeserializerBase; base-type:<...>; id-resolver:<...>]", typeDeserializerBase.toString());
    }
}