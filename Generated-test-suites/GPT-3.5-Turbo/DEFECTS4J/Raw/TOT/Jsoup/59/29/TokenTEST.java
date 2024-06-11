import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.example.com");
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
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("content");
        tag.pendingAttributeValueS = "class";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;

        tag.reset();

        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertEquals("", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.attributes = new Attributes();
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("content");

        tag.newAttribute();

        Assert.assertEquals(1, tag.attributes.size());
        Assert.assertEquals("id", tag.attributes.get("id").getKey());
        Assert.assertEquals("content", tag.attributes.get("id").getValue());
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertEquals("", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue.append("content");

        tag.finaliseTag();

        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertEquals("", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "di";

        tag.appendTagName("v");

        Assert.assertEquals("div", tag.tagName);
        Assert.assertEquals("div", tag.normalName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "cl";

        tag.appendAttributeName("ass");

        Assert.assertEquals("class", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeValue = new StringBuilder("val");

        tag.appendAttributeValue("ue");

        Assert.assertEquals("value", tag.pendingAttributeValue.toString());
        Assert.assertNull(tag.pendingAttributeValueS);
        Assert.assertTrue(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();

        tag.setEmptyAttributeValue();

        Assert.assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "example";

        character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("content");
        startTag.pendingAttributeValueS = "class";
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
        comment.data.append("example");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.data.toString());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        Assert.assertSame(eof, eof.reset());
    }

    @Test
    public void testTokenTypeDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testTokenTypeStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testTokenTypeEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testTokenTypeComment() {
        Token.Comment comment = new Token.Comment();
        
        Assert.assertTrue(comment.isComment());
        Assert.assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testTokenTypeCharacter() {
        Token.Character character = new Token.Character();
        
        Assert.assertTrue(character.isCharacter());
        Assert.assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testTokenTypeEOF() {
        Token.EOF eof = new Token.EOF();
        
        Assert.assertTrue(eof.isEOF());
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }

}