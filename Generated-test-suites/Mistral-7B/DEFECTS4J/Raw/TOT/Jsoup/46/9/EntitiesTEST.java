import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> fullEntities = Entities.full;
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Assert.assertTrue(Entities.isNamedEntity(value));
        }

        // Test unknown entity name
        Assert.assertFalse(Entities.isNamedEntity("unknownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.base.getMap();
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            Assert.assertTrue(Entities.isBaseNamedEntity(value));
        }

        // Test unknown entity name
        Assert.assertFalse(Entities.isBaseNamedEntity("unknownEntity"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntities = Entities.full.getMap();
        for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
            Character key = entry.getKey();
            Character value = entry.getValue();
            Assert.assertEquals(value, Entities.getCharacterByName(entry.getKey().toString()));
        }

        // Test unknown entity name
        Assert.assertNull(Entities.getCharacterByName("unknownEntity"));
    }

    @Test
    public void testEscapeWithDefaultOutputSettings() {
        String input = "this is a test &amp; < special character > with &nbsp; some &quot; quotes &";
        String expectedOutput = "this is a test &amp; &lt; special character &gt; with &nbsp; some &quot; quotes &";

        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, false, false, false);
        String output = accumulator.toString();
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeWithXHTMLOutputSettings() {
        String input = "this is a test &amp; < special character > with &nbsp; some &quot; quotes &";
        String expectedOutput = "this is a test &amp; &lt; special character &gt; with &nbsp; some &quot; quotes &";

        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, null, false, true, false);
        String output = accumulator.toString();
        Assert.assertEquals(expectedOutput, output);

        // Test with a string containing only XML declaration
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        accumulator.setLength(0);
        Entities.escape(accumulator, xmlDeclaration, null, false, true, false);
        output = accumulator.toString();
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", output);
    }

    @Test
    public void testEscapeWithCustomOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.extended);

        String input = "this is a test &amp; < special character > with &nbsp; some &quot; quotes &";
        StringBuilder accumulator = new StringBuilder();
        Entities.escape(accumulator, input, outputSettings, false, false, false);
        String output = accumulator.toString();
        String expectedOutput = "this is a test &amp; &lt; special character &gt; with &nbsp; some &quot; quotes &";
        Assert.assertEquals(expectedOutput, output);

        // Test with a custom character encoding
        outputSettings.setEncoder(CharsetEncoder.forName("ISO-8859-1"));
        accumulator.setLength(0);
        Entities.escape(accumulator, input, outputSettings, false, false, false);
        output = accumulator.toString();
        expectedOutput = "this is a test &amp; &lt; special character &gt; with &nbsp; some &quot; quotes &";
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "this is a test &amp; &lt; special character &gt; with &nbsp; some &quot; quotes &";
        String expectedOutput = "this is a test & < special character > with some \" quotes &";

        String output = Entities.unescape(input, false);
        Assert.assertEquals(expectedOutput, output);

        // Test with strict unescaping
        output = Entities.unescape(input, true);
        Assert.assertEquals(expectedOutput, output);
    }
}