import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character("");
        eofToken = new Token.EOF();
    }

    @Test
    public void testTokenType_Doctype() {
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);
    }

    @Test
    public void testTokenType_StartTag() {
        assertEquals(Token.TokenType.StartTag, startTagToken.type);
    }

    @Test
    public void testTokenType_EndTag() {
        assertEquals(Token.TokenType.EndTag, endTagToken.type);
    }

    @Test
    public void testTokenType_Comment() {
        assertEquals(Token.TokenType.Comment, commentToken.type);
    }

    @Test
    public void testTokenType_Character() {
        assertEquals(Token.TokenType.Character, characterToken.type);
    }

    @Test
    public void testTokenType_EOF() {
        assertEquals(Token.TokenType.EOF, eofToken.type);
    }

    @Test
    public void testDoctype_GetName() {
        doctypeToken.name.append("html");
        assertEquals("html", doctypeToken.getName());
    }

    @Test
    public void testDoctype_GetPublicIdentifier() {
        doctypeToken.publicIdentifier.append("-//W3C//DTD HTML 4.0//EN");
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctypeToken.getPublicIdentifier());
    }

    @Test
    public void testDoctype_GetSystemIdentifier() {
        doctypeToken.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeToken.getSystemIdentifier());
    }

    @Test
    public void testDoctype_IsForceQuirks() {
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTag_Constructor_TagName() {
        Token.Attributes attributes = new Token.Attributes();
        startTagToken = new Token.StartTag("div", attributes);
        assertEquals("div", startTagToken.tagName);
        assertEquals(attributes, startTagToken.attributes);
    }

    @Test
    public void testStartTag_Constructor_TagName_NullAttributes() {
        startTagToken = new Token.StartTag("div", null);
        assertEquals("div", startTagToken.tagName);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testStartTag_IsSelfClosing_False() {
        assertFalse(startTagToken.isSelfClosing());
    }

    @Test
    public void testEndTag_Constructor_TagName() {
        endTagToken = new Token.EndTag("div");
        assertEquals("div", endTagToken.tagName);
    }

    @Test
    public void testComment_GetData() {
        commentToken.data.append("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
    }

    @Test
    public void testCharacter_GetData() {
        characterToken = new Token.Character("Lorem Ipsum");
        assertEquals("Lorem Ipsum", characterToken.getData());
    }

    @Test
    public void testEOF_IsEOF() {
        assertTrue(eofToken.isEOF());
    }
}