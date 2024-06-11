import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Token doctypeToken;
    private Token startTagToken;
    private Token endTagToken;
    private Token commentToken;
    private Token characterToken;
    private Token eofToken;

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
    public void testTokenType() {
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("Comment", commentToken.tokenType());
        assertEquals("Character", characterToken.tokenType());
        assertEquals("EOF", eofToken.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        doctypeToken.asDoctype().getName().append("html");
        doctypeToken.asDoctype().setForceQuirks(true);

        doctypeToken.reset();

        assertNull(doctypeToken.asDoctype().getName());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTagToken.name("html").selfClosing(true);

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertFalse(startTagToken.isSelfClosing());
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTagToken.name("html");

        endTagToken.reset();

        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertFalse(endTagToken.isSelfClosing());
        assertNull(endTagToken.getAttributes());
    }

    @Test
    public void testCommentReset() {
        commentToken.getData().append("This is a comment");

        commentToken.reset();

        assertNull(commentToken.getData());
        assertFalse(commentToken.isBogus());
    }

    @Test
    public void testCharacterReset() {
        characterToken.getData().append("A");

        characterToken.reset();

        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFReset() {
        eofToken.reset();

        assertNull(eofToken.getData());
    }

    @Test
    public void testTokenIsDoctype() {
        assertTrue(doctypeToken.isDoctype());
        assertFalse(startTagToken.isDoctype());
        assertFalse(endTagToken.isDoctype());
        assertFalse(commentToken.isDoctype());
        assertFalse(characterToken.isDoctype());
        assertFalse(eofToken.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        assertFalse(doctypeToken.isStartTag());
        assertFalse(endTagToken.isStartTag());
        assertFalse(commentToken.isStartTag());
        assertFalse(characterToken.isStartTag());
        assertFalse(eofToken.isStartTag());

        startTagToken.name("html");

        assertTrue(startTagToken.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        assertFalse(doctypeToken.isEndTag());
        assertFalse(startTagToken.isEndTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(eofToken.isEndTag());

        endTagToken.name("/html");

        assertTrue(endTagToken.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        assertFalse(doctypeToken.isComment());
        assertFalse(startTagToken.isComment());
        assertFalse(endTagToken.isComment());
        assertFalse(characterToken.isComment());
        assertFalse(eofToken.isComment());

        commentToken.getData().append("<!-- This is a comment -->");

        assertTrue(commentToken.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        assertFalse(doctypeToken.isCharacter());
        assertFalse(startTagToken.isCharacter());
        assertFalse(endTagToken.isCharacter());
        assertFalse(commentToken.isCharacter());
        assertFalse(eofToken.isCharacter());

        characterToken.getData().append("A");

        assertTrue(characterToken.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        assertFalse(doctypeToken.isEOF());
        assertFalse(startTagToken.isEOF());
        assertFalse(endTagToken.isEOF());
        assertFalse(commentToken.isEOF());
        assertFalse(characterToken.isEOF());

        eofToken.reset();

        assertTrue(eofToken.isEOF());
    }
}