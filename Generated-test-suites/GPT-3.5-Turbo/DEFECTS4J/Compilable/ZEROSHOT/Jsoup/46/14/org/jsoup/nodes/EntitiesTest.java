package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("gt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertTrue(Entities.isNamedEntity("quot"));
    assertFalse(Entities.isNamedEntity("abc"));
  }

  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("gt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertFalse(Entities.isBaseNamedEntity("quot"));
    assertFalse(Entities.isBaseNamedEntity("abc"));
  }

  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf((char) 60), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf((char) 62), Entities.getCharacterByName("gt"));
    assertEquals(Character.valueOf((char) 38), Entities.getCharacterByName("amp"));
    assertNull(Entities.getCharacterByName("abc"));
  }

  @Test
  public void testEscape() {
    String escaped = Entities.escape("<b>Hello &amp; World</b>", new Document.OutputSettings());
    assertEquals("&lt;b&gt;Hello &amp;amp; World&lt;/b&gt;", escaped);
  }

  @Test
  public void testUnescape() {
    String unescaped = Entities.unescape("&lt;b&gt;Hello &amp;amp; World&lt;/b&gt;");
    assertEquals("<b>Hello &amp; World</b>", unescaped);
  }
}