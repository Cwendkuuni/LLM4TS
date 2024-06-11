package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

    @Test
    public void testCharacterReference() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&#x1F600;".getBytes()))) {
            @Override
            public void characterReferenceError() {
                // do nothing
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\uD834\uDD1E", token.getText());
        assertEquals(CharacterReference.REFERENCE, token.referencedCharacter);
    }

    @Test
    public void testCharacterReferenceWithAdditionalAllowedCharacter() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&#x1F600;&#xA0;".getBytes()))) {
            @Override
            public Character consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
                assertEquals('\u00A0', additionalAllowedCharacter);
                return super.consumeCharacterReference(null, false);
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\uD834\uDD1E", token.getText());
        assertEquals(CharacterReference.REFERENCE, token.referencedCharacter);
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReferenceMissingSemicolon() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&#x1F600".getBytes()))) {
            @Override
            public void characterReferenceError() {
                // do nothing
            }
        };
        tokeniser.read();
    }

    @Test
    public void testNamedCharacterReference() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&copy;".getBytes()))) {
            @Override
            public void characterReferenceError() {
                // do nothing
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("\u00A9", token.getText());
        assertNull(token.referencedCharacter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNamedCharacterReferenceMissingSemicolon() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("&copy;".getBytes()))) {
            @Override
            public void characterReferenceError() {
                // do nothing
            }
        };
        tokeniser.read();
        tokeniser.read(); // consume ';'
    }

    @Test
    public void testSelfClosingFlagAcknowledgement() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<br />".getBytes()))) {
            @Override
            public void acknowledgeSelfClosingFlag() {
                // do nothing
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("br", token.tagName);
        assertTrue(token.selfClosing);
    }

    @Test
    public void testStateTransitions() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<!--comment-->".getBytes()))) {
            @Override
            public TokeniserState getState() {
                return TokeniserState.Comment;
            }

            @Override
            public void advanceTransition(TokeniserState state) {
                super.advanceTransition(state);
                assertTrue(reader.current() == '-');
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.type);
        assertEquals(Comment.TYPE_COMMENT, token.comment.type);
        assertEquals("comment", token.comment.data);
        assertEquals(0, token.comment.originalSourceStart);
        assertEquals(6, token.comment.originalSourceEnd);
    }

    @Test
    public void testTagCreation() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<p class=foo>Hello World</p>".getBytes()))) {
            @Override
            public Token.Tag createTagPending(boolean start) {
                Token.Tag tag = super.createTagPending(start);
                if (start) {
                    tag.attributes.add(new Attribute("class", "foo"));
                }
                return tag;
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("p", token.tagName);
        assertEquals(1, token.attributes.size());
        assertEquals("class", token.attributes.get(0).name);
        assertEquals("foo", token.attributes.get(0).value);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("H", token.getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("e", token.getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("l", token.getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("o", token.getText());

        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.type);
        assertEquals("p", token.tagName);
    }

    @Test
    public void testTransitionOnEmptyLine() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("\r\n".getBytes()))) {
            @Override
            public TokeniserState getState() {
                return TokeniserState.Data;
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testDoctypeCreation() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" " +
                "\"http://www.w3.org/TR/html4/loose.dtd\">".getBytes()))) {
            @Override
            public void createDoctypePending() {
                doctypePending.publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
                doctypePending.systemId = "http://www.w3.org/TR/html4/loose.dtd";
            }

            @Override
            public void emitDoctypePending() {
                emit(doctypePending);
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.type);
        assertEquals(Doctype.TYPE_DOCTYPE_DECL, token.doctype.type);
        assertEquals("html", token.doctype.name);
        assertEquals(doctypePending.publicId, token.doctype.publicId);
        assertEquals(doctypePending.systemId, token.doctype.systemId);
    }

    @Test
    public void testErrorHandling() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<script>throw new Error(\"test\");</script>".getBytes()))) {
            @Override
            public void error(String errorMsg) {
                throw new AssertionError(errorMsg);
            }
        };
        try {
            tokeniser.read();
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testErrorHandlingWithTrackingErrorsOff() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<script>throw new Error(\"test\");</script>".getBytes()))) {
            @Override
            public void setTrackErrors(boolean trackErrors) {
                // do nothing
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.type);
        assertEquals("script", token.tagName);
    }

    @Test
    public void testErrorHandlingWithTrackingErrorsOn() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<script>throw new Error(\"test\");</script>".getBytes()))) {
            @Override
            public void setTrackErrors(boolean trackErrors) {
                this.trackErrors = trackErrors;
            }

            @Override
            public void error(String errorMsg) {
                assertEquals("test", errorMsg);
                fail();
            }
        };
        try {
            tokeniser.read();
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testErrorHandlingWithTrackingErrorsOnAndErrorsCollected() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("<script>throw new Error(\"test\");</script>".getBytes()))) {
            @Override
            public void setTrackErrors(boolean trackErrors) {
                this.trackErrors = trackErrors;
            }

            @Override
            public void error(ParseError error) {
                List<ParseError> errors = this.errors;
                assertEquals(1, errors.size());
                assertEquals("test", errors.get(0).getMessage());
                assertEquals(reader.pos(), errors.get(0).getSourcePosition());
            }
        };
        try {
            tokeniser.read();
            fail();
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testEOFErrorHandling() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes()))) {
            @Override
            public void eofError(TokeniserState state) {
                throw new AssertionError(" Unexpectedly reached end of file (EOF) at " + reader.pos());
            }
        };
        try {
            tokeniser.read();
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testEOFErrorHandlingWithTrackingErrorsOff() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes()))) {
            @Override
            public void setTrackErrors(boolean trackErrors) {
                // do nothing
            }

            @Override
            public void eofError(TokeniserState state) {
                // do nothing
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Eof, token.type);
    }

    @Test
    public void testEOFErrorHandlingWithTrackingErrorsOn() throws IOException {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes()))) {
            @Override
            public void setTrackErrors(boolean trackErrors) {
                this.trackErrors = trackErrors;
            }

            @Override
            public void eofError(TokeniserState state) {
                List<ParseError> errors = this.errors;
                assertEquals(1, errors.size());
                assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
                assertEquals(0, errors.get(0).getSourcePosition().line);
                assertEquals(0, errors.get(0).getSourcePosition().column);
            }
        };
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Eof, token.type);
    }
}