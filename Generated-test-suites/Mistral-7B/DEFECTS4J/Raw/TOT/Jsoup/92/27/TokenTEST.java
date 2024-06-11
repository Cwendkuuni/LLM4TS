import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.reset();

        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().setPubSysKey("_pub_sys_key_");
        doctypeToken.asDoctype().getPublicIdentifier().append("public id");
        doctypeToken.asDoctype().getSystemIdentifier().append("system id");
        doctypeToken.asDoctype().isForceQuirks(true);

        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPubSysKey());
        assertEquals("public id", doctypeToken.asDoctype().getPublicIdentifier().toString());
        assertEquals("system id", doctypeToken.asDoctype().getSystemIdentifier().toString());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.reset();

        startTagToken.asStartTag().name("html");
        startTagToken.asStartTag().name("html").attributes(new Token.Attributes().put("class", "htmlClass"));

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTagToken.asStartTag().name());
        assertNotNull(startTagToken.asStartTag().getAttributes());
        assertEquals("htmlClass", startTagToken.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.reset();

        endTagToken.asEndTag().name("html");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("html", endTagToken.asEndTag().name());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.reset();

        commentToken.asComment().getData().append("This is a comment");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("--This is a comment--", commentToken.toString());
        assertEquals("This is a comment", commentToken.asComment().getData().toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.reset();

        characterToken.asCharacter().data("&amp;");

        assertEquals("Character", characterToken.tokenType());
        assertEquals("&amp;", characterToken.getData());
    }

    @Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("<<test>>");

        assertEquals("CData", cDataToken.tokenType());
        assertEquals("<![CDATA[<test>]]>", cDataToken.toString());
    }

    @Test(expected = AssertionError.class)
    public void testMalformedDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        doctypeToken.reset();

        doctypeToken.asDoctype().getName("html");
        doctypeToken.asDoctype().getPublicIdentifier().append("public id");
        doctypeToken.asDoctype().getSystemIdentifier().append("system id");
        doctypeToken.asDoctype().isForceQuirks(true);
        doctypeToken.asDoctype().getName("body"); // Should throw an AssertionError as Doctype cannot have a name after the public identifier and system identifier are set.
    }

    @Test(expected = AssertionError.class)
    public void testMalformedStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.reset();

        startTagToken.asStartTag().name("html");
        startTagToken.asStartTag().name("body").attributes(new Token.Attributes().put("class", "htmlClass")); // Should throw an AssertionError as StartTag cannot have a name after attributes are set.
    }
}