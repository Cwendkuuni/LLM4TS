import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testTokenInstanceOf() {
        Token token = new Token.Doctype();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Doctype);

        token = new Token.StartTag("html");
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.StartTag);

        token = new Token.EndTag("html");
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.EndTag);

        token = new Token.Comment();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Comment);

        token = new Token.Character(' ');
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Character);

        token = new Token.EOF();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.EOF);
    }

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.tokenType());

        token = new Token.StartTag("html");
        assertEquals(TokenType.StartTag, token.tokenType());

        token = new Token.EndTag("html");
        assertEquals(TokenType.EndTag, token.tokenType());

        token = new Token.Comment();
        assertEquals(TokenType.Comment, token.tokenType());

        token = new Token.Character(' ');
        assertEquals(TokenType.Character, token.tokenType());

        token = new Token.EOF();
        assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    public void testDoctypeInstanceMethods() {
        Token token = new Token.Doctype();
        assertNull(token.getName());
        assertFalse(token.isForceQuirks());

        token = new Token.Doctype();
        token.getName().append("html");
        assertEquals("html", token.getName());

        token.setForceQuirks(true);
        assertTrue(token.isForceQuirks());
    }

    @Test
    public void testTagInstanceMethods() {
        Token token = new Token.StartTag("html");
        assertEquals("html", token.name());
        assertFalse(token.isSelfClosing());
        assertNull(token.getAttributes());

        token.name("body");
        assertEquals("body", token.name());

        token.newAttribute();
        token.appendAttributeName("class");
        token.appendAttributeValue("myClass");
        token.newAttribute();
        token.appendAttributeName("id");
        token.appendAttributeValue("myId");
        assertEquals("class=\"myClass\" id=\"myId\"", token.getAttributes().toString());

        token.finaliseTag();
        assertEquals(1, token.getAttributes().size());

        token = new Token.EndTag("html");
        assertEquals("html", token.name());
        assertFalse(token.isSelfClosing());
        assertNull(token.getAttributes());
    }

    @Test
    public void testCommentInstanceMethods() {
        Token token = new Token.Comment();
        assertNull(token.getData());

        token.getData().append("This is a comment.");
        assertEquals("This is a comment.", token.getData());
    }

    @Test
    public void testCharacterInstanceMethods() {
        Token token = new Token.Character(' ');
        assertEquals(" ", token.getData());
    }

    @Test
    public void testTokenTypeEnum() {
        TokenType type = TokenType.Doctype;
        assertEquals("Doctype", type.toString());

        type = TokenType.StartTag;
        assertEquals("StartTag", type.toString());

        type = TokenType.EndTag;
        assertEquals("EndTag", type.toString());

        type = TokenType.Comment;
        assertEquals("Comment", type.toString());

        type = TokenType.Character;
        assertEquals("Character", type.toString());

        type = TokenType.EOF;
        assertEquals("EOF", type.toString());
    }

    @Test
    public void testTokenInstanceMethods() {
        Token token = new Token.Doctype();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.StartTag("html");

        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EndTag("html");

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.Comment();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.Character(' ');

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EOF();

        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());

        token = token.asDoctype();
        assertSame(token, token);
        assertSame(token.getClass(), Token.Doctype.class);

        token = token.asStartTag();
        assertNull(token);

        token = token.asEndTag();
        assertNull(token);

        token = token.asComment();
        assertNull(token);

        token = token.asCharacter();
        assertNull(token);

        token = token.asEOF();
        assertNull(token);
    }
}