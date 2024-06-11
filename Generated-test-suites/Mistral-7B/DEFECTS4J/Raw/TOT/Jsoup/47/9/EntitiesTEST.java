import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isNamedEntity("amp"));
        assertTrue(entities.isNamedEntity("lt"));
        assertTrue(entities.isNamedEntity("gt"));
        assertTrue(entities.isNamedEntity("quot"));
        assertFalse(entities.isNamedEntity(""));
        assertFalse(entities.isNamedEntity("xxx"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        assertTrue(entities.isBaseNamedEntity("amp"));
        assertTrue(entities.isBaseNamedEntity("lt"));
        assertTrue(entities.isBaseNamedEntity("gt"));
        assertTrue(entities.isBaseNamedEntity("quot"));
        assertFalse(entities.isBaseNamedEntity(""));
        assertFalse(entities.isBaseNamedEntity("xxx"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        assertEquals('&', entities.getCharacterByName("amp"));
        assertEquals('<', entities.getCharacterByName("lt"));
        assertEquals('>', entities.getCharacterByName("gt"));
        assertEquals('"', entities.getCharacterByName("quot"));
        assertNull(entities.getCharacterByName(""));
        assertNull(entities.getCharacterByName("xxx"));
    }

    @Test
    public void testEscapeString() {
        Entities entities = new Entities();
        Document.OutputSettings out = new Document.OutputSettings();

        String input = "&amp;<lt>Hello&quot; World&gt;";
        String expected = "&amp;amp; &lt;lt&gt; Hello&quot; &quot; World&quot; &gt;";
        String output = entities.escape(input, out);
        assertEquals(expected, output);

        input = "&amp;<lt>Hello&quot; World&gt;";
        expected = "&amp;amp; &lt;lt&gt; Hello&quot; World&gt;";
        output = entities.escape(input, out);
        assertEquals(expected, output);

        input = "&amp;<lt>Hello&quot; World&gt;<script>alert('XSS')</script>";
        expected = "&amp;amp; &lt;lt&gt; Hello&quot; World&gt; &lt;script&gt;alert('XSS')&lt;/script&gt;";
        output = entities.escape(input, out);
        assertEquals(expected, output);

        input = "<script>alert('XSS')</script>";
        expected = "&lt;script&gt;alert('XSS')&lt;/script&gt;";
        output = entities.escape(input, out);
        assertEquals(expected, output);

        input = "&amp;<lt>Hello&quot; World&gt;<script>alert('XSS')</script>";
        expected = "&amp;amp; &lt;lt&gt; Hello&quot; World&gt; &lt;script&gt;alert('XSS')&lt;/script&gt;";
        output = entities.escape(input, out);
        assertEquals(expected, output);
    }

    @Test
    public void testUnescapeString() {
        Entities entities = new Entities();

        String input = "&amp;amp; &lt;lt&gt; Hello&quot; World&quot; &gt;";
        String expected = "&amp;<lt>Hello&quot; World&gt;";
        String output = entities.unescape(input);
        assertEquals(expected, output);

        input = "&amp;amp; &lt;lt&gt; Hello&quot; World&quot; &gt;";
        expected = "&amp;<lt>Hello&quot; World&gt;";
        output = entities.unescape(input);
        assertEquals(expected, output);

        input = "&amp;amp; &lt;lt&gt; Hello&quot; World&gt;<script>alert('XSS')</script>";
        expected = "&amp;<lt>Hello&quot; World&gt;<script>alert('XSS')</script>";
        output = entities.unescape(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities entities = new Entities();
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(null);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = null;

        // ASCII character
        assertTrue(entities.canEncode(charset, 'A', encoder));

        // Non-ASCII character
        char c = '\u00C3';
        assertTrue(entities.canEncode(charset, c, encoder));

        // Surrogate character
        char d = '\uD800';
        char e = '\uDC00';
        assertFalse(entities.canEncode(charset, d, encoder));
        assertFalse(entities.canEncode(charset, e, encoder));
        assertFalse(entities.canEncode(charset, d + e, encoder));
    }
}