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
        String input = "<div class=\"test\">Hello World</div>";
        String expectedOutput = "&lt;div class=&quot;test&quot;&gt;Hello World&lt;/div&gt;";
        String output = Entities.escape(input, Document.OutputSettings.html());

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div class=&quot;test&quot;&gt;Hello World&lt;/div&gt;";
        String expectedOutput = "<div class=\"test\">Hello World</div>";
        String output = Entities.unescape(input);

        assertEquals(expectedOutput, output);
    }
}