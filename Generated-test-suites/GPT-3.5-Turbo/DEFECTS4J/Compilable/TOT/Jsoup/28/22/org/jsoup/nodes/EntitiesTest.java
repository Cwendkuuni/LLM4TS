package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    
    @BeforeClass
    public static void setUp() {
        // Preparing for the tests
    }

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String input = "<tag>";
        String expectedOutput = "&lt;tag&gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;tag&gt;";
        String expectedOutput = "<tag>";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeWithStrictMode() {
        String input = "&lttag&gt";
        String expectedOutput = "&lttag&gt";
        String output = Entities.unescape(input, true);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testUnescapeWithNonStrictMode() {
        String input = "&lttag&gt";
        String expectedOutput = "<tag>";
        String output = Entities.unescape(input, false);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeXHTMLMode() {
        String input = "<tag>";
        String expectedOutput = "&lt;tag&gt;";
        String output = Entities.escape(input, null, Entities.EscapeMode.xhtml);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeBaseMode() {
        String input = "abc";
        String expectedOutput = "abc";
        String output = Entities.escape(input, null, Entities.EscapeMode.base);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeExtendedMode() {
        String input = "<quote>";
        String expectedOutput = "&lt;quote&gt;";
        String output = Entities.escape(input, null, Entities.EscapeMode.extended);
        assertEquals(expectedOutput, output);
    }
    
    // Additional test cases for comprehensive coverage
    @Test
    public void testEscapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testEscapeWithSpecialCharacters() {
        String input = "Special & \" ' < >";
        String expectedOutput = "Special &amp; &quot; &#x27; &lt; &gt;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testUnescapeWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testUnescapeWithMissingSemicolonAtEnd() {
        String input = "&lt;tag&gt";
        String expectedOutput = "&lt;tag&gt";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
    
    @Test
    public void testUnescapeWithInvalidNumericEntity() {
        String input = "&#xyz;";
        String expectedOutput = "&#xyz;";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
}
