package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        doctype.name.append("test");
        doctype.reset();
        assertFalse(doctype.forceQuirks);
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("href");
        startTag.appendAttributeValue("https://example.com");
        startTag.newAttribute();
        assertEquals("href", startTag.getAttributes().iterator().next().getKey());
        assertEquals("https://example.com", startTag.getAttributes().iterator().next().getValue());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("test character");
        assertEquals("test character", character.getData());
    }
}