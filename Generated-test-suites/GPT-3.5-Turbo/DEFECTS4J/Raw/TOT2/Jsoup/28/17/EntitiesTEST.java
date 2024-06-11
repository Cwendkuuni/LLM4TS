import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertFalse(Entities.isNamedEntity("unknown")); // edge case
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertNull(Entities.getCharacterByName("unknown")); // edge case
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\"")); // typical use case
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;")); // typical use case
    }

    @Test
    public void testEscapeWithCharsetEncoder() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\"", null, Entities.EscapeMode.xhtml)); // typical use case
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<>&'", Entities.unescape("&lt;&gt;&amp;&apos;", true)); // typical use case
    }

    @Test
    public void testUnescapeNonStrict() {
        assertEquals("<>&'", Entities.unescape("&lt;&gt;&amp;&apos;")); // typical use case
    }
}