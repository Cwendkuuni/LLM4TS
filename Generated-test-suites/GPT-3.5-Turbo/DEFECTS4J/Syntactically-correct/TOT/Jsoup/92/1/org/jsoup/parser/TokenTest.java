import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctype_reset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag_reset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "class";
        tag.hasPendingAttributeValue = true;
        tag.pendingAttributeValue.append("content");

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTag_nameAttr() {
        Token.Attributes attributes = new Token.Attributes();
        attributes.put("class", "content");
        attributes.put("id", "1");

        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", attributes);

        assertEquals("div", startTag.tagName);
        assertNotNull(startTag.attributes);
        assertEquals(2, startTag.attributes.size());
        assertEquals("content", startTag.attributes.get("class"));
        assertEquals("1", startTag.attributes.get("id"));
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTag_toString_withAttributes() {
        Token.Attributes attributes = new Token.Attributes();
        attributes.put("class", "content");
        attributes.put("id", "1");

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes = attributes;

        assertEquals("<div class=\"content\" id=\"1\">", startTag.toString());
    }

    @Test
    public void testStartTag_toString_withoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTag_toString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment_reset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacter_reset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCData_toString() {
        Token.CData cData = new Token.CData("This is CDATA");

        assertEquals("<![CDATA[This is CDATA]]>", cData.toString());
    }

    @Test
    public void testEOF_reset() {
        Token.EOF eof = new Token.EOF();

        Token result = eof.reset();

        assertSame(eof, result);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();

        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Token result = doctype.asDoctype();

        assertSame(doctype, result);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();

        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Token result = startTag.asStartTag();

        assertSame(startTag, result);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();

        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Token result = endTag.asEndTag();

        assertSame(endTag, result);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();

        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        Token result = comment.asComment();

        assertSame(comment, result);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();

        assertTrue(token.isCharacter());
    }
    
    @Test
    public void testIsCData() {
        Token token = new Token.CData("This is CDATA");

        assertTrue(token.isCData());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        Token result = character.asCharacter();

        assertSame(character, result);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenType_enum() {
        TokenType tokenType = TokenType.Doctype;

        assertEquals("Doctype", tokenType.name());
        TokenType[] values = TokenType.values();
        assertEquals(6, values.length);
        assertEquals(TokenType.Doctype, values[0]);
        assertEquals(TokenType.StartTag, values[1]);
        assertEquals(TokenType.EndTag, values[2]);
        assertEquals(TokenType.Comment, values[3]);
        assertEquals(TokenType.Character, values[4]);
        assertEquals(TokenType.EOF, values[5]);
    }
}