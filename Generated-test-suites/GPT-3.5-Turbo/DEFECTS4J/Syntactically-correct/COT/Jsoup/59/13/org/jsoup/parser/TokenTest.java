import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("doctype");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tag";
        tag.normalName = "tag";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "value";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();

        startTag.nameAttr("tag", attributes);

        assertEquals("tag", startTag.tagName);
        assertEquals("tag", startTag.normalName);
        assertSame(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        Attributes attributes = new Attributes();
        attributes.put("attr", "value");
        startTag.attributes = attributes;

        assertEquals("<tag attr=\"value\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";

        assertEquals("<tag>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();

        character.data("data");

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertEquals(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();

        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;

        assertEquals(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();

        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;

        assertEquals(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();

        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;

        assertEquals(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();

        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;

        assertEquals(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();

        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;

        assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenTypeDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testTokenTypeStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testTokenTypeEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testTokenTypeComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testTokenTypeCharacter() {
        Token.Character character = new Token.Character();

        assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testTokenTypeEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.EOF, eof.type);
    }
}