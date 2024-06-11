import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    public void testDoctype_name() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctype_name_publicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("XHTML");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("XHTML", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctype_name_publicIdentifier_systemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("XHTML");
        doctype.systemIdentifier("/index.html");
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("XHTML", doctype.getPublicIdentifier());
        assertEquals("/index.html", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_name_publicIdentifier_systemIdentifier_forceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("html");
        doctype.publicIdentifier("XHTML");
        doctype.systemIdentifier("/index.html");
        doctype.forceQuirks(true);
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("html", doctype.getName());
        assertEquals("XHTML", doctype.getPublicIdentifier());
        assertEquals("/index.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    // Add test cases for other token types (Tag, Character, Comment, EOF) here...

    @Test
    public void testTag() {
        Token tag = new Token.StartTag();
        assertEquals("StartTag", tag.tokenType());
        tag.name("div");
        assertEquals("div", tag.name());
        tag.selfClosing(true);
        assertTrue(tag.isSelfClosing());

        tag = new Token.EndTag();
        assertEquals("EndTag", tag.tokenType());
        tag.name("div");
        assertEquals("div", tag.name());
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTag_name_attributes() {
        Token tag = new Token.StartTag("div");
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("main");

        Attributes attributes = tag.getAttributes();
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("red", attributes.get("class"));
        assertEquals("id", attributes.keySet().iterator().next());
        assertEquals("main", attributes.get("id"));
        assertEquals(2, attributes.size());
    }

    // Add other test cases for Tag methods here...

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());
        comment.appendAttributeValue("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("A");
        assertEquals("Character", character.tokenType());
        assertEquals("A", character.getData());
    }

    @Test
    public void testCharacter_multiple() {
        Token character = new Token.Character("ABC");
        assertEquals("Character", character.tokenType());
        assertEquals("ABC", character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
        assertTrue(eof.isEOF());
    }

    // Add other test cases for specific token types here...

    @Test
    public void testTokenType() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        token = new Token.EndTag();
        assertTrue(token.isEndTag());
        token = new Token.Comment();
        assertTrue(token.isComment());
        token = new Token.Character('A');
        assertTrue(token.isCharacter());
        token = new Token.Doctype();
        assertTrue(token.isDoctype());
        token = new Token.EOF();
        assertTrue(token.isEOF());
    }

    @Test
    public void testToken_type_casting() {
        Token token = new Token.StartTag();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.StartTag);

        token = new Token.EndTag();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.EndTag);

        token = new Token.Comment();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Comment);

        token = new Token.Character('A');
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Character);

        token = new Token.Doctype();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.Doctype);

        token = new Token.EOF();
        assertTrue(token instanceof Token);
        assertTrue(token instanceof Token.EOF);
    }
}