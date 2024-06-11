import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks().equals(false);

        doctype.reset();

        doctype.getName().equals("");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks().equals(false);
    }

    @Test
    public void testTokenTypeDoctype() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.tokenType());
    }

    @Test
    public void testResetTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.normalName = "tagName";
        tag.pendingAttributeName = "attributeName";
        tag.selfClosing = false;
        tag.attributes = new org.jsoup.nodes.Attributes();

        tag.reset();

        tag.tagName = null;
        tag.normalName = null;
        tag.pendingAttributeName = null;
        tag.selfClosing = false;
        tag.attributes = null;
    }

    @Test
    public void testTokenTypeTag() {
        Token token = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, token.tokenType());
    }

    @Test(expected = AssertionError.class)
    public void testTagNameNotNull() {
        Token.StartTag tag = new Token.StartTag();
        tag.tagName = null;
        tag.name();
    }

    @Test
    public void testNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        Assert.assertNotNull(tag.attributes);
    }

    @Test
    public void testNewAttributeMultiple() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.newAttribute();
        Assert.assertEquals(2, tag.attributes.size());
    }

    @Test
    public void testFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.finaliseTag();
        Assert.assertEquals(1, tag.attributes.size());
    }

    @Test
    public void testResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.getData().equals("");
        comment.bogus = false;

        comment.reset();

        comment.getData().equals("");
        comment.bogus = false;
    }

    @Test
    public void testTokenTypeComment() {
        Token token = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, token.tokenType());
    }

    @Test
    public void testResetCharacter() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        character.data = null;
    }

    @Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();
        Assert.assertEquals(TokenType.Character, token.tokenType());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");
        Assert.assertEquals("data", character.getData());
    }

    @Test
    public void testResetEOF() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
    }

    @Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, token.tokenType());
    }
}