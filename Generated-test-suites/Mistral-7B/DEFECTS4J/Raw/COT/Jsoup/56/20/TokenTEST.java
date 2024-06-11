import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());
        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());
        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());
        token = new Token.Character();
        assertEquals("Character", token.tokenType());
        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        TokenType tokenType = TokenType.Doctype;
        Token doctype = new Token.Doctype();
        assertEquals(tokenType, doctype.type);

        doctype.reset();

        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeGetters() {
        TokenType tokenType = TokenType.Doctype;
        String name = "html";
        String publicIdentifier = "HTML";
        String systemIdentifier = "about:legacy-compat";
        boolean forceQuirks = true;
        Token doctype = new Token.Doctype();

        doctype.name(name);
        doctype.getPublicIdentifier().equals(publicIdentifier);
        doctype.getSystemIdentifier().equals(systemIdentifier);
        doctype.isForceQuirks().equals(forceQuirks);

        doctype.reset();

        doctype.name(name);
        doctype.getPublicIdentifier().equals(publicIdentifier);
        doctype.getSystemIdentifier().equals(systemIdentifier);
        doctype.isForceQuirks().equals(forceQuirks);
    }

    @Test
    public void testTagReset() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        String normalName = "div";
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.getName().equals(tagName);
        tag.normalName().equals(normalName);
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());

        tag.reset();

        tag.name(tagName);
        tag.getName().equals(tagName);
        tag.normalName().equals(normalName);
        assertNull(tag.getAttributes());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagNewAttribute() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        String attributeName = "class";
        String attributeValue = "red";
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.hasPendingAttributeValue();
        tag.getAttributes().get("class").equals("red");

        tag.reset();

        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.getAttributes().get("class").equals("red");
    }

    @Test(expected = AssertionError.class)
    public void testTagFinaliseTagDuplicateAttribute() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        String attributeName = "class";
        String attributeValue = "red";
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.finaliseTag();
    }

    @Test(expected = AssertionError.class)
    public void testTagFinaliseTagNullAttribute() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.finaliseTag();
    }

    @Test(expected = AssertionError.class)
    public void testTagFinaliseTagEmptyAttribute() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("");
        tag.finaliseTag();
    }

    @Test
    public void testTagToString() {
        TokenType tokenType = TokenType.StartTag;
        String tagName = "div";
        Attributes attributes = new Attributes();
        attributes.put("class", "red");
        Token tag = new Token.StartTag();

        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.finaliseTag();

        String expected = "<div class=\"red\">";
        assertEquals(expected, tag.toString());
    }

    @Test
    public void testCommentReset() {
        TokenType tokenType = TokenType.Comment;
        StringBuilder data = new StringBuilder();
        Token comment = new Token.Comment();

        comment.reset();

        assertEquals(tokenType, comment.type);
        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCommentGetters() {
        TokenType tokenType = TokenType.Comment;
        StringBuilder data = new StringBuilder();
        Token comment = new Token.Comment();

        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData());
        comment.setBogus(true);
        assertTrue(comment.isBogus());

        comment.reset();

        comment.getData().append("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        TokenType tokenType = TokenType.Character;
        Character character = new Token.Character();

        character.reset();

        assertEquals(tokenType, character.type);
        assertNull(character.getData());
    }

    @Test
    public void testCharacterGetters() {
        TokenType tokenType = TokenType.Character;
        Character character = new Token.Character();
        String data = "!";

        character.data(data);
        assertEquals(data, character.getData());

        character.reset();

        character.data(data);
        assertEquals(data, character.getData());
    }

    @Test
    public void testEOFReset() {
        TokenType tokenType = TokenType.EOF;
        Token eof = new Token.EOF();

        eof.reset();

        assertEquals(tokenType, eof.type);
    }

    @Test
    public void testIsDoctype() {
        TokenType tokenType = TokenType.Doctype;
        Token doctype = new Token.Doctype();
        Token character = new Token.Character();

        assertTrue(doctype.isDoctype());
        assertFalse(character.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        TokenType tokenType = TokenType.StartTag;
        Token startTag = new Token.StartTag();
        Token character = new Token.Character();

        assertTrue(startTag.isStartTag());
        assertFalse(character.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        TokenType tokenType = TokenType.EndTag;
        Token endTag = new Token.EndTag();
        Token character = new Token.Character();

        assertTrue(endTag.isEndTag());
        assertFalse(character.isEndTag());
    }

    @Test
    public void testIsComment() {
        TokenType tokenType = TokenType.Comment;
        Token comment = new Token.Comment();
        Token character = new Token.Character();

        assertTrue(comment.isComment());
        assertFalse(character.isComment());
    }

    @Test
    public void testIsCharacter() {
        TokenType tokenType = TokenType.Character;
        Token character = new Token.Character();
        Token doctype = new Token.Doctype();

        assertTrue(character.isCharacter());
        assertFalse(doctype.isCharacter());
    }

    @Test
    public void testIsEOF() {
        TokenType tokenType = TokenType.EOF;
        Token eof = new Token.EOF();
        Token doctype = new Token.Doctype();

        assertTrue(eof.isEOF());
        assertFalse(doctype.isEOF());
    }

    @Test
    public void testAsDoctype() {
        TokenType tokenType = TokenType.Doctype;
        Token doctype = new Token.Doctype();
        Token character = new Token.Character();

        assertSame(doctype, doctype.asDoctype());
        assertNull(character.asDoctype());
    }

    @Test
    public void testAsStartTag() {
        TokenType tokenType = TokenType.StartTag;
        Token startTag = new Token.StartTag();
        Token character = new Token.Character();

        assertSame(startTag, startTag.asStartTag());
        assertNull(character.asStartTag());
    }

    @Test
    public void testAsEndTag() {
        TokenType tokenType = TokenType.EndTag;
        Token endTag = new Token.EndTag();
        Token character = new Token.Character();

        assertSame(endTag, endTag.asEndTag());
        assertNull(character.asEndTag());
    }

    @Test
    public void testAsComment() {
        TokenType tokenType = TokenType.Comment;
        Token comment = new Token.Comment();
        Token character = new Token.Character();

        assertSame(comment, comment.asComment());
        assertNull(character.asComment());
    }

    @Test
    public void testAsCharacter() {
        TokenType tokenType = TokenType.Character;
        Token character = new Token.Character();
        Token doctype = new Token.Doctype();

        assertSame(character, character.asCharacter());
        assertNull(doctype.asCharacter());
    }

    @Test
    public void testAsTokenTypeDoctype() {
        TokenType tokenType = TokenType.Doctype;
        Token doctype = new Token.Doctype();

        assertEquals(tokenType, doctype.asTokenType());
    }

    @Test
    public void testAsTokenTypeStartTag() {
        TokenType tokenType = TokenType.StartTag;
        Token startTag = new Token.StartTag();

        assertEquals(tokenType, startTag.asTokenType());
    }

    @Test
    public void testAsTokenTypeEndTag() {
        TokenType tokenType = TokenType.EndTag;
        Token endTag = new Token.EndTag();

        assertEquals(tokenType, endTag.asTokenType());
    }

    @Test
    public void testAsTokenTypeComment() {
        TokenType tokenType = TokenType.Comment;
        Token comment = new Token.Comment();

        assertEquals(tokenType, comment.asTokenType());
    }

    @Test
    public void testAsTokenTypeCharacter() {
        TokenType tokenType = TokenType.Character;
        Token character = new Token.Character();

        assertEquals(tokenType, character.asTokenType());
    }

    @Test
    public void testAsTokenTypeEOF() {
        TokenType tokenType = TokenType.EOF;
        Token eof = new Token.EOF();

        assertEquals(tokenType, eof.asTokenType());
    }
}