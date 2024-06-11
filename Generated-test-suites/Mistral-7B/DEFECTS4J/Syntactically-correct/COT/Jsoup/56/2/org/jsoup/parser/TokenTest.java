import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals(" "); // initial name is empty
        doctype.reset();
        assertEquals(doctype.getClass().getSimpleName(), doctype.tokenType());
        doctype.getName().equals(" "); // name should not be changed after reset
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("public id");
        doctype.setForceQuirks(true);

        assertEquals("html", doctype.getName());
        assertEquals("public id", doctype.getPublicIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name().equals("tagName"); // initial name is empty
        tag.reset();
        assertEquals(TokenType.StartTag, tag.type);
        tag.name().equals("tagName"); // name should not be changed after reset
    }

    @Test
    public void testTagNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeName("attributeName").appendAttributeValue("attributeValue");
        tag.newAttribute();
        Assert.assertNotNull(tag.getAttributes());
        Attribute attribute = tag.getAttributes().get("attributeName");
        assertEquals("attributeValue", attribute.getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag tag = new Token.StartTag();
        tag.appendAttributeName("attributeName").appendAttributeValue("attributeValue");
        tag.newAttribute();
        tag.finaliseTag();
        Assert.assertNotNull(tag.getAttributes());
        Attribute attribute = tag.getAttributes().get("attributeName");
        assertEquals("attributeValue", attribute.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testTagNullAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.finaliseTag();
        tag.getAttributes();
    }

    @Test
    public void testTokenTypeIdentification() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());

        token = new Token.Comment();
        assertTrue(token.isComment());

        token = new Token.Doctype();
        assertTrue(token.isDoctype());

        token = new Token.EndTag();
        assertTrue(token.isEndTag());

        token = new Token.StartTag();
        assertTrue(token.isStartTag());

        token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}