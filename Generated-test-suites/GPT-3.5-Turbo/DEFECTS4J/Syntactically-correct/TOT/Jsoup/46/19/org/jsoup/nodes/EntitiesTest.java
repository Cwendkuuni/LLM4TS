import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {
   
    private static Map<String, Character> full;
    private static Map<Character, String> xhtmlByVal;
    private static Map<String, Character> base;
    private static Map<Character, String> baseByVal;
    private static Map<Character, String> fullByVal;
   
    @BeforeClass
    public static void setUp() {
        full = new HashMap<String, Character>() {{
            put("&amp;", '&');
            put("&lt;", '<');
            put("&gt;", '>');
            put("&quot;", '"');
        }};
      
        xhtmlByVal = new HashMap<Character, String>() {{
            put('&', "amp");
            put('<', "lt");
            put('>', "gt");
            put('"', "quot");
        }};
      
        base = new HashMap<String, Character>() {{
            put("&amp;", '&');
            put("&lt;", '<');
            put("&gt;", '>');
            put("&quot;", '"');
        }};
      
        baseByVal = new HashMap<Character, String>() {{
            put('&', "amp");
            put('<', "lt");
            put('>', "gt");
            put('"', "quot");
        }};
      
        fullByVal = new HashMap<Character, String>() {{
            put('&', "amp");
            put('<', "lt");
            put('>', "gt");
            put('"', "quot");
        }};
    }
   
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("test"));
        assertFalse(Entities.isNamedEntity("&lt;"));
    }
   
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("test"));
        assertFalse(Entities.isBaseNamedEntity("&lt;"));
    }
   
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("test"));
        assertNull(Entities.getCharacterByName("&lt;"));
    }
   
    @Test
    public void testEscape() {
        String escapedString = Entities.escape("hello & world <>");
        assertEquals("hello &amp; world &lt;&gt;", escapedString);
    }
   
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("hello &amp; world &lt;&gt;");
        assertEquals("hello & world <>", unescapedString);
    }
   
    @Test
    public void testEscape_NormaliseWhite() {
        String escapedString = Entities.escape("   hello   &   world   <  >  ", true);
        assertEquals(" hello &amp; world &lt; >", escapedString);
    }
   
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertFalse(Entities.canEncode(CoreCharset.ascii, 'Ä', null));
    }
   
    @Test
    public void testLoadEntities() {
        Map<String, Character> loadedEntities = Entities.loadEntities("entities-full.properties");
        assertEquals(full, loadedEntities);
    }
   
    @Test
    public void testToCharacterKey() {
        Map<Character, String> characterKeyMap = Entities.toCharacterKey(full);
        assertEquals(fullByVal, characterKeyMap);
    }
}