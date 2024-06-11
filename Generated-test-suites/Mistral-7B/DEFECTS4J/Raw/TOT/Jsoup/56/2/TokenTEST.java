import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

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
    public void testDoctypeToken() {
        Token doctype = new Token.Doctype();
        doctype.newAttribute();
        doctype.name("html");
        doctype.getName();
        assertEquals("Doctype", doctype.tokenType());
        doctype.reset();

        doctype.isDoctype();
        assertTrue(doctype.isDoctype());

        doctype.asDoctype();
        assertEquals(doctype, doctype.asDoctype());

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        startTagToken.name("div");
        startTagToken.newAttribute();
        startTagToken.name("class");
        startTagToken.setValue("test-class");
        startTagToken.newAttribute();
        startTagToken.name("id");
        startTagToken.setValue("test-id");

        assertEquals("StartTag", startTagToken.tokenType().name());
        assertEquals("div", startTagToken.name());
        assertEquals("test-class", startTagToken.getAttributes().get("class"));
        assertEquals("test-id", startTagToken.getAttributes().get("id"));

        startTagToken.isSelfClosing();
        assertFalse(startTagToken.isSelfClosing());

        startTagToken.finaliseTag();

        startTagToken.getAttributes().removeAttribute("class");
        startTagToken.getAttributes().removeAttribute("id");

        startTagToken.reset();
        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testEndTagToken() {
        endTagToken.name("div");
        assertEquals("EndTag", endTagToken.tokenType().name());
        assertEquals("div", endTagToken.name());
        assertFalse(endTagToken.isSelfClosing());
    }

    @Test
    public void testCommentToken() {
        commentToken.appendAttributeValue("-- This is a comment --");
        assertEquals("Comment", commentToken.tokenType().name());
        assertEquals("-- This is a comment --", commentToken.getData());

        commentToken.bogus = true;
        assertTrue(commentToken.bogus);

        commentToken.reset();
        assertTrue(commentToken.isComment());
        assertNull(commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        characterToken.data("Hello World!");
        assertEquals("Character", characterToken.tokenType().name());
        assertEquals("Hello World!", characterToken.getData());

        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFToken() {
        assertTrue(eofToken.isEOF());
        assertEquals("EOF", eofToken.tokenType().name());

        eofToken.reset();
        assertFalse(eofToken.isEOF());
    }
}