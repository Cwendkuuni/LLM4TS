import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "div";
        startTag.attributes.put("id", "test");

        assertEquals("<div id=\"test\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";

        assertEquals("</p>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");

        assertEquals("comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data("test");

        assertEquals("test", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isEndTag());
    }

    // Add more test cases for edge cases and exception handling scenarios
    
}