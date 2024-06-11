package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        token.type = Token.TokenType.Character;
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
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
        tag.tagName = "tag";
        tag.normalName = "tag";
        tag.pendingAttributeName = "name";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = "s";
        tag.hasEmptyAttributeValue = true;
        tag.selfClosing = true;
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        tag.attributes = attributes;

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
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "name";
        tag.newAttribute();
        tag.pendingAttributeName = "name2";
        tag.finaliseTag();
        tag.pendingAttributeName = "name3";
        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = null;
        tag.normalName = null;

        tag.appendTagName("tag");
        assertEquals("tag", tag.tagName);
        assertEquals("tag", tag.normalName);

        tag.appendTagName('s');
        assertEquals("tags", tag.tagName);
        assertEquals("tags", tag.normalName);
    }

    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = null;

        tag.appendAttributeName("name");
        assertEquals("name", tag.pendingAttributeName);

        tag.appendAttributeName('2');
        assertEquals("name2", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.ensureAttributeValue();
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());

        tag.ensureAttributeValue();
        tag.appendAttributeValue('2');
        assertEquals("value2", tag.pendingAttributeValue.toString());

        tag.ensureAttributeValue();
        tag.appendAttributeValue(new char[]{'3', '4'});
        assertEquals("value234", tag.pendingAttributeValue.toString());

        tag.ensureAttributeValue();
        tag.appendAttributeValue(new int[]{119, 120});
        assertEquals("value234wx", tag.pendingAttributeValue.toString());

        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        Attributes attributes = new Attributes();
        attributes.put("attribute", "value");
        startTag.attributes = attributes;

        Token.StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertEquals(false, resetStartTag.hasEmptyAttributeValue);
        assertEquals(false, resetStartTag.hasPendingAttributeValue);
        assertEquals(false, resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        assertEquals("</tag>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertEquals(false, resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Token.Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testEofReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF resetEOF = eof.reset();

        assertEquals(resetEOF, eof);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        assertEquals(token, token.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        assertEquals(token, token.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        assertEquals(token, token.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        assertEquals(token, token.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        assertEquals(token, token.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());
    }
}