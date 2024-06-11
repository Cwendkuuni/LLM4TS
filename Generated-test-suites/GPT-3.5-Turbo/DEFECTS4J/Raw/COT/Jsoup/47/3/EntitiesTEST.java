import org.junit.Test;
import org.jsoup.nodes.Entities.*;
import org.jsoup.nodes.Entities.EscapeMode.*;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("abc"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("abc"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("abc"));
    }
    
    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings(EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", out));
        assertEquals("&amp;", Entities.escape("&", out));
        assertEquals("&nbsp;", Entities.escape("\u00A0", out));
        assertEquals("&quot;", Entities.escape("\"", out));
        assertEquals("abc", Entities.escape("abc", out));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\u00A0", Entities.unescape("&nbsp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals("abc", Entities.unescape("abc"));
    }

}