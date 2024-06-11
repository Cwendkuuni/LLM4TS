import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        assertEquals("test", doctype.getName());
    }

    @Test
    public void testTokenTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("test");
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTokenCharacterGetData() {
        Token.Character character = new Token.Character("test");
        assertEquals("test", character.getData());
    }

    @Test
    public void testTokenIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testTokenIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testTokenIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testTokenIsCharacter() {
        Token.Character character = new Token.Character("test");
        assertTrue(character.isCharacter());
    }

    @Test
    public void testTokenEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}