package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() throws IOException {
        // Test basic functionality of the tokeniser
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body><p>foo</p></body></html>".getBytes());
        InputStreamReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader, new ArrayList<>());
        List<Token> tokens = new ArrayList<>();
        Token token;
        while ((token = tokeniser.read()) != null) {
            tokens.add(token);
        }

        // Test basic token types
        assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
        assertEquals(Token.TokenType.CharacterData, tokens.get(1).type);
        assertEquals(Token.TokenType.EndTag, tokens.get(2).type);
        assertEquals(Token.TokenType.StartTag, tokens.get(3).type);
        assertEquals(Token.TokenType.CharacterData, tokens.get(4).type);
        assertEquals(Token.TokenType.EndTag, tokens.get(5).type);
        assertEquals(Token.TokenType.EndDocument, tokens.get(6).type);

        // Test that the tokeniser correctly transitions through states
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.advanceTransition(TokeniserState.StartTagBody);
        assertEquals(TokeniserState.StartTagBody, tokeniser.getState());
        tokeniser.advanceTransition(TokeniserState.CharacterData);
        assertEquals(TokeniserState.CharacterData, tokeniser.getState());
        tokeniser.advanceTransition(TokeniserState.EndTag);
        assertEquals(TokeniserState.EndTag, tokeniser.getState());
        tokeniser.acknowledgeSelfClosingFlag();
        assertEquals(TokeniserState.EndTag, tokeniser.getState());
        tokeniser.acknowledgeSelfClosingFlag();
        assertEquals(TokeniserState.CharacterData, tokeniser.getState());

        // Test that the tokeniser correctly handles character references
        assertEquals("&amp;", tokens.get(1).toString());
        assertEquals("&quot;", tokens.get(4).toString());
        assertEquals("&apos;", tokens.get(5).toString());
        assertEquals("&gt;", tokens.get(6).toString());
        assertEquals("&lt;", tokens.get(7).toString());
        assertEquals("&nbsp;", tokens.get(8).toString());
        assertEquals("&eacute;", tokens.get(9).toString());

        // Test that the tokeniser correctly handles comments
        assertEquals(Comment.class, tokens.get(10).getClass());
        assertEquals("<!-- Test -->", tokens.get(10).asComment().getData().trim());

        // Test that the tokeniser correctly handles self-closing tags
        assertSame(tokens.get(2), tokens.get(4));
        assertEquals("img", tokens.get(2).asStartTag().getName());
        assertSame(tokens.get(3), tokens.get(5));
        assertEquals("img", tokens.get(3).asEndTag().getName());

        // Test that the tokeniser correctly handles doctype declarations
        assertEquals(Doctype.class, tokens.get(11).getClass());
        assertEquals("<!DOCTYPE html SYSTEM \"about:legacy-compat\">", tokens.get(11).asDoctype().asString());

        // Test that the tokeniser correctly handles errors
        tokeniser = new Tokeniser(new InputStreamReader(new ByteArrayInputStream("foo".getBytes())), new ArrayList<>());
        token = tokeniser.read();
        assertNull(token);
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.error(TokeniserState.Data);
        assertNull(tokeniser.read());
        assertEquals(TokeniserState.Error, tokeniser.getState());
    }
}