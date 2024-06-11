import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("testName");
        doctype.reset();
        Assert.assertEquals("testName", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setPubSysKey("testPubSysKey");
        doctype.reset();
        Assert.assertNull(doctype.getPubSysKey());
    }

    @Test(expected = NullPointerException.class)
    public void testDoctypeGetPubSysKeyNull() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        doctype.getPubSysKey();
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.isForceQuirks();
        doctype.reset();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeSetForceQuirksTrue() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setForceQuirks(true);
        doctype.reset();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeSetForceQuirksFalse() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setForceQuirks(false);
        doctype.reset();
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testTagName";
        tag.reset();
        Assert.assertNull(tag.tagName);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("testTagName");
        tag.reset();
        Assert.assertEquals("testTagName", tag.name());
    }

    @Test
    public void testTagNameLower() {
        Token.Tag tag = new Token.StartTag();
        tag.name("TestTagName");
        tag.reset();
        Assert.assertEquals("testtagname", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosingFalse() {
        Token.StartTag tag = new Token.StartTag();
        Assert.assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagIsSelfClosingTrue() {
        Token.StartTag tag = new Token.StartTag().selfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributesNull() {
        Token.StartTag tag = new Token.StartTag();
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName");
        tag.newAttribute();
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName").appendAttributeValue("testAttributeValue");
        tag.newAttribute();
        Assert.assertEquals("testAttributeName", tag.getAttributes().get("testAttributeName"));
        Assert.assertEquals("testAttributeValue", tag.getAttributes().get("testAttributeName"));
    }

    @Test
    public void testTagNewAttributeValueEmptyString() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName").setEmptyAttributeValue();
        tag.newAttribute();
        Assert.assertEquals("", tag.getAttributes().get("testAttributeName"));
    }

    @Test
    public void testTagNewAttributeValueString() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName").appendAttributeValue("testAttributeValue");
        tag.newAttribute();
        Assert.assertEquals("testAttributeValue", tag.getAttributes().get("testAttributeName"));
    }

    @Test(expected = NullPointerException.class)
    public void testTagNewAttributeValueNull() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName");
        tag.newAttribute();
        tag.getAttributes().put("testAttributeName", null);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().appendAttributeName("testAttributeName").appendAttributeValue("testAttributeValue");
        tag.finaliseTag();
        Assert.assertEquals("testAttributeName", tag.getAttributes().get("testAttributeName"));
        Assert.assertEquals("testAttributeValue", tag.getAttributes().get("testAttributeName"));
    }

    @Test
    public void testTokenIsStartTag() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertFalse(endTag.isStartTag());
        Assert.assertFalse(comment.isStartTag());
        Assert.assertFalse(character.isStartTag());
        Assert.assertFalse(doctype.isStartTag());
        Assert.assertFalse(eof.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertFalse(startTag.isEndTag());
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertFalse(comment.isEndTag());
        Assert.assertFalse(character.isEndTag());
        Assert.assertFalse(doctype.isEndTag());
        Assert.assertFalse(eof.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertFalse(startTag.isComment());
        Assert.assertFalse(endTag.isComment());
        Assert.assertTrue(comment.isComment());
        Assert.assertFalse(character.isComment());
        Assert.assertFalse(doctype.isComment());
        Assert.assertFalse(eof.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertFalse(startTag.isCharacter());
        Assert.assertFalse(endTag.isCharacter());
        Assert.assertFalse(comment.isCharacter());
        Assert.assertTrue(character.isCharacter());
        Assert.assertFalse(doctype.isCharacter());
        Assert.assertFalse(eof.isCharacter());
    }

    @Test
    public void testTokenIsCData() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token cdata = new Token.CData("testCData");
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertFalse(startTag.isCData());
        Assert.assertFalse(endTag.isCData());
        Assert.assertFalse(comment.isCData());
        Assert.assertFalse(character.isCData());
        Assert.assertFalse(doctype.isCData());
        Assert.assertTrue(cdata.isCData());
        Assert.assertFalse(eof.isCData());
    }

    @Test
    public void testTokenIsEOF() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        Assert.assertFalse(startTag.isEOF());
        Assert.assertFalse(endTag.isEOF());
        Assert.assertFalse(comment.isEOF());
        Assert.assertFalse(character.isEOF());
        Assert.assertFalse(doctype.isEOF());
        Assert.assertTrue(eof.isEOF());
        Assert.assertFalse(cdata.isEOF());
    }

    @Test
    public void testTokenTypeToString() {
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();
        TokenType expectedType = TokenType.StartTag;
        TokenType actualType;
        String expectedString = "StartTag";
        String actualString;
        actualType = startTag.getType();
        actualString = startTag.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals(expectedString, actualString);

        expectedType = TokenType.EndTag;
        actualType = endTag.getType();
        actualString = endTag.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals("EndTag", actualString);

        expectedType = TokenType.Comment;
        actualType = comment.getType();
        actualString = comment.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals("Comment", actualString);

        expectedType = TokenType.Character;
        actualType = character.getType();
        actualString = character.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals("Character", actualString);

        expectedType = TokenType.EOF;
        actualType = eof.getType();
        actualString = eof.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals("EOF", actualString);

        expectedType = TokenType.Doctype;
        actualType = doctype.getType();
        actualString = doctype.toString();
        Assert.assertEquals(expectedType, actualType);
        Assert.assertEquals("Doctype", actualString);
    }

    @Test
    public void testTokenTypeToStringOther() {
        Token startTag = new Token.StartTag();
        TokenType expectedType = TokenType.StartTag;
        String expectedString = "StartTag";
        String actualString;
        actualString = startTag.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals(expectedString, actualString);

        Token endTag = new Token.EndTag();
        expectedString = "EndTag";
        actualString = endTag.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals("EndTag", actualString);

        Token comment = new Token.Comment();
        expectedString = "Comment";
        actualString = comment.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals("Comment", actualString);

        Token character = new Token.Character();
        expectedString = "Character";
        actualString = character.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals("Character", actualString);

        Token eof = new Token.EOF();
        expectedString = "EOF";
        actualString = eof.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals("EOF", actualString);

        Token doctype = new Token.Doctype();
        expectedString = "Doctype";
        actualString = doctype.toString();
        Assert.assertNotEquals(expectedType.toString(), actualString);
        Assert.assertEquals("Doctype", actualString);
    }
}