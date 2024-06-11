import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");

        assertEquals("testName", doctype.getName());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("testValue");

        assertEquals("testValue", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("tagName");
        String expected = "<tagName>";

        assertEquals(expected, startTag.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.EOF, eof.type);
    }

}