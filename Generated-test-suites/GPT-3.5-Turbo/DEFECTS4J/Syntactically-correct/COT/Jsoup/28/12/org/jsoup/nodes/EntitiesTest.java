
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
        assertEquals('>', Entities.getCharacterByName("gt").charValue());
        assertEquals('&', Entities.getCharacterByName("amp").charValue());
        assertEquals('\'', Entities.getCharacterByName("apos").charValue());
        assertEquals('\"', Entities.getCharacterByName("quot").charValue());
        assertNull(Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        String string = "<p>Hello, world!</p>";
        String escaped = Entities.escape(string, Document.OutputSettings.html());
        assertEquals("&lt;p&gt;Hello, world!&lt;/p&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;p&gt;Hello, world!&lt;/p&gt;";
        String unescaped = Entities.unescape(string);
        assertEquals("<p>Hello, world!</p>", unescaped);
    }
    
}
