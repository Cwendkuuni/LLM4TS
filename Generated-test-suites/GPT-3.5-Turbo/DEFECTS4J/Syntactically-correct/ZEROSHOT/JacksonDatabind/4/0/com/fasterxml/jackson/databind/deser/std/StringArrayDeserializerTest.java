package com.fasterxml.jackson.databind.deser.std;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = StringArrayDeserializer.instance;
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        stringArrayDeserializer._elementDeserializer = null;
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[]{"hello", "world"});
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(jp.getText()).thenReturn("hello", "world");
        when(buffer.appendCompletedChunk(new Object[]{"hello", "world"})).thenReturn(new Object[]{"hello", "world"});
        when(buffer.completeAndClearBuffer(new Object[]{"hello", "world"}, 2, String.class)).thenReturn(new String[]{"hello", "world"});
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(true);
        
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        stringArrayDeserializer._elementDeserializer = deser;
        when(jp.isExpectedStartArrayToken()).thenReturn(true);
        when(ctxt.leaseObjectBuffer()).thenReturn(buffer);
        when(buffer.resetAndStart()).thenReturn(new Object[]{"hello", "world"});
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(deser.getNullValue()).thenReturn(null);
        when(deser.deserialize(jp, ctxt)).thenReturn("hello", "world");
        when(buffer.appendCompletedChunk(new Object[]{"hello", "world"})).thenReturn(new Object[]{"hello", "world"});
        when(buffer.completeAndClearBuffer(new Object[]{"hello", "world"}, 2, String.class)).thenReturn(new String[]{"hello", "world"});
        when(ctxt.returnObjectBuffer(buffer)).thenReturn(true);
        
        String[] result = stringArrayDeserializer._deserializeCustom(jp, ctxt);
        assertEquals("hello", result[0]);
        assertEquals("world", result[1]);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        ObjectBuffer buffer = mock(ObjectBuffer.class);
        when(typeDeserializer.deserializeTypedFromArray(jp, ctxt)).thenReturn(new String[]{"hello", "world"});
        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        
        Object result = stringArrayDeserializer.deserializeWithType(jp, ctxt, typeDeserializer);
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals("hello", arrayResult[0]);
        assertEquals("world", arrayResult[1]);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(jp.getText()).thenReturn("hello");
        
        String[] result = stringArrayDeserializer.handleNonArray(jp, ctxt);
        assertEquals("hello", result[0]);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)).thenReturn(true);
        when(ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)).thenReturn(false);
        when(ctxt.constructType(String.class)).thenReturn(mock(JavaType.class));
        when(ctxt.findContextualValueDeserializer(ctxt.constructType(String.class), property)).thenReturn(deser);
        when(ctxt.handleSecondaryContextualization(deser, property)).thenReturn(deser);
        when(stringArrayDeserializer.isDefaultDeserializer(deser)).thenReturn(false);
        
        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(ctxt, property);
        assertTrue(result instanceof StringArrayDeserializer);
        StringArrayDeserializer arrayResult = (StringArrayDeserializer) result;
        assertNotEquals(stringArrayDeserializer, arrayResult);
        assertEquals(deser, arrayResult._elementDeserializer);
    }

}