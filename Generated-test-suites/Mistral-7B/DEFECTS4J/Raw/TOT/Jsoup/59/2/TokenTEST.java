import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.toString());

        doctype.setPubSysKey("PSK");
        assertEquals("PSK", doctype.getPubSysKey());

        doctype.setPublicIdentifier("PID");
        assertEquals("PID", doctype.getPublicIdentifier());

        doctype.setSystemIdentifier("SID");
        assertEquals("SID", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagTokenStartTag() {
        Token startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType().name());

        startTag.name("html");
        assertEquals("html", startTag.name());
        assertNull(startTag.getAttributes());

        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test");
        assertEquals("class", startTag.pendingAttributeName);
        assertEquals("test", startTag.pendingAttributeValue.toString());
        assertEquals(1, startTag.attributes.size());

        startTag.newAttribute();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("testId");
        assertEquals("id", startTag.pendingAttributeName);
        assertEquals("testId", startTag.pendingAttributeValue.toString());
        assertEquals(2, startTag.attributes.size());

        startTag.finaliseTag();
        assertEquals("<html class=\"test\" id=\"testId\">", startTag.toString());
    }

    @Test
    public void testTagTokenEndTag() {
        Token endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType().name());

        endTag.name("html");
        assertEquals("html", endTag.name());
        assertNull(endTag.getAttributes());

        assertEquals("</html>", endTag.toString());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType().name());

        comment.appendAttributeValue("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        assertEquals("Character", character.tokenType().name());

        character.data("Hello World!");
        assertEquals("Hello World!", character.getData());
        assertEquals("Hello World!", character.toString());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType().name());

        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());

        assertTrue(eof.isEOF());
    }

    @Test
    public void testResetToken() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        doctype.getName("html");
        startTag.name("html");
        endTag.name("html");
        comment.appendAttributeValue("This is a comment.");
        character.data("Hello World!");

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);
        assertFalse(startTag.isSelfClosing());

        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertNull(endTag.attributes);
        assertFalse(endTag.isSelfClosing());

        comment.reset();
        assertNull(comment.data);
        assertFalse(comment.bogus);

        character.reset();
        assertNull(character.data);

        eof.reset();
        assertNull(eof);
    }
}