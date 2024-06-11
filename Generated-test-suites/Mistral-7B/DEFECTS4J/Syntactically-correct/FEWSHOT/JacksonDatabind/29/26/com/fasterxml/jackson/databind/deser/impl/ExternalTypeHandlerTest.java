package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExternalTypeHandlerTest {

    private static class TestTypeDeserializer extends StdDeserializer<String> {
        public TestTypeDeserializer() {
            this(null);
        }

        public TestTypeDeserializer(Class<?> vc) {
            super(String.class, vc);
        }

        @Override
        public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getText();
        }

        @Override
        public String getPropertyName() {
            return "testType";
        }

        @Override
        public TypeDeserialator getContentDeserializer() {
            return null;
        }

        @Override
        public String getTypeId() {
            return "testTypeId";
        }

        @Override
        public void resolveType(DeserializationContext ctxt, BeanPropertyPropertyType propertyType) throws JsonMappingException {
            // no-op
        }
    }

    private static class TestProperty {
        private final String name;
        private SettableBeanProperty property;

        public TestProperty(String name, SettableBeanProperty property) {
            this.name = name;
            this.property = property;
        }

        public String getName() {
            return name;
        }

        public SettableBeanProperty getProperty() {
            return property;
        }
    }

    private ExternalTypeHandler createTestHandler(SettableBeanProperty[] properties,
            Map<String, Integer> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens) {
        return new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        when(property1.getName()).thenReturn("prop1");
        when(property1.hasTypePropertyName("prop1")).thenReturn(true);
        when(property1.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());
        when(property2.getName()).thenReturn("prop2");
        when(property2.hasTypePropertyName("prop1")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property1, property2 },
                new HashMap<>(), new String[] { "testTypeId1", "testTypeId2" }, new TokenBuffer[2]);

        when(parser.getText()).thenReturn("testTypeId1");

        assertTrue(handler.handleTypePropertyValue(parser, context, "prop1", bean));
        assertSame(property1, handler._properties[0]);
        assertEquals("testTypeId1", handler._typeIds[0]);

        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");
    }

    @Test
    public void testHandleTypePropertyValue_missingTypePropertyName() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn("prop");
        when(property.hasTypePropertyName("prop")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property },
                new HashMap<>(), new String[0], new TokenBuffer[0]);
        when(parser.getText()).thenReturn("testTypeId");

        assertFalse(handler.handleTypePropertyValue(parser, context, "prop", bean));

        verifyNoMoreInteractions(parser);
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        when(property1.getName()).thenReturn("prop1");
        when(property1.hasTypePropertyName("prop1")).thenReturn(false);
        when(property1.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());
        when(property2.getName()).thenReturn("prop2");
        when(property2.hasTypePropertyName("prop1")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property1, property2 },
                new HashMap<>(), null, null);

        when(parser.getText()).thenReturn("testTypeId");

        handler.handlePropertyValue(parser, context, "prop1", bean);

        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).skipChildren();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");
    }

    @Test
    public void testHandlePropertyValue_noTypeDeserializer() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn("prop");
        when(property.hasTypePropertyName("prop")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property },
                new HashMap<>(), null, null);

        when(parser.getText()).thenReturn("testTypeId");

        handler.handlePropertyValue(parser, context, "prop", bean);

        verify(parser, times(1)).nextToken();
        verify(parser, times(1)).skipChildren();
        verifyNoMoreInteractions(parser);
        verifyNoMoreInteractions(context);
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        TypeDeserializer typeDeserializer1 = new TestTypeDeserializer();
        TypeDeserializer typeDeserializer2 = new TestTypeDeserializer();
        when(property1.getName()).thenReturn("prop1");
        when(property1.hasTypePropertyName("prop1")).thenReturn(false);
        when(property1.getTypeDeserializer()).thenReturn(typeDeserializer1);
        when(property2.getName()).thenReturn("prop2");
        when(property2.hasTypePropertyName("prop1")).thenReturn(false);
        when(property2.getTypeDeserializer()).thenReturn(typeDeserializer2);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property1, property2 },
                new HashMap<>(), new String[] { "testTypeId1", "testTypeId2" }, new TokenBuffer[2]);

        when(parser.getText()).thenReturn("testTypeId1");

        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);

        handler.complete(parser, context, bean);

        verify(parser, times(5)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");

        assertEquals("testTypeId1", handler._typeIds[0]);
        assertEquals("testTypeId2", handler._typeIds[1]);
    }

    @Test
    public void testComplete_missingTypeIds() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        when(property1.getName()).thenReturn("prop1");
        when(property1.hasTypePropertyName("prop1")).thenReturn(false);
        when(property1.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());
        when(property2.getName()).thenReturn("prop2");
        when(property2.hasTypePropertyName("prop1")).thenReturn(false);
        when(property2.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property1, property2 },
                new HashMap<>(), null, null);

        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);

        handler.complete(parser, context, bean);

        verify(parser, times(6)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");

        assertNull(handler._typeIds[0]);
        assertNull(handler._typeIds[1]);
    }

    @Test
    public void testComplete_missingProperty() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        SettableBeanProperty missingProperty = mock(SettableBeanProperty.class);
        when(property1.getName()).thenReturn("prop1");
        when(property1.hasTypePropertyName("prop1")).thenReturn(false);
        when(property1.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());
        when(property2.getName()).thenReturn("prop2");
        when(property2.hasTypePropertyName("prop1")).thenReturn(false);
        when(property2.getTypeDeserializer()).thenReturn(new TestTypeDeserializer());
        when(missingProperty.getName()).thenReturn("missingProp");
        when(missingProperty.hasTypePropertyName("prop1")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property1, property2, missingProperty },
                new HashMap<>(), new String[] { "testTypeId1", "testTypeId2" }, new TokenBuffer[3]);

        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop2", bean);

        handler.complete(parser, context, bean);

        verify(parser, times(6)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");

        assertEquals("testTypeId1", handler._typeIds[0]);
        assertEquals("testTypeId2", handler._typeIds[1]);
        assertNull(handler._typeIds[2]);
    }

    @Test
    public void testComplete_missingBothTypeAndProperty() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();
        SettableBeanProperty property = mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn("prop");
        when(property.hasTypePropertyName("prop")).thenReturn(false);

        ExternalTypeHandler handler = createTestHandler(new SettableBeanProperty[] { property },
                new HashMap<>(), null, null);

        handler.handlePropertyValue(parser, context, "prop", bean);

        handler.complete(parser, context, bean);

        verify(parser, times(4)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context).handleUnexpectedValue(parser, JsonToken.START_OBJECT, "Unexpected JSON structure");

        assertNull(handler._typeIds[0]);
    }

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}