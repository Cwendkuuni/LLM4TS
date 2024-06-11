package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

    @Test
    public void testRead() {
        // Test the read method

        // Test case 1: empty reader
        CharacterReader reader1 = new CharacterReader("");
        ParseErrorList errors1 = new ParseErrorList();
        Tokeniser tokeniser1 = new Tokeniser(reader1, errors1);
        Token result1 = tokeniser1.read();
        assertNull(result1);

        // Test case 2: character token
        CharacterReader reader2 = new CharacterReader("abc");
        ParseErrorList errors2 = new ParseErrorList();
        Tokeniser tokeniser2 = new Tokeniser(reader2, errors2);
        Token result2 = tokeniser2.read();
        assertTrue(result2 instanceof Token.Character);
        assertEquals("abc", ((Token.Character) result2).getData());
        assertNull(tokeniser2.read());

        // Test case 3: start tag token
        CharacterReader reader3 = new CharacterReader("<div>");
        ParseErrorList errors3 = new ParseErrorList();
        Tokeniser tokeniser3 = new Tokeniser(reader3, errors3);
        Token result3 = tokeniser3.read();
        assertTrue(result3 instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) result3).getName());
        assertNull(tokeniser3.read());

        // Test case 4: end tag token
        CharacterReader reader4 = new CharacterReader("</div>");
        ParseErrorList errors4 = new ParseErrorList();
        Tokeniser tokeniser4 = new Tokeniser(reader4, errors4);
        Token result4 = tokeniser4.read();
        assertTrue(result4 instanceof Token.EndTag);
        assertEquals("div", ((Token.EndTag) result4).getName());
        assertNull(tokeniser4.read());
    }

    @Test
    public void testEmit() {
        // Test the emit method

        // Test case 1: character token
        ParseErrorList errors1 = new ParseErrorList();
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), errors1);
        tokeniser1.emit(new Token.Character("abc"));
        Token result1 = tokeniser1.read();
        assertTrue(result1 instanceof Token.Character);
        assertEquals("abc", ((Token.Character) result1).getData());

        // Test case 2: start tag token
        ParseErrorList errors2 = new ParseErrorList();
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), errors2);
        tokeniser2.emit(new Token.StartTag("div"));
        Token result2 = tokeniser2.read();
        assertTrue(result2 instanceof Token.StartTag);
        assertEquals("div", ((Token.StartTag) result2).getName());

        // Test case 3: end tag token
        ParseErrorList errors3 = new ParseErrorList();
        Tokeniser tokeniser3 = new Tokeniser(new CharacterReader(""), errors3);
        tokeniser3.emit(new Token.EndTag("div"));
        Token result3 = tokeniser3.read();
        assertTrue(result3 instanceof Token.EndTag);
        assertEquals("div", ((Token.EndTag) result3).getName());
    }

    @Test
    public void testTransition() {
        // Test the transition method

        // Test case 1: transition to a different state
        TokeniserState state1 = TokeniserState.Data;
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.transition(state1);
        assertEquals(state1, tokeniser1.getState());

        // Test case 2: transition to the same state
        TokeniserState state2 = TokeniserState.Data;
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.transition(state2);
        assertEquals(state2, tokeniser2.getState());
    }

    @Test
    public void testAdvanceTransition() {
        // Test the advanceTransition method

        // Test case 1: advance to a different state
        TokeniserState state1 = TokeniserState.Data;
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.advanceTransition(state1);
        assertEquals(state1, tokeniser1.getState());
        assertFalse(tokeniser1.reader.isEmpty());

        // Test case 2: advance to the same state
        TokeniserState state2 = TokeniserState.Data;
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.advanceTransition(state2);
        assertEquals(state2, tokeniser2.getState());
        assertFalse(tokeniser2.reader.isEmpty());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        // Test the acknowledgeSelfClosingFlag method

        // Test case 1: selfClosingFlagAcknowledged is set to true
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser1.selfClosingFlagAcknowledged);

        // Test case 2: selfClosingFlagAcknowledged is set to false
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.selfClosingFlagAcknowledged = false;
        tokeniser2.acknowledgeSelfClosingFlag();
        assertTrue(tokeniser2.selfClosingFlagAcknowledged);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        // Test the isAppropriateEndTagToken method

        // Test case 1: tagPending and lastStartTag have the same tag name
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.tagPending = new Token.StartTag("div");
        tokeniser1.lastStartTag = new Token.StartTag("div");
        assertTrue(tokeniser1.isAppropriateEndTagToken());

        // Test case 2: tagPending and lastStartTag have different tag names
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser2.tagPending = new Token.StartTag("div");
        tokeniser2.lastStartTag = new Token.StartTag("span");
        assertFalse(tokeniser2.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        // Test the appropriateEndTagName method

        // Test case 1: lastStartTag is not null
        Tokeniser tokeniser1 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        tokeniser1.lastStartTag = new Token.StartTag("div");
        assertEquals("div", tokeniser1.appropriateEndTagName());

        // Test case 2: lastStartTag is null
        Tokeniser tokeniser2 = new Tokeniser(new CharacterReader(""), new ParseErrorList());
        assertNull(tokeniser2.appropriateEndTagName());
    }

    // Other tests for Tokeniser should include tests for methods createTagPending, emitTagPending, createCommentPending, 
    // emitCommentPending, createDoctypePending, emitDoctypePending, createTempBuffer, error, eofError, 
    // characterReferenceError, error, currentNodeInHtmlNS.
    
}