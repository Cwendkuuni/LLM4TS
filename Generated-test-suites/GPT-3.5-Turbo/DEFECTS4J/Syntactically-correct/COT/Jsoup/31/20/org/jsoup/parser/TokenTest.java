
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            //Override Token to create an instance for testing
        };

        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals("", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals("", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();

        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag() {
            //Override Tag to create an instance for testing
        };

        tag.newAttribute();

        assertNotNull(tag.getAttributes());
        assertEquals(0, tag.getAttributes().size());
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag() {
            //Override Tag to create an instance for testing
        };

        tag.finaliseTag();

        assertNotNull(tag.getAttributes());
        assertEquals(0, tag.getAttributes().size());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();

        tag.name("test");

        assertEquals("test", tag.name());

    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();

        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagAppendTagNameString() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName("test");

        assertEquals("test", tag.tagName);
    }

    @Test
    public void testTagAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendTagName('t');

        assertEquals("t", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeNameString() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName("test");

        assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeName('t');

        assertEquals("t", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValueString() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue("test");

        assertNotNull(tag.pendingAttributeValue);
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag();
        tag.appendAttributeValue('t');

        assertNotNull(tag.pendingAttributeValue);
        assertEquals("t", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");

        assertEquals("<tag>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tag");

        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();

        assertEquals("", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("test");

        assertEquals("test", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertEquals(doctype, doctype.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertEquals(startTag, startTag.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals(endTag, endTag.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals(comment, comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("test");

        assertTrue(character.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("test");

        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }
}
