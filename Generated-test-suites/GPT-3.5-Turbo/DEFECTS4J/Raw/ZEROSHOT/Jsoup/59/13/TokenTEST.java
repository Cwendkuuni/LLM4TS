package org.jsoup.parser;

import static org.junit.Assert.*;
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
        
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "pendingAttributeName";
        tag.pendingAttributeValue.append("pendingAttributeValue");
        tag.pendingAttributeValueS = "pendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        tag.reset();
        
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        
        startTag.nameAttr("name", attributes);
        
        assertEquals("name", startTag.tagName);
        assertEquals("name", startTag.normalName);
        assertSame(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "name";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr1", "value1");
        
        assertEquals("<name attr1=\"value1\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "name";
        
        assertEquals("</name>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";
        
        character.reset();
        
        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("data");
        
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertSame(eof, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
}