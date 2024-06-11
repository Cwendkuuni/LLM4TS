package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertEquals("Named entity: " + value + " should be present in full entity set.", true, Entities.isNamedEntity(value));
        }

        for (Map.Entry<Character, String> entry : Entities.base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            assertEquals("Named entity: " + value + " should be present in base entity set.", true, Entities.isBaseNamedEntity(value));
        }

        for (Map.Entry<Character, String> entry : Entities.xhtmlByVal.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            assertEquals("Named entity: " + value + " should be present in xhtml entity set.", true, Entities.isNamedEntity(value));
            assertEquals("Named entity: " + value + " should be present in xhtml entity set.", true, Entities.isBaseNamedEntity(value));
        }

        assertEquals("Named entity 'foo' should not be present.", false, Entities.isNamedEntity("foo"));
        assertEquals("Named entity 'foo' should not be present.", false, Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            Character result = Entities.getCharacterByName(value);
            assertEquals("Named entity: " + value + " should map to character: " + result + ".", result, key);
        }
    }

    @Test
    public void testEscape() {
        String input = "&lt;foobar&gt; <'>&";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder expectedOutput = new StringBuilder();

        // Test XHTML output
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        Entities.escape(expectedOutput, input, outputSettings, false, false, false);
        String xhtmlOutput = expectedOutput.toString();
        assertEquals("Escaped XHTML output should be: <entity name=\"lt\">&lt;</entity> <entity name=\"apos\">'</entity> <entity name=\"amp\">&amp;</entity> <entity name=\"apos\">'</entity> <entity name=\"gt\">&gt;</entity> <entity name=\"apos\">'</entity>", xhtmlOutput, "&lt;entity name=\"lt\"&gt;&apos;&amp;&apos;<entity name=\"gt\"&gt;&apos;");

        // Test default HTML output
        outputSettings.escapeMode(Entities.EscapeMode.base);
        Entities.escape(expectedOutput, input, outputSettings, false, false, false);
        String htmlOutput = expectedOutput.toString();
        assertEquals("Escaped HTML output should be: &lt;foobar&gt; &amp;lt; &apos;&apos; &amp;amp; &apos;&apos; &amp;gt; &apos;&apos;", htmlOutput, "&lt;foobar&gt; &amp;lt; &apos;&apos; &amp;amp; &apos;&apos; &amp;gt; &apos;&apos;");

        // Test extended HTML output
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        Entities.escape(expectedOutput, input, outputSettings, false, false, false);
        String extendedOutput = expectedOutput.toString();
        assertEquals("Escaped extended HTML output should be: &#x26;lt;foobar&#x26;gt; &#x26;amp; &#x22; &#x26;amp; &#x22; &#x26;gt; &#x22; &#x26;amp; &#x22;", extendedOutput, "&#x26;lt;foobar&#x26;gt; &#x26;amp; &#x22; &#x26;amp; &#x22; &#x26;gt; &#x22; &#x26;amp; &#x22;");
    }

    @Test
    public void testUnescape() {
        String input = "&lt;entity name=\"lt\"&gt;&apos;&amp;&apos;<entity name=\"gt\"&gt;&apos;";
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        String result = Entities.unescape(input, false);
        assertEquals("Unescaped output should be: <entity name=\"lt\">'&<entity name=\"gt\">'", result, "<entity name=\"lt\">'&<entity name=\"gt\">'");

        outputSettings.setStrict(true);
        result = Entities.unescape(input, true);
        assertEquals("Unescaped strict output should be: <lt>&apos;amp;&apos;gt>", result, "<lt>&apos;amp;&apos;gt>");
    }

}