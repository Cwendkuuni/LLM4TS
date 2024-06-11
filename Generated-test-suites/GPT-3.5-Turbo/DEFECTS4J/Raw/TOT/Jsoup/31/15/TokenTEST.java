
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        Assert.assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        Assert.assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        Assert.assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        Assert.assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        Assert.assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals("data", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Assert.assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("data");
        Assert.assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }

    @Test
    public void testFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.newAttribute();
        startTag.finaliseTag();
        Assert.assertEquals(1, startTag.attributes.size());
    }
}

