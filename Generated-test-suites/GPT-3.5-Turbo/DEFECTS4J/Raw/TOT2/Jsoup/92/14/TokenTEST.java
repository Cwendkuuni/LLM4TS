import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.appendAttributeValue("id=\"test\"");
        tag.reset();
        assertEquals("", tag.name());
        assertFalse(tag.isSelfClosing());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.newAttribute();
        assertNotNull(tag.getAttributes());
        assertEquals("test", tag.getAttributes().get("class"));
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("class");
        tag.finaliseTag();
        assertNotNull(tag.getAttributes());
        assertEquals("", tag.getAttributes().get("class"));
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test");
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("Test");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertNotNull(eof);
    }
}