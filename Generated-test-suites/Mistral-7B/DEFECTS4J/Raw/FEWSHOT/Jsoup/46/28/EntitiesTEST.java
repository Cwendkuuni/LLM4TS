package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Named entity '" + name + "' is not recognized", Entities.isNamedEntity(name));
            assertEquals("Named entity '" + name + "' has incorrect character value", character, Entities.getCharacterByName(name));
        }

        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue("Base named entity '" + name + "' is not recognized", Entities.isBaseNamedEntity(name));
            assertEquals("Base named entity '" + name + "' has incorrect character value", character, Entities.getCharacterByName(name));
        }

        assertFalse("Unknown named entity is recognized", Entities.isNamedEntity("not_an_entity"));
        assertNull("Unknown named entity has character value", Entities.getCharacterByName("not_an_entity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals("Entity for lt symbol should be '<'", '<', Entities.getCharacterByName("lt"));
        assertEquals("Entity for gt symbol should be '>', '\u003E'", '\u003E', Entities.getCharacterByName("gt"));
        assertEquals("Entity for ampersand symbol should be '&'", '&', Entities.getCharacterByName("amp"));
        assertEquals("Entity for quotation mark should be '\"'", '\u0022', Entities.getCharacterByName("quot"));

        // Test for invalid named entities
        assertNull("Invalid named entity should return null", Entities.getCharacterByName("invalid"));
        assertNull("Invalid named entity should return null", Entities.getCharacterByName("   "));
    }

    @Test
    public void testEscape() {
        String input = "&amp;lt;p&gt;Hello &quot;world&quot;!&nbsp;&#123; this is a test.&apos;";
        String expectedOutput = "&amp;amp;lt;p&amp;gt;Hello &amp;quot;world&amp;quot;!&nbsp; &#x00A0; &#x007B; this is a test.";
        assertEquals("Input should be escaped correctly to output", expectedOutput, Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.extended, null, null)));

        String input2 = "&amp;lt;p&gt;Hello &quot;world!&nbsp; &apos;this is a test.&apos;";
        String expectedOutput2 = "&amp;amp;lt;p&amp;gt;Hello &amp;quot;world!&amp;nbsp; &amp;apos;this is a test.";
        assertEquals("Input with extra space should be escaped correctly", expectedOutput2, Entities.escape(input2, new Document.OutputSettings(Entities.EscapeMode.extended, null, null)));

        String input3 = "&amp;lt;p&gt;Hello &quot;world!&nbsp; &apos;this is a test.";
        String expectedOutput3 = "&amp;lt;p&gt;Hello &quot;world!&nbsp;&#xA0; &apos;this is a test.";
        assertEquals("Input with extra space and non-standard encoding should be escaped correctly", expectedOutput3, Entities.escape(input3, new Document.OutputSettings(Entities.EscapeMode.extended, null, new StringCharacterEncoder("UTF-16"))));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;p&gt;Hello &amp;quot;world!&nbsp; &amp;apos;this is a test.";
        String expectedOutput = "&lt;p&gt;Hello &quot;world!&nbsp;&apos; this is a test.";
        assertEquals("Input should be correctly unescaped", expectedOutput, Entities.unescape(input));

        String input2 = "&amp;amp;lt;p&amp;gt;Hello &amp;quot;world!&amp;nbsp; &amp;apos;this is a test.";
        String expectedOutput2 = "&amp;lt;p&gt;Hello &quot;world!&nbsp;&apos; this is a test.";
        assertEquals("Input with double escapes should be correctly unescaped", expectedOutput2, Entities.unescape(input2));
    }

    @Test
    public void testCanEncode() {
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        for (char c : "!@#$%^&*()_+-=[]{}|;:,.<>?".toCharArray()) {
            assertTrue("Encoding should be possible for character " + c, Entities.canEncode(Entities.CoreCharset.utf, c, encoder));
        }
    }
}