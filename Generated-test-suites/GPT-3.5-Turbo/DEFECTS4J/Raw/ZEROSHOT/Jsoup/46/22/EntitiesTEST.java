import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        assertFalse(Entities.isNamedEntity("invalid"));
        assertFalse(Entities.isNamedEntity("foobar"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));

        assertFalse(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
        assertFalse(Entities.isBaseNamedEntity("foobar"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));

        assertNull(Entities.getCharacterByName("invalid"));
        assertNull(Entities.getCharacterByName("foobar"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<>&\"", Document.OutputSettings.base());
        assertEquals("&lt;&gt;&amp;&quot;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;&gt;&amp;&quot;");
        assertEquals("<>&\"", unescaped);
    }
}