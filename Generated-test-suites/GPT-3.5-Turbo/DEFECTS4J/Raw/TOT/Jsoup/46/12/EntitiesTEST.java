import org.jsoup.nodes.Entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    // isNamedEntity method tests
    @Test
    public void testIsNamedEntity_ExistingEntity_ReturnsTrue() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntity_NonExistingEntity_ReturnsFalse() {
        assertFalse(Entities.isNamedEntity("xyz"));
    }

    // isBaseNamedEntity method tests
    @Test
    public void testIsBaseNamedEntity_ExistingBaseEntity_ReturnsTrue() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testIsBaseNamedEntity_NonExistingBaseEntity_ReturnsFalse() {
        assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    // getCharacterByName method tests
    @Test
    public void testGetCharacterByName_ExistingEntity_ReturnsCharacter() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }

    @Test
    public void testGetCharacterByName_NonExistingEntity_ReturnsNull() {
        assertNull(Entities.getCharacterByName("xyz"));
    }

    // escape method tests
    @Test
    public void testEscape_NormalString_ReturnsEscapedString() {
        String expected = "&lt;div class=&quot;test&quot;&gt;Hello &amp; World&lt;/div&gt;";
        String actual = Entities.escape("<div class=\"test\">Hello & World</div>", Document.OutputSettings.html());
        assertEquals(expected, actual);
    }

    @Test
    public void testEscape_EmptyString_ReturnsEmptyString() {
        String expected = "";
        String actual = Entities.escape("", Document.OutputSettings.html());
        assertEquals(expected, actual);
    }

    // unescape method tests
    @Test
    public void testUnescape_NormalEscapedString_ReturnsUnescapedString() {
        String expected = "<div class=\"test\">Hello & World</div>";
        String actual = Entities.unescape("&lt;div class=&quot;test&quot;&gt;Hello &amp; World&lt;/div&gt;");
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescape_EmptyString_ReturnsEmptyString() {
        String expected = "";
        String actual = Entities.unescape("");
        assertEquals(expected, actual);
    }

    // unescape with strict parameter tests
    @Test
    public void testUnescape_StrictEscapedString_ReturnsUnescapedString() {
        String expected = "<div class=\"test\">Hello &amp; World</div>";
        String actual = Entities.unescape("&lt;div class=&quot;test&quot;&gt;Hello &amp;amp; World&lt;/div&gt;", true);
        assertEquals(expected, actual);
    }

    @Test
    public void testUnescape_NotStrictEscapedString_ReturnsUnescapedString() {
        String expected = "<div class=\"test\">Hello & World</div>";
        String actual = Entities.unescape("&lt;div class=&quot;test&quot;&gt;Hello &amp;amp; World&lt;/div&gt;", false);
        assertEquals(expected, actual);
    }

    // Additional test scenarios can be added for each method

}