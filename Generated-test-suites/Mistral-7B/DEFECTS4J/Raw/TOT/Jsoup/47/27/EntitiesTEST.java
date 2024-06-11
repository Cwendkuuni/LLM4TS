import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("unknownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertFalse(Entities.isBaseNamedEntity("gt")); // not in base set
        assertFalse(Entities.isBaseNamedEntity(" unknownEntity")); // not a valid name
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('>', Entities.getCharacterByName("gt"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("unknownEntity"));
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n&lt;user name=&apos;John&apos; &amp; age=&quot;30&quot;\n</root>";
        String expectedOutput = "&amp;lt;&amp;lt;user name=&amp;amp;amp;apos;John&amp;amp;amp;amp;apos; &amp;amp;amp;amp;amp;amp;quot;30&amp;amp;amp;amp;amp;amp;quot;\n";

        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, null, false, false, false);

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        String input = "<html><body><p>This is a &lt;strong&gt;test&lt;/strong&gt;.</p></body></html>";
        String expectedOutput = "&lt;?xml version=\"1.0\" encoding=\"UTF-8\"?&gt;\n<html>\n<body>\n<p>This is a &amp;lt;strong&gt;test&amp;lt;/strong&gt;.</p>\n</body>\n</html>";

        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, null, false, true, false);

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testUnescapeMethod() {
        String input = "&amp;lt;user&amp;gt; &amp;amp;amp;lt;age&amp;gt;30&amp;amp;amp;quot;John &amp;amp;amp;quot;Smith&amp;amp;amp;quot;";
        String expectedOutput = "<user age='30'>John Smith";

        String actualOutput = Entities.unescape(input, true);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithInvalidString() {
        Entities.unescape(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnescapeMethodWithInvalidBoolean() {
        Entities.unescape("test", false);
    }
}