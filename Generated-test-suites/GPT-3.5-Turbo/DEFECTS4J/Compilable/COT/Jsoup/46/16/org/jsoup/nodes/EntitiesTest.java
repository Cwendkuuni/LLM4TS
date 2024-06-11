package org.jsoup.nodes;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    boolean result = Entities.isNamedEntity("lt");
    Assert.assertTrue(result);
  }

  @Test
  public void testIsBaseNamedEntity() {
    boolean result = Entities.isBaseNamedEntity("amp");
    Assert.assertTrue(result);
  }

  @Test
  public void testGetCharacterByName() {
    Character result = Entities.getCharacterByName("gt");
    Assert.assertEquals(Character.valueOf('>'), result);
  }

  @Test
  public void testEscape() {
    String result = Entities.escape("<test>", new Document.OutputSettings());
    Assert.assertEquals("&lt;test&gt;", result);
  }

  @Test
  public void testEscape_withXhtmlMode() {
    String result = Entities.escape("<test>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
    Assert.assertEquals("&lt;test&gt;", result);
  }

  @Test
  public void testUnescape() {
    String result = Entities.unescape("&lt;test&gt;");
    Assert.assertEquals("<test>", result);
  }

  @Test
  public void testUnescape_withStrictMode() {
    String result = Entities.unescape("&lt;test&gt;", true);
    Assert.assertEquals("<test>", result);
  }

  // Edge case test
  @Test
  public void testEscape_withEmptyString() {
    String result = Entities.escape("", new Document.OutputSettings());
    Assert.assertEquals("", result);
  }

  // Exception handling test
  @Test
  public void testUnescape_withInvalidEntity() {
    String result = Entities.unescape("&invalidentity;");
    Assert.assertEquals("&invalidentity;", result);
  }
}