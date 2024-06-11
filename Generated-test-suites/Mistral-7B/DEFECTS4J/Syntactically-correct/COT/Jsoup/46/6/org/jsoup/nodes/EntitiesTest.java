import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(isNamedEntity("lt"));
        assertTrue(isNamedEntity("amp"));
        assertFalse(isNamedEntity("invalidEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(isBaseNamedEntity("lt"));
        assertTrue(isBaseNamedEntity("amp"));
        assertFalse(isBaseNamedEntity("invalidEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', getCharacterByName("lt"));
        assertEquals('&', getCharacterByName("amp"));
        assertNull(getCharacterByName("invalidEntity"));
    }

    @Test
    public void testEscape() {
        StringBuilder actual = new StringBuilder();
        String input = "&lt;<>&amp;\"'<>&gt;<space>  &nbsp; &amp;#x1F601;";
        EscapeMode escapeMode = EscapeMode.base;
        escape(actual, input, escapeMode.getMap(), false, false, false);

        String expected = "&amp;lt;<&amp;gt;&amp;amp;#x26;&amp;quot;'&amp;lt;>&amp;gt; &amp;#xA0; &amp;#x26;#x1F601;";
        assertEquals(expected, actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithNullInput() {
        StringBuilder actual = new StringBuilder();
        String input = null;
        EscapeMode escapeMode = EscapeMode.base;
        escape(actual, input, escapeMode.getMap(), false, false, false);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;<&amp;gt;&amp;amp;#x26;&amp;quot;'&amp;lt;>&amp;gt; &amp;#xA0; &amp;#x26;#x1F601;";
        String expected = "&lt;<&gt;amp;&quot;'<&gt; > &#xA0; &amp;#x1F601;";
        assertEquals(expected, unescape(input));
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.ascii;
        char c = 'À';
        assertTrue(canEncode(coreCharset, c, null));

        coreCharset = CoreCharset.fallback;
        c = '∞';
        assertFalse(canEncode(coreCharset, c, null));
    }
}