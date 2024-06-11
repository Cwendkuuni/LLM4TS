
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertFalse(Entities.isNamedEntity("xyz"));
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertFalse(Entities.isBaseNamedEntity("quot"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertNull(Entities.getCharacterByName("xyz"));
  }
  
  @Test
  public void testEscape() {
    assertEquals("This &amp; that", Entities.escape("This & that", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
    assertEquals("This & that", Entities.escape("This & that", new Document.OutputSettings(Entities.EscapeMode.base)));
    assertEquals("This & that", Entities.escape("This & that", new Document.OutputSettings(Entities.EscapeMode.extended)));
  }
  
  @Test
  public void testUnescape() {
    assertEquals("This & that", Entities.unescape("This &amp; that"));
  }
  
  // Additional test cases for edge cases and exception handling scenarios
  
  @Test
  public void testEscapeWithNullString() {
    assertEquals("", Entities.escape(null, new Document.OutputSettings(Entities.EscapeMode.xhtml)));
  }
  
  @Test
  public void testEscapeWithEmptyString() {
    assertEquals("", Entities.escape("", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
  }
  
  @Test
  public void testEscapeWithNormaliseWhite() {
    assertEquals("This   that", Entities.escape("This\t\tthat", new Document.OutputSettings(Entities.EscapeMode.xhtml)));
  }
  
  @Test
  public void testUnescapeWithNullString() {
    assertEquals("", Entities.unescape(null));
  }
  
  @Test
  public void testUnescapeWithStrict() {
    assertEquals("This &amp; that", Entities.unescape("This &#x26; that", true));
  }
  
  @Test
  public void testUnescapeWithoutStrict() {
    assertEquals("This & that", Entities.unescape("This &#x26; that", false));
  }
}
