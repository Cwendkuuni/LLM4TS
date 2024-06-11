import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name.append("html");
        assertEquals("Doctype[html]", doctypeWithName.tokenType());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().forceQuirks = true;
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().name("div");
        assertEquals("StartTag[div]", startTag.tokenType());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().name("a").newAttribute().put("href", "link.html");
        String expectedTag = "<div>" +
                "<a href=\"link.html\"></a>" +
                "</div>";
        assertEquals(expectedTag, startTagWithAttributes.toString());
    }

    @org.junit.Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        endTag.asEndTag().name("p");
        assertEquals("EndTag[p]", endTag.tokenType());
    }

    @org.junit.Test
    public void testComment() {
        Token comment = new Token.Comment();
        comment.asComment().appendData("This is a comment.");
        assertEquals("Comment[This is a comment.]", comment.tokenType());
    }

    @org.junit.Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertEquals("Character[A]", character.tokenType());
    }

    @org.junit.Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @org.junit.Test(expected = ClassCastException.class)
    public void testInvalidCast() {
        Token token = new Token.Comment();
        Token invalidCast = token.asStartTag();
    }

    @org.junit.Test
    public void testIsTokenType() {
        Token token = new Token.StartTag();

        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token.asStartTag().name("div");
        assertTrue(token.isStartTag());
        assertTrue(token.asStartTag().isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token.asDoctype();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isDoctype());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token.asComment();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isDoctype());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token.asCharacter();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());

        token.asEOF();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isDoctype());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }
}