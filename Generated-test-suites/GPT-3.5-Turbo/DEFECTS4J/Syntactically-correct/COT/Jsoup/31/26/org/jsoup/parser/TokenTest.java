import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testToken_type() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testToken_getName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("DOCTYPE");
        assertEquals("DOCTYPE", doctype.getName());
    }

    @Test
    public void testToken_getPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0//EN");
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testToken_getSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testToken_isForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTag_name() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
    }

    @Test
    public void testTag_isSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTag_getAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testComment_getData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter_getData() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testToken_isDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testToken_asDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.asDoctype() instanceof Token.Doctype);
    }

    @Test
    public void testToken_isStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testToken_asStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.asStartTag() instanceof Token.StartTag);
    }

    @Test
    public void testToken_isEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testToken_asEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.asEndTag() instanceof Token.EndTag);
    }

    @Test
    public void testToken_isComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testToken_asComment() {
        Token token = new Token.Comment();
        assertTrue(token.asComment() instanceof Token.Comment);
    }

    @Test
    public void testToken_isCharacter() {
        Token token = new Token.Character("This is a character");
        assertTrue(token.isCharacter());
    }

    @Test
    public void testToken_asCharacter() {
        Token token = new Token.Character("This is a character");
        assertTrue(token.asCharacter() instanceof Token.Character);
    }

    @Test
    public void testToken_isEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }

}