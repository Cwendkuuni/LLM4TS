import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer; // For creating tokens for testing

public class TokenTest {

    @Test
    public void testDoctype_simple() {
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctype_withName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().name("html");
        assertEquals("html", doctype.asDoctype().getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_withDuplicateName() {
        Token doctype1 = new Token.Doctype();
        Token doctype2 = new Token.Doctype();
        doctype1.asDoctype().name("html");
        doctype2.asDoctype().name("html");
    }

    @Test
    public void testDoctype_withSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().systemIdentifier("about:blank");
        assertEquals("about:blank", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_withForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().isForceQuirks(true);
        assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag_simple() {
        Token tag = new Token.StartTag();
        assertTrue(tag.isStartTag());
        assertEquals(TokenType.StartTag, tag.type);
    }

    @Test
    public void testTag_withName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        assertEquals("div", tag.asStartTag().name());
    }

    @Test
    public void testTag_selfClosing() {
        Token tag = new Token.StartTag();
        tag.asStartTag().isSelfClosing(true);
        assertTrue(tag.asStartTag().isSelfClosing());
    }

    @Test
    public void testTag_withAttributes() {
        Token tag = new Token.StartTag();
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue("test");

        assertTrue(tag.asStartTag().getAttributes() != null);
        Attribute attribute = tag.asStartTag().getAttributes().get("class");
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testTag_emptyName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("");
        assertEquals("", tag.asStartTag().name());
    }

    @Test
    public void testTag_nullName() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name(null);
        //Assertions.assertNull(tag.asStartTag().name()); // JUnit 4 does not support null checks on strings
        assertTrue(tag.asStartTag().name().length() == 0);
    }

    @Test
    public void testEndTag_simple() {
        Token tag = new Token.EndTag();
        assertTrue(tag.isEndTag());
        assertEquals(TokenType.EndTag, tag.type);
    }

    @Test
    public void testEndTag_withName() {
        Token tag = new Token.EndTag();
        tag.asEndTag().name("div");
        assertEquals("div", tag.asEndTag().name());
    }

    @Test(expected = AssertionError.class)
    public void testEndTag_withNonExistentName() {
        Token tag = new Token.EndTag();
        tag.asEndTag().name("nonExistentTag");
    }

    @Test
    public void testComment_simple() {
        Token comment = new Token.Comment();
        assertTrue(comment.isComment());
        assertEquals(TokenType.Comment, comment.type);
    }

    @Test
    public void testComment_withData() {
        Token comment = new Token.Comment();
        comment.asComment().data("This is a test comment.");
        assertEquals("This is a test comment.", comment.asComment().getData());
    }

    @Test
    public void testCharacter_simple() {
        Token character = new Token.Character("A");
        assertTrue(character.isCharacter());
        assertEquals(TokenType.Character, character.type);
        assertEquals("A", character.getData());
    }

    @Test
    public void testCharacter_multibyte() {
        Token character = new Token.Character("\u00E9");
        assertTrue(character.isCharacter());
        assertEquals(TokenType.Character, character.type);
        assertEquals("\u00E9", character.getData());
    }

    @Test
    public void testEOF_simple() {
        Token eof = new Token.EOF();
        assertTrue(eof.isEOF());
        assertEquals(TokenType.EOF, eof.type);
    }
}