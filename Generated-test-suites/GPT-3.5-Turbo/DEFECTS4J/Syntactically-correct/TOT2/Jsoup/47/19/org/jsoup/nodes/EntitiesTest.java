package org.jsoup.nodes;

import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.CoreCharset;
import org.jsoup.nodes.EntitiesTest;
import org.jsoup.parser.Document;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;
import java.nio.charset.CharsetEncoder;
import java.io.IOException;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertNull(Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape() {
        Document.OutputSettings out = new Document.OutputSettings();
        out.escapeMode(EscapeMode.xhtml);
        String htmlString = "<div>Hello &amp; World</div>";
        String escapedString = Entities.escape(htmlString, out);
        assertEquals("&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String htmlString = "&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;";
        String unescapedString = Entities.unescape(htmlString);
        assertEquals("<div>Hello &amp; World</div>", unescapedString);
    }

    @Test
    public void testEscapeWithNullDocumentOutputSettings(){
        String htmlString = "<div>Hello &amp; World</div>";
        String escapedString = Entities.escape(htmlString, null);
        assertEquals("<div>Hello &amp; World</div>", escapedString);
    }

    @Test
    public void testUnescapeWithStrictFlag(){
        String htmlString = "&lt;div&gt;Hello &amp;amp; World&lt;/div&gt;";
        String unescapedString = Entities.unescape(htmlString, true);
        assertEquals("<div>Hello &amp; World</div>", unescapedString);
    }

    @Test
    public void testCanEncode(){
        CharsetEncoder encoder = CharsetEncoder.forName("UTF-8");
        assertTrue(Entities.canEncode(CoreCharset.utf, 'a', encoder));
        assertFalse(Entities.canEncode(CoreCharset.utf, '\u0100', encoder));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '\u0100', encoder));
    }

    @Test
    public void testToCharacterKey(){
        String testValue = "test";
        String testValueCaps = "TEST";
        assertEquals(Character.valueOf('t'), Entities.toCharacterKey(Collections.singletonMap(testValue, 't')).keySet().iterator().next());
        assertEquals(Character.valueOf('T'), Entities.toCharacterKey(Collections.singletonMap(testValueCaps, 'T')).keySet().iterator().next());
    }

}