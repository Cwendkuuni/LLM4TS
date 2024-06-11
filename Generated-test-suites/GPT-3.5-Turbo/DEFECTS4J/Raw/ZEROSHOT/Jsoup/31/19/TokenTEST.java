import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        Assert.assertEquals("//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.selfClosing = true;

        Assert.assertEquals("div", startTag.name());
        Assert.assertTrue(startTag.isSelfClosing());
        Assert.assertNotNull(startTag.getAttributes());

        Token.StartTag startTagWithAttrs = new Token.StartTag("a");
        Assert.assertEquals("a", startTagWithAttrs.name());
        Assert.assertFalse(startTagWithAttrs.isSelfClosing());
        Assert.assertNotNull(startTagWithAttrs.getAttributes());

        Token.StartTag startTagWithAttrs2 = new Token.StartTag("a", new org.jsoup.nodes.Attributes());
        Assert.assertEquals("a", startTagWithAttrs2.name());
        Assert.assertFalse(startTagWithAttrs2.isSelfClosing());
        Assert.assertNotNull(startTagWithAttrs2.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";

        Assert.assertEquals("div", endTag.name());

        Token.EndTag endTagWithName = new Token.EndTag("p");
        Assert.assertEquals("p", endTagWithName.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("Hello");

        Assert.assertEquals("Hello", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        Assert.assertTrue(eof.isEOF());
    }
}