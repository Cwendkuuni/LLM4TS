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

        doctype.getName("testName");
        doctype.reset();
        doctype.getName().equals("");
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName("testName");
        Assert.assertEquals("testName", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getPublicIdentifier().equals("");
        doctype.setPublicIdentifier("testPublicIdentifier");
        Assert.assertEquals("testPublicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getSystemIdentifier().equals("");
        doctype.setSystemIdentifier("testSystemIdentifier");
        Assert.assertEquals("testSystemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
        doctype.setForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = null;
        tag.reset();
        Assert.assertNull(tag.tagName);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("testTagName");
        Assert.assertEquals("testTagName", tag.name());
    }

    @Test
    public void testTagNameLowerCase() {
        Token.Tag tag = new Token.StartTag();
        tag.name("TestTagName");
        Assert.assertEquals("testtagname", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        Assert.assertFalse(tag.isSelfClosing());
        tag.setSelfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.getAttributes().put("testAttribute", "testValue");
        Assert.assertEquals("testValue", tag.getAttributes().get("testAttribute").getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testTagNewAttributeNull() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute(null);
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute("testAttribute");
        Assert.assertNotNull(tag.getAttributes());
        Assert.assertEquals(1, tag.getAttributes().size());
    }

    @Test
    public void testTagNewAttributeValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute("testAttribute").setValue("testValue");
        Assert.assertEquals("testValue", tag.getAttributes().get("testAttribute").getValue());
    }

    @Test
    public void testTagNewAttributeEmptyValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute("testAttribute").setEmptyValue();
        Assert.assertEquals("", tag.getAttributes().get("testAttribute").getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute("testAttribute").setValue("testValue");
        tag.finaliseTag();
        Assert.assertTrue(tag.getAttributes().size() > 0);
    }

    @Test
    public void testTokenType() {
        Token token = new Token();
        Assert.assertEquals(Token.TokenType.Character, token.tokenType());
    }

    @Test
    public void testDoctypeToken() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.tokenType());
    }

    @Test
    public void testStartTagToken() {
        Token token = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, token.tokenType());
    }

    @Test
    public void testEndTagToken() {
        Token token = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, token.tokenType());
    }

    @Test
    public void testCommentToken() {
        Token token = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, token.tokenType());
    }

    @Test
    public void testCharacterToken() {
        Token token = new Token.Character();
        Assert.assertEquals(TokenType.Character, token.tokenType());
    }

    @Test
    public void testEOFToken() {
        Token token = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, token.tokenType());
    }

    @Test
    public void testTokenIsDoctype() {
        Token token = new Token.Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        Token token = new Token.StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        Token token = new Token.EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Token token = new Token.Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Token token = new Token.Character();
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }
}