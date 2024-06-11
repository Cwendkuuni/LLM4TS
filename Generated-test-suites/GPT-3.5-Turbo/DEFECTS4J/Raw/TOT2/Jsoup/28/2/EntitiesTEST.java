import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;&amp;&apos;&quot;", Entities.escape("<>&'\""));
    }

    @Test
    public void testUnescape() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;"));
    }

    @Test
    public void testUnescapeStrict() {
        assertEquals("<>&'\"", Entities.unescape("&lt;&gt;&amp;&apos;&quot;", true));
    }
}