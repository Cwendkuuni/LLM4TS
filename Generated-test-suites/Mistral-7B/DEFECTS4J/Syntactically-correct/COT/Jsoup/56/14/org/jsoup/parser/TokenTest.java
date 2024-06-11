import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(0, doctype.getName().length());
        assertEquals(0, doctype.getPublicIdentifier().length());
        assertEquals(0, doctype.getSystemIdentifier().length());
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("public id");
        doctype.getSystemIdentifier().append("system id");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("public id", doctype.getPublicIdentifier());
        assertEquals("system id", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.reset();
        assertEquals(TokenType.StartTag, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals(0, tag.pendingAttributeValue.length());
        assertEquals(0, tag.pendingAttributeValueS.length());
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @org.junit.Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertTrue(tag.attributes.size() == 0);
    }

    @org.junit.Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();
        assertNotNull(tag.attributes);
        assertTrue(tag.attributes.size() == 1);
        assertEquals("class", tag.attributes.keySet().iterator().next());
        assertEquals("test", tag.attributes.get("class").getValue());
    }

    @org.junit.Test
    public void testTagName() {
        Token.StartTag tag = new Token.StartTag();
        tag.name("html");
        assertEquals("html", tag.name());
        assertEquals("html", tag.normalName());
    }

    @org.junit.Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        tag.isSelfClosing = true;
        assertTrue(tag.isSelfClosing());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isSelfClosing());
    }

    @org.junit.Test
    public void testTagGetAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute().name("class").value("test");
        tag.newAttribute().name("id").value("123");

        Attributes attributes = tag.getAttributes();
        assertEquals(2, attributes.size());
        assertTrue(attributes.keySet().contains("class"));
        assertTrue(attributes.keySet().contains("id"));
        assertEquals("test", attributes.get("class").getValue());
        assertEquals("123", attributes.get("id").getValue());
    }

    @org.junit.Test
    public void testTokengetType() {
        Token token = new Token.Doctype();
        assertEquals(TokenType.Doctype, token.type);

        token = new Token.StartTag();
        assertEquals(TokenType.StartTag, token.type);

        token = new Token.EndTag();
        assertEquals(TokenType.EndTag, token.type);

        token = new Token.Comment();
        assertEquals(TokenType.Comment, token.type);

        token = new Token.Character();
        assertEquals(TokenType.Character, token.type);

        token = new Token.EOF();
        assertEquals(TokenType.EOF, token.type);
    }

    @org.junit.Test(expected = AssertionError.class)
    public void testTokenNullType() {
        Token token = null;
        assertEquals(TokenType.EOF, token.type); // This will fail with NullPointerException
    }

    @org.junit.Test
    public void testTokenIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        token = new Token.StartTag();
        assertFalse(token.isDoctype());
    }

    @org.junit.Test
    public void testTokenAsDoctype() {
        Token token = new Token.Doctype();
        assertEquals(token.getClass(), token.asDoctype().getClass());
        assertEquals(TokenType.Doctype, token.asDoctype().type);
    }

    @org.junit.Test
    public void testTokenIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        token = new Token.EndTag();
        assertFalse(token.isStartTag());
    }

    @org.junit.Test
    public void testTokenAsStartTag() {
        Token token = new Token.StartTag();
        assertEquals(token.getClass(), token.asStartTag().getClass());
        assertEquals(TokenType.StartTag, token.asStartTag().type);
    }

    @org.junit.Test
    public void testTokenIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        token = new Token.StartTag();
        assertFalse(token.isEndTag());
    }

    @org.junit.Test
    public void testTokenAsEndTag() {
        Token token = new Token.EndTag();
        assertEquals(token.getClass(), token.asEndTag().getClass());
        assertEquals(TokenType.EndTag, token.asEndTag().type);
    }

    @org.junit.Test
    public void testTokenIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        token = new Token.Doctype();
        assertFalse(token.isComment());
    }

    @org.junit.Test
    public void testTokenAsComment() {
        Token token = new Token.Comment();
        assertEquals(token.getClass(), token.asComment().getClass());
        assertEquals(TokenType.Comment, token.asComment().type);
    }

    @org.junit.Test
    public void testTokenIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());

        token = new Token.Doctype();
        assertFalse(token.isCharacter());
    }

    @org.junit.Test
    public void testTokenAsCharacter() {
        Token token = new Token.Character();
        assertEquals(token.getClass(), token.asCharacter().getClass());
        assertEquals(TokenType.Character, token.asCharacter().type);
    }

    @org.junit.Test
    public void testTokenIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        token = new Token.Doctype();
        assertFalse(token.isEOF());
    }

    @org.junit.Test
    public void testTokenAsEOF() {
        Token token = new Token.EOF();
        assertEquals(token.getClass(), token.asEOF().getClass());
        assertEquals(TokenType.EOF, token.asEOF().type);
    }
}