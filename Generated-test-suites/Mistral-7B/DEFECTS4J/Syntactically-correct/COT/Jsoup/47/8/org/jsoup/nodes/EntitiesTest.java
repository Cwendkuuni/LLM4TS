import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> full = Entities.full;
        for (Map.Entry<String, Character> entry : full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Assert.assertTrue(Entities.isNamedEntity(name));
            Assert.assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseByVal = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            Assert.assertTrue(Entities.isBaseNamedEntity(name));
            Assert.assertEquals(character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsNotNamedEntity() {
        Assert.assertFalse(Entities.isNamedEntity(""));
        Assert.assertFalse(Entities.isNamedEntity(null));
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testEscapeMethodBasic() {
        String input = "This is a & test of & the &amp; escape method.";
        String output = Entities.escape(input, null);
        String expectedOutput = "This is a &amp; test of &amp; the &amp;amp; escape method.";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithOutputSettings() {
        String input = "This is a < test of & the &amp; escape method.";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String output = Entities.escape(null, input, outputSettings, false, false, false);
        String expectedOutput = "This is a &lt; test of &amp; the &amp;amp; escape method.";
        Assert.assertEquals(expectedOutput, output);

        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        output = Entities.escape(null, input, outputSettings, false, false, false);
        expectedOutput = "This is a &lt; test of &amp; the &amp;amp; escape method.";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeThrowsExceptionWithNullInput() {
        String input = null;
        Entities.escape(null, input, null, false, false, false);
    }

    @Test
    public void testUnescapeMethodBasic() {
        String input = "This is a &amp; test of &amp; the &amp;amp; escape method.";
        String output = Entities.unescape(input, false);
        String expectedOutput = "This is a & test of & the & escape method.";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescapeMethodStrict() {
        String input = "This is a &amp; test of &amp; the &amp;amp; escape method.";
        String output = Entities.unescape(input, true);
        String expectedOutput = "This is a &amp; test of &amp;amp; the &amp;amp;amp; escape method.";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeThrowsExceptionWithNullInput() {
        String input = null;
        Entities.unescape(input, false);
    }

    @Test
    public void testCanEncodePerformance() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = org.jsoup.nodes.Entities.class.getDeclaredField("encoder").setAccessible(true).get(null);
        for (char c = 0; c <= 0xFFFF; c++) {
            try {
                encoder.canEncode(c);
            } catch (Exception e) {
                Assert.assertFalse(false);
            }
        }
    }

    @Test
    public void testUnsupportedEncodingException() {
        CharsetEncoder encoder = org.jsoup.nodes.Entities.class.getDeclaredField("encoder").setAccessible(true).get(null);
        try {
            encoder.canEncode((char) 0x10FFFF);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals("Unsupported encoding", e.getMessage());
        }
    }
}