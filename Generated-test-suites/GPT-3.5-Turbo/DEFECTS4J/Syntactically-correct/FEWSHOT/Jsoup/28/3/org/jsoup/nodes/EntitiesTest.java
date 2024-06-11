
package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("amp"));
    assertFalse(Entities.isNamedEntity("fakeentity"));
  }

  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertNull(Entities.getCharacterByName("fakeentity"));
  }

  @Test
  public void testEscape() {
    Document.OutputSettings out = new Document.OutputSettings();
    out.escapeMode = Entities.EscapeMode.xhtml;
    assertEquals("&#60;", Entities.escape("<", out));
    assertEquals("&amp;", Entities.escape("&", out));
    assertEquals("&gt;", Entities.escape(">", out));
    assertEquals("&quot;", Entities.escape("\"", out));
    assertEquals("&lt;", Entities.escape(">", out));
  }

  @Test
  public void testUnescape() {
    assertEquals("<", Entities.unescape("&#60;"));
    assertEquals("&", Entities.unescape("&amp;"));
    assertEquals(">", Entities.unescape("&gt;"));
    assertEquals("\"", Entities.unescape("&quot;"));
    assertEquals("<", Entities.unescape("&lt;"));
    assertEquals("&#60;", Entities.unescape("&#60;", true));
    assertEquals("&amp;", Entities.unescape("&amp;", true));
    assertEquals("&gt;", Entities.unescape("&gt;", true));
    assertEquals("&quot;", Entities.unescape("&quot;", true));
    assertEquals("&lt;", Entities.unescape("&lt;", true));
  }
}
