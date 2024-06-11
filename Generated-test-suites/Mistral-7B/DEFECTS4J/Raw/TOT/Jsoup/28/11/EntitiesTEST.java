import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            boolean expected = true;
            boolean actual = Entities.isNamedEntity(entityName);
            Assert.assertEquals("Named entity " + entityName + " should be recognized.", expected, actual);
        }

        String unknownEntity = "abc123";
        boolean expected = false;
        boolean actual = Entities.isNamedEntity(unknownEntity);
        Assert.assertEquals("Unknown entity " + unknownEntity + " should not be recognized.", expected, actual);
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            Character expected = entity;
            Character actual = Entities.getCharacterByName(entityName);
            Assert.assertEquals("Named entity " + entityName + " should return character " + expected, expected, actual);
        }
    }

    @Test
    public void testEscape() {
        String input = "&amp;<>&\"'";
        Document.OutputSettings settings = new Document.OutputSettings();
        String expected = "&amp;amp;(&amp;)&gt;&gt;< &quot; &apos;";
        String actual = Entities.escape(input, settings);
        Assert.assertEquals("Input \"" + input + "\" should be escaped to \"" + expected + "\".", expected, actual);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;amp;(&amp;)&gt;&gt;< &quot; &apos;";
        String expected = "&amp;<>&\"'";
        String actual = Entities.unescape(input);
        Assert.assertEquals("Input \"" + input + "\" should be unescaped to \"" + expected + "\".", expected, actual);

        String inputWithInvalid = "&amp;abc";
        String expectedWithInvalid = "&amp;";
        String actualWithInvalid = Entities.unescape(inputWithInvalid);
        Assert.assertEquals("Input \"" + inputWithInvalid + "\" should be unescaped to \"" + expectedWithInvalid + "\".", expectedWithInvalid, actualWithInvalid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode("invalid");
        String input = "&amp;<>&\"'";
        String expected = "&amp;amp;(&amp;)&gt;&gt;< &quot; &apos;";
        String actual = Entities.escape(input, null, settings);
    }
}