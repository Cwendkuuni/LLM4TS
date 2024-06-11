import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String input = "This is a <test> string";
        String expectedOutput = "This is a &lt;test&gt; string";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String input = "This is a &lt;test&gt; string";
        String expectedOutput = "This is a <test> string";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

    @Test
    public void testUnescapeStrict() {
        String input = "This is a &lt;test&gt; string";
        String expectedOutput = "This is a &lt;test&gt; string";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

}