import org.jsoup.parser.Parser;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("foo"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("foo"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertNull(Entities.getCharacterByName("foo"));
    }
    
    @Test
    public void testEscape() {
        String escapedString = Entities.escape("<div>&nbsp;</div>", Parser.htmlParser().settings());
        assertEquals("&lt;div&gt;&amp;nbsp;&lt;/div&gt;", escapedString);
    }
    
    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;div&gt;&amp;nbsp;&lt;/div&gt;");
        assertEquals("<div>&nbsp;</div>", unescapedString);
    }
}