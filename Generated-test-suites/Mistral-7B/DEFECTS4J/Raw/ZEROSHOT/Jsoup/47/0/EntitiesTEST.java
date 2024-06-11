package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};
        for (String name : names) {
            assertTrue(entities.isNamedEntity(name));
        }

        String[] invalidNames = {"invalidName", "SpecialCharacter: §"};
        for (String name : invalidNames) {
            assertFalse(entities.isNamedEntity(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};
        for (String name : names) {
            assertFalse(entities.isBaseNamedEntity(name));
        }

        String[] baseNames = {"amp", "quot"};
        for (String name : baseNames) {
            assertTrue(entities.isBaseNamedEntity(name));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] names = {"lt", "gt", "amp", "quot"};
        Character[] expected = {'<', '>', '&', '\''};
        for (int i = 0; i < names.length; i++) {
            assertEquals(expected[i], entities.getCharacterByName(names[i]));
        }
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String testString = "&lt;test&gt; &amp; &apos; &quot; &#xA0; < & > & & &amp; &quot;";
        String escapedString = entities.escape(testString, outputSettings);
        String expectedEscapedString = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#xA0; &amp;lt; &amp;gt; &amp;amp; &amp;amp; &amp;amp; &amp;quot;";
        assertEquals(expectedEscapedString, escapedString);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String testString = "&amp;lt;test&amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#xA0; &amp;lt; &amp;gt; &amp;amp; &amp;amp; &amp;amp; &amp;quot;";
        String unescapedString = entities.unescape(testString);
        String expectedUnescapedString = "&lt;test&gt; &amp; &apos; &quot; &#xA0; < > &amp; &amp; &amp; &quot;";
        assertEquals(expectedUnescapedString, unescapedString);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        Character c = '\u00E1';
        CoreCharset coreCharset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null;
        assertTrue(entities.canEncode(coreCharset, c, encoder));
    }
}