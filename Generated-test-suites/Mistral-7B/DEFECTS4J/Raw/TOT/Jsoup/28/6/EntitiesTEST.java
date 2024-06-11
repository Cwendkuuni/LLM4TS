import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("ltx"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
    }

    @Test
    public void testEscape() {
        String input = "This is a test &amp; with an ampersand and <lt> a less than sign.";
        String expected = "This is a test &amp;amp; with an ampersand and &lt; a less than sign.";
        assertEquals(expected, Entities.escape(input, Document.OutputSettings.base()));
    }

    @Test
    public void testUnescape() {
        String input = "This is a test &amp;amp; with an ampersand and &lt; a less than sign.";
        String expected = "This is a test &amp; with an ampersand and < a less than sign.";
        assertEquals(expected, Entities.unescape(input));
    }

    
}