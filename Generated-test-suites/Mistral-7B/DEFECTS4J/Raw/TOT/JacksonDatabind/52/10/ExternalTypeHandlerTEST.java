package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private static class TestTypeDeserializer extends None {
        private static final PropertyName TYPE_PROPERTY = new PropertyName("_type");
        private final TypeIdResolver idResolver;

        public TestTypeDeserializer(TypeIdResolver idResolver) {
            this.idResolver = idResolver;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String typeId = p.getText();
            return idResolver.idFromValueAndType(null, Object.class) + ":" + typeId;
        }

        @Override
        public PropertyName getPropertyName() {
            return TYPE_PROPERTY;
        }

        public String getTypeIdResolver() {
            return idResolver;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createTestExternalTypeHandler(Map<String, Object> properties) {
        List<ExtTypedProperty> extProps = new ArrayList<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            SettableBeanProperty prop = mock(SettableBeanProperty.class);
            when(prop.getName()).thenReturn((String) entry.getKey());
            TypeDeserializer typeDeser = new TestTypeDeserializer(mock(TypeIdResolver.class));
            ExtTypedProperty extProp = new ExtTypedProperty(prop, typeDeser);
            extProps.add(extProp);
        }
        return new ExternalTypeHandler((ExtTypedProperty[]) extProps.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
    }

    @SuppressWarnings("unchecked")
    private static <T> T createTestExternalTypeHandlerWithTypeDeser(Map<String, Object> properties,
            TypeDeserializer typeDeser) {
        List<ExtTypedProperty> extProps = new ArrayList<>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            SettableBeanProperty prop = mock(SettableBeanProperty.class);
            when(prop.getName()).thenReturn((String) entry.getKey());
            extProps.add(new ExtTypedProperty(prop, typeDeser));
        }
        return new ExternalTypeHandler((ExtTypedProperty[]) extProps.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
    }

    @org.junit.Test
    public void testHandleTypePropertyValue() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(p, ctxt, "prop1", bean);
        verify(ctxt).handleUnexpectedValue(eq(bean), eq(ExternalTypeHandler.class), any(JsonParser.class), any(DeserializationContext.class),
                eq("prop1"), eq("int:1"), anyString());

        handler.handleTypePropertyValue(p, ctxt, "prop2", bean);
        verify(ctxt).handleUnknownProperty(eq(bean), eq("prop2"), anyString());

        handler.start();
        handler = handler.start();

        handler.handleTypePropertyValue(p, ctxt, "prop1", bean);
        verify(bean).getClass();
        assertEquals(1, handler.getIndexOfProperty("prop1"));
        assertEquals("int:1", handler.getTypeId(0));

        handler.handleTypePropertyValue(p, ctxt, "prop2", bean);
        verify(bean).getClass();
        assertEquals(1, handler.getIndexOfProperty("prop1"));
        assertEquals("String:abc", handler.getTypeId(1));
    }

    @org.junit.Test
    public void testHandlePropertyValue() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        verify(bean).getClass();
        assertEquals(1, handler.getIndexOfProperty("prop1"));
        assertEquals("int:1", handler.getTypeId(0));

        handler.handlePropertyValue(p, ctxt, "prop2", bean);
        verify(bean).getClass();
        assertEquals(1, handler.getIndexOfProperty("prop1"));
        assertEquals("String:abc", handler.getTypeId(1));
    }

    @org.junit.Test
    public void testHandlePropertyValueWithTypeDeserializer() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", new TestTypeDeserializer(mock(TypeIdResolver.class)));
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandlerWithTypeDeser(properties, new TestTypeDeserializer(mock(TypeIdResolver.class)));

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(p, ctxt, "prop1", bean);
        verify(bean).getClass();
        assertEquals(0, handler.getIndexOfProperty("prop1"));
        assertEquals("_type:int:1", handler.getTypeId(0));

        handler.handlePropertyValue(p, ctxt, "prop2", bean);
        verify(bean).getClass();
        assertEquals(1, handler.getIndexOfProperty("prop2"));
        assertEquals("String:abc", handler.getTypeId(1));
    }

    @org.junit.Test
    public void testComplete() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);
        handler.start();
        handler = handler.start();

        Object bean = new Object();

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        handler.complete(p, ctxt, bean);
        verify(bean).getClass();
        verify(bean, times(2)).getClass();
        assertEquals(2, handler.getIndexOfProperty("prop1"));
        assertEquals(2, handler.getIndexOfProperty("prop2"));
        assertEquals("int:1", handler.getTypeId(0));
        assertEquals("String:abc", handler.getTypeId(1));
    }

    @org.junit.Test
    public void testCompleteWithPropertyBasedCreator() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", new TestTypeDeserializer(mock(TypeIdResolver.class)));
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandlerWithTypeDeser(properties, new TestTypeDeserializer(mock(TypeIdResolver.class)));
        handler.start();
        handler = handler.start();

        Object bean = new Object();
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        handler.complete(p, ctxt, buffer, creator);
        verify(bean).getClass();
        verify(bean, times(2)).getClass();
        assertEquals(2, handler.getIndexOfProperty("prop1"));
        assertEquals(2, handler.getIndexOfProperty("prop2"));
        assertEquals("_type:int:1", handler.getTypeId(0));
        assertEquals("String:abc", handler.getTypeId(1));

        verify(buffer, times(2)).assignParameter(any(SettableBeanProperty.class), any());
        verify(creator).build(eq(ctxt), eq(buffer));
    }

    @org.junit.Test
    public void testCompleteNullValue() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", null);

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);
        handler.start();
        handler = handler.start();

        Object bean = new Object();

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        handler.complete(p, ctxt, bean);
        verify(bean, times(1)).getClass();
        verify(bean).getClass();
        assertEquals(2, handler.getIndexOfProperty("prop1"));
        assertEquals(2, handler.getIndexOfProperty("prop2"));
        assertEquals("int:1", handler.getTypeId(0));
        assertNull(handler.getTypeId(1));
    }

    @org.junit.Test
    public void testCompleteMissingTypeId() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", "prop:missing");

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);
        handler.start();
        handler = handler.start();

        Object bean = new Object();

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        handler.complete(p, ctxt, bean);
        verify(bean, times(1)).getClass();
        verify(bean).getClass();
        assertEquals(2, handler.getIndexOfProperty("prop1"));
        assertEquals(2, handler.getIndexOfProperty("prop2"));
        assertEquals("int:1", handler.getTypeId(0));
        assertNull(handler.getTypeId(1));

        verify(ctxt, times(1)).reportMappingException(anyString(), anyString(), anyString(), any(Throwable.class));
    }

    @org.junit.Test
    public void testCompleteMissingProperty() throws Exception {
        Map<String, Object> properties = new HashMap<>();
        properties.put("prop1", "int:1");
        properties.put("prop2", "String:abc");

        ExternalTypeHandler handler = createTestExternalTypeHandler(properties);
        handler.start();
        handler = handler.start();

        Object bean = new Object();

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);

        handler.complete(p, ctxt, bean);
        verify(bean, times(1)).getClass();
        verify(bean).getClass();
        assertEquals(2, handler.getIndexOfProperty("prop1"));
        assertEquals(2, handler.getIndexOfProperty("prop2"));
        assertEquals("int:1", handler.getTypeId(0));
        assertEquals("String:abc", handler.getTypeId(1));

        handler.complete(p, ctxt, bean);
        verify(bean, times(2)).getClass();
        verify(bean, times(1)).getClass();
        verify(ctxt, times(1)).reportMappingException(anyString(), anyString(), anyString(), any(Throwable.class));
    }
}