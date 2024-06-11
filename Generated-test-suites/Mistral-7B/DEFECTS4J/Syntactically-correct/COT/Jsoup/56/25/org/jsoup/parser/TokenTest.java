import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("_publicId_");
        doctype.systemIdentifier("_systemId_");
        doctype.forceQuirks(true);

        Token.Doctype resetDoctype = doctype.reset();

        Assert.assertFalse(doctype.isForceQuirks());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals("", doctype.getSystemIdentifier());

        Assert.assertSame(doctype, resetDoctype);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("_publicId_");
        doctype.systemIdentifier("_systemId_");
        doctype.forceQuirks(true);

        Assert.assertEquals("html", doctype.getName());
        Assert.assertEquals("_publicId_", doctype.getPublicIdentifier());
        Assert.assertEquals("_systemId_", doctype.getSystemIdentifier());
        Assert.assertEquals(true, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");

        Token.StartTag resetStartTag = startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals(0, startTag.pendingAttributeValue.length());
        Assert.assertEquals(0, startTag.pendingAttributeValueS.length());
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div").attribute("class", "test");

        Token.StartTag newAttributeStartTag = startTag.newAttribute();

        Assert.assertNotNull(startTag.attributes);
        Assert.assertEquals(1, startTag.attributes.size());
        Assert.assertNotNull(startTag.attributes.get("class"));
        Assert.assertEquals("test", startTag.attributes.get("class").getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div").attribute("class", "test");
        startTag.finaliseTag();

        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testTagNewAttributeAfterFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div").finaliseTag();
        startTag.newAttribute();
    }

    @Test
    public void testTagToString() {
        Token.StartTag startTag = new Token.StartTag().name("div").attribute("class", "test");

        String tagToString = startTag.toString();

        Assert.assertEquals("<div class=\"test\"></div>", tagToString);
    }

    @Test
    public void testTagName() {
        Token.StartTag startTag = new Token.StartTag().name("div");

        Assert.assertEquals("div", startTag.name());
    }

    @Test
    public void testTagNormalName() {
        Token.StartTag startTag = new Token.StartTag().name("DIV");

        Assert.assertEquals("div", startTag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag().selfClosing(true);

        Assert.assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag().name("div").attribute("class", "test");

        Attributes attributes = startTag.getAttributes();

        Assert.assertNotNull(attributes);
        Assert.assertEquals(1, attributes.size());
        Assert.assertNotNull(attributes.get("class"));
        Assert.assertEquals("test", attributes.get("class").getValue());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data("This is a comment");

        Token.Comment resetComment = comment.reset();

        Assert.assertFalse(comment.bogus);
        Assert.assertEquals("", comment.getData());
    }

    @Test
    public void testCommentGetter() {
        Token.Comment comment = new Token.Comment().data("This is a comment");

        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character().data("A");

        Token.Character resetCharacter = character.reset();

        Assert.assertNull(character.data);
    }

    @Test
    public void testCharacterGetter() {
        Token.Character character = new Token.Character().data("A");

        Assert.assertEquals("A", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEof = eof.reset();

        Assert.assertSame(eof, resetEof);
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenResetUnknownToken() {
        Token unknownToken = new Token();

        unknownToken.reset();
    }
}