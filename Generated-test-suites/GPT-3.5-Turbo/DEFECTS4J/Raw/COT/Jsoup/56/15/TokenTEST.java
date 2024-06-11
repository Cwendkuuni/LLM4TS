
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();
        assertEquals("", resetDoctype.getName());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "name";
        startTag.normalName = "name";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertTrue(resetStartTag.pendingAttributeValue.length() == 0);
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "name";
        endTag.normalName = "name";
        endTag.pendingAttributeName = "attribute";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        Token.EndTag resetEndTag = endTag.reset();
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
        assertNull(resetEndTag.pendingAttributeName);
        assertTrue(resetEndTag.pendingAttributeValue.length() == 0);
        assertNull(resetEndTag.pendingAttributeValueS);
        assertFalse(resetEndTag.hasEmptyAttributeValue);
        assertFalse(resetEndTag.hasPendingAttributeValue);
        assertFalse(resetEndTag.selfClosing);
        assertNull(resetEndTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();
        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Token.Character resetCharacter = character.reset();
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();
        assertNotNull(resetEOF);
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assertSame(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assertSame(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assertSame(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assertSame(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token token = character;
        assertSame(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeEnum() {
        assertEquals(6, Token.TokenType.values().length);
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}

