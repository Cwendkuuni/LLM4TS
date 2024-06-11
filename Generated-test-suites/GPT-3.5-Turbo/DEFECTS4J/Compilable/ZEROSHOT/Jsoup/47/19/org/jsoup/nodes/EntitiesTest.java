package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertFalse(Entities.isNamedEntity("abc"));
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertFalse(Entities.isBaseNamedEntity("abc"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertNull(Entities.getCharacterByName("abc"));
  }
  
  @Test
  public void testEscape() {
    assertEquals("&amp; &lt; &quot;", Entities.escape("& < \"", new Document.OutputSettings()));
  }
  
  @Test
  public void testUnescape() {
    assertEquals("& < \"", Entities.unescape("&amp; &lt; &quot;"));
  }
}