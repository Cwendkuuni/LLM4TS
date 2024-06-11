package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("");
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testEmit() {
        Token.Character characterToken = new Token.Character("test");
        tokeniser.emit(characterToken);
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(characterToken, emittedToken);
    }

    @Test
    public void testEmitString() {
        String str = "test";
        tokeniser.emit(str);
        Token.Character emittedToken = (Token.Character) tokeniser.read();
        Assert.assertEquals(str, emittedToken.getData());
    }

    @Test
    public void testEmitChar() {
        char c = 't';
        tokeniser.emit(c);
        Token.Character emittedToken = (Token.Character) tokeniser.read();
        Assert.assertEquals(Character.toString(c), emittedToken.getData());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser.Token.Tag startTag = tokeniser.createTagPending(true);
        startTag.tagName = "test";
        tokeniser.emitTagPending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(startTag, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser.Token.Comment commentToken = tokeniser.commentPending;
        tokeniser.emitCommentPending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(commentToken, emittedToken);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser.Token.Doctype doctypeToken = tokeniser.doctypePending;
        tokeniser.emitDoctypePending();
        Token emittedToken = tokeniser.read();
        Assert.assertEquals(doctypeToken, emittedToken);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.tagPending.tagName = "test";
        tokeniser.lastStartTag = tokeniser.tagPending;
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsTrackErrors() {
        Assert.assertTrue(tokeniser.isTrackErrors());
    }

    @Test
    public void testSetTrackErrors() {
        tokeniser.setTrackErrors(false);
        Assert.assertFalse(tokeniser.isTrackErrors());
    }

    @Test
    public void testErrorState() {
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.Data;
        tokeniser.error(state);
        List<Tokeniser.ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character in input", errors.get(0).getErrorMsg());
        Assert.assertEquals(state, errors.get(0).getState());
    }

    @Test
    public void testEofErrorState() {
        Tokeniser.TokeniserState state = Tokeniser.TokeniserState.Data;
        tokeniser.eofError(state);
        List<Tokeniser.ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getErrorMsg());
        Assert.assertEquals(state, errors.get(0).getState());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError();
        List<Tokeniser.ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference", errors.get(0).getErrorMsg());
    }

    @Test
    public void testError() {
        String errorMsg = "Test error";
        tokeniser.error(errorMsg);
        List<Tokeniser.ParseError> errors = tokeniser.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(errorMsg, errors.get(0).getErrorMsg());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
    }

    private static class Token {
        TokenType type;

        enum TokenType {
            Character,
            StartTag,
            EndTag,
            Comment,
            Doctype
        }

        static class Character extends Token {
            private String data;

            public Character(String data) {
                this.data = data;
                type = TokenType.Character;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Character character = (Character) obj;
                return data.equals(character.data);
            }

            @Override
            public int hashCode() {
                return data.hashCode();
            }
        }

        static class Tag extends Token {
            String tagName;
            boolean selfClosing;

            public Tag(String tagName, boolean selfClosing) {
                this.tagName = tagName;
                this.selfClosing = selfClosing;
            }

            public void finaliseTag() {
                // Finalize the tag
            }
        }

        static class StartTag extends Tag {
            public StartTag() {
                super("", false);
            }
        }

        static class EndTag extends Tag {
            public EndTag() {
                super("", false);
            }
        }

        static class Comment extends Token {
            // Comment implementation
        }

        static class Doctype extends Token {
            // Doctype implementation
        }
    }

    private static class CharacterReader {
        private String input;
        private int pos;

        public CharacterReader(String input) {
            this.input = input;
            this.pos = 0;
        }

        public boolean isEmpty() {
            return input == null || input.isEmpty();
        }

        public char current() {
            return input.charAt(pos);
        }

        public String consumeHexSequence() {
            return input.substring(pos);
        }

        public String consumeDigitSequence() {
            return input.substring(pos);
        }

        public boolean matchConsume(String str) {
            if (input.substring(pos).startsWith(str)) {
                for (int i = 0; i < str.length(); i++) {
                    pos++;
                }
                return true;
            }
            return false;
        }

        public boolean matchConsumeIgnoreCase(String str) {
            return matchConsume(str);
        }

        public void mark() {
            // Store current position
        }

        public String consumeLetterSequence() {
            return input.substring(pos);
        }

        public boolean matches(char c) {
            return input.charAt(pos) == c;
        }

        public String consume(int length) {
            String consumed = input.substring(pos, pos + length);
            pos += length;
            return consumed;
        }

        public boolean matchesLetter() {
            return true;
        }

        public boolean matchesDigit() {
            return true;
        }

        public boolean matches(char... chars) {
            return true;
        }

        public boolean matchesLetter() {
            return true;
        }

        public boolean matchesDigit() {
            return true;
        }

        public void unconsume() {
            pos--;
        }

        public boolean matchConsume(char c) {
            if (input.charAt(pos) == c) {
                pos++;
                return true;
            }
            return false;
        }

        public boolean matchConsume(String str) {
            return true;
        }

        public boolean matchConsumeIgnoreCase(String str) {
            return true;
        }

        public void rewindToMark() {
            // Rewind to marked position
        }
    }

    private static class Validate {
        public static void isFalse(boolean expression, String errorMessage) {
            if (expression) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }

    private static class Entities {
        public static boolean isNamedEntity(String name) {
            return false;
        }

        public static Character getCharacterByName(String name) {
            return null;
        }
    }

    private static class ParseError {
        private String errorMsg;
        private Tokeniser.TokeniserState state;
        private int pos;

        public ParseError(String errorMsg, int pos) {
            this.errorMsg = errorMsg;
            this.pos = pos;
        }

        public ParseError(String errorMsg, Tokeniser.TokeniserState state, int pos) {
            this.errorMsg = errorMsg;
            this.state = state;
            this.pos = pos;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public Tokeniser.TokeniserState getState() {
            return state;
        }

        public int getPos() {
            return pos;
        }
    }
}