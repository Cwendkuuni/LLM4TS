package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonParserSequenceTest {
    
    @Test
    public void testClose() throws IOException {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        Mockito.doNothing().when(parser1).close();
        Mockito.doNothing().when(parser2).close();
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        sequence.close();
        
        Mockito.verify(parser1, Mockito.times(1)).close();
        Mockito.verify(parser2, Mockito.times(1)).close();
    }
    
    @Test
    public void testNextToken_withNullToken() throws IOException, JsonParseException {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        Mockito.when(parser1.nextToken()).thenReturn(null);
        Mockito.when(parser2.nextToken()).thenReturn(JsonToken.START_OBJECT);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        JsonToken result = sequence.nextToken();
        
        assertEquals(JsonToken.START_OBJECT, result);
        
        Mockito.verify(parser1, Mockito.times(1)).nextToken();
        Mockito.verify(parser2, Mockito.times(0)).nextToken();
    }
    
    @Test
    public void testNextToken_withNonNullToken() throws IOException, JsonParseException {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        Mockito.when(parser1.nextToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(parser2.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        JsonToken result = sequence.nextToken();
        
        assertEquals(JsonToken.START_OBJECT, result);
        
        Mockito.verify(parser1, Mockito.times(0)).nextToken();
        Mockito.verify(parser2, Mockito.times(0)).nextToken();
    }
    
    @Test
    public void testNextToken_withNullTokenAndNoMoreParsers() throws IOException, JsonParseException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        
        Mockito.when(parser.nextToken()).thenReturn(null);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser});
        JsonToken result = sequence.nextToken();
        
        assertNull(result);
        
        Mockito.verify(parser, Mockito.times(1)).nextToken();
    }
    
    @Test
    public void testNextToken_withNonNullTokenAndNoMoreParsers() throws IOException, JsonParseException {
        JsonParser parser = Mockito.mock(JsonParser.class);
        
        Mockito.when(parser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser});
        JsonToken result = sequence.nextToken();
        
        assertEquals(JsonToken.END_OBJECT, result);
        
        Mockito.verify(parser, Mockito.times(0)).nextToken();
    }
    
    @Test
    public void testContainedParsersCount() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        int count = sequence.containedParsersCount();
        
        assertEquals(2, count);
    }
    
    @Test
    public void testSwitchToNext_withNextParser() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        boolean result = sequence.switchToNext();
        
        assertTrue(result);
        
        Mockito.verify(parser1, Mockito.times(0)).close();
        Mockito.verify(parser2, Mockito.times(0)).close();
    }
    
    @Test
    public void testSwitchToNext_withNoNextParser() {
        JsonParser parser = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser});
        boolean result = sequence.switchToNext();
        
        assertFalse(result);
        
        Mockito.verify(parser, Mockito.times(0)).close();
    }
    
    @Test
    public void testSwitchToNext_withNextParserAndSwitchAgain() {
        JsonParser parser1 = Mockito.mock(JsonParser.class);
        JsonParser parser2 = Mockito.mock(JsonParser.class);
        
        JsonParserSequence sequence = new JsonParserSequence(new JsonParser[]{parser1, parser2});
        sequence.switchToNext();
        
        boolean result = sequence.switchToNext();
        
        assertFalse(result);
        
        Mockito.verify(parser1, Mockito.times(0)).close();
        Mockito.verify(parser2, Mockito.times(0)).close();        
    }
    
}