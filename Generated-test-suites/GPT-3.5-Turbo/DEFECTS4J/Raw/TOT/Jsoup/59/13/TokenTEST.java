
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "SYSTEM";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();
        Assert.assertEquals("", resetDoctype.name.toString());
        Assert.assertNull(resetDoctype.pubSysKey);
        Assert.assertEquals("", resetDoctype.publicIdentifier.toString());
        Assert.assertEquals("", resetDoctype.systemIdentifier.toString());
        Assert.assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "example");

        startTag.nameAttr("div", attributes);
        Assert.assertEquals("div", startTag.tagName);
        Assert.assertEquals("div", startTag.normalName);
        Assert.assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "example");

        startTag.nameAttr("div", attributes);

        Token.StartTag resetStartTag = startTag.reset();
        Assert.assertNull(resetStartTag.tagName);
        Assert.assertNull(resetStartTag.normalName);
        Assert.assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        Token.EndTag resetEndTag = endTag.reset();
        Assert.assertNull(resetEndTag.tagName);
        Assert.assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();
        Assert.assertEquals("", resetComment.data.toString());
        Assert.assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("Hello World");

        Token.Character resetCharacter = character.reset();
        Assert.assertNull(resetCharacter.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEof = eof.reset();
        Assert.assertEquals(eof, resetEof);
    }
}
