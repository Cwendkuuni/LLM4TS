import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    @Test
    public void testResetDoctype() {
        Token doctype = new Token.Doctype();
        doctype.reset();
        Assert.assertNull(doctype.getName());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testGetNameDoctype() {
        Token doctype = new Token.Doctype();
        doctype.getName("html");
        Assert.assertEquals("html", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifierDoctype() {
        Token doctype = new Token.Doctype();
        doctype.getPublicIdentifier("PUBLIC");
        Assert.assertEquals("PUBLIC", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifierDoctype() {
        Token doctype = new Token.Doctype();
        doctype.getSystemIdentifier("http://example.com");
        Assert.assertEquals("http://example.com", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirksDoctype() {
        Token doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
        doctype.isForceQuirks(true);
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testResetTag() {
        Token tag = new Token.StartTag();
        tag.reset();
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertNull(tag.pendingAttributeName);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testNewAttributeTag() {
        Token tag = new Token.StartTag();
        tag.newAttribute("class");
        tag.newAttribute("id", "testId");
        Assert.assertEquals(2, tag.attributes.size());
        Assert.assertNotNull(tag.attributes.get("class"));
        Assert.assertEquals("testId", tag.attributes.get("id").getValue());
    }

    @Test
    public void testFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.newAttribute("class");
        tag.finaliseTag();
        Assert.assertNotNull(tag.attributes);
    }

    @Test
    public void testNameTag() {
        Token tag = new Token.StartTag();
        tag.name("div");
        Assert.assertEquals("div", tag.name());
    }

    @Test
    public void testSelfClosingTag() {
        Token tag = new Token.StartTag();
        tag.isSelfClosing(true);
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test(expected = IllegalStateException.class)
    public void testNewAttributeAfterFinaliseTag() {
        Token tag = new Token.StartTag();
        tag.finaliseTag();
        tag.newAttribute("class");
    }

    @Test
    public void testResetCharacter() {
        Token character = new Token.Character();
        character.reset();
        Assert.assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        Token character = new Token.Character();
        character.data("Hello World!");
        Assert.assertEquals("Hello World!", character.getData());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.EOF();
        Assert.assertFalse(token.isCharacter());
        token = new Token.Character();
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.Character();
        Assert.assertFalse(token.isEOF());
        token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }
}