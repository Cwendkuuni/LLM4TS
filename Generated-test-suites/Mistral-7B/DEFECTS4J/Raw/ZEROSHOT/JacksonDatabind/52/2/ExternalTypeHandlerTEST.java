package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

import static com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.*;

@RunWith(MockitoJUnitRunner.class)
public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    @Before
    public void setup() throws Exception {
        mockParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);

        ExtTypedProperty prop1 = new ExtTypedProperty(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class));
        ExtTypedProperty prop2 = new ExtTypedProperty(Mockito.mock(SettableBeanProperty.class), Mockito.mock(TypeDeserializer.class));

        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);

        String[] typeIds = new String[]{"type1", "type2"};
        TokenBuffer[] tokens = new TokenBuffer[2];

        handler = new ExternalTypeHandler(new ExtTypedProperty[]{prop1, prop2}, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JacksonAnnotation jacksonAnnotation1 = Mockito.mock(JsonTypeInfo.As.External.class);
        JacksonAnnotation jacksonAnnotation2 = Mockito.mock(JsonTypeInfo.As.External.class);
        when(mockParser.getCurrentName()).thenReturn("prop1");
        when(mockParser.getText()).thenReturn("type1");

        handler.handleTypePropertyValue(mockParser, mockContext, jacksonAnnotation1, null);
        verify(mockParser).nextToken();
        verify(mockParser).nextToken();
        verify(mockParser, times(1)).getText();
        assertEquals("type1", handler._typeIds[0]);
        assertEquals(JsonToken.START_OBJECT, handler._tokens[0].currentToken());

        reset(mockParser);
        when(mockParser.getCurrentName()).thenReturn("prop2");
        when(mockParser.getText()).thenReturn("type2");

        handler.handleTypePropertyValue(mockParser, mockContext, jacksonAnnotation2, null);
        verify(mockParser, times(2)).nextToken();
        verify(mockParser, times(1)).getText();
        assertEquals("type2", handler._typeIds[1]);
        assertEquals(JsonToken.START_OBJECT, handler._tokens[1].currentToken());
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JacksonAnnotation jacksonAnnotation = Mockito.mock(JsonTypeInfo.As.External.class);
        Object bean = Mockito.mock(Object.class);

        when(mockParser.getCurrentName()).thenReturn("prop1");

        handler.handlePropertyValue(mockParser, mockContext, jacksonAnnotation, bean);
        verify(mockParser, times(1)).nextToken();
        verify(mockParser, times(1)).getText();
        assertEquals("type1", handler._typeIds[0]);
        assertEquals(JsonToken.START_OBJECT, handler._tokens[0].currentToken());
        assertNull(handler._tokens[0]);

        reset(mockParser);

        when(mockParser.getCurrentName()).thenReturn("prop2");

        handler.handlePropertyValue(mockParser, mockContext, jacksonAnnotation, bean);
        verify(mockParser, times(2)).nextToken();
        verify(mockParser, times(1)).getText();
        assertEquals("type2", handler._typeIds[1]);
        assertEquals(JsonToken.START_OBJECT, handler._tokens[1].currentToken());
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testComplete() throws Exception {
        Object bean = Mockito.mock(Object.class);
        String jsonString = "{\"prop1\":\"type1\",\"prop2\":\"type2\"}";

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));

        handler.complete(parser, mockContext, bean);

        verify(mockParser, times(3)).nextToken();
        verify(mockParser, times(2)).getText();

        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
    }

    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws Exception {
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler.complete(mockParser, mockContext, buffer, creator);

        Object result = creator.build(mockContext, buffer);

        verify(mockParser, times(2)).nextToken();
        verify(mockParser, times(1)).getText();
        verify(buffer).assignParameter(Mockito.any(SettableBeanProperty.class), Mockito.any(Object.class));
        verify(buffer).assignParameter(Mockito.any(SettableBeanProperty.class), Mockito.anyString());

        assertEquals("type1", handler._typeIds[0]);
        assertEquals("type2", handler._typeIds[1]);
        assertNull(handler._tokens[0]);
        assertNull(handler._tokens[1]);
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingTypeIdProperty() throws Exception {
        Object bean = Mockito.mock(Object.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler.complete(mockParser, mockContext, bean);

        verify(mockParser, times(2)).nextToken();
        verify(mockParser, times(1)).getText();

        String jsonString = "{\"prop1\":\"value1\"}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));

        handler.complete(parser, mockContext, bean);

        verify(mockParser, times(3)).nextToken();
        verify(mockParser, times(2)).getText();

        assertEquals("value1", handler._tokens[0].asString());
    }

    @Test(expected = IOException.class)
    public void testCompleteWithMissingProperty() throws Exception {
        Object bean = Mockito.mock(Object.class);

        when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler.complete(mockParser, mockContext, bean);

        verify(mockParser, times(2)).nextToken();
        verify(mockParser, times(1)).getText();

        String jsonString = "{\"typeId\":\"type1\"}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(new ByteArrayInputStream(jsonString.getBytes()));

        handler.complete(parser, mockContext, bean);
    }
}