package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TokenTest {
    
    private Token token;
    
    @Before
    public void setUp() {
        token = new Token() {
            @Override
            Token reset() {
                return this;
            }
        };
    }
    
    @Test
    public void testTokenType() {
        assertEquals("Token", token.tokenType());
    }
    
    @Test
    public void testReset() {
        Token resetToken = token.reset();
        assertSame(resetToken, token);
    }
    
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("doctype");
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;
        
        Token resetDoctype = doctype.reset();
        
        assertSame(resetDoctype, doctype);
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }


    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr", "value");
        
        Token resetStartTag = startTag.reset();
        
        assertSame(resetStartTag, startTag);
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
        endTag.tagName = "tag";
        endTag.normalName = "tag";
        
        Token resetEndTag = endTag.reset();
        
        assertSame(resetEndTag, endTag);
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment data");
        comment.bogus = true;
        
        Token resetComment = comment.reset();
        
        assertSame(resetComment, comment);
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "character data";
        
        Token resetCharacter = character.reset();
        
        assertSame(resetCharacter, character);
        assertNull(character.data);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token resetEOF = eof.reset();
        
        assertSame(resetEOF, eof);
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertTrue(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token asToken = doctype;
        
        Token.Doctype asDoctype = asToken.asDoctype();
        
        assertSame(doctype, asDoctype);
    }
    
    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertTrue(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token asToken = startTag;
        
        Token.StartTag asStartTag = asToken.asStartTag();
        
        assertSame(startTag, asStartTag);
    }
    
    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        
        assertTrue(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token asToken = endTag;
        
        Token.EndTag asEndTag = asToken.asEndTag();
        
        assertSame(endTag, asEndTag);
    }
    
    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        
        assertTrue(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        Token asToken = comment;
        
        Token.Comment asComment = asToken.asComment();
        
        assertSame(comment, asComment);
    }
    
    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        
        assertTrue(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();
        Token asToken = character;
        
        Token.Character asCharacter = asToken.asCharacter();
        
        assertSame(character, asCharacter);
    }
    
    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
    
    @Test
    public void testTokenTypeEnums() {
        assertEquals(Token.TokenType.Doctype, Token.TokenType.valueOf("Doctype"));
        assertEquals(Token.TokenType.StartTag, Token.TokenType.valueOf("StartTag"));
        assertEquals(Token.TokenType.EndTag, Token.TokenType.valueOf("EndTag"));
        assertEquals(Token.TokenType.Comment, Token.TokenType.valueOf("Comment"));
        assertEquals(Token.TokenType.Character, Token.TokenType.valueOf("Character"));
        assertEquals(Token.TokenType.EOF, Token.TokenType.valueOf("EOF"));
    }
}