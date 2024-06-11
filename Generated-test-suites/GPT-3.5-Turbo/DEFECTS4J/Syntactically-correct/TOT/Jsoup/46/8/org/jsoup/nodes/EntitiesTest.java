import org.jsoup.nodes.Entities;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        //Typical use case: Valid named entity
        assertTrue(Entities.isNamedEntity("lt"));

        //Edge case: Invalid named entity
        assertFalse(Entities.isNamedEntity("abc"));

        //Error scenario: Null input
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        //Typical use case: Valid base named entity
        assertTrue(Entities.isBaseNamedEntity("quote"));

        //Edge case: Invalid base named entity
        assertFalse(Entities.isBaseNamedEntity("xyz"));

        //Error scenario: Null input
        assertFalse(Entities.isBaseNamedEntity(null));
    }

    @Test
    public void testGetCharacterByName() {
        //Typical use case: Valid named entity
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));

        //Edge case: Invalid named entity
        assertNull(Entities.getCharacterByName("xyz"));

        //Error scenario: Null input
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        //Typical use case: String with special characters
        String input = "<strong> Hello &amp; Bye </strong>";
        String expectedOutput = "&lt;strong&gt; Hello &amp;amp; Bye &lt;/strong&gt;";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));

        //Edge case: Empty string
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));

        //Error scenario: Null input
        assertNull(Entities.escape(null, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        //Typical use case: String with escaped characters
        String input = "&lt;strong&gt; Hello &amp;amp; Bye &lt;/strong&gt;";
        String expectedOutput = "<strong> Hello &amp; Bye </strong>";
        assertEquals(expectedOutput, Entities.unescape(input));

        //Edge case: Empty string
        input = "";
        expectedOutput = "";
        assertEquals(expectedOutput, Entities.unescape(input));

        //Error scenario: Null input
        assertNull(Entities.unescape(null));
    }
}