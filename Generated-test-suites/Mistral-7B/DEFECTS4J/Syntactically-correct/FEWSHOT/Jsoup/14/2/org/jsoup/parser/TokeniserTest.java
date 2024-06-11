package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader mockReader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        mockReader = new CharacterReader("") {
            @Override
            public int read() throws IOException {
                return -1;
            }

            @Override
            public char current() {
                return '\0';
            }

            @Override
            public void mark(int readAheadLimit) {
                // no-op
            }

            @Override
            public void unmark(int readAheadLimit) {
                // no-op
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean matches(char expected) {
                return false;
            }

            @Override
            public boolean matchesIgnoreCase(char expected) {
                return false;
            }

            @Override
            public boolean matchesAny(char... expected) {
                return false;
            }

            @Override
            public boolean matchesLetter() {
                return false;
            }

            @Override
            public boolean matchesDigit() {
                return false;
            }

            @Override
            public boolean consumeLetterSequence() {
                return "";
            }

            @Override
            public boolean consumeDigitSequence() {
                return "";
            }

            @Override
            public boolean consumeHexSequence() {
                return "";
            }
        };

        tokeniser = new Tokeniser(mockReader);
    }

    @Test
    public void testTransition() {
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testReadNullChar() {
        mockReader.current = '\0';
        tokeniser.read();
        assertNull(tokeniser.emitPending);
    }

    @Test
    public void testReadCharacter() {
        mockReader.current = 'a';
        tokeniser.read();
        assertEquals(new Token.Character("a"), tokeniser.emitPending);
    }

    @Test
    public void testReadStartTag() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('/');
        mockReader.matchConsume('d');
        mockReader.matchConsume('i');
        mockReader.matchConsume('v');
        tokeniser.read();
        Token.StartTag startTag = (Token.StartTag) tokeniser.emitPending;
        tokeniser.acknowledgeSelfClosingFlag();
        assertEquals("<div", startTag.tagName);
        assertEquals(Token.StartTag.TAG_OPEN, startTag.tagType);
    }

    @Test
    public void testReadEndTag() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('/');
        mockReader.matchConsume('/');
        mockReader.matchConsume('d');
        mockReader.matchConsume('i');
        tokeniser.read();
        Token.EndTag endTag = (Token.EndTag) tokeniser.emitPending;
        assertEquals("/div", endTag.tagName);
        assertEquals(Token.EndTag.TAG_CLOSE, endTag.tagType);
    }

    @Test
    public void testReadEmptyElement() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('/');
        mockReader.matchConsume('/');
        mockReader.matchConsume('i');
        tokeniser.read();
        Token.EmptyElement emptyElement = (Token.EmptyElement) tokeniser.emitPending;
        assertEquals("/i", emptyElement.tagName);
        assertEquals(Token.EmptyElement.EMPTY_TAG_OPEN, emptyElement.tagType);
    }

    @Test
    public void testReadComment() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('!');
        mockReader.matchConsume('c');
        mockReader.matchConsume('d');
        mockReader.consumeLetterSequence();
        tokeniser.read();
        Token.Comment comment = (Token.Comment) tokeniser.emitPending;
        assertEquals("<!--", comment.tagName);
        assertEquals(Token.Comment.COMMENT, comment.tagType);
    }

    @Test
    public void testReadScript() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('s');
        mockReader.matchConsume('c');
        mockReader.matchConsume('r');
        mockReader.matchConsume('i');
        mockReader.matchConsume('p');
        tokeniser.createTempBuffer();
        tokeniser.read();
        assertEquals(new Token.StartTag(Token.Tag.SCRIPT, null), tokeniser.emitPending);
        tokeniser.emit(tokeniser.emitPending);
        tokeniser.createTempBuffer();
        tokeniser.read();
        assertEquals(Token.Tag.SCRIPT, tokeniser.getState());
        assertEquals('\0', tokeniser.reader.current());
    }

    @Test
    public void testReadError() {
        tokeniser.error(TokeniserState.Script);
        assertTrue(tokeniser.isTrackErrors());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testReadEofError() {
        mockReader.mark(1);
        tokeniser.eofError(TokeniserState.Script);
        assertTrue(tokeniser.isTrackErrors());
        assertEquals(1, tokeniser.errors.size());
    }

    @Test
    public void testReadCharacterReference() {
        mockReader.matchConsume('&');
        mockReader.consumeLetterSequence();
        mockReader.matchConsume(';');
        tokeniser.read();
        assertEquals(new Token.CharacterReference("amp"), tokeniser.emitPending);
    }

    @Test
    public void testReadNamedCharacterReference() {
        mockReader.matchConsume('&');
        mockReader.consumeLetterSequence();
        mockReader.matchConsume('A');
        mockReader.matchConsume('m');
        mockReader.matchConsume('p');
        mockReader.matchConsume(';');
        tokeniser.read();
        assertEquals(Entities.getCharacterByName("amp"), tokeniser.emitPending);
    }

    @Test
    public void testReadAttributeValueQuoted() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('a');
        mockReader.matchConsume(' ');
        mockReader.matchConsume('t');
        mockReader.matchConsume('t');
        mockReader.matchConsume('r');
        mockReader.matchConsume('i');
        mockReader.matchConsume('b');
        mockReader.matchConsume('=');
        mockReader.matchConsume('"');
        mockReader.consumeLetterSequence();
        mockReader.matchConsume('"');
        mockReader.matchConsume('>');
        tokeniser.read();
        Token.Attribute attribute = (Token.Attribute) tokeniser.emitPending;
        assertEquals("attrib", attribute.name);
        assertEquals("value", attribute.value);
    }

    @Test
    public void testReadAttributeValueUnquoted() {
        mockReader.matchConsume('<');
        mockReader.matchConsume('a');
        mockReader.matchConsume(' ');
        mockReader.matchConsume('t');
        mockReader.matchConsume('t');
        mockReader.matchConsume('r');
        mockReader.matchConsume('i');
        mockReader.matchConsume('b');
        mockReader.matchConsume('=');
        mockReader.consumeLetterSequence();
        mockReader.matchConsume(' ');
        tokeniser.read();
        Token.Attribute attribute = (Token.Attribute) tokeniser.emitPending;
        assertEquals("attrib", attribute.name);
        assertEquals("value", attribute.value);
    }
}