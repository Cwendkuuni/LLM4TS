package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

    // Check if the static factory methods return the expected EscapeMode
    @Test
    public void testGetEscapeModes() {
        assertEquals(Entities.EscapeMode.xhtml, Entities.EscapeMode.xhtml);
        assertEquals(Entities.EscapeMode.base, Entities.EscapeMode.base);
        assertEquals(Entities.EscapeMode.extended, Entities.EscapeMode.extended);
    }

    // Test isNamedEntity method
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("ABC"));
    }

    // Test isBaseNamedEntity method
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("copyright"));
    }

    // Test getCharacterByName method
    @Test
    public void testGetCharacterByName() {
        assertEquals( '<', Entities.getCharacterByName("lt") );
        assertEquals( (char) 0xA0, Entities.getCharacterByName("nbsp") );
    }

    // Test escape method with default settings
    @Test
    public void testEscape() {
        String string = "&lt; &gt; &amp; &quot; &apos; <nbsp>'; &auml; &copy; &iexcl; &oelig; &uacute;";
        String expected = "&amp;lt;&amp;gt;&amp;amp;&amp;quot;&amp;apos;<span style='white-space:pre'> &nbsp;</span>&#196;&#169;&#233;&#195;&#174;";
        String result = Entities.escape(string, null);
        assertEquals(expected, result);
    }

    // Test escape method with XHTML settings
    @Test
    public void testEscapeXHTML() {
        String string = "&lt; &gt; &amp; &quot; &apos; <nbsp>&auml; &copy; &iexcl; &oelig; &uacute;";
        String expected = "&lt; &gt; &amp; &quot; &apos; &nbsp; &#196; &copy; &#160; &#233; &#195; &#174;";
        String result = Entities.escape(string, Entities.EscapeMode.xhtml);
        assertEquals(expected, result);
    }

    // Test unescape method with default settings
    @Test
    public void testUnescape() {
        String string = "&amp;lt;&amp;gt;&amp;amp;&amp;quot;&amp;apos;<span style='white-space:pre'> &nbsp;</span>&#196;&#169;&#233;&#195;&#174;";
        String expected = "&lt; &gt; &amp; &quot; &apos; <nbsp>&auml; &copy; &iexcl; &oelig; &uacute;";
        String result = Entities.unescape(string, false);
        assertEquals(expected, result);
    }

    // Test canEncode method
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a'));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '€'));
    }

    // Test map loading from properties file
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = new HashMap<String, Character>();
        entities.putAll(Entities.loadEntities("entities-test.properties"));

        Character c = entities.get("lt");
        assertEquals( '<', c );
        assertEquals( null, entities.get("ABC") );
    }

    // Test edge cases: empty string, null
    @Test(expected = NullPointerException.class)
    public void testEscapeNullString() {
        Entities.escape(null, null);
    }

    @Test
    public void testEscapeEmptyString() {
        assertEquals("", Entities.escape("", null));
    }

    // Test escape method with surrogate pairs
    @Test
    public void testEscapeSurrogatePairs() {
        String string = "\uD840\uDC00\uD840\uDC01";
        String expected = "&#x10000;&#x10001;";
        String result = Entities.escape(string, null);
        assertEquals(expected, result);
    }

    // Test edge cases: invalid character encoding
    @Test(expected = IllegalArgumentException.class)
    public void testEscapeInvalidCharacterEncoding() {
        String string = "abcd";
        Entities.escape(string, new CharsetEncoder() {
            @Override
            public boolean canEncode(CharSequence cs) {
                throw new UnsupportedOperationException("This should not be called");
            }

            @Override
            public CharSequence encode(CharSequence cs) {
                throw new UnsupportedOperationException("This should not be called");
            }
        });
    }
}