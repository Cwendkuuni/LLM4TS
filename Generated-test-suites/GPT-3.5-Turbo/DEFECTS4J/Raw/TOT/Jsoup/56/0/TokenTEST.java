import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setup() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        // Typical use case
        doctypeToken.name.append("html");
        doctypeToken.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctypeToken.forceQuirks = true;

        doctypeToken.reset();

        assertEquals("", doctypeToken.name.toString());
        assertEquals("", doctypeToken.publicIdentifier.toString());
        assertEquals("", doctypeToken.systemIdentifier.toString());
        assertFalse(doctypeToken.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        // Typical use case
        startTagToken.tagName = "div";
        startTagToken.normalName = "div";
        startTagToken.pendingAttributeName = "id";
        startTagToken.pendingAttributeValue.append("my-id");
        startTagToken.selfClosing = true;
        startTagToken.hasEmptyAttributeValue = true;
        startTagToken.hasPendingAttributeValue = true;

        startTagToken.reset();

        assertEquals(null, startTagToken.tagName);
        assertEquals(null, startTagToken.normalName);
        assertEquals(null, startTagToken.pendingAttributeName);
        assertEquals("", startTagToken.pendingAttributeValue.toString());
        assertFalse(startTagToken.selfClosing);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertEquals(null, startTagToken.attributes);
    }

    @Test
    public void testEndTagReset() {
        // Typical use case
        endTagToken.tagName = "div";
        endTagToken.normalName = "div";
        endTagToken.pendingAttributeName = "id";
        endTagToken.pendingAttributeValue.append("my-id");
        endTagToken.selfClosing = true;
        endTagToken.hasEmptyAttributeValue = true;
        endTagToken.hasPendingAttributeValue = true;

        endTagToken.reset();

        assertEquals(null, endTagToken.tagName);
        assertEquals(null, endTagToken.normalName);
        assertEquals(null, endTagToken.pendingAttributeName);
        assertEquals("", endTagToken.pendingAttributeValue.toString());
        assertFalse(endTagToken.selfClosing);
        assertFalse(endTagToken.hasEmptyAttributeValue);
        assertFalse(endTagToken.hasPendingAttributeValue);
        assertEquals(null, endTagToken.attributes);
    }

    @Test
    public void testCommentReset() {
        // Typical use case
        commentToken.data.append("This is a comment");
        commentToken.bogus = true;

        commentToken.reset();

        assertEquals("", commentToken.data.toString());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testCharacterReset() {
        // Typical use case
        characterToken.data("A");

        characterToken.reset();

        assertEquals(null, characterToken.getData());
    }

    @Test
    public void testEOFReset() {
        eofToken.reset();

        // No state to reset for EOF token
    }

    @Test
    public void testIsDoctype() {
        // Typical use case
        assertTrue(doctypeToken.isDoctype());

        // Edge case: different token type
        assertFalse(startTagToken.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        // Typical use case
        assertTrue(startTagToken.isStartTag());

        // Edge case: different token type
        assertFalse(doctypeToken.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        // Typical use case
        assertTrue(endTagToken.isEndTag());

        // Edge case: different token type
        assertFalse(doctypeToken.isEndTag());
    }

    @Test
    public void testIsComment() {
        // Typical use case
        assertTrue(commentToken.isComment());

        // Edge case: different token type
        assertFalse(doctypeToken.isComment());
    }

    @Test
    public void testIsCharacter() {
        // Typical use case
        assertTrue(characterToken.isCharacter());

        // Edge case: different token type
        assertFalse(doctypeToken.isCharacter());
    }

    @Test
    public void testIsEOF() {
        // Typical use case
        assertTrue(eofToken.isEOF());

        // Edge case: different token type
        assertFalse(doctypeToken.isEOF());
    }
}