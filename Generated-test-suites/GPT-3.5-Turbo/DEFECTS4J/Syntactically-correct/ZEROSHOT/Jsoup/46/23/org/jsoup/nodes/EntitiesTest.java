package org.jsoup.nodes;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    boolean result = Entities.isNamedEntity("lt");
    assertTrue(result);
    
    result = Entities.isNamedEntity("amp");
    assertTrue(result);
    
    result = Entities.isNamedEntity("gt");
    assertTrue(result);
    
    result = Entities.isNamedEntity("quot");
    assertTrue(result);
    
    result = Entities.isNamedEntity("nbsp");
    assertFalse(result);
    
    result = Entities.isNamedEntity("test");
    assertFalse(result);
  }

  @Test
  public void testIsBaseNamedEntity() {
    boolean result = Entities.isBaseNamedEntity("lt");
    assertTrue(result);
    
    result = Entities.isBaseNamedEntity("amp");
    assertTrue(result);
    
    result = Entities.isBaseNamedEntity("gt");
    assertTrue(result);
    
    result = Entities.isBaseNamedEntity("quot");
    assertTrue(result);
    
    result = Entities.isBaseNamedEntity("nbsp");
    assertFalse(result);
    
    result = Entities.isBaseNamedEntity("test");
    assertFalse(result);
  }

  @Test
  public void testGetCharacterByName() {
    Character result = Entities.getCharacterByName("lt");
    assertEquals(Character.valueOf('<'), result);
    
    result = Entities.getCharacterByName("amp");
    assertEquals(Character.valueOf('&'), result);
    
    result = Entities.getCharacterByName("gt");
    assertEquals(Character.valueOf('>'), result);
    
    result = Entities.getCharacterByName("quot");
    assertEquals(Character.valueOf('\"'), result);
    
    result = Entities.getCharacterByName("nbsp");
    assertNull(result);
    
    result = Entities.getCharacterByName("test");
    assertNull(result);
  }

  @Test
  public void testEscape() {
    String result = Entities.escape("test string", new Document.OutputSettings());
    assertEquals("test string", result);
    
    result = Entities.escape("<p>paragraph</p>", new Document.OutputSettings());
    assertEquals("&lt;p&gt;paragraph&lt;/p&gt;", result);
    
    result = Entities.escape("&copy;", new Document.OutputSettings());
    assertEquals("&amp;copy;", result);
  }

  @Test
  public void testUnescape() {
    String result = Entities.unescape("test string");
    assertEquals("test string", result);
    
    result = Entities.unescape("&lt;p&gt;paragraph&lt;/p&gt;");
    assertEquals("<p>paragraph</p>", result);
    
    result = Entities.unescape("&amp;copy;");
    assertEquals("&copy;", result);
  }

}