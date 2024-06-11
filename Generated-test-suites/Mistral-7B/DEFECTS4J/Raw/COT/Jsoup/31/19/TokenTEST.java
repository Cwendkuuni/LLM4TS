import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeConstructor() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
        assertNull(doctype.getName());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeSetters() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("html");
        assertEquals("html", doctype.getName());

        doctype.getPublicIdentifier().append("publicid");
        assertEquals("publicid", doctype.getPublicIdentifier());

        doctype.getSystemIdentifier().append("systemid");
        assertEquals("systemid", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagConstructor() {
        Token tag = new Token.Tag();
        assertEquals(TokenType.StartTag, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagSetters() {
        Token tag = new Token.Tag();
        tag.tagName = "div";
        assertEquals("div", tag.tagName);

        tag.newAttribute();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue = "myclass";
        tag.newAttribute();
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue = "myid";
        tag.newAttribute();
        tag.attributes = new Attributes();
        tag.attributes.put(new Attribute("class", "myclass"));
        tag.attributes.put(new Attribute("id", "myid"));

        assertEquals("div class=\"myclass\" id=\"myid\"", tag.toString());

        tag.finaliseTag();
        assertEquals("div class=\"myclass\" id=\"myid\"", tag.toString());
    }

    @Test
    public void testEmptyTagConstructor() {
        Token tag = new Token.Tag();
        tag.tagName = "img";
        tag.selfClosing = true;
        assertEquals(TokenType.StartTag, tag.type);
        assertEquals("img", tag.tagName);
        assertTrue(tag.selfClosing);
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.attributes);
        assertEquals("<img>", tag.toString());
    }

    @Test(expected = AssertionError.class)
    public void testEmptyTagSetters() {
        Token tag = new Token.Tag();
        tag.tagName = "img";
        tag.selfClosing = true;
        tag.newAttribute();
        tag.pendingAttributeName = "src";
        tag.pendingAttributeValue = "image.png";
        tag.newAttribute();
        tag.newAttribute(); // should throw an AssertionError
    }

    @Test
    public void testCommentConstructor() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.getData());
        assertEquals("<!---->", comment.toString());
    }

    @Test
    public void testCommentSetters() {
        Token comment = new Token.Comment();
        comment.getData().append("Test comment");
        assertEquals("Test comment", comment.getData().toString());
        assertEquals("<!--Test comment-->", comment.toString());
    }

    @Test
    public void testCharacterConstructor() {
        Token character = new Token.Character("A");
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());
        assertEquals("A", character.toString());
    }

    @Test
    public void testCharacterSetters() {
        Token character = new Token.Character('\u00A9');
        assertEquals(TokenType.Character, character.type);
        assertEquals("\u00A9", character.getData());
        assertEquals("\u00A9", character.toString());
    }

    @Test
    public void testEOFConstructor() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);
        assertEquals("", eof.toString());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character("A");
        Token eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertTrue(startTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertTrue(comment.isComment());
        assertTrue(character.isCharacter());
        assertTrue(eof.isEOF());
    }
}