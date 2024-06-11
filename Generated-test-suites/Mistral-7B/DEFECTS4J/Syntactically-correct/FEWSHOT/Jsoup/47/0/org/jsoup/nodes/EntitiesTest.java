package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Character c : Entities.full.values()) {
            assertTrue(Entities.isNamedEntity(String.valueOf(c)));
        }
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Character c : Entities.xhtmlByVal.keySet()) {
            assertTrue(Entities.isBaseNamedEntity(String.valueOf(c)));
        }
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
        }
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder expected = new StringBuilder();

        String input = "&lt;&gt;&amp;&quot;&#xA0;&#x26;hello world&nbsp;&#x1F604;";

        Entities.escape(expected, input, out, false, false, false);

        assertEquals("&amp;amp;lt;&amp;gt;&amp;amp;amp;quot;&amp;#xA0;&amp;#x26;hello%20world&amp;#x200B;&amp;#x1F604;", expected.toString());

        out.setEscapeMode(Entities.EscapeMode.xhtml);

        expected.setLength(0);
        Entities.escape(expected, input, out, false, false, false);

        assertEquals("&lt;>&gt;&amp;quot;hello world&nbsp;&#x1F604;", expected.toString());
    }

    @Test
    public void testUnescape() {
        Document.OutputSettings out = new Document.OutputSettings();

        String input = "&amp;amp;lt;&gt;&amp;amp;amp;quot;hello world&amp;#x200B;&#x1F604;";

        String output = Entities.unescape(input, false);

        assertEquals("&lt;&gt;&quot;hello world&nbsp;&#1F604;", output);

        out.setEscapeMode(Entities.EscapeMode.xhtml);

        input = "&lt;>&gt;&quot;hello world&nbsp;&#x1F604;";

        output = Entities.unescape(input, false);

        assertEquals("<>&\"hello world\u200b\u1F604>", output);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = Mockito.mock(CharsetEncoder.class); // Mockito is not included in JUnit, you may use other mocking library or write it manually

        for (Character c : Entities.full.values()) {
            if (Character.isLowSurrogate(c) || Character.isHighSurrogate(c)) {
                continue;
            }
            if (coreCharset == CoreCharset.ascii) {
                assertTrue(Entities.canEncode(coreCharset, c, encoder));
            } else {
                assertTrue(encoder.canEncode(c.toString()));
                assertTrue(Entities.canEncode(coreCharset, c, encoder));
            }
        }

        assertFalse(Entities.canEncode(CoreCharset.ascii, '\uD800', encoder));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '\uDC00', encoder));
    }
}