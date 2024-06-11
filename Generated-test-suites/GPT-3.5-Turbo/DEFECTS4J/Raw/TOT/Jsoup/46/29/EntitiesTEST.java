import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Before
    public void setUp() throws Exception {
        // initialization code before each test
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertTrue(Entities.isNamedEntity("gt")); // typical use case
        assertFalse(Entities.isNamedEntity("invalid")); // error scenario
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt")); // typical use case
        assertTrue(Entities.isBaseNamedEntity("gt")); // typical use case
        assertFalse(Entities.isBaseNamedEntity("invalid")); // error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt")); // typical use case
        assertNull(Entities.getCharacterByName("invalid")); // error scenario
    }

    @Test
    public void testEscape() {
        String escapedString = Entities.escape("Hello <world> &amp; friends!", Document.OutputSettings.html());
        assertEquals("Hello &lt;world&gt; &amp;amp; friends!", escapedString); // typical use case
        
        escapedString = Entities.escape("Hello & world!", Document.OutputSettings.xhtml());
        assertEquals("Hello &amp; world!", escapedString); // typical use case
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("Hello &lt;world&gt; &amp;amp; friends!");
        assertEquals("Hello <world> &amp; friends!", unescapedString); // typical use case
        
        unescapedString = Entities.unescape("Hello & world!");
        assertEquals("Hello & world!", unescapedString); // typical use case
    }

    @Test
    public void testEscapeSpecialCharacters() {
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, "<html>\n<title>Test</title>\n<body>Testing &amp; Entity &lt; &gt;</body>\n</html>", Document.OutputSettings.html(), true, true, true);
        assertEquals("&lt;html&gt;\n&lt;title&gt;Test&lt;/title&gt;\n&lt;body&gt;Testing &amp;amp; Entity &amp;lt; &amp;gt;&lt;/body&gt;\n&lt;/html&gt;", accumulator.toString()); // typical use case
    }

    @Test
    public void testEscapeNullString() {
        String escapedString = Entities.escape(null, Document.OutputSettings.html());
        assertNull(escapedString); // error scenario
    }

    @Test
    public void testEscapeEmptyString() {
        String escapedString = Entities.escape("", Document.OutputSettings.html());
        assertEquals("", escapedString); // typical use case
    }

    @Test
    public void testUnescapeNullString() {
        String unescapedString = Entities.unescape(null);
        assertNull(unescapedString); // error scenario
    }

    @Test
    public void testUnescapeEmptyString() {
        String unescapedString = Entities.unescape("");
        assertEquals("", unescapedString); // typical use case
    }

    @Test
    public void testCanEncode() {
        boolean canEncode = Entities.canEncode(Entities.CoreCharset.ascii, 'a', null);
        assertTrue(canEncode); // typical use case

        canEncode = Entities.canEncode(Entities.CoreCharset.ascii, '\u0260', null);
        assertFalse(canEncode); // edge case

        canEncode = Entities.canEncode(Entities.CoreCharset.ascii, 'ü', null);
        assertTrue(canEncode); // edge case

        canEncode = Entities.canEncode(Entities.CoreCharset.utf, '\u0131', null);
        assertTrue(canEncode); // typical use case

        canEncode = Entities.canEncode(Entities.CoreCharset.utf, '\u0000', null);
        assertTrue(canEncode); // typical use case

        canEncode = Entities.canEncode(Entities.CoreCharset.fallback, '\u0000', null);
        assertTrue(canEncode); // typical use case
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertNotNull(entities); // typical use case
        assertFalse(entities.isEmpty()); // typical use case
        assertEquals(2165, entities.size()); // typical use case

        entities = Entities.loadEntities("entities-full.properties");
        assertNotNull(entities); // typical use case
        assertFalse(entities.isEmpty()); // typical use case
        assertEquals(2273, entities.size()); // typical use case

        entities = Entities.loadEntities("invalid-file.properties");
        assertNull(entities); // error scenario
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = Entities.loadEntities("entities-full.properties");
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(inMap.size(), outMap.size()); // typical use case

        for (Map.Entry<String, Character> entry : inMap.entrySet()) {
            Character character = entry.getValue();
            String name = entry.getKey();

            assertTrue(outMap.containsKey(character)); // typical use case

            if (outMap.get(character).equals(name.toLowerCase())) {
                assertTrue(name.toLowerCase().equals(name)); // typical use case
            }
        }
    }

}