import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        Token.StartTag startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        Token.EndTag endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        Token.Comment comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);

        Token.Character character = new Token.Character();
        assertEquals(TokenType.Character, character.type);

        Token.EOF eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeMethods() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EndTag();
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

        token = new Token.Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "html";
        doctype.publicIdentifier.append("pub");
        doctype.systemIdentifier.append("sys");
        doctype.forceQuirks = true;

        Token resetDoctype = doctype.reset();
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    // Additional test cases for other methods...

}

