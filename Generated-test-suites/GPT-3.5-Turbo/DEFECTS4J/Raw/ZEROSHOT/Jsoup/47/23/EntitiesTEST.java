import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt"));
    assertTrue(Entities.isNamedEntity("gt"));
    assertTrue(Entities.isNamedEntity("amp"));
    assertTrue(Entities.isNamedEntity("quot"));
    assertFalse(Entities.isNamedEntity("invalid"));
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt"));
    assertTrue(Entities.isBaseNamedEntity("gt"));
    assertTrue(Entities.isBaseNamedEntity("amp"));
    assertFalse(Entities.isBaseNamedEntity("quot"));
    assertFalse(Entities.isBaseNamedEntity("invalid"));
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
    assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    assertEquals(Character.valueOf(0x00022), Entities.getCharacterByName("quot"));
    assertNull(Entities.getCharacterByName("invalid"));
  }
  
  @Test
  public void testEscape() {
    assertEquals("&lt;&gt;&amp;&quot;", Entities.escape("<>&\""));
  }
  
  @Test
  public void testUnescape() {
    assertEquals("<>&\"", Entities.unescape("&lt;&gt;&amp;&quot;"));
    assertEquals("&#x4E00;", Entities.unescape("&x4E00;"));
  }
}