import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("gt")); // GT is not a base named entity
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("zzz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("zzz"));
    }

    @Test
    public void testGetCharacterByName() {
        Character c = Entities.getCharacterByName("lt");
        assertEquals('<', c.charValue());
        c = Entities.getCharacterByName("gt");
        assertEquals('>', c.charValue());
        c = Entities.getCharacterByName("amp");
        assertEquals('&', c.charValue());
        c = Entities.getCharacterByName("quot");
        assertEquals('"', c.charValue());
        c = Entities.getCharacterByName("zzz");
        assertNull(c);
    }

    @Test
    public void testEscape() {
        StringBuilder expected = new StringBuilder();
        String input = "&lt;<&gt; &quot; &amp; &apos; &nbsp; &amp;#123; &amp;#x1F601;";
        Entities.escape(expected, input, null, false, false, false);
        String result = escape(input, null);
        assertEquals(expected.toString(), result);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;<&gt; &quot; &amp; &apos; &nbsp; &amp;#123; &amp;#x1F601;";
        String expected = "<<>";
        String result = Entities.unescape(input, false);
        assertEquals(expected, result);
    }

    @Test
    public void testUnescapeWithStrict() {
        String input = "&lt;<&gt; &quot; &amp; &apos; &nbsp; &amp;#123; &amp;#x1F601;";
        String expected = "<&lt;>&quot; &amp; &apos; &nbsp; &#123; &#x1F601;";
        String result = Entities.unescape(input, true);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullString() {
        Entities.unescape(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullMap() {
        Entities entities = new Entities();
        try {
            entities.unescape(null, false);
        } catch (NullPointerException e) {
            throw new AssertionError("Expected IllegalArgumentException, but got " + e);
        }
        Entities.unescape(entities.baseByVal, null);
    }
}