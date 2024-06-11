package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test case 1
        String input1 = "hello world";
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(input1), new ParseErrorList());
        Token expectedToken1 = new Token.Character("hello world");
        assertEquals(expectedToken1, tokeniser1.read());

        // Test case 2
        String input2 = "<p class=\"foo\">Hello, <em>world</em>!</p>";
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(input2), new ParseErrorList());
        tokeniser2.createTagPending(true);
        tokeniser2.tagPending.tagName = "p";
        Token expectedToken2 = new Token.StartTag("p", "class", "foo");
        assertEquals(expectedToken2, tokeniser2.read());

        // Test case 3
        String input3 = "<h1>Heading</h1>";
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(input3), new ParseErrorList());
        tokeniser3.createTagPending(true);
        tokeniser3.tagPending.tagName = "h1";
        Token expectedToken3 = new Token.StartTag("h1");
        assertEquals(expectedToken3, tokeniser3.read());

        // Test case 4
        String input4 = "</h1>";
        Tokeniser tokeniser4 = new Tokeniser(new CharacterReader(input4), new ParseErrorList());
        tokeniser4.createTagPending(false);
        tokeniser4.tagPending.tagName = "h1";
        Token expectedToken4 = new Token.EndTag("h1");
        assertEquals(expectedToken4, tokeniser4.read());

        // Test case 5
        String input5 = "<!DOCTYPE html>";
        Tokeniser tokeniser5 = new Tokeniser(new CharacterReader(input5), new ParseErrorList());
        tokeniser5.createDoctypePending();
        Token expectedToken5 = new Token.Doctype();
        assertEquals(expectedToken5, tokeniser5.read());

        // Test case 6
        String input6 = "<!-- Comment -->";
        Tokeniser tokeniser6 = new Tokeniser(new CharacterReader(input6), new ParseErrorList());
        tokeniser6.createCommentPending();
        Token expectedToken6 = new Token.Comment(" Comment ");
        assertEquals(expectedToken6, tokeniser6.read());

        // Test case 7
        String input7 = "&lt;";
        Tokeniser tokeniser7 = new Tokeniser(new CharacterReader(input7), new ParseErrorList());
        Token expectedToken7 = new Token.Character("<");
        assertEquals(expectedToken7, tokeniser7.read());

        // Test case 8
        String input8 = "&gt;";
        Tokeniser tokeniser8 = new Tokeniser(new CharacterReader(input8), new ParseErrorList());
        Token expectedToken8 = new Token.Character(">");
        assertEquals(expectedToken8, tokeniser8.read());

        // Test case 9
        String input9 = "&amp;";
        Tokeniser tokeniser9 = new Tokeniser(new CharacterReader(input9), new ParseErrorList());
        Token expectedToken9 = new Token.Character("&");
        assertEquals(expectedToken9, tokeniser9.read());

        // Test case 10
        String input10 = "&quot;";
        Tokeniser tokeniser10 = new Tokeniser(new CharacterReader(input10), new ParseErrorList());
        Token expectedToken10 = new Token.Character("\"");
        assertEquals(expectedToken10, tokeniser10.read());

        // Test case 11
        String input11 = "&apos;";
        Tokeniser tokeniser11 = new Tokeniser(new CharacterReader(input11), new ParseErrorList());
        Token expectedToken11 = new Token.Character("'");
        assertEquals(expectedToken11, tokeniser11.read());

        // Test case 12
        String input12 = "&nbsp;";
        Tokeniser tokeniser12 = new Tokeniser(new CharacterReader(input12), new ParseErrorList());
        Token expectedToken12 = new Token.Character("\u00A0");
        assertEquals(expectedToken12, tokeniser12.read());
    }

    @Test
    public void testEmit() {
        // Test case 1
        Token expectedToken1 = new Token.StartTag();
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.emit(expectedToken1);
        assertTrue(tokeniser1.isEmitPending);
        assertEquals(expectedToken1, tokeniser1.emitPending);

        // Test case 2
        String expectedStr2 = "hello world";
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.emit(expectedStr2);
        assertTrue(tokeniser2.isEmitPending);
        Token expectedToken2 = new Token.Character(expectedStr2);
        assertEquals(expectedToken2, tokeniser2.emitPending);

        // Test case 3
        char expectedChar3 = 'a';
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser3.emit(expectedChar3);
        assertTrue(tokeniser3.isEmitPending);
        Token expectedToken3 = new Token.Character(String.valueOf(expectedChar3));
        assertEquals(expectedToken3, tokeniser3.emitPending);
    }

    @Test
    public void testCreateTagPending() {
        // Test case 1
        boolean start1 = true;
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Tag expectedTag1 = new Token.StartTag();
        assertEquals(expectedTag1, tokeniser1.createTagPending(start1));

        // Test case 2
        boolean start2 = false;
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        Tag expectedTag2 = new Token.EndTag();
        assertEquals(expectedTag2, tokeniser2.createTagPending(start2));
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.createTagPending(true);
        tokeniser1.tagPending.tagName = "p";
        tokeniser1.lastStartTag = (Token.StartTag) tokeniser1.tagPending;
        assertTrue(tokeniser1.isAppropriateEndTagToken());

        // Test case 2
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.createTagPending(true);
        tokeniser2.tagPending.tagName = "p";
        tokeniser2.lastStartTag = new Token.StartTag();
        assertFalse(tokeniser2.isAppropriateEndTagToken());

        // Test case 3
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser3.createTagPending(true);
        tokeniser3.tagPending.tagName = "h1";
        tokeniser3.lastStartTag = new Token.StartTag();
        assertFalse(tokeniser3.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.createTagPending(true);
        tokeniser1.tagPending.tagName = "p";
        tokeniser1.lastStartTag = (Token.StartTag) tokeniser1.tagPending;
        assertEquals("p", tokeniser1.appropriateEndTagName());

        // Test case 2
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.createTagPending(true);
        tokeniser2.tagPending.tagName = "h1";
        tokeniser2.lastStartTag = (Token.StartTag) tokeniser2.tagPending;
        assertEquals("h1", tokeniser2.appropriateEndTagName());
    }

    @Test
    public void testError() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.error(TokeniserState.Data);
        assertFalse(tokeniser1.errors.canAddError());

        // Test case 2
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.error("Error");
        assertFalse(tokeniser2.errors.canAddError());
    }

    @Test
    public void testEofError() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.eofError(TokeniserState.Data);
        assertFalse(tokeniser1.errors.canAddError());
    }

    @Test
    public void testCharacterReferenceError() {
        // Test case 1
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.characterReferenceError("Error");
        assertFalse(tokeniser1.errors.canAddError());
    }

}