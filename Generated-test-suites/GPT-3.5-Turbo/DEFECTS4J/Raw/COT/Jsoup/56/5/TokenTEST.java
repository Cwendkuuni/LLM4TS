import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        Assert.assertEquals("", resetDoctype.name.toString());
        Assert.assertEquals("", resetDoctype.publicIdentifier.toString());
        Assert.assertEquals("", resetDoctype.systemIdentifier.toString());
        Assert.assertEquals(false, resetDoctype.forceQuirks);
    }

    @Test
    public void testGetNameDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifierDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("publicId");

        Assert.assertEquals("publicId", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifierDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemId");

        Assert.assertEquals("systemId", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirksDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        Assert.assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("content");
        startTag.pendingAttributeValueS = "contentS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        Token.StartTag resetStartTag = startTag.reset();

        Assert.assertNull(resetStartTag.tagName);
        Assert.assertNull(resetStartTag.normalName);
        Assert.assertNull(resetStartTag.pendingAttributeName);
        Assert.assertEquals("", resetStartTag.pendingAttributeValue.toString());
        Assert.assertNull(resetStartTag.pendingAttributeValueS);
        Assert.assertEquals(false, resetStartTag.hasEmptyAttributeValue);
        Assert.assertEquals(false, resetStartTag.hasPendingAttributeValue);
        Assert.assertEquals(false, resetStartTag.selfClosing);
        Assert.assertNull(resetStartTag.attributes);
    }

    @Test
    public void testIsSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        Assert.assertEquals(true, startTag.isSelfClosing());
    }

    @Test
    public void testNewAttributeStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = null;

        startTag.newAttribute();

        Assert.assertNotNull(startTag.attributes);
    }

    @Test
    public void testFinaliseTagStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        startTag.pendingAttributeName = "class";

        startTag.finaliseTag();

        Assert.assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testNameStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";

        Assert.assertEquals("div", startTag.name());
    }

    @Test
    public void testNormalNameStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "div";

        Assert.assertEquals("div", startTag.normalName());
    }

    @Test
    public void testNameAttrStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("div", attributes);

        Assert.assertEquals("div", startTag.tagName);
        Assert.assertEquals("div", startTag.normalName);
        Assert.assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testAppendTagNameStringStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "d";
        startTag.normalName = "d";

        startTag.appendTagName("iv");

        Assert.assertEquals("div", startTag.tagName);
        Assert.assertEquals("div", startTag.normalName);
    }

    @Test
    public void testAppendTagNameCharStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "d";
        startTag.normalName = "d";

        startTag.appendTagName('i');

        Assert.assertEquals("di", startTag.tagName);
        Assert.assertEquals("di", startTag.normalName);
    }

    @Test
    public void testAppendAttributeNameStringStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "c";

        startTag.appendAttributeName("lass");

        Assert.assertEquals("class", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeNameCharStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "c";

        startTag.appendAttributeName('l');

        Assert.assertEquals("cl", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValueStringStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        startTag.pendingAttributeValue.append("co");

        startTag.appendAttributeValue("nt");

        Assert.assertEquals("content", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueCharStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        startTag.pendingAttributeValue.append('c');

        startTag.appendAttributeValue('o');

        Assert.assertEquals("co", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueCharArrayStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        startTag.pendingAttributeValue.append('c');

        startTag.appendAttributeValue(new char[]{'o', 'n'});

        Assert.assertEquals("con", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueIntArrayStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.ensureAttributeValue();
        startTag.pendingAttributeValue.append('c');

        startTag.appendAttributeValue(new int[]{111, 110});

        Assert.assertEquals("con", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testSetEmptyAttributeValueStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.setEmptyAttributeValue();

        Assert.assertEquals(true, startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testEnsureAttributeValueStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValueS = "value";

        startTag.ensureAttributeValue();

        Assert.assertEquals("value", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void testResetEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        Token.EndTag resetEndTag = endTag.reset();

        Assert.assertNull(resetEndTag.tagName);
        Assert.assertNull(resetEndTag.normalName);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();

        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();

        Assert.assertEquals(Token.Doctype.class, token.asDoctype().getClass());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();

        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();

        Assert.assertEquals(Token.StartTag.class, token.asStartTag().getClass());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();

        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();

        Assert.assertEquals(Token.EndTag.class, token.asEndTag().getClass());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();

        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();

        Assert.assertEquals(Token.Comment.class, token.asComment().getClass());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();

        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();

        Assert.assertEquals(Token.Character.class, token.asCharacter().getClass());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        Assert.assertTrue(token.isEOF());
    }

    @Test
    public void testIsEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();

        Assert.assertEquals(Token.EOF.class, resetEOF.getClass());
    }
}