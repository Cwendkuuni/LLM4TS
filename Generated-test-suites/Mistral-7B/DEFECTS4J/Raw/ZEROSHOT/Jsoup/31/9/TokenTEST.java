import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        String expectedType = "Doctype";
        String actualType = doctypeToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testDoctypeToken_getName() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.name("html");
        String expectedName = "html";
        String actualName = doctypeToken.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testDoctypeToken_getPublicIdentifier() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.publicIdentifier("HTML");
        String expectedPublicIdentifier = "HTML";
        String actualPublicIdentifier = doctypeToken.getPublicIdentifier();
        assertEquals(expectedPublicIdentifier, actualPublicIdentifier);
    }

    @Test
    public void testDoctypeToken_getSystemIdentifier() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.systemIdentifier("index.html");
        String expectedSystemIdentifier = "index.html";
        String actualSystemIdentifier = doctypeToken.getSystemIdentifier();
        assertEquals(expectedSystemIdentifier, actualSystemIdentifier);
    }

    @Test
    public void testDoctypeToken_isForceQuirks() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.forceQuirks(true);
        boolean expectedForceQuirks = true;
        boolean actualForceQuirks = doctypeToken.isForceQuirks();
        assertEquals(expectedForceQuirks, actualForceQuirks);
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        String expectedType = "StartTag";
        String actualType = tagToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testTagToken_name() {
        Token tagToken = new Token.StartTag("div");
        String expectedName = "div";
        String actualName = tagToken.name();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testTagToken_isSelfClosing() {
        Token tagToken = new Token.StartTag("img");
        boolean expectedSelfClosing = false;
        boolean actualSelfClosing = tagToken.isSelfClosing();
        assertEquals(expectedSelfClosing, actualSelfClosing);

        Token imgTag = new Token.StartTag("img").selfClosing(true);
        boolean imgTagSelfClosing = true;
        boolean imgTagActualSelfClosing = imgTag.isSelfClosing();
        assertEquals(imgTagSelfClosing, imgTagActualSelfClosing);
    }

    @Test
    public void testTagToken_getAttributes() {
        Token tagToken = new Token.StartTag("input").attributes(new org.jsoup.nodes.Attributes().put("type", "text"));
        Attributes expectedAttributes = new org.jsoup.nodes.Attributes().put("type", "text");
        Attributes actualAttributes = tagToken.getAttributes();
        assertEquals(expectedAttributes, actualAttributes);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag("div");
        String expectedType = "EndTag";
        String actualType = endTagToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testEndTagToken_name() {
        Token endTagToken = new Token.EndTag("div");
        String expectedName = "div";
        String actualName = endTagToken.name();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        String expectedType = "Comment";
        String actualType = commentToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testCommentToken_getData() {
        Token commentToken = new Token.Comment();
        commentToken.data("This is a comment.");
        String expectedData = "This is a comment.";
        String actualData = commentToken.getData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character('&');
        String expectedType = "Character";
        String actualType = characterToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testCharacterToken_getData() {
        Token characterToken = new Token.Character('&');
        String expectedData = "&";
        String actualData = characterToken.getData();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        String expectedType = "EOF";
        String actualType = eofToken.tokenType();
        assertEquals(expectedType, actualType);
    }

    @Test(expected = AssertionError.class)
    public void testTokenTypeMismatch() {
        Token tagToken = new Token.StartTag("div");
        Token eofToken = new Token.EOF();

        tagToken.asEndTag();
        eofToken.asStartTag();
    }
}