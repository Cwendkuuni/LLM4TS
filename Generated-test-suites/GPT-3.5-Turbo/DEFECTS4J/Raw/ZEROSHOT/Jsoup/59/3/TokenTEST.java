package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
        
        assertEquals(TokenType.Character, token.type);
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("wrapper");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("class", "wrapper"));
        
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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "Hello";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        eof.reset();
        
        assertSame(eof, eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        Token doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        
        Token.Doctype doctype = token.asDoctype();
        
        assertSame(token, doctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        
        Token.StartTag startTag = token.asStartTag();
        
        assertSame(token, startTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        
        Token.EndTag endTag = token.asEndTag();
        
        assertSame(token, endTag);
    }
    
    @Test
    public void testIsComment() {
        Token comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        
        Token.Comment comment = token.asComment();
        
        assertSame(token, comment);
    }
    
    @Test
    public void testIsCharacter() {
        Token character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        
        Token.Character character = token.asCharacter();
        
        assertSame(token, character);
    }
    
    @Test
    public void testIsEOF() {
        Token eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
}