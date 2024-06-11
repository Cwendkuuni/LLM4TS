package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("pub");
        doctype.systemIdentifier.append("sys");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertEquals(false, resetDoctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.attributes = new Attributes();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("test");
        tag.pendingAttributeValueS = "test";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;

        Token.Tag resetTag = tag.reset();

        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertEquals(false, resetTag.hasEmptyAttributeValue);
        assertEquals(false, resetTag.hasPendingAttributeValue);
        assertEquals(false, resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertEquals(false, resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEof = eof.reset();

        assertEquals(TokenType.EOF, resetEof.type);
    }
}