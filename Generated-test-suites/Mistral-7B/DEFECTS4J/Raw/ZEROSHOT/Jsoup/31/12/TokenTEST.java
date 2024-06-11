import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().getName("html");
        assertEquals("Doctype", doctypeWithName.tokenType());
        assertEquals("html", doctypeWithName.asDoctype().getName());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().setPublicIdentifier("_publicId");
        assertEquals("Doctype", doctypeWithPublicIdentifier.tokenType());
        assertEquals("_publicId", doctypeWithPublicIdentifier.asDoctype().getPublicIdentifier());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().setSystemIdentifier("_systemId");
        assertEquals("Doctype", doctypeWithSystemIdentifier.tokenType());
        assertEquals("_systemId", doctypeWithSystemIdentifier.asDoctype().getSystemIdentifier());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().setForceQuirks(true);
        assertEquals("Doctype", doctypeWithForceQuirks.tokenType());
        assertTrue(doctypeWithForceQuirks.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testTag() {
        Token tag = new Token.StartTag("div");
        assertEquals("StartTag", tag.tokenType());
        assertEquals("div", tag.asStartTag().name());

        Token tagWithAttributes = new Token.StartTag("p").name("class", "red").name("id", "id1");
        assertEquals("StartTag", tagWithAttributes.tokenType());
        assertEquals("p", tagWithAttributes.asStartTag().name());
        assertEquals(2, tagWithAttributes.asStartTag().getAttributes().size());
        assertEquals("red", tagWithAttributes.asStartTag().getAttributes().get("class"));
        assertEquals("id1", tagWithAttributes.asStartTag().getAttributes().get("id"));

        Token tagWithSelfClosing = new Token.StartTag("img").name("src", "image.jpg").selfClosing(true);
        assertEquals("StartTag", tagWithSelfClosing.tokenType());
        assertEquals("img", tagWithSelfClosing.asStartTag().name());
        assertTrue(tagWithSelfClosing.asStartTag().isSelfClosing());

        Token tagWithEmptyName = new Token.EndTag("");
        assertEquals("EndTag", tagWithEmptyName.tokenType());
        assertEquals("", tagWithEmptyName.asEndTag().name());

        Token tagWithName = new Token.EndTag("p");
        assertEquals("EndTag", tagWithName.tokenType());
        assertEquals("p", tagWithName.asEndTag().name());
    }

    @org.junit.Test
    public void testComment() {
        Token comment = new Token.Comment().appendAttributeValue("-- This is a comment");
        assertEquals("Comment", comment.tokenType());
        assertEquals("-- This is a comment", comment.asComment().getData());
    }

    @org.junit.Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertEquals("Character", character.tokenType());
        assertEquals("A", character.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }
}