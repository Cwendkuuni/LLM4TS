import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctypeGetNameWithNull() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName(null);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setPublicIdentifier("publicIdentifier");
        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setSystemIdentifier("systemIdentifier");
        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.reset();
        Assert.assertNull(tag.tagName);
        tag.reset();
        Assert.assertNull(tag.tagName);
        tag.attributes = new org.jsoup.nodes.Attributes();
        tag.reset();
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("tagName");
        Assert.assertEquals("tagName", tag.name());
    }

    @Test(expected = AssertionError.class)
    public void testTagNameWithNull() {
        Token.Tag tag = new Token.StartTag();
        tag.name(null);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.finaliseTag();
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagNameWithEmptyValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.setEmptyAttributeValue();
        Assert.assertTrue(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("attributeName");
        tag.appendAttributeValue("attributeValue");
        tag.newAttribute();
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertEquals("attributeName", tag.attributes.getKey(0).getName());
        Assert.assertEquals("attributeValue", tag.attributes.getValue(0).getValue());
    }

    @Test
    public void testTagNewAttributeWithEmptyValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("attributeName");
        tag.setEmptyAttributeValue();
        tag.newAttribute();
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertEquals("attributeName", tag.attributes.getKey(0).getName());
        Assert.assertNull(tag.attributes.getValue(0).getValue());
    }

    @Test
    public void testTagNewAttributeWithMultipleValues() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("attributeName");
        tag.appendAttributeValue("value1");
        tag.appendAttributeValue("value2");
        tag.newAttribute();
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.pendingAttributeValue);
        Assert.assertEquals("attributeName", tag.attributes.getKey(0).getName());
        Assert.assertEquals("value1 value2", tag.attributes.getValue(0).getValue());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        tag.isSelfClosing = true;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.appendAttributeName("attributeName");
        tag.appendAttributeValue("value1");
        tag.appendAttributeName("attributeName2");
        tag.appendAttributeValue("value2");
        tag.newAttribute();
        Assert.assertNotNull(tag.getAttributes());
        Assert.assertEquals(2, tag.getAttributes().size());
    }

    @Test
    public void testTagToString() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.attributes = new org.jsoup.nodes.Attributes();
        tag.attributes.put("attributeName", new org.jsoup.nodes.Attribute("attributeName", "value1"));
        tag.attributes.put("attributeName2", new org.jsoup.nodes.Attribute("attributeName2", "value2"));
        Assert.assertEquals("<tagName attributeName=\"value1\" attributeName2=\"value2\">", tag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testTagToStringWithNullAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("tagName");
        tag.toString();
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data = new StringBuilder("data");
        comment.reset();
        Assert.assertFalse(comment.bogus);
        Assert.assertEquals("", comment.getData());
    }

    @Test
    public void testCommentgetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        Assert.assertEquals("data", comment.getData());
    }

    @Test(expected = AssertionError.class)
    public void testCommentgetDataWithNullData() {
        Token.Comment comment = new Token.Comment();
        comment.data = null;
        comment.getData();
    }

    @Test
    public void testCommenttoString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("<!--data-->");
        Assert.assertEquals("<!--data-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        character.reset();
        Assert.assertNull(character.data);
    }

    @Test
    public void testCharactergetData() {
        Token.Character character = new Token.Character();
        character.data = "data";
        Assert.assertEquals("data", character.getData());
    }

    @Test(expected = AssertionError.class)
    public void testCharactergetDataWithNullData() {
        Token.Character character = new Token.Character();
        character.data = null;
        character.getData();
    }

    @Test
    public void testCharactertoString() {
        Token.Character character = new Token.Character();
        character.data = "data";
        Assert.assertEquals("data", character.toString());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token();
        Assert.assertFalse(token.isDoctype());
        Assert.assertFalse(token.isStartTag());
        Assert.assertFalse(token.isEndTag());
        Assert.assertFalse(token.isComment());
        Assert.assertFalse(token.isCharacter());
        Assert.assertFalse(token.isEOF());

        Token doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertFalse(doctype.isStartTag());
        Assert.assertFalse(doctype.isEndTag());
        Assert.assertFalse(doctype.isComment());
        Assert.assertFalse(doctype.isCharacter());
        Assert.assertFalse(doctype.isEOF());

        Token startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isDoctype());
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertFalse(startTag.isEndTag());
        Assert.assertFalse(startTag.isComment());
        Assert.assertFalse(startTag.isCharacter());
        Assert.assertFalse(startTag.isEOF());

        Token endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isDoctype());
        Assert.assertFalse(endTag.isStartTag());
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertFalse(endTag.isComment());
        Assert.assertFalse(endTag.isCharacter());
        Assert.assertFalse(endTag.isEOF());

        Token comment = new Token.Comment();
        Assert.assertFalse(comment.isDoctype());
        Assert.assertFalse(comment.isStartTag());
        Assert.assertFalse(comment.isEndTag());
        Assert.assertTrue(comment.isComment());
        Assert.assertFalse(comment.isCharacter());
        Assert.assertFalse(comment.isEOF());

        Token character = new Token.Character();
        Assert.assertFalse(character.isDoctype());
        Assert.assertFalse(character.isStartTag());
        Assert.assertFalse(character.isEndTag());
        Assert.assertFalse(character.isComment());
        Assert.assertTrue(character.isCharacter());
        Assert.assertFalse(character.isEOF());

        Token eof = new Token.EOF();
        Assert.assertFalse(eof.isDoctype());
        Assert.assertFalse(eof.isStartTag());
        Assert.assertFalse(eof.isEndTag());
        Assert.assertFalse(eof.isComment());
        Assert.assertFalse(eof.isCharacter());
        Assert.assertTrue(eof.isEOF());
    }
}