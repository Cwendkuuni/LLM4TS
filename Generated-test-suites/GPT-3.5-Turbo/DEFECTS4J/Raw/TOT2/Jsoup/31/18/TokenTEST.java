import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("DOCTYPE");
        assertEquals("DOCTYPE", doctype.getName());
    }

    @Test
    public void testTagGetName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tag");
        assertEquals("tag", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAppendTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendTagName("abc");
        assertEquals("abc", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeName("id");
        assertEquals("id", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("C");
        assertEquals("C", character.getData());
    }

    @Test
    public void testEOFGetType() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag tag = new Token.StartTag();
        assertTrue(tag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag tag = new Token.EndTag();
        assertTrue(tag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("C");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

}