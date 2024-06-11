import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("x"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("x"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithNull() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithNull() {
        Entities.getCharacterByName(null);
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        String input = "Hello & World! < Greetings >";
        String expectedOutput = "Hello &amp; World! &lt; Greetings &gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        String input = "Hello & World! < Greetings >";
        String expectedOutput = "Hello &amp; World! &lt;Greetings&gt;";
        String output = Entities.escape(input, Entities.EscapeMode.xhtml);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithCustomSettings() {
        String input = "Hello & World! < Greetings >";
        String expectedOutput = "Hello &amp;amp; World! &lt;amp;amp;Greetings&gt;";
        String output = Entities.escape(input, new Entities.EscapeMode("custom", new HashMap<Character, String>() {{
            put('<', "&lt;");
            put('&', "&amp;amp;");
        }}));
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithNullInput() {
        Entities.escape(null, null);
    }

    @Test
    public void testUnescapeMethodWithDefaultSettings() {
        String input = "Hello &amp; World! &lt;Greetings&gt;";
        String expectedOutput = "Hello & World! < Greetings >";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodWithStrictSettings() {
        String input = "Hello &amp; World! &lt;Greetings&gt;";
        String expectedOutput = "Hello & World! <Greetings>";
        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncodeMethod() {
        CoreCharset charset = CoreCharset.ascii;
        char c = 'é';
        boolean canEncode = Entities.canEncode(charset, c, null);
        assertTrue(canEncode);

        charset = CoreCharset.utf;
        canEncode = Entities.canEncode(charset, c, null);
        assertTrue(canEncode);

        charset = CoreCharset.fallback;
        CharsetEncoder encoder = Character.isDefinedChar(c) ? Character. encodingForUnicode(c) : null;
        canEncode = Entities.canEncode(charset, c, encoder);
        assertTrue(canEncode);

        c = '\uD800';
        canEncode = Entities.canEncode(charset, c, encoder);
        assertFalse(canEncode);
    }

    @Test
    public void testLoadEntitiesMethod() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Character lt = entities.get("lt");
        Character amp = entities.get("amp");
        assertEquals('<', lt.charValue());
        assertEquals('&', amp.charValue());
    }

    @Test(expected = IOException.class)
    public void testLoadEntitiesMethodWithMissingFile() {
        Entities.loadEntities("non-existent-file.properties");
    }

    @Test
    public void testToStringMethod() {
        CoreCharset charset = CoreCharset.ascii;
        char c = 'é';
        String encoded = Entities.canEncode(charset, c, null) ? Character.toString(c) : "&#x" + Integer.toHexString(c.codePointAt(0)) + ";";
        assertEquals("&#xe9;", encoded);
    }
}