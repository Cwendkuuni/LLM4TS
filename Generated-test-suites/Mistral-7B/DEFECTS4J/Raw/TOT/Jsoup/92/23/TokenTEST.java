import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        doctype.getName("html");
        doctype.setForceQuirks(true);

        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.tokenType());
        assertEquals("Doctype", resetDoctype.getClass().getSimpleName());
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());

        assertEquals("html", doctype.getName());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTag = new Token.StartTag();
        startTag.name("html");
        startTag.attributes(new Attributes().put("id", "root"));

        Token resetStartTag = startTag.reset();
        assertEquals(TokenType.StartTag, resetStartTag.tokenType());
        assertEquals("StartTag", resetStartTag.getClass().getSimpleName());
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertNull(resetStartTag.pendingAttributeValue);
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.getAttributes());

        assertEquals("html", startTag.tagName);
        assertEquals("html", startTag.name());
        assertEquals("html", startTag.name().toLowerCase());
        assertFalse(startTag.isSelfClosing());
        assertNotNull(startTag.getAttributes());
        assertEquals("root", startTag.getAttributes().toString());
    }

    @Test
    public void testEndTagToken() {
        Token endTag = new Token.EndTag();
        endTag.name("html");

        Token resetEndTag = endTag.reset();
        assertEquals(TokenType.EndTag, resetEndTag.tokenType());
        assertEquals("EndTag", resetEndTag.getClass().getSimpleName());
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertNull(resetEndTag.pendingAttributeValue);
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.getAttributes());

        assertEquals("html", endTag.tagName);
        assertEquals("html", endTag.name());
        assertEquals("html", endTag.name().toLowerCase());
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());
    }

    @Test
    public void testCommentToken() {
        Token comment = new Token.Comment();
        comment.data("This is a comment");

        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.tokenType());
        assertEquals("Comment", resetComment.getClass().getSimpleName());
        assertNull(resetComment.getData());
        assertFalse(resetComment.isForceQuirks());
        assertFalse(resetComment.isSelfClosing());
        assertNull(resetComment.getAttributes());

        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.isForceQuirks());
        assertFalse(comment.isSelfClosing());
        assertNull(comment.getAttributes());
    }

    @Test
    public void testCharacterToken() {
        Token character = new Token.Character();
        character.data("&amp;");

        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.tokenType());
        assertEquals("Character", resetCharacter.getClass().getSimpleName());
        assertNull(resetCharacter.getData());
        assertFalse(resetCharacter.isForceQuirks());
        assertFalse(resetCharacter.isSelfClosing());
        assertNull(resetCharacter.getAttributes());

        assertEquals("&amp;", character.getData());
        assertFalse(character.isForceQuirks());
        assertFalse(character.isSelfClosing());
        assertNull(character.getAttributes());
    }

    @Test
    public void testCDataToken() {
        Token cData = new Token.CData("<![CDATA[&amp;<foo>]]>");

        Token resetCData = cData.reset();
        assertEquals(TokenType.Character, resetCData.tokenType());
        assertEquals("Character", resetCData.getClass().getSimpleName());
        assertNull(resetCData.getData());
        assertFalse(resetCData.isForceQuirks());
        assertFalse(resetCData.isSelfClosing());
        assertNull(resetCData.getAttributes());

        assertEquals("<![CDATA[&amp;<foo>]]>", cData.toString());
        assertFalse(cData.isForceQuirks());
        assertFalse(cData.isSelfClosing());
        assertNull(cData.getAttributes());
    }

    @Test
    public void testEOFToken() {
        Token eof = new Token.EOF();

        Token resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.tokenType());
        assertEquals("EOF", resetEof.getClass().getSimpleName());
        assertNull(resetEof.getData());
        assertFalse(resetEof.isForceQuirks());
        assertFalse(resetEof.isSelfClosing());
        assertNull(resetEof.getAttributes());

        assertTrue(eof.isEOF());
        assertNull(eof.getData());
        assertFalse(eof.isForceQuirks());
        assertFalse(eof.isSelfClosing());
        assertNull(eof.getAttributes());
    }
}