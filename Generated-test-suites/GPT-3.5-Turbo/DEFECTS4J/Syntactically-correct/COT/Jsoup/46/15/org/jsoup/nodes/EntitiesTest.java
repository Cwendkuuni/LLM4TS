
import org.jsoup.nodes.*;

import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {
    private Entities entities;

    @Before
    public void setUp() {
        entities = new Entities();
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        Character ltChar = Entities.getCharacterByName("lt");
        assertEquals(Character.valueOf('<'), ltChar);

        Character ampChar = Entities.getCharacterByName("amp");
        assertEquals(Character.valueOf('&'), ampChar);

        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("Hello <World> &amp;", null);
        assertEquals("Hello &lt;World&gt; &amp;amp;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("Hello &lt;World&gt; &amp;amp;");
        assertEquals("Hello <World> &amp;", unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("Hello &lt;World&gt; &amp;amp;", true);
        assertEquals("Hello <World> &amp;", unescaped);
    }

    @Test
    public void testUnescapeNotStrict() {
        String unescaped = Entities.unescape("Hello &lt;World&gt; &amp;amp;", false);
        assertEquals("Hello <World> &amp;amp;", unescaped);
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntitiesInvalidFile() {
        Entities.loadEntities("invalid-file.properties");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToCharacterKeyDuplicateKeys() {
        Map<String, Character> duplicateKeysMap = Map.of("lt", '<', "LT", '<');
        Entities.toCharacterKey(duplicateKeysMap);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String escaped = Entities.escape("Hello <World> &amp;", outputSettings);
        assertEquals("Hello &lt;World&gt; &amp;", escaped);
    }

    // Add any additional test cases for edge cases and exception handling scenarios here

}

