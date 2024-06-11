import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("'"+value+"' should be a named entity", Entities.isNamedEntity(value));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> base = Entities.base;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertTrue("'"+value+"' should be a base named entity", Entities.isBaseNamedEntity(value));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> full = Entities.full;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            Character expected = entry.getValue();
            Character actual = Entities.getCharacterByName(entry.getValue().toString());
            assertEquals("Expected '"+expected+"' but got '"+actual+"'", expected, actual);
        }
    }

    @Test
    public void testEscapeMethodWithDefaultOutputSettings() {
        String input = "This is a test & with & an ampersand and < angle bracket >.";
        String expectedOutput = "This is a test &amp; an ampersand and &lt; angle bracket &gt;.";
        String actualOutput = Entities.escape(input, new Document.OutputSettings());
        assertEquals("Unexpected output from escape method", expectedOutput, actualOutput);
    }

    @Test
    public void testEscapeMethodWithXHTMLOutputSettings() {
        String input = "This is a test & with & an ampersand and < angle bracket >.";
        String expectedOutput = "This is a test &amp; an ampersand and &lt; angle bracket &gt;.";
        String actualOutput = Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.xhtml));
        assertEquals("Unexpected output from escape method with XHTML settings", expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithInvalidName() {
        assertFalse("Invalid named entity should return false", Entities.isNamedEntity("invalid"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameWithInvalidName() {
        try {
            Entities.getCharacterByName("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCanEncode() {
        for (Entities.CoreCharset coreCharset : Entities.CoreCharset.values()) {
            for (char c = 0; c < Character.MAX_VALUE; c++) {
                boolean canEncode = Entities.canEncode(coreCharset, c, null);
                switch (coreCharset) {
                    case ascii:
                        assertTrue("ASCII should encode character '"+Character.toString(c)+"'", canEncode);
                        break;
                    case utf:
                        // UTF-8 only tests as it's the most common encoder
                        if (Character.isISO8859_1(c)) {
                            assertTrue("ISO-8859-1 should encode character '"+Character.toString(c)+"'", canEncode);
                        } else {
                            assertTrue("UTF should be able to encode any character", canEncode);
                        }
                        break;
                    case fallback:
                        // fallback encoder is not specified so test fails
                        assertFalse("Fallback encoder should not be able to encode character '"+Character.toString(c)+"'", canEncode);
                        break;
                }
            }
        }
    }

}