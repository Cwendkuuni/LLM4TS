import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expectedResult = true;
            boolean result = Entities.isNamedEntity(value);
            Assert.assertEquals(expectedResult, result);
        }

        // Test for non-existent entity
        String nonExistentEntity = "nonexistent";
        boolean result = Entities.isNamedEntity(nonExistentEntity);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseMap = Entities.base;
        for (Map.Entry<Character, String> entry : baseMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            boolean expectedResult = true;
            boolean result = Entities.isBaseNamedEntity(value);
            Assert.assertEquals(expectedResult, result);
        }

        // Test for non-existent entity
        String nonExistentEntity = "nonexistent";
        boolean result = Entities.isBaseNamedEntity(nonExistentEntity);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullMap = Entities.full;
        for (Map.Entry<Character, String> entry : fullMap.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Character expectedResult = key;
            Character result = Entities.getCharacterByName(value);
            Assert.assertEquals(expectedResult, result);
        }
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        String inputString = "&<>'\"";
        StringBuilder expectedOutput = new StringBuilder("&amp;&lt;&gt;&amp;quot;");
        String outputString = Entities.escape(inputString, null);
        Assert.assertEquals(expectedOutput.toString(), outputString);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        String inputString = "&<>'\"";
        StringBuilder expectedOutput = new StringBuilder("&amp;");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.Entities.EscapeMode.xhtml);
        String outputString = Entities.escape(inputString, outputSettings);
        Assert.assertEquals(expectedOutput.toString(), outputString);
    }

    @Test
    public void testUnescapeMethod() {
        String inputString = "&amp;lt;&amp;gt;&amp;quot;";
        String expectedOutput = "<'\"";
        String outputString = Entities.unescape(inputString, false);
        Assert.assertEquals(expectedOutput, outputString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithInvalidSettings() {
        String inputString = "&<>'\"";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(null);
        Entities.escape(inputString, outputSettings);
    }
}