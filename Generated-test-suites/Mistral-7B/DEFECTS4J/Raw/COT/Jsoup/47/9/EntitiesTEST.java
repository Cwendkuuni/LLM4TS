import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(isNamedEntity("lt"));
        assertFalse(isNamedEntity("gt")); // this should fail as 'gt' is a base entity
        assertFalse(isNamedEntity("amp")); // this should pass as 'amp' is a base entity
        assertFalse(isNamedEntity("quot")); // this should pass as 'quot' is a base entity
        // test with unknown entities
        assertFalse(isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(isBaseNamedEntity("lt"));
        assertTrue(isBaseNamedEntity("gt"));
        assertTrue(isBaseNamedEntity("amp"));
        assertTrue(isBaseNamedEntity("quot"));
        // test with non-base entities
        assertFalse(isBaseNamedEntity("nbsp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));
        assertEquals('>', getCharacterByName("gt"));
        assertEquals('"', getCharacterByName("quot"));
        // test with unknown entities
        assertNull(getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>Hello &quot;World&quot; &gt;<!DOCTYPE html>";
        String expectedOutput = "&amp;amp; &amp;lt; &amp;lt;Hello &amp;quot; &amp;quot;World&amp;quot; &amp;gt; &amp;gt;&lt;!DOCTYPE html&gt;";
        String output = escape(input, null);
        assertEquals(expectedOutput, output);

        String inputWithSurrogatePair = "\ud800\udc00";
        String outputWithSurrogatePair = "&#x10000;";
        String expectedOutputWithSurrogatePair = escape(inputWithSurrogatePair, null, null, false, false, false).toString();
        assertEquals(outputWithSurrogatePair, expectedOutputWithSurrogatePair);
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeWithNullString() {
        escape(null, null, null, false, false, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithEmptyString() {
        escape("", null, null, false, false, false);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.ascii;
        char c = '€'; // euro symbol
        boolean canEncode = canEncode(coreCharset, c, null);
        assertFalse(canEncode);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;Hello &amp;quot;World&amp;quot; &amp;gt;<!DOCTYPE html>";
        String output = unescape(input);
        assertEquals("&lt;Hello \"World\"&gt;<!DOCTYPE html>", output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeWithNullString() {
        unescape(null);
    }
}