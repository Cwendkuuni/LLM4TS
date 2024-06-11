package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for Tokeniser class.
 */
public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @After
    public void tearDown() {
        reader = null;
        errors = null;
        tokeniser = null;
    }

    // Test read() method
    @Test
    public void testRead_DefaultState() {
        tokeniser.read();
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testRead_DataState() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.reader.append("config");
        tokeniser.read();
        assertEquals(Token.TokenType.Character, tokeniser.emitPending.type);
        assertEquals("config", tokeniser.emitPending.data);
    }

    @Test
    public void testRead_StartTagState() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.reader.append("<script");
        tokeniser.read();
        assertSame(TokeniserState.StartTag, tokeniser.getState());
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testRead_EndTagState() {
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.reader.append("</script>");
        tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tokeniser.emitPending.type);
        assertEquals("script", tokeniser.emitPending.name);
    }

    @Test
    public void testRead_DoctypeState() {
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.reader.append("<!DOCTYPE html>");
        tokeniser.read();
        assertEquals(Token.TokenType.Doctype, tokeniser.emitPending.type);
        assertSame(Entities.HTML_DOCTYPE_NAME, tokeniser.emitPending.data);
    }

    @Test
    public void testRead_CommentState() {
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.reader.append("<!-- comment -->");
        tokeniser.read();
        assertEquals(Token.TokenType.Comment, tokeniser.emitPending.type);
        assertEquals("comment", tokeniser.emitPending.data);
    }

    @Test
    public void testRead_SelfClosingTagState() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.reader.append("<img src=\"image.jpg\" />");
        tokeniser.read();
        assertEquals(Token.TokenType.StartTag, tokeniser.emitPending.type);
        assertEquals("img", tokeniser.emitPending.name);
        tokeniser.emitPending.selfClosing = true;
        tokeniser.read();
        assertEquals(Token.TokenType.EndTag, tokeniser.emitPending.type);
        assertEquals("img", tokeniser.emitPending.name);
    }

    @Test(expected = AssertionError.class)
    public void testRead_InvalidState() {
        tokeniser.transition(new TokeniserState("InvalidState"));
        tokeniser.read();
    }

    // Test emit() method
    @Test
    public void testEmit_StartTag() {
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.reader.append("<script>");
        tokeniser.emit(tokeniser.read());
        assertEquals("<script>", tokeniser.emitPending.toString());
        tokeniser.emitPending = null;
    }

    @Test
    public void testEmit_EndTag() {
        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.reader.append("</script>");
        tokeniser.emit(tokeniser.read());
        assertEquals("</script>", tokeniser.emitPending.toString());
        tokeniser.emitPending = null;
    }

    @Test
    public void testEmit_Character() {
        tokeniser.emit("config");
        assertEquals("config", tokeniser.emitPending.data);
        tokeniser.emitPending = null;
    }

    @Test
    public void testEmit_Doctype() {
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.emit(tokeniser.read());
        assertEquals("<!DOCTYPE html>", tokeniser.emitPending.toString());
        tokeniser.emitPending = null;
    }

    @Test
    public void testEmit_Comment() {
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.emit(tokeniser.read());
        assertEquals("<!-- comment -->", tokeniser.emitPending.toString());
        tokeniser.emitPending = null;
    }

    // Test consumeCharacterReference() method
    @Test
    public void testConsumeCharacterReference_Numeric() {
        tokeniser.reader.append("#x{1F600}");
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertEquals(1, codepoints.length);
        assertEquals(0x1F600, codepoints[0]);
        tokeniser.reader.rewindToMark();
    }

    @Test
    public void testConsumeCharacterReference_Named() {
        tokeniser.reader.append("#alt;foo");
        int[] codepoints = tokeniser.consumeCharacterReference('a', false);
        assertEquals(1, codepoints.length);
        assertEquals('α', codepoints[0]);
        tokeniser.reader.rewindToMark();
    }

    @Test(expected = AssertionError.class)
    public void testConsumeCharacterReference_Invalid() {
        tokeniser.reader.append("#foo");
        tokeniser.consumeCharacterReference(null, false);
    }

    @Test
    public void testConsumeCharacterReference_InAttribute() {
        tokeniser.reader.append(" class=\"foo&bar;\"");
        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        assertEquals(2, codepoints.length);
        assertEquals(0x26, codepoints[0]);
        assertEquals(0x61, codepoints[1]);
        tokeniser.reader.rewindToMark();
    }

    // Test error() method
    @Test
    public void testError_UnexpectedCharacter() {
        tokeniser.reader.append("&");
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Unexpected character '&' in input state [DATA]", errors.get(0).toString());
        tokeniser.reader.rewindToMark();
    }

    @Test
    public void testError_UnexpectedEndOfFile() {
        tokeniser.reader.append("</script>");
        tokeniser.reader.consumeTo('>');
        tokeniser.error(TokeniserState.StartTag);
        assertEquals(1, errors.size());
        assertEquals("Unexpectedly reached end of file (EOF) in input state [START_TAG]", errors.get(0).toString());
        tokeniser.reader.rewindToMark();
    }

    @Test
    public void testError_InvalidCharacterReference() {
        tokeniser.reader.append("#foo");
        tokeniser.error(TokeniserState.Data);
        assertEquals(1, errors.size());
        assertEquals("Invalid character reference: invalid named reference 'foo'", errors.get(0).toString());
        tokeniser.reader.rewindToMark();
    }
}