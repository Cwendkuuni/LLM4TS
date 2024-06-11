
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String string = "<>&'\"";
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape(string, Entities.EscapeMode.xhtml));
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape(string, Entities.EscapeMode.base));
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape(string, Entities.EscapeMode.extended));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;&gt;&amp;&apos;&quot;";
        assertEquals("<>&'\"", Entities.unescape(string));
    }

    @Test
    public void testUnescapeWithStrict() {
        String string = "&lt;&gt;&amp;&apos;&quot;;";
        assertEquals("<>&'\";", Entities.unescape(string, true));
    }
}
