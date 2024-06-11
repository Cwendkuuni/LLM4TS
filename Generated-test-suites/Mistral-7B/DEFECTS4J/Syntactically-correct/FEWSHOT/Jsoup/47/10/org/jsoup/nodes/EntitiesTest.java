package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Character character : Entities.full.values()) {
            String name = Character.getName(character);
            assertTrue("Named entity: " + name + " is not recognized", Entities.isNamedEntity(name));
        }

        assertFalse("Named entity 'nonexistent' is not recognized", Entities.isNamedEntity("nonexistent"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Character character : Entities.base.values()) {
            String name = Character.getName(character);
            assertTrue("Base named entity: " + name + " is not recognized", Entities.isBaseNamedEntity(name));
        }

        for (Character character : Entities.full.values()) {
            if (!Entities.isBaseNamedEntity(Character.getName(character))) {
                String name = Character.getName(character);
                assertFalse("Named entity: " + name + " is a base named entity, but it is not", Entities.isBaseNamedEntity(name));
            }
        }
    }

    @Test
    public void testGetCharacterByName() {
        for (Character character : Entities.full.values()) {
            String name = Character.getName(character);
            Character result = Entities.getCharacterByName(name);
            assertEquals("Character for named entity: " + name + " is not as expected", character, result);
        }

        assertNull("Character for nonexistent named entity is not null", Entities.getCharacterByName("nonexistent"));
    }

    @Test
    public void testEscape() {
        String original = "&lt;p&gt;Hello, &amp;world!&quot; &gt;<img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;";
        String xhtml = Entities.escape(original, Document.OutputSettings.xhtml());
        String base = Entities.escape(original, Document.OutputSettings.base());
        String extended = Entities.escape(original, Document.OutputSettings.extended());

        assertEquals("Escaped XHTML: ", "<p>&lt;p&gt;Hello, &amp;amp;amp;amp;world!&quot; &gt;&gt;<img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;", xhtml);
        assertEquals("Escaped Base: ", "&lt;p&gt;&amp;lt;p&gt;Hello, &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;#x26;world!&quot; &amp;gt;&amp;gt;&lt;img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;", base);
        assertEquals("Escaped Extended: ", "&lt;p&gt;&lt;lt;p&gt;Hello, &amp;amp;#x26;amp;#x26;world!&quot; &gt;&gt;&lt;img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;", extended);
    }

    @Test
    public void testUnescape() {
        String escapedXhtml = "&lt;p&gt;&lt;lt;p&gt;Hello, &amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;amp;#x26;world!&quot; &gt;&gt;&lt;img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;";
        String escapedBase = "&lt;p&gt;&amp;lt;p&gt;Hello, &amp;amp;world!&quot; &gt;&gt;&lt;img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;";
        String escapedExtended = "&lt;p&gt;&lt;lt;p&gt;Hello, &amp;#x26;world!&quot; &gt;&gt;&lt;img src=&quot;file.jpg&quot; alt=&quot;Image&quot; /&gt;";

        assertEquals("Unescaped XHTML: ", "<p><p>Hello, <world!>", Entities.unescape(escapedXhtml, true));
        assertEquals("Unescaped Base: ", "<p>&lt;p&gt;Hello, <world!>", Entities.unescape(escapedBase, true));
        assertEquals("Unescaped Extended: ", "<p><lt;p&gt;Hello, <world!>", Entities.unescape(escapedExtended, true));

        assertEquals("Unescaped XHTML (strict): ", "<p><p>Hello, <world!>", Entities.unescape(escapedXhtml, false));
        assertEquals("Unescaped Base (strict): ", "&lt;p&gt;&lt;p&gt;Hello, &amp;world!", Entities.unescape(escapedBase, false));
        assertEquals("Unescaped Extended (strict): ", "&lt;p&gt;&lt;lt;p&gt;Hello, &amp;#x26;world!", Entities.unescape(escapedExtended, false));
    }

    @Test
    public void testCanEncode() {
        for (char c : "ASCII: abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray()) {
            assertTrue("Can encode: " + c, Entities.canEncode(Entities.CoreCharset.ascii, c, null));
        }

        for (char c : "UTF: 😃😹😺😽😿🙀".toCharArray()) {
            assertTrue("Can encode: " + c, Entities.canEncode(Entities.CoreCharset.utf, c, null));
        }

        char nonAscii = 0x10FFFF; // highest Unicode code point
        assertFalse("Cannot encode: " + nonAscii, Entities.canEncode(Entities.CoreCharset.ascii, nonAscii, null));
        assertFalse("Cannot encode: " + nonAscii, Entities.canEncode(Entities.CoreCharset.utf, nonAscii, null));
    }
}