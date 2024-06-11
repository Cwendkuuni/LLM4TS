import org.junit. Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            boolean expectedResult = true;
            boolean actualResult = Entities.isNamedEntity(name);
            Assert.assertEquals("Expected isNamedEntity to return true for " + name, expectedResult, actualResult);
        }

        String nonEntity = "abc123";
        boolean actualResult = Entities.isNamedEntity(nonEntity);
        Assert.assertEquals("Expected isNamedEntity to return false for " + nonEntity, false, actualResult);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character character = entry.getKey();
            String name = entry.getValue();
            boolean expectedResult = true;
            boolean actualResult = Entities.isBaseNamedEntity(name);
            Assert.assertEquals("Expected isBaseNamedEntity to return true for " + name, expectedResult, actualResult);
        }

        String nonBaseEntity = "amp";
        boolean actualResult = Entities.isBaseNamedEntity(nonBaseEntity);
        Assert.assertEquals("Expected isBaseNamedEntity to return false for " + nonBaseEntity, false, actualResult);
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> entities = Entities.full;
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            Character actualResult = Entities.getCharacterByName(name);
            Assert.assertEquals("Expected getCharacterByName to return " + character + " for " + name, character, actualResult);
        }

        String unknownEntity = "xyz";
        Character actualResult = Entities.getCharacterByName(unknownEntity);
        Assert.assertNull("Expected getCharacterByName to return null for unknown entity " + unknownEntity, actualResult);
    }

    @Test
    public void testEscape() {
        StringBuilder accumulator = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.xhtml);
        String input = "&lt;xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n>&amp; &lt; &gt; &quot; \n</p>\n</body>\n</html>\n";
        Entities.escape(accumulator, input, outputSettings, false, false, false);
        String expectedOutput = "&lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n>&amp; &amp;lt; &amp;gt; &amp;quot; &nbsp; \n</p>\n</body>\n</html>\n";
        Assert.assertEquals("Expected the escaped output to be: " + expectedOutput, expectedOutput, accumulator.toString());

        // Test with different EscapeMode
        outputSettings.setEscapeMode(Entities.EscapeMode.base);
        accumulator.setLength(0);
        Entities.escape(accumulator, input, outputSettings, false, false, false);
        expectedOutput = "&amp;lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n>&amp;amp; &amp;lt; &amp;gt; &amp;quot; &nbsp; \n</p>\n</body>\n</html>\n";
        Assert.assertEquals("Expected the escaped output to be: " + expectedOutput, expectedOutput, accumulator.toString());

        outputSettings.setEscapeMode(Entities.EscapeMode.extended);
        accumulator.setLength(0);
        Entities.escape(accumulator, input, outputSettings, false, false, false);
        expectedOutput = "&lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n>&amp;lt; &amp;gt; &amp;quot; &nbsp; \n</p>\n</body>\n</html>\n";
        Assert.assertEquals("Expected the escaped output to be: " + expectedOutput, expectedOutput, accumulator.toString());
    }

    @Test
    public void testUnescape() {
        StringBuilder accumulator = new StringBuilder();
        String input = "&lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n>&amp;lt; &amp;gt; &amp;quot; &nbsp;\n</p>\n</body>\n</html>\n";
        Entities.unescape(accumulator, true);
        String expectedOutput = "&lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=&apos;test&apos;\n< &gt; \n</p>\n</body>\n</html>\n";
        Assert.assertEquals("Expected the unescaped output to be: " + expectedOutput, expectedOutput, accumulator.toString());

        // Test with strict=false
        accumulator.setLength(0);
        Entities.unescape(accumulator, false);
        expectedOutput = "&lt;?xml version=&quot;1.0&quot;&gt;\n<html>\n<body>\n<p id=test\n< &gt; \n</p>\n</body>\n</html>\n";
        Assert.assertEquals("Expected the unescaped output to be: " + expectedOutput, expectedOutput, accumulator.toString());
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        for (char character = 0; character <= 0x7F; character++) {
            boolean expectedResult = true;
            boolean actualResult = Entities.canEncode(charset, character, null);
            Assert.assertEquals("Expected canEncode to return true for ASCII character " + (char) character, expectedResult, actualResult);
        }

        char supplementaryCharacter = 0x1F000;
        boolean actualResult = Entities.canEncode(charset, supplementaryCharacter, null);
        Assert.assertFalse("Expected canEncode to return false for supplementary character " + supplementaryCharacter, actualResult);
    }
}