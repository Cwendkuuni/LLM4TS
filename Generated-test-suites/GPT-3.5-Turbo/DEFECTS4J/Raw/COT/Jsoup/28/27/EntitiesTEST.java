
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.CharsetEncoder;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals((Character) '<', character);
    }

    @Test
    public void testEscape() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String escapedString = Entities.escape("<p>Test</p>", outputSettings);
        Assert.assertEquals("&lt;p&gt;Test&lt;/p&gt;", escapedString);
    }

    @Test
    public void testUnescape() {
        String unescapedString = Entities.unescape("&lt;p&gt;Test&lt;/p&gt;");
        Assert.assertEquals("<p>Test</p>", unescapedString);
    }

    @Test
    public void testUnescapeStrict() {
        String unescapedString = Entities.unescape("&#60;p&#62;Test&#60;/p&#62;", true);
        Assert.assertEquals("<p>Test</p>", unescapedString);
    }

    @Test
    public void testUnescapeNonStrict() {
        String unescapedString = Entities.unescape("&#60;p&#62;Test&#60;/p&#62;", false);
        Assert.assertEquals("<p>Test</p>", unescapedString);
    }

    @Test
    public void testEscapeWithEncoder() {
        CharsetEncoder encoder = CharsetEncoder.getDefaultCharset().newEncoder();
        EscapeMode escapeMode = EscapeMode.xhtml;
        String escapedString = Entities.escape("<p>Test</p>", encoder, escapeMode);
        Assert.assertEquals("&lt;p&gt;Test&lt;/p&gt;", escapedString);
    }
}
