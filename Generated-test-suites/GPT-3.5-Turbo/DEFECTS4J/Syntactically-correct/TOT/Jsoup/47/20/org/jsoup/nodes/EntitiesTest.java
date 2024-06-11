import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void isNamedEntityTest() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void isBaseNamedEntityTest() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void getCharacterByNameTest() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void escapeTest() {
        String input = "<div>Text with <b>HTML tags</b></div>";
        String expectedOutput = "&lt;div&gt;Text with &lt;b&gt;HTML tags&lt;/b&gt;&lt;/div&gt;";

        String output = Entities.escape(input, new Document.OutputSettings());

        assertEquals(expectedOutput, output);
    }

    @Test
    public void unescapeTest() {
        String input = "&lt;div&gt;Text with &lt;b&gt;HTML tags&lt;/b&gt;&lt;/div&gt;";
        String expectedOutput = "<div>Text with <b>HTML tags</b></div>";

        String output = Entities.unescape(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void unescapeStrictTest() {
        String input = "&lt;div&gt;Text with &lt;b&gt;HTML tags&lt;/b&gt;&lt;/div&gt;";
        String expectedOutput = "<div>Text with <b>HTML tags</b></div>";

        String output = Entities.unescape(input, true);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void unescapeWithInvalidEntityTest() {
        String input = "&lt;div&gt;Text with &lt;b&gt;&foobar;&lt;/b&gt;&lt;/div&gt;";
        String expectedOutput = "<div>Text with <b>&foobar;</b></div>";

        String output = Entities.unescape(input);

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithNonAsciiCharactersTest() {
        String input = "Text with å and ü characters";
        String expectedOutput = "Text with &#x00E5; and &#x00FC; characters";

        String output = Entities.escape(input, new Document.OutputSettings());

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithXhtmlEscapeModeTest() {
        String input = "<div>Text with <b>HTML tags</b></div>";
        String expectedOutput = "&lt;div&gt;Text with <b>HTML tags</b>&lt;/div&gt;";

        String output = Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithBaseEscapeModeTest() {
        String input = "<div>Text with <b>HTML tags</b></div>";
        String expectedOutput = "&lt;div&gt;Text with &lt;b&gt;HTML tags&lt;/b&gt;&lt;/div&gt;";

        String output = Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithExtendedEscapeModeTest() {
        String input = "<div>Text with <b>HTML tags</b></div>";
        String expectedOutput = "&lt;div&gt;Text with &lt;b&gt;HTML tags&lt;/b&gt;&lt;/div&gt;";

        String output = Entities.escape(input, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithNormaliseWhiteTest() {
        String input = "Text with    multiple   white   spaces";
        String expectedOutput = "Text with multiple white spaces";

        String output = Entities.escape(input, new Document.OutputSettings().normaliseWhite(true));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithStripLeadingWhiteTest() {
        String input = "  Text with leading white spaces";
        String expectedOutput = "Text with leading white spaces";

        String output = Entities.escape(input, new Document.OutputSettings().stripLeadingWhite(true));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithInAttributeTest() {
        String input = "<div>Text with <b>HTML tags</b></div>";
        String expectedOutput = "<div>Text with &lt;b&gt;HTML tags&lt;/b&gt;</div>";

        String output = Entities.escape(input, new Document.OutputSettings().inAttribute(true));

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithInvalidCharacterTest() {
        String input = "Text with invalid character: \u00E9";
        String expectedOutput = "Text with invalid character: &#xE9;";

        String output = Entities.escape(input, new Document.OutputSettings());

        assertEquals(expectedOutput, output);
    }

    @Test
    public void escapeWithInvalidCharsetTest() {
        String input = "Text with invalid character: \u00E9";
        String expectedOutput = "Text with invalid character: \u00E9";

        String output = Entities.escape(input, new Document.OutputSettings().encoder(Charset.forName("INVALID_CHARSET").newEncoder()));

        assertEquals(expectedOutput, output);
    }
}