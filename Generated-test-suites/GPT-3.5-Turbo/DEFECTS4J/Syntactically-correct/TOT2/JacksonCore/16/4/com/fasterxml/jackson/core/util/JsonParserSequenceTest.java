package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class JsonParserSequenceTest {
  
  @Test
  public void testCreateFlattened() {
    JsonParser parser1 = new JsonParser();
    JsonParser parser2 = new JsonParser();
    JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    assertNotNull(sequence);
    assertEquals(2, sequence.containedParsersCount());
  }
  
  @Test
  public void testSwitchToNext() {
    JsonParser parser1 = new JsonParser();
    JsonParser parser2 = new JsonParser();
    JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    assertTrue(sequence.switchToNext());
    assertEquals(parser2, sequence.delegate);
    
    assertFalse(sequence.switchToNext());
  }
  
  @Test
  public void testNextToken() {
    JsonParser parser1 = new JsonParser();
    JsonParser parser2 = new JsonParser();
    JsonParserSequence sequence = JsonParserSequence.createFlattened(parser1, parser2);
    
    assertEquals(JsonToken.START_ARRAY, sequence.nextToken());
  }

}