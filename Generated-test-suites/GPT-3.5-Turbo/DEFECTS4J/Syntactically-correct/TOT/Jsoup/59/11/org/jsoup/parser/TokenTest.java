package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test cases for Abstract Token class
    @Test
    public void testTokenType() {
        Token.EOF token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    // Test cases for Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "pubKey";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    // Test cases for Tag class
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("attrVal");
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        Token.Tag resetTag = tag.reset();

        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    // Test cases for StartTag class
    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.nameAttr("test", attributes);

        assertEquals("test", startTag.tagName);
        assertEquals("test", startTag.normalName);
        assertEquals(attributes, startTag.attributes);
    }

    // Test cases for EndTag class
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";

        assertEquals("test", endTag.tagName);
    }

    // Test cases for Comment class
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("commentData");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    // Test cases for Character class
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "testData";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    // Test cases for EOF class
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();

        assertEquals(eof, resetEOF);
    }

}