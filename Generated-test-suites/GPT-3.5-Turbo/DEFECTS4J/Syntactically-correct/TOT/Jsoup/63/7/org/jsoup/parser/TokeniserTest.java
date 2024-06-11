package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class TokeniserTest {
    
    @Test
    public void testRead() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.StartTag, result.type);
        Assert.assertEquals("html", ((Token.StartTag) result).tagName);
    }
    
    @Test
    public void testEmit() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "html";
        
        tokeniser.emit(startTag);
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.StartTag, result.type);
        Assert.assertEquals("html", ((Token.StartTag) result).tagName);
    }
    
    @Test
    public void testEmitString() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        String str = "test";
        
        tokeniser.emit(str);
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.Character, result.type);
        Assert.assertEquals("test", ((Token.Character) result).getData());
    }
    
    @Test
    public void testEmitCharArray() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        char[] chars = {'t', 'e', 's', 't'};
        
        tokeniser.emit(chars);
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.Character, result.type);
        Assert.assertEquals("test", ((Token.Character) result).getData());
    }
    
    @Test
    public void testEmitInt() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        int[] codepoints = {'t', 'e', 's', 't'};
        
        tokeniser.emit(codepoints);
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.Character, result.type);
        Assert.assertEquals("test", ((Token.Character) result).getData());
    }
    
    @Test
    public void testConsumeCharacterReference() {
        CharacterReader reader = new CharacterReader("&#65;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        int[] result = tokeniser.consumeCharacterReference(null, false);
        
        int[] expected = {65};
        
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testCreateTagPending() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "html";
        
        tokeniser.emitTagPending();
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.StartTag, result.type);
        Assert.assertEquals("html", ((Token.StartTag) result).tagName);
    }
    
    @Test
    public void testCreateCommentPending() {
        CharacterReader reader = new CharacterReader("<!-- comment -->");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = " comment ";
        
        tokeniser.emitCommentPending();
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.Comment, result.type);
        Assert.assertEquals(" comment ", ((Token.Comment) result).getData());
    }
    
    @Test
    public void testCreateDoctypePending() {
        CharacterReader reader = new CharacterReader("<!DOCTYPE html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        
        tokeniser.emitDoctypePending();
        
        Token result = tokeniser.read();
        
        Assert.assertEquals(Token.TokenType.Doctype, result.type);
        Assert.assertEquals("html", ((Token.Doctype) result).getName());
    }
    
    @Test
    public void testCreateTempBuffer() {
        CharacterReader reader = new CharacterReader("<html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.createTempBuffer();
        
        Assert.assertEquals("", tokeniser.dataBuffer.toString());
    }
    
    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = "html";
        tokeniser.tagPending = new Token.StartTag();
        tokeniser.tagPending.tagName = "html";
        
        boolean result = tokeniser.isAppropriateEndTagToken();
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testAppropriateEndTagName() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.lastStartTag = "html";
        
        String result = tokeniser.appropriateEndTagName();
        
        Assert.assertEquals("html", result);
    }
    
    @Test
    public void testError() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.error("Error message");
        
        ParseError result = errors.get(0);
        
        Assert.assertEquals("Error message", result.getErrorMsg());
    }
    
    @Test
    public void testErrorState() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        TokeniserState state = TokeniserState.Data;
        ParseErrorList states = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.error(state);
        
        ParseError result = errors.get(0);
        
        Assert.assertEquals(String.format("Unexpected character '%s' in input state [%s]", reader.current(), state), result.getErrorMsg());
    }
    
    @Test
    public void testEofError() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        TokeniserState state = TokeniserState.Data;
        ParseErrorList states = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.eofError(state);
        
        ParseError result = errors.get(0);
        
        Assert.assertEquals(String.format("Unexpectedly reached end of file (EOF) in input state [%s]", state), result.getErrorMsg());
    }
    
    @Test
    public void testCharacterReferenceError() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        tokeniser.characterReferenceError("Invalid character reference");
        
        ParseError result = errors.get(0);
        
        Assert.assertEquals("Invalid character reference: Invalid character reference", result.getErrorMsg());
    }
    
    @Test
    public void testCurrentNodeInHtmlNS() {
        CharacterReader reader = new CharacterReader("<html>test</html>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        
        boolean result = tokeniser.currentNodeInHtmlNS();
        
        Assert.assertTrue(result);
    }
}