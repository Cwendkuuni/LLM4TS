import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertFalse(Entities.isNamedEntity("test"));
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertFalse(Entities.isBaseNamedEntity("test"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertNull(Entities.getCharacterByName("test"));
  }
  
  @Test
  public void testEscape() {
    String escapedString = Entities.escape("<test>", new Document.OutputSettings());
    assertEquals("&lt;test&gt;", escapedString);
  }
  
  @Test
  public void testUnescape() {
    String unescapedString = Entities.unescape("&lt;test&gt;");
    assertEquals("<test>", unescapedString);
  }
}