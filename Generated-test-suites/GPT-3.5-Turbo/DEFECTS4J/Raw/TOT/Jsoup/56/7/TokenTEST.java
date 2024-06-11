import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("HTML");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.name.toString());
        Assert.assertEquals("", doctype.publicIdentifier.toString());
        Assert.assertEquals("", doctype.systemIdentifier.toString());
        Assert.assertFalse(doctype.isForceQuirks());
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
        doctype.publicIdentifier.append("publicId");

        Assert.assertEquals("publicId", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemId");

        Assert.assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;

        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myId");
        startTag.pendingAttributeValueS = "mySId";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "div";
        startTag.normalName = "div";

        Assert.assertEquals("div", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "div";

        Assert.assertEquals("div", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();

        Assert.assertFalse(startTag.isSelfClosing());

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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        Assert.assertEquals("This is a character", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();

        Assert.assertTrue(doctype instanceof Token);
        Assert.assertFalse(startTag instanceof Token);
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertFalse(startTag.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(startTag instanceof Token);
        Assert.assertFalse(doctype instanceof Token);
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertFalse(doctype.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(endTag instanceof Token);
        Assert.assertFalse(doctype instanceof Token);
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertFalse(doctype.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(comment instanceof Token);
        Assert.assertFalse(doctype instanceof Token);
        Assert.assertTrue(comment.isComment());
        Assert.assertFalse(doctype.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(character instanceof Token);
        Assert.assertFalse(doctype instanceof Token);
        Assert.assertTrue(character.isCharacter());
        Assert.assertFalse(doctype.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(eof instanceof Token);
        Assert.assertFalse(doctype instanceof Token);
        Assert.assertTrue(eof.isEOF());
        Assert.assertFalse(doctype.isEOF());
    }
}