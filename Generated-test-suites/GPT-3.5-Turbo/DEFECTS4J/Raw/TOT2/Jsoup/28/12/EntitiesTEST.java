import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("test"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("test"));
    }
    
    @Test
    public void testEscape() {
        String input = "Test & <escape>";
        String expected = "Test &amp; &lt;escape&gt;";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings(), Entities.EscapeMode.xhtml));
        
        String input2 = "Test €";
        String expected2 = "Test &#8364;";
        assertEquals(expected2, Entities.escape(input2, new Document.OutputSettings(), Entities.EscapeMode.xhtml));
    }
    
    @Test
    public void testUnescape() {
        String input = "Test &amp; &lt;escape&gt;";
        String expected = "Test & <escape>";
        assertEquals(expected, Entities.unescape(input));
        
        String input2 = "Test &#8364;";
        String expected2 = "Test €";
        assertEquals(expected2, Entities.unescape(input2));
    }
}