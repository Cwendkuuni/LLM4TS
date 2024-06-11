import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character entityName = entry.getKey();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(entry.getValue());
            Assert.assertEquals("isNamedEntity test failed for entity: " + entityName, expected, actual);
        }

        // Test for non-existent entity
        String nonExistentEntity = "xxx";
        boolean actual = Entities.isNamedEntity(nonExistentEntity);
        Assert.assertFalse("isNamedEntity test failed for non-existent entity: " + nonExistentEntity, actual);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.base;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String entityName = entry.getKey();
            Character expected = entry.getValue();
            boolean actual = Entities.isBaseNamedEntity(entityName);
            Assert.assertEquals("isBaseNamedEntity test failed for entity: " + entityName, expected, actual);
        }

        // Test for non-existent entity
        String nonExistentEntity = "xxx";
        boolean actual = Entities.isBaseNamedEntity(nonExistentEntity);
        Assert.assertFalse("isBaseNamedEntity test failed for non-existent entity: " + nonExistentEntity, actual);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character entityName = entry.getKey();
            Character expected = entry.getValue();
            Character actual = Entities.getCharacterByName(entry.getValue().toString());
            Assert.assertEquals("getCharacterByName test failed for entity: " + entityName, expected, actual);
        }

        // Test for non-existent entity
        String nonExistentEntity = "xxx";
        Character actual = Entities.getCharacterByName(nonExistentEntity);
        Assert.assertNull("getCharacterByName test failed for non-existent entity: " + nonExistentEntity, actual);
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>Hello &amp; World&gt; <>&\"'";
        StringBuilder expectedOutput = new StringBuilder("&amp;amp;<amp;&gt;Hello &amp;amp;amp;World&gt; <&gt;&amp;amp;quot;&#039;");
        String output = Entities.escape(input, null);
        StringBuilder actualOutput = new StringBuilder(output);
        Assert.assertEquals("escape test failed", expectedOutput.toString(), actualOutput.toString());

        // Test for different escape modes
        Document.OutputSettings outputSettingsXhtml = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        StringBuilder expectedOutputXhtml = new StringBuilder("&amp;<amp;&gt;Hello &amp;quot;World&gt; <&gt;");
        String outputXhtml = Entities.escape(input, outputSettingsXhtml);
        StringBuilder actualOutputXhtml = new StringBuilder(outputXhtml);
        Assert.assertEquals("escape test for XHTML mode failed", expectedOutputXhtml.toString(), actualOutputXhtml.toString());

        Document.OutputSettings outputSettingsBase = new Document.OutputSettings().escapeMode(Entities.EscapeMode.base);
        StringBuilder expectedOutputBase = new StringBuilder("&amp;#38;<&#60;Hello &amp;#38;#32;World&gt; <&#62;>&amp;#34;'");
        String outputBase = Entities.escape(input, outputSettingsBase);
        StringBuilder actualOutputBase = new StringBuilder(outputBase);
        Assert.assertEquals("escape test for base mode failed", expectedOutputBase.toString(), actualOutputBase.toString());
    }

    @Test
    public void testUnescape() {
        String inputEscaped = "&amp;amp;<amp>&gt;Hello &amp;quot;World&gt; <&gt;";
        String expectedOutput = "&amp;<lt>Hello &amp;World&gt; <>";
        String output = Entities.unescape(inputEscaped);
        Assert.assertEquals("unescape test failed", expectedOutput, output);

        String inputEscapedStrict = "&amp;amp;<amp>&gt;Hello &amp;quot;World&gt; <&gt;";
        String outputStrict = Entities.unescape(inputEscaped, true);
        Assert.assertEquals("unescape test with strict flag failed", expectedOutput, outputStrict);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = Character. encodingFor (coreCharset.name ());
        for (char c : "!@#$%^&*()_+-={}[]|\\:\"<>") {
            boolean expected = true;
            boolean actual = Entities.canEncode(coreCharset, c, encoder);
            Assert.assertEquals("canEncode test failed for character: " + c, expected, actual);
        }

        CharsetEncoder fallbackEncoder = Character. encodingFor ("UTF-8");
        for (char c : "\uD800\uDC00-\uD834\uDFFF") {
            boolean expected = false;
            boolean actual = Entities.canEncode(coreCharset, c, fallbackEncoder);
            Assert.assertEquals("canEncode test failed for surrogate pair: " + Integer.toHexString((int) c), expected, actual);
        }
    }
}