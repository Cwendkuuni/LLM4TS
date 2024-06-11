import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.reset();

        doctypeToken.name("html");
        doctypeToken.pubSysKey("DTD HTML 5");
        doctypeToken.publicIdentifier("public \"-//W3C//DTD HTML 5//EN\"");
        doctypeToken.systemIdentifier("/path/to/dtd/html5.dtd");
        doctypeToken.forceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertEquals("DTD HTML 5", doctypeToken.asDoctype().getPubSysKey());
        assertEquals("public \"-//W3C//DTD HTML 5//EN\"", doctypeToken.asDoctype().getPublicIdentifier());
        assertEquals("/path/to/dtd/html5.dtd", doctypeToken.asDoctype().getSystemIdentifier());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.reset();
    }

    @Test
    public void testEmptyStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.reset();

        startTagToken.name("img");
        startTagToken.attributes = new Attributes();
        startTagToken.selfClosing = true;

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("img", startTagToken.name());
        assertEquals("img", startTagToken.normalName());
        assertTrue(startTagToken.isSelfClosing());
        assertNull(startTagToken.getAttributes());

        startTagToken.newAttribute();
        startTagToken.appendAttributeName("src");
        startTagToken.appendAttributeValue("image.png");
        startTagToken.newAttribute();
        startTagToken.appendAttributeName("alt");
        startTagToken.appendAttributeValue("Image description");
        startTagToken.finaliseTag();

        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("src", "image.png");
        expectedAttributes.put("alt", "Image description");

        assertEquals(expectedAttributes.toString(), startTagToken.getAttributes().toString());

        startTagToken.reset();
    }

    @Test
    public void testEmptyEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.reset();

        endTagToken.name("p");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("p", endTagToken.name());
        assertEquals("p", endTagToken.normalName());

        startTagToken = new Token.StartTag();
        startTagToken.reset();
        startTagToken.name("p");
        startTagToken.finaliseTag();

        endTagToken.newAttribute();
        endTagToken.appendAttributeName("name");
        endTagToken.appendAttributeValue("test");
        endTagToken.finaliseTag();

        assertEquals("<p></p>", startTagToken.toString() + endTagToken.toString());

        endTagToken.reset();
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.reset();

        commentToken.appendAttributeName("--comment with empty line--");
        commentToken.appendAttributeValue("\n");
        commentToken.appendAttributeName("--test comment--");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("", commentToken.name());
        assertEquals("", commentToken.normalName());
        assertEquals("--comment with empty line--\n--test comment--", commentToken.getData());

        commentToken.reset();
    }

    @Test
    public void testCDataToken() {
        Token cdataToken = new Token.CData("[CDATA[Hello World]]");
        cdataToken.reset();

        assertEquals("CData", cdataToken.tokenType());
        assertEquals("[CDATA[Hello World]]", cdataToken.toString());

        cdataToken.reset();
    }

    @Test(expected = AssertionError.class)
    public void testInvalidToken() {
        Token invalidToken = new Token();
        invalidToken.type = new TokenType("InvalidToken");
        invalidToken.reset();
        invalidToken.name("tag");

        assertEquals("InvalidToken", invalidToken.tokenType());
        assertEquals("tag", invalidToken.name());
        assertEquals("tag", invalidToken.normalName());

        // Unsupported token type should throw an AssertionError
        invalidToken.asDoctype();
    }
}