import org.jsoup.helper.StringUtil;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("invalid"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("invalid"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("<div>Hello &amp; World</div>", Document.OutputSettings.html());
        assertEquals("&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;");
        assertEquals("<div>Hello &amp; World</div>", unescaped);
    }

    @Test
    public void testEscapeEdgeCases() {
        // Test normaliseWhite functionality
        String input = "    <div>    Hello     &amp;    World    </div>    ";
        String expectedOutput = " <div> Hello &amp; World </div> ";
        String escaped = Entities.escape(input, Document.OutputSettings.html());
        assertEquals(expectedOutput, escaped);

        // Test codePoint < Character.MIN_SUPPLEMENTARY_CODE_POINT
        String input2 = "\u00A0"; // No-Break Space (html specific escape)
        String expectedOutput2 = "&nbsp;";
        String escaped2 = Entities.escape(input2, Document.OutputSettings.html());
        assertEquals(expectedOutput2, escaped2);
    }

    @Test
    public void testUnescapeErrorScenario() {
        String unescaped = Entities.unescape("&lt;div&gt;Hello &amp; World&lt;/div");
        assertEquals("&lt;div&gt;Hello &amp; World&lt;/div", unescaped);
    }

}