import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
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
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        Assert.assertNotNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.finaliseTag();
        Assert.assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "p";
        Assert.assertEquals("p", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.selfClosing = true;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("div");
        Assert.assertEquals("div", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName('p');
        Assert.assertEquals("p", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("href");
        Assert.assertEquals("href", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName('c');
        Assert.assertEquals("c", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("http://www.example.com");
        Assert.assertNotNull(tag.pendingAttributeValue);
        Assert.assertEquals("http://www.example.com", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue('a');
        Assert.assertNotNull(tag.pendingAttributeValue);
        Assert.assertEquals("a", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.appendAttributeValue("class");
        startTag.appendAttributeValue("container");
        Assert.assertEquals("<div class=\"container\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");
        Assert.assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("This is a character");
        Assert.assertEquals("This is a character", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
}