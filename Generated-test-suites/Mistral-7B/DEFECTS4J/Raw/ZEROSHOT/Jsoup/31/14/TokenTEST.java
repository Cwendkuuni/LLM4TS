import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name("html");
        assertEquals("Doctype[html]", doctypeWithName.toString());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().name("html").publicIdentifier("XHTML");
        assertEquals("Doctype[html XHTML]", doctypeWithPublicIdentifier.toString());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().name("html").systemIdentifier("/path/to/file.html");
        assertEquals("Doctype[html /path/to/file.html]", doctypeWithSystemIdentifier.toString());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().name("html").forceQuirks(true);
        assertEquals("Doctype[html][forceQuirks=true]", doctypeWithForceQuirks.toString());
    }

    @Test
    public void testTag() {
        Token tag = new Token.StartTag("div");
        assertTrue(tag.isStartTag());
        assertEquals("StartTag[div]", tag.toString());

        Token tagWithAttributes = new Token.StartTag("p").name("p").attributes(new Attributes().put("class", "foo").put("id", "bar"));
        assertEquals("StartTag[p class=foo id=bar]", tagWithAttributes.toString());

        Token tagWithSelfClosing = new Token.StartTag("img").name("img").selfClosing(true);
        assertTrue(tagWithSelfClosing.isSelfClosing());
        assertEquals("StartTag[img]", tagWithSelfClosing.toString());

        Token tagWithEmptyName = new Token.StartTag();
        assertEquals("StartTag[]", tagWithEmptyName.toString());

        Token tagEnd = new Token.EndTag("div");
        assertTrue(tagEnd.isEndTag());
        assertEquals("EndTag[div]", tagEnd.toString());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertTrue(comment.isComment());
        assertEquals("Comment[]", comment.toString());

        Token commentWithData = new Token.Comment("This is a comment");
        assertEquals("Comment[This is a comment]", commentWithData.toString());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertTrue(character.isCharacter());
        assertEquals("Character[A]", character.toString());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
        assertEquals("EOF", eof.tokenType());
    }
}