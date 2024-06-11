import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;

    @Before
    public void setUp() {
        // Initialize test objects
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character("Lorem ipsum");
    }

    @Test
    public void testDoctypeToken() {
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertFalse(doctype.forceQuirks);

        doctype.name.append("doctypeName");
        assertEquals("doctypeName", doctype.getName());

        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testStartTagToken() {
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertFalse(startTag.selfClosing);

        startTag.name("tagName");
        assertEquals("tagName", startTag.name());

        startTag.newAttribute();
        Attributes attributes = startTag.getAttributes();
        assertNotNull(attributes);
        assertTrue(attributes.size() == 1);

        startTag.appendTagName("a");
        startTag.appendTagName('b');
        assertEquals("ab", startTag.tagName);

        startTag.appendAttributeName("attributeName");
        assertEquals("attributeName", startTag.pendingAttributeName);

        startTag.appendAttributeValue("attributeValue");
        assertEquals("attributeValue", startTag.pendingAttributeValue.toString());

        startTag.finaliseTag();
        assertTrue(startTag.attributes.size() == 1);

        startTag.attributes.put(new Attribute("newAttribute", "newValue"));
        startTag.newAttribute();
        assertTrue(startTag.attributes.size() == 2);
        assertEquals("newAttribute", startTag.attributes.get("newAttribute"));
    }

    @Test
    public void testEndTagToken() {
        assertEquals(Token.TokenType.EndTag, endTag.type);

        endTag.name("tagName");
        assertEquals("tagName", endTag.name());
    }

    @Test
    public void testCommentToken() {
        assertEquals(Token.TokenType.Comment, comment.type);

        comment.data.append("commentData");
        assertEquals("commentData", comment.getData());
    }

    @Test
    public void testCharacterToken() {
        assertEquals(Token.TokenType.Character, character.type);

        assertEquals("Lorem ipsum", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

}