import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("");
        doctype.reset();
        Assert.assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeSetName() {
        Doctype doctype = new Doctype();
        doctype.name("HTML");
        Assert.assertEquals("HTML", doctype.getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeSetNameNull() {
        Doctype doctype = new Doctype();
        doctype.name(null);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        Assert.assertNull(doctype.getPublicIdentifier());
        doctype.setPublicIdentifier("PUBLIC");
        Assert.assertEquals("PUBLIC", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSetPublicIdentifierNull() {
        Doctype doctype = new Doctype();
        doctype.setPublicIdentifier(null);
        Assert.assertNull(doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        Assert.assertNull(doctype.getSystemIdentifier());
        doctype.setSystemIdentifier("SYSTEM");
        Assert.assertEquals("SYSTEM", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeSetSystemIdentifierNull() {
        Doctype doctype = new Doctype();
        doctype.setSystemIdentifier(null);
        Assert.assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
        doctype.setForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
    }

    // Tag tests

    @Test
    public void testTagReset() {
        Tag tag = new Tag();
        tag.name = null;
        tag.normalName = null;
        tag.reset();
        Assert.assertNull(tag.name);
        Assert.assertNull(tag.normalName);
    }

    @Test
    public void testTagName() {
        Tag tag = new Tag();
        tag.name("tagName");
        Assert.assertEquals("tagName", tag.name());
    }

    @Test(expected = AssertionError.class)
    public void testTagNameNull() {
        Tag tag = new Tag();
        tag.name(null);
    }

    @Test
    public void testTagNameLowerCase() {
        Tag tag = new Tag();
        tag.name("TagName");
        Assert.assertEquals("tagname", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Tag tag = new Tag();
        Assert.assertFalse(tag.isSelfClosing());
        tag.setSelfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        StartTag startTag = new StartTag();
        Attribute attribute = new Attribute("name", "value");
        startTag.attributes.put(attribute);
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        Assert.assertNull(startTag.getAttributes());
        startTag.appendAttributeName("name");
        startTag.appendAttributeValue("value");
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = AssertionError.class)
    public void testTagNewAttributeNullName() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.appendAttributeName(null);
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.finaliseTag();
        Assert.assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testTagAppendTagName() {
        Tag tag = new Tag();
        tag.appendTagName("tag");
        Assert.assertEquals("tag", tag.name());
    }

    @Test
    public void testTagAppendTagNameLowerCase() {
        Tag tag = new Tag();
        tag.appendTagName("TAG");
        Assert.assertEquals("tag", tag.name());
    }

    @Test
    public void testTagAppendAttributeName() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("name");
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
    }

    @Test
    public void testTagAppendAttributeNameLowerCase() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("Name");
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
    }

    @Test
    public void testTagAppendAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.appendAttributeValue("value");
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testTagAppendAttributeValueEmptyString() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.setEmptyAttributeValue();
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("", attribute.getValue());
    }

    @Test
    public void testTagAppendAttributeValueSingleChar() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.appendAttributeValue('a');
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("a", attribute.getValue());
    }

    @Test
    public void testTagAppendAttributeValueMultipleChars() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        startTag.appendAttributeValue("abcd");
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("abcd", attribute.getValue());
    }

    @Test
    public void testTagAppendAttributeValueMultipleCharsArray() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        char[] chars = {'a', 'b', 'c', 'd'};
        startTag.appendAttributeValue(chars);
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("abcd", attribute.getValue());
    }

    @Test
    public void testTagAppendAttributeValueMultipleCharsIntArray() {
        StartTag startTag = new StartTag();
        startTag.newAttribute();
        int[] codepoints = {0x0061, 0x0062, 0x0063, 0x0064};
        startTag.appendAttributeValue(codepoints);
        Attribute attribute = startTag.attributes.get("name");
        Assert.assertNotNull(attribute);
        Assert.assertEquals("abcd", attribute.getValue());
    }
}