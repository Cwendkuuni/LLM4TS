import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.name.append(">");
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = "container";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = false;
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
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        attributes.put("class", "header");
        attributes.put("id", "logo");

        startTag.nameAttr("body", attributes);

        Assert.assertEquals("body", startTag.tagName);
        Assert.assertEquals("body", startTag.normalName);
        Assert.assertEquals(attributes, startTag.attributes);
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
        comment.data.append("Hello");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Hello World";

        character.reset();

        Assert.assertNull(character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("Hello World");

        Assert.assertEquals("<![CDATA[Hello World]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertEquals(eof, eof.reset());
    }
}