import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

        token = new Token.Character('a');
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        TokenDoctype token = new Token.Doctype();
        token.name("html");
        assertEquals("html", token.getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        TokenDoctype token = new Token.Doctype();
        token.publicIdentifier("publicId");
        assertEquals("publicId", token.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        TokenDoctype token = new Token.Doctype();
        token.systemIdentifier("systemId");
        assertEquals("systemId", token.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        TokenDoctype token = new Token.Doctype();
        token.isForceQuirks();
        assertFalse(token.isForceQuirks());

        token.setForceQuirks(true);
        assertTrue(token.isForceQuirks());
    }

    @Test(expected = IllegalStateException.class)
    public void testTag_newAttribute_whenPreviousAttributeIsNull_throwsException() {
        TokenTag tag = new Token.StartTag();
        tag.newAttribute();
    }

    @Test
    public void testTag_newAttribute() {
        TokenTag tag = new Token.StartTag();
        tag.newAttribute();
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTag_getAttributes() {
        TokenTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.newAttribute("testAttribute", "testValue");
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.put("testAttribute", new Attribute("testAttribute", "testValue"));
        assertEquals(expectedAttributes, tag.getAttributes());
    }

    @Test
    public void testTag_appendTagName() {
        TokenTag tag = new Token.StartTag();
        tag.appendTagName("div");
        assertEquals("div", tag.name());
    }

    @Test
    public void testTag_appendAttributeName() {
        TokenTag tag = new Token.StartTag();
        tag.appendAttributeName("class");
        assertEquals("class", tag.pendingAttributeName);
    }

    @Test
    public void testTag_appendAttributeValue() {
        TokenTag tag = new Token.StartTag();
        tag.appendAttributeValue("testClass");
        assertEquals("testClass", tag.pendingAttributeValue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testTag_finaliseTag_whenAttributesIsNull_throwsException() {
        TokenTag tag = new Token.StartTag();
        tag.finaliseTag();
    }
}