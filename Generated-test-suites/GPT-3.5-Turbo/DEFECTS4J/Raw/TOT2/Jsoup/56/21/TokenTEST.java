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
        doctype.name.append("Test");
        doctype.forceQuirks = true;
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        tag.newAttribute();

        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test");
        comment.bogus = true;
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertSame(eof, eof.reset());
    }
}