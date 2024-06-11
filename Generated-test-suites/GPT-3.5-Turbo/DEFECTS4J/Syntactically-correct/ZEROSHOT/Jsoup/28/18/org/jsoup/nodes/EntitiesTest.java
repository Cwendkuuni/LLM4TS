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
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        String input = "This is a <test> & string.";
        String expected = "This is a &lt;test&gt; &amp; string.";
        assertEquals(expected, Entities.escape(input, Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        String input = "This is a &lt;test&gt; &amp; string.";
        String expected = "This is a <test> & string.";
        assertEquals(expected, Entities.unescape(input));
    }
    
}