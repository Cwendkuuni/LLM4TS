import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue("amp is a named entity", Entities.isNamedEntity("amp"));
        assertFalse("lt is not a named entity", Entities.isNamedEntity("lt"));
        assertFalse("gt is not a named entity", Entities.isNamedEntity("gt"));
        assertTrue("lt; is a named entity", Entities.isNamedEntity("lt;"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue("quot is a base named entity", Entities.isBaseNamedEntity("quot"));
        assertFalse("amp is not a base named entity", Entities.isBaseNamedEntity("amp"));
        assertFalse("lt is not a base named entity", Entities.isBaseNamedEntity("lt"));
        assertTrue("gt is a base named entity", Entities.isBaseNamedEntity("gt"));
        assertTrue("gt; is a base named entity", Entities.isBaseNamedEntity("gt;"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals("Character value of amp is &", Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals("Character value of lt is <", Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals("Character value of gt is >", Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull("Invalid named entity returns null", Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        assertEquals("Escaped string for & is &amp;", "&amp;", Entities.escape("&", Document.OutputSettings.xhtml()));
        assertEquals("Escaped string for < is &lt;", "&lt;", Entities.escape("<", Document.OutputSettings.xhtml()));
        assertEquals("Escaped string for > is &gt;", "&gt;", Entities.escape(">", Document.OutputSettings.xhtml()));
        assertEquals("Escaped string for \" is &quot;", "&quot;", Entities.escape("\"", Document.OutputSettings.xhtml()));
        assertEquals("Escaped string for &nbsp; is \\u00A0", "\\u00A0", Entities.escape("\u00A0", Document.OutputSettings.xhtml()));
        assertEquals("Escaped string for non-ascii character is &#x1F4A9;", "&#x1F4A9;", Entities.escape("\uD83D\uDCA9", Document.OutputSettings.xhtml()));
    }

    @Test
    public void testUnescape() {
        assertEquals("Unescaped string for &amp; is &", "&", Entities.unescape("&amp;"));
        assertEquals("Unescaped string for &lt; is <", "<", Entities.unescape("&lt;"));
        assertEquals("Unescaped string for &gt; is >", ">", Entities.unescape("&gt;"));
        assertEquals("Unescaped string for &quot; is \"", "\"", Entities.unescape("&quot;"));
        assertEquals("Unescaped string for &#x1F4A9; is non-ascii character", "\uD83D\uDCA9", Entities.unescape("&#x1F4A9;"));
    }
}