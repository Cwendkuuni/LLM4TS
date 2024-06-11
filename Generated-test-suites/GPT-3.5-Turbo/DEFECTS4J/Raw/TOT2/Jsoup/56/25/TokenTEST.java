import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.reset();
        assertNull(tag.tagName);
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data("Test data");
        assertEquals("Test data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}