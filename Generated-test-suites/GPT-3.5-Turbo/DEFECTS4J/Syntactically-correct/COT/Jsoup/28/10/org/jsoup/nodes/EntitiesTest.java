
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String string = "<div>Test</div>";
        String escapedString = "&lt;div&gt;Test&lt;/div&gt;";
        assertEquals(escapedString, Entities.escape(string, Document.OutputSettings.xhtml));
    }

    @Test
    public void testUnescape() {
        String string = "&lt;div&gt;Test&lt;/div&gt;";
        String unescapedString = "<div>Test</div>";
        assertEquals(unescapedString, Entities.unescape(string));
    }
}
