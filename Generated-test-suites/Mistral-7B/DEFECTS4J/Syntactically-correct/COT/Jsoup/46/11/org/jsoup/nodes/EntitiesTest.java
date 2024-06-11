import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            Assert.assertTrue("Named entity " + entityName + " should be known", Entities.isNamedEntity(entityName));
            Assert.assertEquals("Named entity " + entityName + " should return the correct character", entity, Entities.getCharacterByName(entityName));
        }

        Map<String, Character> baseEntities = Entities.base;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            Assert.assertTrue("Base named entity " + entityName + " should be known", Entities.isBaseNamedEntity(entityName));
            Assert.assertEquals("Base named entity " + entityName + " should return the correct character", entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntityWithNonBaseEntity() {
        String nonBaseEntity = "copy";
        Assert.assertFalse("Non-base entity " + nonBaseEntity + " should not be known", Entities.isBaseNamedEntity(nonBaseEntity));
    }

    @Test
    public void testGetCharacterByNameWithUnknownEntity() {
        String unknownEntity = "unknown";
        Assert.assertNull("Unknown entity " + unknownEntity + " should return null", Entities.getCharacterByName(unknownEntity));
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeWithNullInput() {
        Entities.escape(null, null, null, false, false, false);
    }

    @Test
    public void testEscapeNormalCase() {
        String input = "This is a test &amp; test &lt; test &gt; test &quot; test";
        String expectedOutput = "This is a test &amp; amp; test &lt; lt; test &gt; gt; test &quot; quot;";
        String output = Entities.escape(input, null, null, false, false, false);
        Assert.assertEquals("Escaped output doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeInAttributeMode() {
        String input = "This is a test &amp; test &lt; test &gt; test &quot; test";
        String expectedOutput = "This is a test &amp;amp; test &amp;lt; test &amp;gt; test &quot;quot;&quot;";
        String output = Entities.escape(input, null, null, true, false, false);
        Assert.assertEquals("Escaped output in attribute mode doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeWithNormalizeWhiteSpace() {
        String input = " This is a test &amp; test  < test > test \" test ";
        String expectedOutput = " This is a test &nbsp; test &nbsp;lt; test &nbsp;gt; test &nbsp;quot; test ";
        String output = Entities.escape(input, null, null, false, true, false);
        Assert.assertEquals("Escaped output with normalize whitespace doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testEscapeWithStripLeadingWhiteSpace() {
        String input = " This is a test &amp; test  < test > test \" test ";
        String expectedOutput = "This is a test &nbsp; test &nbsp;lt; test &nbsp;gt; test &nbsp;quot; test ";
        String output = Entities.escape(input, null, null, false, true, true);
        Assert.assertEquals("Escaped output with strip leading whitespace doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testCanEncodeASCII() {
        for (char c = 0; c < 128; c++) {
            Assert.assertTrue("ASCII character " + (int) c + " can be encoded", Entities.canEncode(Entities.CoreCharset.ascii, c, null));
        }
    }

    @Test
    public void testCanEncodeUTF() {
        for (char c = 0; c < 0x10FFFF + 1; c++) {
            if (Character.isSupplementaryCodePoint(c)) {
                continue;
            }
            Assert.assertTrue("UTF-8 character " + (int) c + " can be encoded", Entities.canEncode(Entities.CoreCharset.utf, c, null));
        }
    }

    @Test
    public void testUnescapeWithValidEntities() {
        String input = "&amp;amp; &lt;lt;&gt; &quot;quot;&quot; &apos;apos;&apos; &eacute;eacute;";
        String expectedOutput = "&amp; &lt; &quot; &apos; é";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("Unescaped output doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescapeWithInvalidEntities() {
        String input = "&invalid; &non-existent; &amp;not-an-entity;";
        String expectedOutput = "&invalid; &non-existent;" +
                " &amp;";
        String output = Entities.unescape(input, false);
        Assert.assertEquals("Unescaped output with invalid entities doesn't match expected output", expectedOutput, output);
    }

    @Test
    public void testUnescapeWithStrictMode() {
        String input = "&amp; &lt; &quot; &apos; é &amp;not-an-entity; &quot;&quot;;";
        String expectedOutput = "&amp; &amp; &lt; lt; &quot; quot; &apos; &apos; &apos; apos; &quot; &quot;";
        String output = Entities.unescape(input, true);
        Assert.assertEquals("Unescaped output in strict mode doesn't match expected output", expectedOutput, output);
    }
}