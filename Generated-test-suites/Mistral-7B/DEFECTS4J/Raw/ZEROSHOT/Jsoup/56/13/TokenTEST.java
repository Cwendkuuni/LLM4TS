import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals(TokenType.Doctype, doctypeToken.tokenType());
        assertEquals(TokenType.StartTag, startTagToken.tokenType());
        assertEquals(TokenType.EndTag, endTagToken.tokenType());
        assertEquals(TokenType.Comment, commentToken.tokenType());
        assertEquals(TokenType.Character, characterToken.tokenType());
        assertEquals(TokenType.EOF, eofToken.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        doctypeToken.asDoctype().getName().append("html");
        doctypeToken.reset();
        assertNull(doctypeToken.asDoctype().getName());
    }

    @Test
    public void testStartTagReset() {
        startTagToken.name("html");
        startTagToken.reset();
        assertNull(startTagToken.name());
    }

    @Test
    public void testEndTagReset() {
        endTagToken.name("html");
        endTagToken.reset();
        assertNull(endTagToken.name());
    }

    @Test
    public void testCommentReset() {
        commentToken.getData().append("this is a test comment");
        commentToken.reset();
        assertNull(commentToken.getData());
    }

    @Test
    public void testCharacterReset() {
        characterToken.getData().append("&");
        characterToken.reset();
        assertNull(characterToken.getData());
    }

    @Test
    public void testEOFReset() {
        // EOF does not have any data to reset
    }

    @Test
    public void testDoctypeGetters() {
        doctypeToken.asDoctype().getName().append("html");
        doctypeToken.asDoctype().getPublicIdentifier().append("public id");
        doctypeToken.asDoctype().setForceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("public id", doctypeToken.asDoctype().getPublicIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testTagGetters() {
        startTagToken.name("html").attributes(new Attributes().put("id", "test"));
        endTagToken.name("body");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.asStartTag().name());
        assertEquals("id", startTagToken.asStartTag().getAttributes().getValue("id"));
        assertEquals("body", endTagToken.name());
        assertNull(endTagToken.getAttributes());
    }

    @Test
    public void testCommentGetter() {
        commentToken.getData().append("this is a test comment");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("this is a test comment", commentToken.asComment().getData());
    }

    @Test
    public void testCharacterGetter() {
        characterToken.getData().append("&");

        assertEquals("Character", characterToken.tokenType());
        assertEquals("&", characterToken.asCharacter().getData());
    }

    @Test
    public void testEOFGetter() {
        assertEquals("EOF", eofToken.tokenType());
        // EOF does not have any data to get
    }
}