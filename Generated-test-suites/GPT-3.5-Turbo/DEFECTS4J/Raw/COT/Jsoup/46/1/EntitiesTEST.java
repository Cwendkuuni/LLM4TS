import org.junit.Test;
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
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals("", Entities.escape("", out));
        assertEquals("Hello world", Entities.escape("Hello world", out));
        assertEquals("&lt;tag&gt;", Entities.escape("<tag>", out));
        assertEquals("123&amp;456", Entities.escape("123&456", out));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("", Entities.unescape(""));
        assertEquals("Hello world", Entities.unescape("Hello world"));
        assertEquals("<tag>", Entities.unescape("&lt;tag&gt;"));
        assertEquals("123&456", Entities.unescape("123&amp;456"));
    }
    
    @Test
    public void testUnescapeStrict() {
        assertEquals("", Entities.unescape("", true));
        assertEquals("Hello world", Entities.unescape("Hello world", true));
        assertEquals("<tag>", Entities.unescape("&lt;tag&gt;", true));
        assertEquals("123&456", Entities.unescape("123&amp;456", true));
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testEscapeWithNormaliseWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.normaliseWhite(true);
        assertEquals("Hello world", Entities.escape("Hello world", out));
        assertEquals("Hello world", Entities.escape("Hello\nworld", out));
        assertEquals("Hello world", Entities.escape("Hello\tworld", out));
    }
    
    @Test
    public void testEscapeWithStripLeadingWhite() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.stripLeadingWhite(true);
        assertEquals("Hello world", Entities.escape("Hello world", out));
        assertEquals("Hello world", Entities.escape("  Hello world", out));
    }
    
    @Test
    public void testEscapeWithInAttribute() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<tag>", Entities.escape("<tag>", out));
        assertEquals("Hello &amp; world", Entities.escape("Hello & world", out));
    }
    
    @Test
    public void testEscapeWithCharsetEncoder() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.encoder(CharsetEncoder.getDefault());
        assertEquals("Hello world", Entities.escape("Hello world", out));
        assertEquals("123&456", Entities.escape("123&456", out));
    }
    
    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(Entities.CoreCharset.ascii, 'a', CharsetEncoder.getDefault()));
        assertFalse(Entities.canEncode(Entities.CoreCharset.ascii, '£', CharsetEncoder.getDefault()));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, 'a', CharsetEncoder.getDefault()));
        assertTrue(Entities.canEncode(Entities.CoreCharset.utf, '£', CharsetEncoder.getDefault()));
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, 'a', CharsetEncoder.getDefault()));
        assertTrue(Entities.canEncode(Entities.CoreCharset.fallback, '£', CharsetEncoder.getDefault()));
    }
    
    @Test
    public void testLoadEntities() {
        assertNotNull(Entities.loadEntities("entities-base.properties"));
        assertNotNull(Entities.loadEntities("entities-full.properties"));
    }
    
    @Test
    public void testToCharacterKey() {
        assertNotNull(Entities.toCharacterKey(Entities.full));
    }
    
}