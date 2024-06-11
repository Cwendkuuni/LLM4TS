
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.jsoup.nodes.Entities.EscapeMode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("quot"));

        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("quot"));

        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));

        assertNull(Entities.getCharacterByName("unknown"));
    }
    
    @Test
    public void testEscape() {
        String input = "<div>Hello</div>";
        String expected = "&lt;div&gt;Hello&lt;/div&gt;";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testEscape_inAttribute() {
        String input = "John & Jane";
        String expected = "John &amp; Jane";
        assertEquals(expected, Entities.escape(input, new Document.OutputSettings()));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;Hello&lt;/div&gt;";
        String expected = "<div>Hello</div>";
        assertEquals(expected, Entities.unescape(input));
    }

    @Test
    public void testUnescape_withStrict() {
        String input = "&lt;div&gt;Hello&lt;/div&gt;";
        String expected = "<div>Hello</div>";
        assertEquals(expected, Entities.unescape(input, true));
    }

    @Test
    public void testUnescape_withoutStrict() {
        String input = "&lt;div&gt;Hello&lt;/div&gt;";
        String expected = "<div>Hello</div>";
        assertEquals(expected, Entities.unescape(input, false));
    }
}
