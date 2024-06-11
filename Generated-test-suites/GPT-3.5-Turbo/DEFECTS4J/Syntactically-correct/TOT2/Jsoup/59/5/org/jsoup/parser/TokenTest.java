import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes());
        startTag.addAttribute("class", "test");
        startTag.reset();
        assertNull(startTag.tagname);
        assertNull(startTag.normalName);
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.getAttributes());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("test data");
        assertEquals("test data", character.getData());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");
        assertFalse(comment.isBogus());
        assertEquals("test comment", comment.getData());
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.getType());
    }

    @Test
    public void testTokenTypeCheckers() {
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());
    }
}