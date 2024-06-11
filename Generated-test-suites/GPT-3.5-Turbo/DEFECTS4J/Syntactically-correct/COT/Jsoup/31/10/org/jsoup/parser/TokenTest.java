import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {};
        Assert.assertEquals("Token", token.tokenType());
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        Assert.assertEquals("name", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("publicIdentifier");
        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemIdentifier");
        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testNewAttribute() {
        Token.Tag tag = new Token.Tag() {};
        tag.newAttribute();
        Assert.assertNotNull(tag.attributes);
    }

    @Test
    public void testFinaliseTag() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "attr";
        tag.finaliseTag();
        Assert.assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = "tagName";
        Assert.assertEquals("tagName", tag.name());
    }

    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.Tag() {};
        tag.selfClosing = true;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testGetAttributes() {
        Token.Tag tag = new Token.Tag() {};
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testAppendTagName() {
        Token.Tag tag = new Token.Tag() {};
        tag.tagName = "tag";
        tag.appendTagName("Name");
        Assert.assertEquals("tagName", tag.tagName);
    }

    @Test
    public void testAppendAttributeName() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeName = "attr";
        tag.appendAttributeName("Name");
        Assert.assertEquals("attrName", tag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValue() {
        Token.Tag tag = new Token.Tag() {};
        tag.pendingAttributeValue = new StringBuilder("value");
        tag.appendAttributeValue("Name");
        Assert.assertEquals("valueName", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testToStringStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "tag";
        Assert.assertEquals("<tag>", startTag.toString());
    }

    @Test
    public void testToStringEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        Assert.assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        Assert.assertEquals("data", comment.getData());
    }

    @Test
    public void testToStringComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        Assert.assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testGetDataCharacter() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals("data", character.getData());
    }

    @Test
    public void testToStringCharacter() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals("data", character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        Assert.assertEquals(doctype, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        Assert.assertEquals(startTag, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        Assert.assertEquals(endTag, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        Assert.assertEquals(comment, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("data");
        Token token = character;
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("data");
        Token token = character;
        Assert.assertEquals(character, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token token = eof;
        Assert.assertTrue(token.isEOF());
    }
}