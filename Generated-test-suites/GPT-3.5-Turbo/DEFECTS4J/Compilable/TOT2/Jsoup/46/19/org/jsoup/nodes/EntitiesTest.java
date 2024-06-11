package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertFalse(Entities.isNamedEntity("test"));
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertFalse(Entities.isBaseNamedEntity("test"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertNull(Entities.getCharacterByName("test"));
  }
  
  @Test
  public void testEscape() {
    String escaped = Entities.escape("This <test> &amp; that", new Document.OutputSettings());
    assertEquals("This &lt;test&gt; &amp;amp; that", escaped);
  }
  
  @Test
  public void testUnescape() {
    String unescaped = Entities.unescape("This &lt;test&gt; &amp;amp; that", false);
    assertEquals("This <test> & that", unescaped);
  }
}