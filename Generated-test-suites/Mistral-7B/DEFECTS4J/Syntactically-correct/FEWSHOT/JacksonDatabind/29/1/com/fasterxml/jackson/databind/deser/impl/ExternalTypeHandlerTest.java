package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

/**
 * Test cases for ExternalTypeHandler class.
 */
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler createHandler(ExtTypedProperty[] properties,
            HashMap<String, Integer> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens) {
        return new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
        when(prop1.hasTypePropertyName(eq("prop1"))).thenReturn(true);

        ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
        when(prop2.hasTypePropertyName(eq("prop2"))).thenReturn(false);

        SettableBeanProperty prop1Bean = mock(SettableBeanProperty.class);
        Object bean1 = mock(Object.class);

        SettableBeanProperty prop2Bean = mock(SettableBeanProperty.class);
        Object bean2 = mock(Object.class);

        String[] typeIdsArr = {"type1", "type2"};
        TokenBuffer[] tokensArr = {new TokenBuffer(null), new TokenBuffer(null)};

        ExternalTypeHandler handler = createHandler(new ExtTypedProperty[] {prop1, prop2},
                new HashMap<String, Integer>() {{
                    put("prop1", 0);
                    put("prop2", 1);
                }}, typeIdsArr, tokensArr);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        handler.handleTypePropertyValue(parser, context, "prop1", bean1);
        verify(parser).getText();
        assertEquals("type1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler.handleTypePropertyValue(parser, context, "prop2", bean1);
        verifyNoMoreInteractions(parser);
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        handler.handleTypePropertyValue(parser, context, "prop1", bean2);
        verifyNoMoreInteractions(parser);
        assertEquals("type1", handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler.handleTypePropertyValue(parser, context, "prop2", bean2);
        verify(parser).getText();
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
        when(prop1.hasTypePropertyName(eq("prop1"))).thenReturn(false);
        when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
        when(prop2.hasTypePropertyName(eq("prop2"))).thenReturn(false);
        when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        SettableBeanProperty prop1Bean = mock(SettableBeanProperty.class);
        Object bean1 = mock(Object.class);

        SettableBeanProperty prop2Bean = mock(SettableBeanProperty.class);
        Object bean2 = mock(Object.class);

        String[] typeIdsArr = {"type1", "type2"};
        TokenBuffer[] tokensArr = {new TokenBuffer(null), new TokenBuffer(null)};

        ExternalTypeHandler handler = createHandler(new ExtTypedProperty[] {prop1, prop2},
                new HashMap<String, Integer>() {{
                    put("prop1", 0);
                    put("prop2", 1);
                }}, null, tokensArr);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        handler.handlePropertyValue(parser, context, "prop1", bean1);
        verifyNoMoreInteractions(parser);
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler.handlePropertyValue(parser, context, "prop2", bean1);
        verify(parser).skipChildren();
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        handler.handlePropertyValue(parser, context, "prop1", bean2);
        verifyNoMoreInteractions(parser);
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        handler.handlePropertyValue(parser, context, "prop2", bean2);
        verify(parser).getText();
        assertNull(handler._typeIds[1]);
        TokenBuffer tokens = handler._tokens[1];
        assertNotNull(tokens);
        assertEquals(JsonToken.START_OBJECT, tokens.currentToken().asInt());
    }

    @Test
    public void testComplete() throws IOException {
        ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
        when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
        when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        SettableBeanProperty prop1Bean = mock(SettableBeanProperty.class);
        Object bean = mock(Object.class);

        String[] typeIdsArr = {"type1", "type2"};
        TokenBuffer[] tokensArr = {new TokenBuffer(null), new TokenBuffer(null)};

        ExternalTypeHandler handler = createHandler(new ExtTypedProperty[] {prop1, prop2},
                new HashMap<String, Integer>() {{
                    put("prop1", 0);
                    put("prop2", 1);
                }}, typeIdsArr, tokensArr);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        handler.complete(parser, context, bean);
        verify(parser).nextToken();
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);

        String prop1Value = "value1";
        when(parser.getText()).thenReturn(prop1Value);
        handler.complete(parser, context, bean);
        verify(prop1Bean.getProperty()).deserialize(parser, context);
        assertEquals(prop1Value, handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        String prop2Value = "value2";
        when(parser.getText()).thenReturn(prop2Value);
        handler.complete(parser, context, bean);
        verify(parser).nextToken();
        assertEquals("type1", handler._typeIds[0]);
        TokenBuffer tokens = handler._tokens[0];
        assertNotNull(tokens);
        assertEquals(JsonToken.START_OBJECT, tokens.currentToken().asInt());
        verify(prop2Bean.getProperty()).deserializeAndSet(parser, context, bean);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExtTypedProperty prop1 = mock(ExtTypedProperty.class);
        when(prop1.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        ExtTypedProperty prop2 = mock(ExtTypedProperty.class);
        when(prop2.getProperty()).thenReturn(mock(SettableBeanProperty.class));

        SettableBeanProperty prop1Bean = mock(SettableBeanProperty.class);
        Object bean = mock(Object.class);

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        String[] typeIdsArr = {"type1", "type2"};
        TokenBuffer[] tokensArr = {new TokenBuffer(null), new TokenBuffer(null)};

        ExternalTypeHandler handler = createHandler(new ExtTypedProperty[] {prop1, prop2},
                new HashMap<String, Integer>() {{
                    put("prop1", 0);
                    put("prop2", 1);
                }}, typeIdsArr, tokensArr);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        handler.complete(parser, context, buffer, null);
        verifyNoMoreInteractions(parser);

        String prop1Value = "value1";
        when(parser.getText()).thenReturn(prop1Value);
        handler.complete(parser, context, buffer, null);
        verify(buffer).assignParameter(prop1Bean.getProperty(), prop1Value);
        assertEquals(prop1Value, handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        String prop2Value = "value2";
        when(parser.getText()).thenReturn(prop2Value);
        handler.complete(parser, context, buffer, null);
        verify(parser).nextToken();
        assertEquals("type1", handler._typeIds[0]);
        TokenBuffer tokens = handler._tokens[0];
        assertNotNull(tokens);
        assertEquals(JsonToken.START_OBJECT, tokens.currentToken().asInt());
        verify(prop2Bean.getProperty()).deserializeAndSet(parser, context, bean);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[1]);
    }
}