import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        doctype.reset();

        doctype.getName().equals("doctype");
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setName("html");
        doctype.setPubSysKey("SYSTEM");
        doctype.setPublicIdentifier("DTD XHTML 1.0 Strict");
        doctype.setSystemIdentifier("/path/to/dtd/xhtml1-strict.dtd");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("SYSTEM", doctype.getPubSysKey());
        assertEquals("DTD XHTML 1.0 Strict", doctype.getPublicIdentifier());
        assertEquals("/path/to/dtd/xhtml1-strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
    }

    @Test
    public void testTagToken() {
        Token tag = new Token.StartTag();
        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);

        tag.name("html");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("default");
        tag.newAttribute();
        tag.appendAttributeName("lang");
        tag.appendAttributeValue("en");
        tag.finaliseTag();

        assertEquals("html", tag.tagName);
        assertEquals("html", tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("default", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNotNull(tag.attributes);
        assertEquals("default", tag.attributes.getClass().getName());
        assertEquals("class", tag.attributes.keySet().iterator().next());
        assertEquals("default", tag.attributes.get("class"));
        assertEquals("en", tag.attributes.get("lang"));

        tag.reset();
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        comment.reset();

        comment.getData().equals("");
        assertFalse(comment.bogus);

        comment.appendAttributeName("data");
        comment.appendAttributeValue("test");
        comment.finaliseTag();

        assertEquals("<!--", comment.toString());
        assertEquals("data", comment.pendingAttributeName);
        assertEquals("test", comment.pendingAttributeValue.toString());
        assertTrue(comment.bogus);

        comment.reset();
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        character.reset();

        character.getData().equals("");

        character.data("Hello World!");
        assertEquals("Hello World!", character.getData());

        character.reset();
    }

    @Test
    public void testCDataToken() {
        Token cdata = new Token.CData("Hello World!");

        assertEquals("<![CDATA[", cdata.toString());
        assertEquals("Hello World!", cdata.getData());

        cdata.reset();
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        assertTrue(eof.isEOF());

        eof.reset();
    }
}