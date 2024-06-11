import org.jsoup.nodes.Entities;
import org.junit.*;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

  @Test
  public void testIsNamedEntity() {
    assertTrue(Entities.isNamedEntity("lt")); // typical use case
    assertTrue(Entities.isNamedEntity("amp")); // typical use case
    assertFalse(Entities.isNamedEntity("hello")); // unknown entity
  }

  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
    assertFalse(Entities.isBaseNamedEntity("gt")); // entity not in base set
  }

  @Test
  public void testGetCharacterByName() {
    Character character = Entities.getCharacterByName("lt"); // typical use case
    assertNotNull(character);
    assertEquals('<', character.charValue());

    character = Entities.getCharacterByName("hello"); // unknown entity
    assertNull(character);
  }

  @Test
  public void testEscape() {
    String escaped = Entities.escape("Hello <world> & goodbye", null); // typical use case
    assertEquals("Hello &lt;world&gt; &amp; goodbye", escaped);
  }

  @Test
  public void testEscapeInAttribute() {
    // Simulating escape in attribute where ">" is not escaped
    StringBuilder accum = new StringBuilder();
    Entities.escape(accum, "Hello <world> & goodbye", null, true, false, false);
    String escaped = accum.toString();
    assertEquals("Hello <world> &amp; goodbye", escaped);
  }

  @Test
  public void testUnescape() {
    String unescaped = Entities.unescape("Hello &lt;world&gt; &amp; goodbye"); // typical use case
    assertEquals("Hello <world> & goodbye", unescaped);
  }

  @Test
  public void testUnescapeStrict() {
    String unescaped = Entities.unescape("Hello &lt;world&gt; &amp; goodbye;", true); // trailing ';' required
    assertEquals("Hello <world> & goodbye;", unescaped);

    unescaped = Entities.unescape("Hello &lt;world&gt; &amp; goodbye"); // trailing ';' optional
    assertEquals("Hello <world> & goodbye", unescaped);
  }

  @Test
  public void testEscapeModeGetMap() {
    Map<Character, String> map = Entities.EscapeMode.xhtml.getMap(); // typical use case
    assertNotNull(map);
    assertEquals("&lt;", map.get('<'));
    assertEquals("&gt;", map.get('>'));
    assertEquals("&amp;", map.get('&'));
    assertEquals("&quot;", map.get('"'));
  }

  @Test
  public void testLoadEntities() {
    // Simulating loading of entities properties file
    Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
    assertNotNull(entities);
    assertEquals(Character.valueOf('<'), entities.get("lt"));
    assertEquals(Character.valueOf('>'), entities.get("gt"));
    assertEquals(Character.valueOf('&'), entities.get("amp"));
    assertEquals(Character.valueOf('"'), entities.get("quot"));
  }

  @Test
  public void testToCharacterKey() {
    // Simulating converting entities map to character key map
    Map<Character, String> characterKeyMap = Entities.toCharacterKey(Entities.base);
    assertNotNull(characterKeyMap);
    assertEquals("lt", characterKeyMap.get('<'));
    assertEquals("gt", characterKeyMap.get('>'));
    assertEquals("amp", characterKeyMap.get('&'));
    assertEquals("quot", characterKeyMap.get('"'));
  }

}