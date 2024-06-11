
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
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
        
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;
        
        assertEquals("html", doctype.getName());
        assertEquals("pub", doctype.getPubSysKey());
        assertEquals("pubId", doctype.getPublicIdentifier());
        assertEquals("sysId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeTokenReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("pubId");
        doctype.systemIdentifier.append("sysId");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = (Token.Doctype) doctype.reset();
        
        assertNull(resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = "attr";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "test"));
        
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        assertTrue(startTag.isSelfClosing());
        assertEquals("id", startTag.pendingAttributeName);
        assertEquals("test", startTag.pendingAttributeValue.toString());
        assertEquals("attr", startTag.pendingAttributeValueS);
        assertTrue(startTag.hasEmptyAttributeValue);
        assertTrue(startTag.hasPendingAttributeValue);
        assertEquals("test", startTag.getAttributes().get("class"));
    }
    
    @Test
    public void testStartTagTokenReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("test");
        startTag.pendingAttributeValueS = "attr";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "test"));
        
        Token.StartTag resetStartTag = (Token.StartTag) startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }
    
    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        assertEquals("div", endTag.name());
        assertEquals("div", endTag.normalName());
    }
    
    @Test
    public void testEndTagTokenReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        Token.EndTag resetEndTag = (Token.EndTag) endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }
    
    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
        
        comment.data.append("test");
        comment.bogus = true;
        
        assertEquals("test", comment.getData());
        assertTrue(comment.bogus);
    }
    
    @Test
    public void testCommentTokenReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;
        
        Token.Comment resetComment = (Token.Comment) comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character();
        
        assertNull(character.data);
        
        character.data("test");
        
        assertEquals("test", character.getData());
    }
    
    @Test
    public void testCharacterTokenReset() {
        Token.Character character = new Token.Character();
        character.data("test");
        
        Token.Character resetCharacter = (Token.Character) character.reset();
        
        assertNull(resetCharacter.data);
    }
    
    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        
        assertEquals(Token.TokenType.EOF, eof.type);
    }
    
    @Test
    public void testEOFTokenReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = (Token.EOF) eof.reset();
        
        assertEquals(Token.TokenType.EOF, resetEOF.type);
    }
}
