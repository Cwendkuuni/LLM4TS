import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EntitiesTest {

    private Entities.EscapeMode escapeMode;
    private Map<Character, String> escapeMap;

    @Before
    public void setUp() {
        escapeMode = Entities.EscapeMode.xhtml;
        escapeMap = escapeMode.getMap();
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt")); // typical use case
        assertTrue(Entities.isNamedEntity("gt")); // typical use case
        assertTrue(Entities.isNamedEntity("amp")); // typical use case
        assertTrue(Entities.isNamedEntity("apos")); // typical use case
        assertTrue(Entities.isNamedEntity("quot")); // typical use case
        assertFalse(Entities.isNamedEntity("invalid")); // error scenario
        assertFalse(Entities.isNamedEntity("abc")); // error scenario
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt")); // typical use case
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt")); // typical use case
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp")); // typical use case
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos")); // typical use case
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot")); // typical use case
        assertNull(Entities.getCharacterByName("invalid")); // error scenario
        assertNull(Entities.getCharacterByName("abc")); // error scenario
    }

    @Test
    public void testEscape() {
        String input = "<>&'\"";
        String expectedOutput = "&lt;&gt;&amp;&apos;&quot;";

        String output = Entities.escape(input, escapeMode);
        assertEquals(expectedOutput, output); // typical use case

        input = "abc123";
        expectedOutput = "abc123";
        output = Entities.escape(input, escapeMode);
        assertEquals(expectedOutput, output); // typical use case

        input = "\uD83D\uDE03";
        expectedOutput = "\uD83D\uDE03";
        output = Entities.escape(input, escapeMode);
        assertEquals(expectedOutput, output); // edge case
    }

    @Test
    public void testUnescape() {
        String input = "&lt;&gt;&amp;&apos;&quot;";
        String expectedOutput = "<>&'\"";

        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output); // typical use case

        input = "abc123";
        expectedOutput = "abc123";
        output = Entities.unescape(input);
        assertEquals(expectedOutput, output); // typical use case

        input = "&#128515;";
        expectedOutput = "\uD83D\uDE03";
        output = Entities.unescape(input);
        assertEquals(expectedOutput, output); // edge case

        input = "&invalid;";
        expectedOutput = "&invalid;";
        output = Entities.unescape(input);
        assertEquals(expectedOutput, output); // error scenario

        input = "&ampinvalid;";
        expectedOutput = "&ampinvalid;";
        output = Entities.unescape(input);
        assertEquals(expectedOutput, output); // error scenario
    }

    @Test
    public void testUnescapeStrict() {
        String input = "&lt;&gt;&amp;&apos;&quot;";
        String expectedOutput = "<>&'\"";

        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output); // typical use case

        input = "abc123";
        expectedOutput = "abc123";
        output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output); // typical use case

        input = "&#128515;";
        expectedOutput = "\uD83D\uDE03";
        output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output); // edge case

        input = "&invalid;";
        expectedOutput = "&invalid;";
        output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output); // error scenario

        input = "&ampinvalid;";
        expectedOutput = "&ampinvalid;";
        output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output); // error scenario
    }
}