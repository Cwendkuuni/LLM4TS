import org.jsoup.nodes.Entities.EscapeMode;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Map;
import java.util.HashMap;

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
        assertEquals("&lt;", Entities.escape("<", new Document.OutputSettings(EscapeMode.xhtml)));
        assertEquals("&amp;", Entities.escape("&", new Document.OutputSettings(EscapeMode.xhtml)));
        assertEquals("\"", Entities.escape("\"", new Document.OutputSettings(EscapeMode.xhtml)));
        assertEquals("&nbsp;", Entities.escape(" ", new Document.OutputSettings(EscapeMode.xhtml)));
        assertEquals("&#x20;", Entities.escape(" ", new Document.OutputSettings(EscapeMode.xhtml)));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals(" ", Entities.unescape("&nbsp;"));
        assertEquals(" ", Entities.unescape("&#x20;"));
    }
    
    @Test
    public void testEscape_withOutputSettings() {
        Document.OutputSettings outputSettings = new Document.OutputSettings(EscapeMode.xhtml);
        assertEquals("&lt;", Entities.escape("<", outputSettings));
        assertEquals("&amp;", Entities.escape("&", outputSettings));
        assertEquals("&quot;", Entities.escape("\"", outputSettings));
        assertEquals(" ", Entities.escape(" ", outputSettings));
        assertEquals("&#x20;", Entities.escape(" ", outputSettings));
    }
    
    @Test
    public void testEscape_withAttribute_inAttribute() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "<", new Document.OutputSettings(EscapeMode.xhtml), true, false, false);
        assertEquals("<", accum.toString());
        
        accum = new StringBuilder();
        Entities.escape(accum, "<", new Document.OutputSettings(EscapeMode.xhtml), false, false, false);
        assertEquals("&lt;", accum.toString());
    }
    
    @Test
    public void testEscape_withAttribute_normaliseWhite() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, " ", new Document.OutputSettings(EscapeMode.xhtml), false, true, false);
        assertEquals(" ", accum.toString());
        
        accum = new StringBuilder();
        Entities.escape(accum, " ", new Document.OutputSettings(EscapeMode.xhtml), false, true, true);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testEscape_withAttribute_stripLeadingWhite() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, " ", new Document.OutputSettings(EscapeMode.xhtml), false, false, false);
        assertEquals(" ", accum.toString());
        
        accum = new StringBuilder();
        Entities.escape(accum, " ", new Document.OutputSettings(EscapeMode.xhtml), false, false, true);
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testEscape_withAttribute_invalidChar() {
        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, "🙂", new Document.OutputSettings(EscapeMode.xhtml), false, false, false);
        assertEquals("&#x1f642;", accum.toString());
    }
    
    @Test
    public void testUnescape_strict() {
        assertEquals("<", Entities.unescape("&lt;", true));
        assertEquals("&", Entities.unescape("&amp;", true));
        assertEquals("\"", Entities.unescape("&quot;", true));
        assertEquals(" ", Entities.unescape("&nbsp;", true));
        assertEquals(" ", Entities.unescape("&#x20;", true));
    }
    
    @Test
    public void testUnescape_nonStrict() {
        assertEquals("<", Entities.unescape("&lt;"));
        assertEquals("&", Entities.unescape("&amp;"));
        assertEquals("\"", Entities.unescape("&quot;"));
        assertEquals(" ", Entities.unescape("&nbsp;"));
        assertEquals(" ", Entities.unescape("&#x20;"));
        assertEquals("", Entities.unescape("&invalid;"));
    }
    
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, '<', null));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '🙂', null));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '🙂', null));
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        assertEquals( Ώ, entities.get("Ogr").charValue());
        assertEquals( Ώ, entities.get("Ogr").charValue());
    }
    
    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        inMap.put("amp", '&');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertEquals("lt", outMap.get('<'));
        assertEquals("amp", outMap.get('&'));
    }
}