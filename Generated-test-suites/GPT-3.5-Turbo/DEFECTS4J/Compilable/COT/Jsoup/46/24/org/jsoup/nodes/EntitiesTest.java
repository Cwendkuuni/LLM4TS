package org.jsoup.nodes;

import org.jsoup.nodes.Entities;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fakeEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fakeEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fakeEntity"));
    }

    @Test
    public void testEscape() {
        String input = "This is a <test> &amp; &quot;string&quot;";
        String expectedOutput = "This is a &lt;test&gt; &amp;amp; &amp;quot;string&amp;quot;";
        assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "This is a &lt;test&gt; &amp;amp; &amp;quot;string&amp;quot;";
        String expectedOutput = "This is a <test> &amp; \"string\"";
        assertEquals(expectedOutput, Entities.unescape(input));
    }
    
    @Test
    public void testUnescapeStrict() {
        String input = "This is a &lt;test&gt; &amp;amp; &amp;quot;string&amp;quot;;";
        String expectedOutput = "This is a <test> &amp; \"string\";";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }

    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testEscapeWithNullString() {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Entities.escape(input, null));
    }
    
    @Test
    public void testEscapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Entities.escape(input, null));
    }
    
    @Test
    public void testUnescapeWithNullString() {
        String input = null;
        String expectedOutput = null;
        assertEquals(expectedOutput, Entities.unescape(input));
    }
    
    @Test
    public void testUnescapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        assertEquals(expectedOutput, Entities.unescape(input));
    }

}
