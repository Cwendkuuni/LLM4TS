package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Tokeniser.ParseError;
import org.jsoup.parser.Tokeniser;

public class TokeniserTest {

    @Test
    public void testTokeniserStateData() {
        Tokeniser tokeniser = createTokeniser("");
        assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateBeforeScript() {
        Tokeniser tokeniser = createTokeniser("<script type='text/javascript'>");
        assertEquals(TokeniserState.BeforeScript, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateScript() {
        Tokeniser tokeniser = createTokeniser("<script type='text/javascript'>");
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateAfterScript() {
        Tokeniser tokeniser = createTokeniser("<script type='text/javascript'>");
        tokeniser.transition(TokeniserState.AfterScript);
        assertEquals(TokeniserState.AfterScript, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateBeforeStyle() {
        Tokeniser tokeniser = createTokeniser("<style type='text/css'>");
        assertEquals(TokeniserState.BeforeStyle, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateStyle() {
        Tokeniser tokeniser = createTokeniser("<style type='text/css'>");
        tokeniser.transition(TokeniserState.Style);
        assertEquals(TokeniserState.Style, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateAfterStyle() {
        Tokeniser tokeniser = createTokeniser("<style type='text/css'>");
        tokeniser.transition(TokeniserState.AfterStyle);
        assertEquals(TokeniserState.AfterStyle, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateBeforeComment() {
        Tokeniser tokeniser = createTokeniser("<!--");
        assertEquals(TokeniserState.BeforeComment, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateComment() {
        Tokeniser tokeniser = createTokeniser("<!--");
        tokeniser.transition(TokeniserState.Comment);
        assertEquals(TokeniserState.Comment, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateAfterComment() {
        Tokeniser tokeniser = createTokeniser("<!--");
        tokeniser.transition(TokeniserState.AfterComment);
        assertEquals(TokeniserState.AfterComment, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateBeforeTag() {
        Tokeniser tokeniser = createTokeniser("<tag>");
        assertEquals(TokeniserState.BeforeTag, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateStartTag() {
        Tokeniser tokeniser = createTokeniser("<tag>");
        tokeniser.transition(TokeniserState.StartTag);
        assertEquals(TokeniserState.StartTag, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateEndTag() {
        Tokeniser tokeniser = createTokeniser("<tag></tag>");
        tokeniser.transition(TokeniserState.EndTag);
        assertEquals(TokeniserState.EndTag, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateAfterEndTag() {
        Tokeniser tokeniser = createTokeniser("<tag></tag>");
        tokeniser.transition(TokeniserState.AfterEndTag);
        assertEquals(TokeniserState.AfterEndTag, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateBeforeText() {
        Tokeniser tokeniser = createTokeniser("<tag>text</tag>");
        tokeniser.transition(TokeniserState.BeforeText);
        assertEquals(TokeniserState.BeforeText, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateText() {
        Tokeniser tokeniser = createTokeniser("<tag>text</tag>");
        tokeniser.transition(TokeniserState.Text);
        assertEquals(TokeniserState.Text, tokeniser.getState());
    }

    @Test
    public void testTokeniserStateAfterText() {
        Tokeniser tokeniser = createTokeniser("<tag>text</tag>");
        tokeniser.transition(TokeniserState.AfterText);
        assertEquals(TokeniserState.AfterText, tokeniser.getState());
    }

    @Test
    public void testTokeniserRead() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new Character("test"), token);
    }

    @Test
    public void testTokeniserReadWithTag() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<tag>test".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new StartTag("tag"), token);
    }

    @Test
    public void testTokeniserReadWithEndTag() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<tag></tag>".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new EndTag("tag"), token);
    }

    @Test
    public void testTokeniserReadWithComment() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<!--comment-->".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new Comment("comment"), token);
    }

    @Test
    public void testTokeniserReadWithDoctype() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<!DOCTYPE html>".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new Doctype("html"), token);
    }

    @Test
    public void testTokeniserReadWithSelfClosingTag() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<img src='image.jpg'/>".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        Token token = tokeniser.read();
        assertEquals(new StartTag("img", null, true), token);
    }

    @Test
    public void testTokeniserReadWithUnclosedTag() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<tag>");
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        tokeniser.error("Unexpectedly reached end of file (EOF)");
        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testTokeniserEmit() {
        Character character = new Character("test");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emit(character);
        assertEquals(character, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitStartTag() {
        StartTag startTag = new StartTag("tag");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emit(startTag);
        assertEquals(startTag, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitEndTag() {
        EndTag endTag = new EndTag("tag");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emit(endTag);
        assertEquals(endTag, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitComment() {
        Comment comment = new Comment("comment");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emit(comment);
        assertEquals(comment, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitDoctype() {
        Doctype doctype = new Doctype("html");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emit(doctype);
        assertEquals(doctype, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitCharacter() {
        Character character = new Character("test");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.emitCharacterReference(character.toString());
        assertEquals(character, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitCharacterWithAdditionalAllowedCharacter() {
        Character character = new Character("test");
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.transition(TokeniserState.BeforeAttribute);
        tokeniser.emitCharacterReference(character.toString(), ' ');
        assertNull(tokeniser.read());
    }

    @Test
    public void testTokeniserEmitCharacterWithInvalidCharacter() {
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.transition(TokeniserState.BeforeAttribute);
        tokeniser.emitCharacterReference("#", null);
        assertNull(tokeniser.read());
    }

    @Test
    public void testTokeniserEmitCharacterNamed() {
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.transition(TokeniserState.BeforeAttribute);
        tokeniser.emitCharacterReference("ampersand", null);
        assertEquals(Entities.AMPERSAND, tokeniser.read());
    }

    @Test
    public void testTokeniserEmitCharacterNamedWithAttribute() {
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.transition(TokeniserState.BeforeAttribute);
        tokeniser.emitCharacterReference("ampersand", " class");
        assertNull(tokeniser.read());
    }

    @Test
    public void testTokeniserError() {
        InputStream inputStream = new ByteArrayInputStream("</tag>".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        tokeniser.error("Unexpected character '%s' in input state [%s]", inputStream.read(), TokeniserState.AfterText);
        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testTokeniserEofError() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Tokeniser tokeniser = new Tokeniser(inputStream, new ArrayList<>());
        tokeniser.eofError(TokeniserState.BeforeScript);
        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testTokeniserCharacterReferenceError() {
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.characterReferenceError("numeric reference with no numerals");
        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testTokeniserCharacterReferenceErrorNamed() {
        Tokeniser tokeniser = new Tokeniser(null, new ArrayList<>());
        tokeniser.characterReferenceError("invalid named referenece 'abcd'");
        assertFalse(tokeniser.isEmitPending());
    }

    private Tokeniser createTokeniser(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return new Tokeniser(inputStream, new ArrayList<>());
    }
}