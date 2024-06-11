package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        token.type = Token.TokenType.Doctype;
        assertTrue(token.isDoctype());

        token.type = Token.TokenType.StartTag;
        assertTrue(token.isStartTag());

        token.type = Token.TokenType.EndTag;
        assertTrue(token.isEndTag());

        token.type = Token.TokenType.Comment;
        assertTrue(token.isComment());

        token.type = Token.TokenType.Character;
        assertTrue(token.isCharacter());

        token.type = Token.TokenType.EOF;
        assertTrue(token.isEOF());
    }

    @Test
    public void testDoctypeTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "sys";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        doctype.pubSysKey = "sys";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("test", doctype.getName());
        assertEquals("sys", doctype.getPubSysKey());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attrName";
        startTag.pendingAttributeValue.append("attrValue");
        startTag.pendingAttributeValueS = "attrValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.pendingAttributeName = "attrName";
        startTag.pendingAttributeValue.append("attrValue");
        startTag.pendingAttributeValueS = "attrValueS";
        startTag.hasPendingAttributeValue = true;

        startTag.newAttribute();

        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertNull(startTag.attributes);

        startTag.newAttribute();
        assertNotNull(startTag.attributes);
    }

    @Test
    public void testStartTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.pendingAttributeName = "attrName";
        startTag.pendingAttributeValue.append("attrValue");
        startTag.pendingAttributeValueS = "attrValueS";
        startTag.hasPendingAttributeValue = true;

        startTag.finaliseTag();

        assertNotNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        endTag.normalName = "test";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");

        String expectedOutput = "<test attr1=\"value1\">";
        assertEquals(expectedOutput, startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";

        String expectedOutput = "</test>";
        assertEquals(expectedOutput, endTag.toString());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");

        String expectedOutput = "<!--test-->";
        assertEquals(expectedOutput, comment.toString());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "test";

        String expectedOutput = "test";
        assertEquals(expectedOutput, character.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
        // additional validation can be performed if required
    }
}