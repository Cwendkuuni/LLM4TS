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
        doctype.reset();
        assertNull(doctype.getPubSysKey());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("test");
        tag.reset();
        assertNull(tag.name());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Test Character");
        assertEquals("Test Character", character.getData());
    }

    @Test
    public void testStartTag() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("div");
        tag.appendAttributeValue("id", "test");
        assertTrue(tag.isStartTag());
        assertEquals("div", tag.name());
        assertTrue(tag.getAttributes().hasKey("id"));
    }

    @Test
    public void testEndTag() {
        Token.Tag tag = new Token.EndTag();
        tag.name("div");
        assertTrue(tag.isEndTag());
        assertEquals("div", tag.name());
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