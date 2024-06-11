package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.Map;

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
  public void testEscapeXhtml() {
    Map<Character, String> escapeMap = Entities.EscapeMode.xhtml.getMap();
    assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
    assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml)));
  }

  @Test
  public void testEscapeBase() {
    Map<Character, String> escapeMap = Entities.EscapeMode.base.getMap();
    assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
    assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
    assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
    assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
    assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base)));
  }

  @Test
  public void testEscapeExtended() {
    Map<Character, String> escapeMap = Entities.EscapeMode.extended.getMap();
    assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
    assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
    assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
    assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
    assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended)));
  }

  @Test
  public void testEscapeNormaliseWhite() {
    Map<Character, String> escapeMap = Entities.EscapeMode.base.getMap();
    assertEquals(" test ", Entities.escape(" test ", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).normaliseWhite(true)));
    assertEquals("test ", Entities.escape("test \n", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).normaliseWhite(true)));
    assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).normaliseWhite(true)));
  }

  @Test
  public void testEscapeStripLeadingWhite() {
    Map<Character, String> escapeMap = Entities.EscapeMode.base.getMap();
    assertEquals(" test", Entities.escape(" test", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).stripLeadingWhite(true)));
    assertEquals("test", Entities.escape("  test", new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).stripLeadingWhite(true)));
    assertNull(Entities.escape(null, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base).stripLeadingWhite(true)));
  }

  @Test
  public void testUnescape() {
    assertEquals("<", Entities.unescape("&lt;"));
    assertEquals(">", Entities.unescape("&gt;"));
    assertEquals("&", Entities.unescape("&amp;"));
    assertEquals("\"", Entities.unescape("&quot;"));
    assertNull(Entities.unescape(null));
  }

  @Test
  public void testUnescapeStrict() {
    assertEquals("<", Entities.unescape("&lt;", true));
    assertEquals(">", Entities.unescape("&gt;", true));
    assertEquals("&", Entities.unescape("&amp;", true));
    assertEquals("\"", Entities.unescape("&quot;", true));
    assertNull(Entities.unescape(null, true));
  }
}