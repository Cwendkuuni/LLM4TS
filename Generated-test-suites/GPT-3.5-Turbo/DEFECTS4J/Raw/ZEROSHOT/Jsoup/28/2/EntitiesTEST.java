import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
    }

    @Test
    public void testEscape() {
        assertEquals("Hello &lt; World", Entities.escape("Hello < World", Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        assertEquals("Hello < World", Entities.unescape("Hello &lt; World"));
    }
    
    // Add tests for other methods in the Entities class
    
}