import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("not_an_entity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("not_an_entity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("not_an_entity"));
    }

    @Test
    public void testEscape() {
        String input = "<div>Test</div>";
        String expectedOutput = "&lt;div&gt;Test&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Test&lt;/div&gt;";
        String expectedOutput = "<div>Test</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;div&gt;";
        String expectedOutput = "<div>";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
}