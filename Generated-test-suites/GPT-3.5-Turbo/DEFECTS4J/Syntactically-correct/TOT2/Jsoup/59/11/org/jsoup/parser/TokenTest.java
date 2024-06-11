import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.publicIdentifier.append("Test Public");
        doctype.systemIdentifier.append("Test System");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("test", new org.jsoup.nodes.Attributes());

        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("test");

        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");

        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.data("Test Character");

        assertEquals("Test Character", character.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        // No specific test for reset in EOF
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