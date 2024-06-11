import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("dummy"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("dummy"));
    }

    @Test
    public void testEscape() {
        String string = "This is a <test> string &amp;";
        String escapedString = Entities.escape(string, Document.OutputSettings.base);
        assertEquals("This is a &lt;test&gt; string &amp;amp;", escapedString);
    }

    @Test
    public void testUnescape() {
        String string = "This is a &lt;test&gt; string &amp;amp;";
        String unescapedString = Entities.unescape(string);
        assertEquals("This is a <test> string &amp;", unescapedString);
    }
}