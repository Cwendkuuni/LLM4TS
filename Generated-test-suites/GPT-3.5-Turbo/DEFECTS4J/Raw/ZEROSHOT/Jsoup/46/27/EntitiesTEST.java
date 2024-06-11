import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("gt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertTrue(Entities.isNamedEntity("quot"));
    assertFalse(Entities.isNamedEntity("foo"));
  }

  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("gt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertTrue(Entities.isBaseNamedEntity("quot"));
    assertFalse(Entities.isBaseNamedEntity("foo"));
  }

  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
    assertNull(Entities.getCharacterByName("foo"));
  }

  @Test
  public void testEscape() {
    String result = Entities.escape("<b>Hello</b>", new Document.OutputSettings());
    assertEquals("&lt;b&gt;Hello&lt;/b&gt;", result);
  }

  @Test
  public void testEscape_inAttribute() {
    String result = Entities.escape("<b>Hello</b>", new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));
    assertEquals("<b>Hello</b>", result);
  }

  @Test
  public void testUnescape() {
    String result = Entities.unescape("&lt;b&gt;Hello&lt;/b&gt;");
    assertEquals("<b>Hello</b>", result);
  }

  @Test
  public void testUnescape_strict() {
    String result = Entities.unescape("&lt;b&gt;Hello&lt;/b&gt;", true);
    assertEquals("<b>Hello</b>", result);
  }
}