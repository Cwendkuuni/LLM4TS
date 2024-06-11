
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);
        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);
        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);
        result = Entities.isBaseNamedEntity("gt");
        Assert.assertTrue(result);
        result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', result);
        result = Entities.getCharacterByName("gt");
        Assert.assertEquals('>', result);
        result = Entities.getCharacterByName("amp");
        Assert.assertEquals('&', result);
        result = Entities.getCharacterByName("quot");
        Assert.assertEquals('"', result);
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("<tag>", null);
        Assert.assertEquals("&lt;tag&gt;", result);
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;tag&gt;");
        Assert.assertEquals("<tag>", result);
    }

    @Test
    public void testUnescapeWithStrict() {
        String result = Entities.unescape("&lt;tag&gt;", true);
        Assert.assertEquals("<tag>", result);
    }
    
    @Test
    public void testEscapeWithAttribute() {
        StringBuilder accum = new StringBuilder();
        String string = "<tag>";
        boolean inAttribute = true;
        boolean normaliseWhite = false;
        boolean stripLeadingWhite = false;
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Entities.Document.OutputSettings out = new Entities.Document.OutputSettings();

        Entities.escape(accum, string, out, inAttribute, normaliseWhite, stripLeadingWhite);
        String result = accum.toString();
        Assert.assertEquals(string, result);
    }
    
    @Test
    public void testEscapeWithNormaliseWhite() {
        StringBuilder accum = new StringBuilder();
        String string = "   ";
        boolean inAttribute = false;
        boolean normaliseWhite = true;
        boolean stripLeadingWhite = false;
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Entities.Document.OutputSettings out = new Entities.Document.OutputSettings();

        Entities.escape(accum, string, out, inAttribute, normaliseWhite, stripLeadingWhite);
        String result = accum.toString();
        Assert.assertEquals(" ", result);
    }
    
    @Test
    public void testEscapeWithStripLeadingWhite() {
        StringBuilder accum = new StringBuilder();
        String string = "   ";
        boolean inAttribute = false;
        boolean normaliseWhite = false;
        boolean stripLeadingWhite = true;
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Entities.Document.OutputSettings out = new Entities.Document.OutputSettings();

        Entities.escape(accum, string, out, inAttribute, normaliseWhite, stripLeadingWhite);
        String result = accum.toString();
        Assert.assertEquals("", result);
    }
    
    @Test
    public void testEscapeWithCodePoint() {
        StringBuilder accum = new StringBuilder();
        String string = "\uD83D\uDE01";
        boolean inAttribute = true;
        boolean normaliseWhite = false;
        boolean stripLeadingWhite = false;
        Entities.EscapeMode escapeMode = Entities.EscapeMode.xhtml;
        Entities.Document.OutputSettings out = new Entities.Document.OutputSettings();

        Entities.escape(accum, string, out, inAttribute, normaliseWhite, stripLeadingWhite);
        String result = accum.toString();
        Assert.assertEquals(string, result);
    }
}
