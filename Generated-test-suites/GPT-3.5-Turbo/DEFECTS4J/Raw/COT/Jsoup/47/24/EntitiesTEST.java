
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  // Test isNamedEntity method
  @Test
  public void testIsNamedEntity() {
    // Test with known entity name
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertTrue(Entities.isNamedEntity("gt"));
    assertTrue(Entities.isNamedEntity("quot"));
    
    // Test with unknown entity name
    assertFalse(Entities.isNamedEntity("invalid"));
  }
  
  // Test isBaseNamedEntity method
  @Test
  public void testIsBaseNamedEntity() {
    // Test with known base entity name
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    
    // Test with known extended entity name
    assertFalse(Entities.isBaseNamedEntity("gt"));
    assertFalse(Entities.isBaseNamedEntity("quot"));
    
    // Test with unknown entity name
    assertFalse(Entities.isBaseNamedEntity("invalid"));
  }
  
  // Test getCharacterByName method
  @Test
  public void testGetCharacterByName() {
    // Test with known entity name
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
    assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
    
    // Test with unknown entity name
    assertNull(Entities.getCharacterByName("invalid"));
  }
  
  // Test escape method
  @Test
  public void testEscape() {
    // Test with special characters
    assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings()));
    assertEquals("&nbsp;", Entities.escape("\u00a0", new Document.OutputSettings()));
    assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings()));
    assertEquals("&gt;", Entities.escape(">", new Document.OutputSettings()));
    assertEquals("&quot;", Entities.escape("\"", new Document.OutputSettings()));
    
    // Test with normal characters
    assertEquals("abcdef", Entities.escape("abcdef", new Document.OutputSettings()));
  }
  
  // Test unescape method
  @Test
  public void testUnescape() {
    // Test with escaped characters
    assertEquals("&", Entities.unescape("&amp;"));
    assertEquals("\u00a0", Entities.unescape("&nbsp;"));
    assertEquals("<", Entities.unescape("&lt;"));
    assertEquals(">", Entities.unescape("&gt;"));
    assertEquals("\"", Entities.unescape("&quot;"));
    
    // Test with unescaped characters
    assertEquals("abcdef", Entities.unescape("abcdef"));
  }
}

