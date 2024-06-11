import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream("<html><head><title>Test</title></head><body></body></html>".getBytes());
        tokeniser = new Tokeniser(new CharReader(inputStream), new ParseErrorList());
        errors = tokeniser.errors;
    }

    @Test
    public void testRead_NormalCharacter() {
        Token token = tokeniser.read();
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("<", token.charPositionInLine);
        Assert.assertEquals("h", token.charPositionInElement);
        Assert.assertEquals("HTML", token.namespace);
        Assert.assertEquals("h", token.getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testRead_PendingTokenIsEmitted() {
        tokeniser.emit(new Token.Character("x"));
        tokeniser.read();
        tokeniser.read();
    }

    @Test
    public void testRead_SelfClosingTag() {
        tokeniser.read(); // <
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        tokeniser.read(); // >
        Token tag = tokeniser.read();
        Assert.assertEquals(TokenType.EndTag, tag.type);
        Assert.assertEquals("html", tag.tagName);
        Assert.assertNull(tag.getData());
        Assert.assertEquals(-1, tag.charPositionInLine);
        Assert.assertEquals(-1, tag.charPositionInElement);
        Assert.assertEquals("HTML", tag.namespace);
    }

    @Test(expected = AssertionError.class)
    public void testError_UnexpectedCharacterInState_Data() {
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('&');
        tokeniser.error(TokeniserState.Data);
    }

    @Test
    public void testError_UnexpectedEndOfFileInState_Data() {
        tokeniser.reader.mark(1);
        tokeniser.reader.consume('\n');
        tokeniser.eofError(TokeniserState.Data);
    }

    @Test
    public void testConsumeCharacterReference_ValidCharacterReference() {
        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('a', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_InvalidCharacterReference() {
        tokeniser.consumeCharacterReference('x', false);
    }

    @Test
    public void testConsumeCharacterReference_NamedCharacterReference() {
        Character character = tokeniser.consumeCharacterReference("amp", false);
        Assert.assertEquals('&', character.charValue());
    }

    @Test
    public void testConsumeCharacterReference_InvalidNamedCharacterReference() {
        tokeniser.consumeCharacterReference("invalid", false);
    }

    @Test
    public void testCreateTagPending_StartTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        Assert.assertEquals(TokenType.StartTag, tag.type);
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testCreateTagPending_EndTag() {
        Token.Tag tag = tokeniser.createTagPending(false);
        Assert.assertEquals(TokenType.EndTag, tag.type);
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testEmitTagPending() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.attributes = new ArrayList<>();
        tag.finaliseTag();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tagEmitted = tokeniser.read();
        Assert.assertEquals(TokenType.StartTag, tagEmitted.type);
        Assert.assertEquals("div", tagEmitted.tagName);
        Assert.assertNull(tagEmitted.attributes);
    }

    @Test
    public void testCreateCommentPending() {
        Token.Comment comment = tokeniser.createCommentPending();
        Assert.assertEquals(TokenType.Comment, comment.type);
        Assert.assertEquals("", comment.getData());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token commentEmitted = tokeniser.read();
        Assert.assertEquals(TokenType.Comment, commentEmitted.type);
        Assert.assertEquals("", commentEmitted.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        Token.Doctype doctype = tokeniser.createDoctypePending();
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        Assert.assertNull(doctype.publicId);
        Assert.assertNull(doctype.systemId);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctypeEmitted = tokeniser.read();
        Assert.assertEquals(TokenType.Doctype, doctypeEmitted.type);
        Assert.assertNull(doctypeEmitted.publicId);
        Assert.assertNull(doctypeEmitted.systemId);
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder data = tokeniser.dataBuffer;
        Assert.assertNotNull(data);
        Assert.assertEquals("", data.toString());
    }

    @Test
    public void testIsAppropriateEndTagToken_MatchingEndTag() {
        tokeniser.read(); // <
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        tokeniser.read(); // >
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.read(); // </
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        Token tag = tokeniser.read();
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
        Assert.assertEquals("html", tokeniser.appropriateEndTagName());
        Assert.assertEquals(tag, tokeniser.read());
    }

    @Test
    public void testIsAppropriateEndTagToken_MismatchingEndTag() {
        tokeniser.read(); // <
        tokeniser.read(); // h
        tokeniser.read(); // t
        tokeniser.read(); // m
        tokeniser.read(); // l
        tokeniser.read(); // >
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.read(); // </
        tokeniser.read(); // body
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_NoPreviousStartTag() {
        tokeniser.read(); // </
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}