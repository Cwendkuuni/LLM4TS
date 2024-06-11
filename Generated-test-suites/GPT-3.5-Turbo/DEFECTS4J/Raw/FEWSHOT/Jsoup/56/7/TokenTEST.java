package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        token.reset();
        assertEquals(TokenType.Doctype, token.type);
        
        token = new Token.StartTag();
        token.reset();
        assertEquals(TokenType.StartTag, token.type);
        
        token = new Token.EndTag();
        token.reset();
        assertEquals(TokenType.EndTag, token.type);
        
        token = new Token.Comment();
        token.reset();
        assertEquals(TokenType.Comment, token.type);
        
        token = new Token.Character();
        token.reset();
        assertEquals(TokenType.Character, token.type);
        
        token = new Token.EOF();
        token.reset();
        assertEquals(TokenType.EOF, token.type);
    }
    
    @Test
    public void testName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());
        
        startTag.name("DIV");
        assertEquals("DIV", startTag.name());
        assertEquals("div", startTag.normalName());
    }
    
    @Test
    public void testIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(false, startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        assertEquals(true, startTag.isSelfClosing());
    }

    @Test
    public void testAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(null, startTag.getAttributes());

        startTag.attr("id", "test");
        assertEquals("id", startTag.attributes().get(0).getKey());
        assertEquals("test", startTag.attributes().get(0).getValue());
        
        startTag.attr("class", "test");
        assertEquals("class", startTag.attributes().get(1).getKey());
        assertEquals("test", startTag.attributes().get(1).getValue());
    }

    @Test
    public void testToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertEquals("<div>", startTag.toString());
        
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());
        
        Token.Comment comment = new Token.Comment();
        comment.data("test");
        assertEquals("<!--test-->", comment.toString());
        
        Token.Character character = new Token.Character();
        character.data("test");
        assertEquals("test", character.toString());
    }

}