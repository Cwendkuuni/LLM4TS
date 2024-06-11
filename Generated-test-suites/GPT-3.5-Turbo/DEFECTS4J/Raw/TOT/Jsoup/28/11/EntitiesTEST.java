import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Before
    public void setUp() {
        // Set up any necessary initialization
    }

    @Test
    public void testIsNamedEntity() {
        // Test when the input is a known named entity
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));

        // Test when the input is not a known named entity
        assertFalse(Entities.isNamedEntity("foo"));
        assertFalse(Entities.isNamedEntity("123"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test when the input is a known named entity
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));

        // Test when the input is not a known named entity
        assertNull(Entities.getCharacterByName("foo"));
        assertNull(Entities.getCharacterByName("123"));
    }

    @Test
    public void testEscape() {
        // Test when the input doesn't contain any special characters
        String result1 = Entities.escape("abc", null, Entities.EscapeMode.base);
        assertEquals("abc", result1);

        // Test when the input contains special characters that need to be escaped
        String result2 = Entities.escape("<>&'\"", null, Entities.EscapeMode.xhtml);
        assertEquals("&lt;&gt;&amp;&apos;&quot;", result2);

        // Test when the input contains characters that can be encoded as such
        String result3 = Entities.escape("abc123", null, Entities.EscapeMode.xhtml);
        assertEquals("abc123", result3);
    }

    @Test
    public void testUnescape() {
        // Test when the input doesn't contain any entities
        String result1 = Entities.unescape("abc");
        assertEquals("abc", result1);

        // Test when the input contains entities that need to be unescaped (strict mode)
        String result2 = Entities.unescape("&lt;&gt;&amp;&apos;&quot;", true);
        assertEquals("<>&'\"", result2);

        // Test when the input contains entities that need to be unescaped (non-strict mode)
        String result3 = Entities.unescape("&lt;&gt;&amp;&apos;&quot;", false);
        assertEquals("<>&'\"", result3);

        // Test when the input contains entities and other characters
        String result4 = Entities.unescape("This is &lt;&gt; &amp; a test");
        assertEquals("This is <>& a test", result4);
    }

    @Test
    public void testLoadEntities() {
        // Test loading the base entity set
        Map<String, Character> base = Entities.loadEntities("entities-base.properties");
        assertNotNull(base);
        assertEquals(5, base.size());
        assertTrue(base.containsKey("amp"));
        assertTrue(base.containsKey("lt"));
        assertTrue(base.containsKey("gt"));
        assertTrue(base.containsKey("quot"));
        assertTrue(base.containsKey("apos"));

        // Test loading the full entity set
        Map<String, Character> full = Entities.loadEntities("entities-full.properties");
        assertNotNull(full);
        assertTrue(full.size() > 5);  // Ensure full set is larger than the base set
    }

    @Test
    public void testToCharacterKey() {
        // Test converting base entity set to character key map
        Map<String, Character> base = Entities.loadEntities("entities-base.properties");
        Map<Character, String> baseByVal = Entities.toCharacterKey(base);
        assertNotNull(baseByVal);
        assertEquals(5, baseByVal.size());
        assertTrue(baseByVal.containsKey('<'));
        assertTrue(baseByVal.containsKey('>'));
        assertTrue(baseByVal.containsKey('&'));
        assertTrue(baseByVal.containsKey('\''));
        assertTrue(baseByVal.containsKey('"'));

        // Test converting full entity set to character key map
        Map<String, Character> full = Entities.loadEntities("entities-full.properties");
        Map<Character, String> fullByVal = Entities.toCharacterKey(full);
        assertNotNull(fullByVal);
        assertTrue(fullByVal.size() > 5);  // Ensure fullByVal map is larger than the baseByVal map
    }
}