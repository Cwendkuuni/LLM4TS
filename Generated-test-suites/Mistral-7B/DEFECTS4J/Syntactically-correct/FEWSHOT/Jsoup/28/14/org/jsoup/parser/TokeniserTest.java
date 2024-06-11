package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @ org.junit.Before
    public void setup() {
        reader = new CharacterReader();
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_EmptyInput() {
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(0, reader.getPosition());
    }

    @Test
    public void testRead_Character() {
        reader.append("a");
        Token token = tokeniser.read();
        assertEquals("a", token.getData());
        assertEquals(1, reader.getPosition());
    }

    @Test
    public void testRead_CharacterReference() {
        reader.append("&#123;");
        Token token = tokeniser.read();
        assertEquals("&#123;", token.getData());
        assertEquals(1, reader.getPosition());
        assertEquals(123, token.getCharacterCode());
    }

    @Test
    public void testRead_NamedCharacterReference() {
        reader.append("&copy;");
        Token token = tokeniser.read();
        assertEquals("&copy;", token.getData());
        assertEquals(169, token.getCharacterCode());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        reader.append("&#x12345;");
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, reader.getPosition());
    }

    @Test
    public void testRead_SelfClosingTag() {
        reader.append("<img src=\"image.png\" />");
        Token tagStart = tokeniser.read();
        Token tagEnd = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tagStart.getType());
        assertEquals("img", tagStart.tagName);
        assertEquals(Token.TokenType.EndTag, tagEnd.getType());
        assertEquals("img", tagEnd.tagName);
        assertTrue(tagEnd.isSelfClosing());
    }

    @Test
    public void testRead_InvalidInput() {
        reader.append("<script>alert('Hello!')</script>");
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, reader.getPosition());
        Validate.isTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(0, error.getColumnNumber());
        assertEquals("Unexpected character 's' in input state [Data]", error.getMessage());
    }

    @Test
    public void testRead_EmptyScriptTag() {
        reader.append("<script></script>");
        Token tagStart = tokeniser.read();
        Token tagEnd = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tagStart.getType());
        assertEquals("script", tagStart.tagName);
        assertEquals(Token.TokenType.EndTag, tagEnd.getType());
        assertEquals("script", tagEnd.tagName);
    }

    @Test
    public void testRead_InvalidScriptTag() {
        reader.append("<script type=\"text/javascript\"<script>");
        Token tagStart = tokeniser.read();
        Token tagEnd = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tagStart.getType());
        assertEquals("script", tagStart.tagName);
        Validate.isTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(17, error.getColumnNumber());
        assertEquals("Unexpected character '<' in input state [ScriptData]", error.getMessage());
    }

    @Test
    public void testRead_Comment() {
        reader.append("<!-- This is a comment -->");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.getType());
        assertEquals("This is a comment", token.getData());
    }

    @Test
    public void testRead_MultipleInputs() {
        reader.append("<html><body><p>Hello, world!</p></body></html>");
        Token token;
        while ((token = tokeniser.read()) != null) {
            if (token.getType() == Token.TokenType.StartTag) {
                System.out.println(token.tagName);
            }
        }
        Validate.isFalse(errors.hasErrors());
    }

    @Test
    public void testErrorHandling() {
        reader.append("<script>alert('Hello!')</script>");
        Token token = tokeniser.read();
        assertNull(token);
        assertEquals(1, reader.getPosition());
        Validate.isTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(0, error.getColumnNumber());
        assertEquals("Unexpected character 's' in input state [Data]", error.getMessage());
    }

    @Test
    public void testTransitions() {
        reader.append("<html><body><p>Hello, world!</p></body></html>");
        tokeniser.transition(TokeniserState.Script);
        Token token = tokeniser.read();
        assertNull(token);
        Validate.isTrue(errors.hasErrors());
        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals(0, error.getColumnNumber());
        assertEquals("Unexpected character '<' in input state [Script]", error.getMessage());

        tokeniser.transition(TokeniserState.Data);
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("html", token.tagName);
    }

    @Test
    public void testConsumeCharacterReference() {
        reader.append("&amp;");
        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', character);

        reader.append("&apos;");
        character = tokeniser.consumeCharacterReference('\'', false);
        assertEquals('\u0026', character);

        reader.append("&quot;");
        character = tokeniser.consumeCharacterReference('"', false);
        assertEquals('\u0022', character);

        reader.append("&#x00A9;");
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('©', character);

        reader.append("&#x2014;");
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('—', character);

        reader.append("&#xFFFD;");
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(replacementChar, character);
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        Token tag = tokeniser.tagPending;
        assertEquals(Token.TokenType.StartTag, tag.getType());
        tag = tokeniser.tagPending;
        assertEquals(Token.TokenType.EndTag, tag.getType());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tag = tokeniser.tagPending;
        assertEquals(Token.TokenType.StartTag, tag.getType());
        assertEquals("", tag.tagName);
        tag = tokeniser.tagPending;
        assertEquals(Token.TokenType.EndTag, tag.getType());
        assertEquals("", tag.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token comment = tokeniser.commentPending;
        assertEquals(Token.TokenType.Comment, comment.getType());
        assertEquals("", comment.getData());
        comment = tokeniser.commentPending;
        assertNull(comment);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token comment = tokeniser.commentPending;
        assertEquals(Token.TokenType.Comment, comment.getType());
        assertEquals("", comment.getData());
        comment = tokeniser.commentPending;
        assertNull(comment);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token doctype = tokeniser.doctypePending;
        assertEquals(Token.TokenType.Doctype, doctype.getType());
        assertEquals("", doctype.getData());
        doctype = tokeniser.doctypePending;
        assertNull(doctype);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctype = tokeniser.doctypePending;
        assertEquals(Token.TokenType.Doctype, doctype.getType());
        assertEquals("", doctype.getData());
        doctype = tokeniser.doctypePending;
        assertNull(doctype);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder buffer = tokeniser.dataBuffer;
        assertEquals(null, buffer);
        buffer = tokeniser.dataBuffer;
        assertEquals(new StringBuilder(), buffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Data);
        Token tag1 = tokeniser.read();
        Token tag2 = tokeniser.read();
        Token endTag = tokeniser.read();
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("p", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testIsAppropriateEndTagToken_Invalid() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.transition(TokeniserState.Data);
        Token tag1 = tokeniser.read();
        Token tag2 = tokeniser.read();
        Token tag3 = tokeniser.read();
        assertFalse(tokeniser.isAppropriateEndTagToken());
        assertEquals(null, tokeniser.appropriateEndTagName());
    }
}