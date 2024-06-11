import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("p");
        assertEquals("p", endTag.name());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Hello");
        assertEquals("Hello", character.getData());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Comment");
        assertFalse(comment.bogus);
        assertEquals("Comment", comment.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertEquals(eof, eof.reset());
    }

}