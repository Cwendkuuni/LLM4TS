package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertEquals(true, Entities.isNamedEntity(entry.getKey()));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            assertEquals(true, Entities.isBaseNamedEntity(entry.getKey()));
        }

        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            if (!Entities.base.containsKey(entry.getKey())) {
                assertEquals(false, Entities.isBaseNamedEntity(entry.getKey()));
            }
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            assertEquals(entry.getValue(), Entities.getCharacterByName(entry.getKey()));
        }
    }

    @Test
    public void testEscape() {
        final String input = "&lt;foo&gt;<bar>baz &quot;quux&quot;&amp;&#xA0;";
        final StringBuilder expectedOutput = new StringBuilder("&amp;lt;foo&amp;gt;&lt;bar&gt;baz &amp;#x60;quux&amp;amp;&#xA0;");

        StringBuilder output = new StringBuilder();
        Entities.escape(output, input, Document.OutputSettings.base(), false, false, false);

        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testUnescape() {
        final String input = "&amp;lt;foo&amp;gt;&lt;bar&gt;baz &amp;#x60;quux&amp;amp;&#xA0;";
        final String expectedOutput = "&lt;foo&gt;<bar>baz &quot;quux&quot;";

        String output = Entities.unescape(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCanEncode() {
        final CoreCharset coreCharset = CoreCharset.ascii;

        for (Character c : Character.values()) {
            if (Character.isHighSurrogate(c)) continue;
            if (Character.isLowSurrogate(c)) continue;

            if (Character.isISOControl(c)) {
                assertEquals(false, Entities.canEncode(coreCharset, c, null));
            } else {
                assertEquals(true, Entities.canEncode(coreCharset, c, null));
            }
        }

        final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        final CoreCharset utf = CoreCharset.utf;

        for (Character c : Character.values()) {
            if (Character.isHighSurrogate(c)) continue;
            if (Character.isLowSurrogate(c)) continue;

            assertEquals(true, Entities.canEncode(utf, c, encoder));
        }
    }
}