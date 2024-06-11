
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foobar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals((Character) '<', Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foobar"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<script>alert('XSS');</script>", Document.OutputSettings.xhtml);
        assertEquals("&lt;script&gt;alert(&apos;XSS&apos;);&lt;/script&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;script&gt;alert(&apos;XSS&apos;);&lt;/script&gt;");
        assertEquals("<script>alert('XSS');</script>", unescaped);
    }

    @Test
    public void testUnescapeStrict() {
        String unescaped = Entities.unescape("&lt;script&gt;alert(&apos;XSS&apos;);&lt;/script&gt;", true);
        assertEquals("<script>alert('XSS');</script>", unescaped);
    }
    
}