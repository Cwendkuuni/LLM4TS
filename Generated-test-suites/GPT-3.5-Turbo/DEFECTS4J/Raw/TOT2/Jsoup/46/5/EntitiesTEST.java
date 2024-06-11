import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }
    
    @Test
    public void testEscape() {
        String escapedString = Entities.escape("This & that < and >", new Document.OutputSettings());
        assertEquals("This &amp; that &lt; and &gt;", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("This &amp; that &lt; and &gt;");
        assertEquals("This & that < and >", unescapedString);
    }
    
    // Add more test cases for edge cases and exception scenarios if necessary
    
}