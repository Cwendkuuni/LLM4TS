package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext ctxt;
    private Object bean;

    @BeforeEach
    public void setup() throws JsonProcessingException {
        JsonFactory factory = new JsonFactory();
        parser = factory.createParser(getClass().getResource("/test-data.json").openStream());
        ctxt = Mockito.mock(DeserializationContext.class);
        bean = new Object();

        SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);
        TypeDeserializer typeDeser1 = Mockito.mock(TypeDeserializer.class);
        TypeDeserializer typeDeser2 = Mockito.mock(TypeDeserializer.class);

        handler = new ExternalTypeHandler.Builder()
                .addExternal(prop1, typeDeser1)
                .addExternal(prop2, typeDeser2)
                .build();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");
        when(typeDeser1.createContextualDeserializer(ctxt, null)).thenReturn(typeDeser1);

        handler.handleTypePropertyValue(parser, ctxt, "prop1", bean);
        verify(typeDeser1).deserialize(parser, ctxt, eq(typeDeser1.getType()));
        verifyNoMoreInteractions(typeDeser2);

        verify(parser).nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken().getId());
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");

        handler.handlePropertyValue(parser, ctxt, "prop1", bean);
        verify(typeDeser1).deserialize(parser, ctxt, eq(typeDeser1.getType()));
        verifyNoMoreInteractions(typeDeser2);

        verify(parser).nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken().getId());

        parser.nextToken();
        assertEquals(JsonToken.FIELD_NAME, parser.getCurrentToken().getId());
        assertEquals("Type1", parser.getText());

        handler.handlePropertyValue(parser, ctxt, "prop1", bean);
        verifyNoMoreInteractions(typeDeser1);
        verifyNoMoreInteractions(typeDeser2);
    }

    @Test
    public void testCompleteWithValidData() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");
        when(typeDeser1.createContextualDeserializer(ctxt, null)).thenReturn(typeDeser1);

        handler.complete(parser, ctxt, bean);
        verify(parser).nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken().getId());

        handler.complete(parser, ctxt, bean);
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testCompleteWithMissingTypeId() throws IOException {
        handler.complete(parser, ctxt, bean);
        verify(ctxt).reportMappingException(anyString());

        handler.handlePropertyValue(parser, ctxt, "prop1", bean);
        handler.handleTypePropertyValue(parser, ctxt, "prop1", bean);
        handler.complete(parser, ctxt, bean);
        verifyNoMoreInteractions(ctxt);
    }

    @Test
    public void testCompleteWithMissingProperty() throws IOException {
        handler.handleTypePropertyValue(parser, ctxt, "prop1", bean);
        handler.handlePropertyValue(parser, ctxt, "prop2", bean);
        handler.complete(parser, ctxt, bean);
        verify(ctxt).reportMappingException(anyString());
    }

    @Test
    public void testCompleteWithNullValue() throws IOException {
        handler.handleTypePropertyValue(parser, ctxt, "prop1", bean);
        handler.handlePropertyValue(parser, ctxt, "prop1", bean);
        handler.complete(parser, ctxt, bean);
        verifyNoMoreInteractions(ctxt);

        // Verify that property was set to null
        SettableBeanProperty prop = Mockito.any();
        verify(prop).set(bean, null);
    }

    @Test
    public void testCompleteWithNestedObjects() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");
        when(typeDeser1.createContextualDeserializer(ctxt, null)).thenReturn(typeDeser1);

        handler.handleTypePropertyValue(parser, ctxt, "prop1.prop2", bean);
        handler.handlePropertyValue(parser, ctxt, "prop1.prop2", bean);
        handler.handleTypePropertyValue(parser, ctxt, "prop1.prop3", bean);
        handler.handlePropertyValue(parser, ctxt, "prop1.prop3", bean);
        handler.complete(parser, ctxt, bean);
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");
        when(typeDeser1.createContextualDeserializer(ctxt, null)).thenReturn(typeDeser1);

        handler.handleTypePropertyValue(parser, ctxt, "prop1.typeId", bean);
        handler.handlePropertyValue(parser, ctxt, "prop1.value", bean);

        SettableBeanProperty creatorProp = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty creatorTypeProp = Mockito.mock(SettableBeanProperty.class);

        Object bean2 = new Object();
        handler.addCreatorProperty(creatorProp, creatorTypeProp);

        handler.complete(parser, ctxt, bean2, creatorProp);
        verify(parser).nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken().getId());

        handler.complete(parser, ctxt, bean2, creatorProp);
        verifyNoMoreInteractions(parser);

        SettableBeanProperty prop = Mockito.any();
        verify(prop).set(bean2, any());
        verify(creatorProp).set(bean2, "Type1");
        verify(creatorTypeProp).set(bean2, "Type1");
    }

    @Test
    public void testCompleteWithCreatorPropertiesAndNullValue() throws IOException {
        when(typeDeser1.getTypeIdResolver().idFromValueAndType(any(), any()))
                .thenReturn("Type1");
        when(typeDeser1.createContextualDeserializer(ctxt, null)).thenReturn(typeDeser1);

        handler.handleTypePropertyValue(parser, ctxt, "prop1.typeId", bean);
        handler.handlePropertyValue(parser, ctxt, "prop1.value", bean);

        SettableBeanProperty creatorProp = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty creatorTypeProp = Mockito.mock(SettableBeanProperty.class);

        Object bean2 = new Object();
        handler.addCreatorProperty(creatorProp, creatorTypeProp);

        handler.complete(parser, ctxt, bean2, creatorProp);
        verify(parser).nextToken();
        assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken().getId());

        handler.complete(parser, ctxt, bean2, creatorProp);
        verifyNoMoreInteractions(parser);

        SettableBeanProperty prop = Mockito.any();
        verify(prop).set(bean2, null);
        verify(creatorProp).set(bean2, "Type1");
        verify(creatorTypeProp).set(bean2, "Type1");
    }
}