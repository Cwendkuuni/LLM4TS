import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.Jsoup;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("fake"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("fake"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("fake"));
    }

    @Test
    public void testEscape() {
        OutputSettings out = new OutputSettings();
        String escaped = Entities.escape("This is a <test> & 'string'", out);
        assertEquals("This is a &lt;test&gt; &amp; &#39;string&#39;", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("This is a &lt;test&gt; &amp; &#39;string&#39;");
        assertEquals("This is a <test> & 'string'", unescaped);
    }

    @Test
    public void testEscapeWithOutputSettings() {
        OutputSettings out = new OutputSettings();
        out.escapeMode(EscapeMode.xhtml);
        String escaped = Entities.escape("This is a <test> & 'string'", out);
        assertEquals("This is a &lt;test&gt; &amp; 'string'", escaped);
    }

    @Test
    public void testCoreCharset() {
        assertTrue(Entities.canEncode(CoreCharset.ascii, 'A', null));
        assertTrue(Entities.canEncode(CoreCharset.utf, '😀', null));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '😀', null));
        assertFalse(Entities.canEncode(CoreCharset.utf, '\uD840', null));
    }

    @Test
    public void testEntitiesLoading() {
        assertNotNull(Entities.full);
        assertNotNull(Entities.xhtmlByVal);
        assertNotNull(Entities.base);
        assertNotNull(Entities.baseByVal);
        assertNotNull(Entities.fullByVal);
    }

}