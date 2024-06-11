import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals(""); // initially empty
        doctype.reset();
        doctype.getName().equals(""); // after reset, still empty
    }

    @Test
    public void testDoctypeSetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.getName().equals("html");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier().equals(""); // initially empty
        doctype.reset();
        doctype.getPublicIdentifier().equals(""); // after reset, still empty
    }

    @Test
    public void testDoctypeSetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier().append("public");
        doctype.reset();
        doctype.getPublicIdentifier().equals(""); // after reset, empty again
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeForceQuirksFalse() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeForceQuirksTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
        doctype.reset();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = null;
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagSetName() {
        Token.Tag tag = new Token.Tag();
        tag.name("div");
        tag.tagName.equals("div");
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName("div");
        tag.tagName.equals("div");
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName('d');
        tag.appendTagName('i');
        tag.appendTagName('v');
        tag.tagName.equals("div");
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag();
        tag.newAttribute();
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag();
        tag.finaliseTag();
        Assert.assertNotNull(tag.attributes);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.name("div");
        tag.name().equals("div");
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.Tag();
        tag.name("DIV");
        tag.normalName().equals("div");
        tag.reset();
        tag.tagName = null;
    }

    @Test
    public void testTagIsSelfClosingFalse() {
        Token.Tag tag = new Token.Tag();
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagIsSelfClosingTrue() {
        Token.Tag tag = new Token.Tag();
        tag.isSelfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());
        tag.reset();
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.toString().equals("<div>");
    }

    @Test
    public void testTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div").attributes().put("class", "red");
        startTag.toString().equals("<div class=\"red\"></div>");
    }

    @Test
    public void testTagToStringEmptyAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div").attributes();
        startTag.toString().equals("<div></div>");
    }

    @Test
    public void testTagToStringEmptyTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.toString().equals("");
        startTag.reset();
        startTag.tagName = null;
    }

    @Test(expected = AssertionError.class)
    public void testTagGetAttributesNull() {
        Token.Tag tag = new Token.Tag();
        tag.getAttributes();
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName("class");
        tag.pendingAttributeName.equals("class");
        tag.reset();
        tag.pendingAttributeName = null;
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName('c');
        tag.appendAttributeName('l');
        tag.appendAttributeName('a');
        tag.pendingAttributeName.equals("class");
        tag.reset();
        tag.pendingAttributeName = null;
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue("red");
        tag.pendingAttributeValue.toString().equals("red");
        tag.reset();
        tag.pendingAttributeValue = null;
        tag.pendingAttributeValueS = null;
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue('r');
        tag.appendAttributeValue('e');
        tag.appendAttributeValue('d');
        tag.pendingAttributeValue.toString().equals("red");
        tag.reset();
        tag.pendingAttributeValue = null;
        tag.pendingAttributeValueS = null;
    }

    @Test
    public void testTagAppendAttributeValueCharArray() {
        Token.Tag tag = new Token.Tag();
        char[] chars = {'r', 'e', 'd'};
        tag.appendAttributeValue(chars);
        tag.pendingAttributeValue.toString().equals("red");
        tag.reset();
        tag.pendingAttributeValue = null;
        tag.pendingAttributeValueS = null;
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.Tag();
        tag.setEmptyAttributeValue();
        tag.hasEmptyAttributeValue = true;
        tag.reset();
        tag.hasEmptyAttributeValue = false;
    }

    @Test
    public void testTagHasPendingAttributeValueFalse() {
        Token.Tag tag = new Token.Tag();
        Assert.assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagHasPendingAttributeValueTrue() {
        Token.Tag tag = new Token.Tag();
        tag.newAttribute();
        tag.hasPendingAttributeValue = true;
        tag.reset();
        tag.hasPendingAttributeValue = false;
    }
}