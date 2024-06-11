import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniserEmptyInput() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[0]));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNull(token);
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserSingleCharacter() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("a".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", token.data());
        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserMultipleCharacters() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("abc".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token token1 = tokeniser.read();
        Assert.assertNotNull(token1);
        Assert.assertEquals(Token.TokenType.Character, token1.type);
        Assert.assertEquals("a", token1.data());

        Token token2 = tokeniser.read();
        Assert.assertNotNull(token2);
        Assert.assertEquals(Token.TokenType.Character, token2.type);
        Assert.assertEquals("b", token2.data());

        Token token3 = tokeniser.read();
        Assert.assertNotNull(token3);
        Assert.assertEquals(Token.TokenType.Character, token3.type);
        Assert.assertEquals("c", token3.data());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserSelfClosingTag() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<img src=\"test.jpg\" />".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.createTagPending(true);
        startTag.tagName = "img";
        startTag.selfClosing = true;
        tokeniser.emitTagPending();

        Token endTag = tokeniser.read();
        Assert.assertNotNull(endTag);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals("img", endTag.tagName);
        Assert.assertNull(endTag.data());

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserStartTagWithAttributes() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<div id='test' class='test-class'></div>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token startTag = tokeniser.read();
        Assert.assertNotNull(startTag);
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals("div", startTag.tagName);

        Token.Attribute attribute = startTag.attributes.get(0);
        Assert.assertEquals("id", attribute.name());
        Assert.assertEquals("test", attribute.value());

        Token.Attribute attribute2 = startTag.attributes.get(1);
        Assert.assertEquals("class", attribute2.name());
        Assert.assertEquals("test-class", attribute2.value());

        tokeniser.emit(startTag);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserEndTagMismatch() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<div><p>Hello World</p></head>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        Token tag = tokeniser.read();
        Assert.assertNotNull(tag);
        Assert.assertEquals(Token.TokenType.StartTag, tag.type);
        Assert.assertEquals("div", tag.tagName);

        tokeniser.emit(tag);

        tag = tokeniser.read();
        Assert.assertNotNull(tag);
        Assert.assertEquals(Token.TokenType.StartTag, tag.type);
        Assert.assertEquals("p", tag.tagName);

        tokeniser.emit(tag);

        tag = tokeniser.read();
        Assert.assertNotNull(tag);
        Assert.assertEquals(Token.TokenType.EndTag, tag.type);
        Assert.assertEquals("head", tag.tagName); // wrong tag name

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character 'h' in input state [HEAD]", error.getMessage());
    }

    @Test
    public void testTokeniserCharacterReference() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&apos; &nbsp; &eacute; &frac12; &#x1F44D;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{'\u00A9', '\u00A0', '\u00E9', '\u00BD', 0x1F44D}, codepoints);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserCharacterReferenceInvalid() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("&invalid;".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        Assert.assertArrayEquals(new int[]{replacementChar}, codepoints);

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Invalid character reference: invalid", error.getMessage());
    }

    @Test
    public void testTokeniserCharacterReferenceInAttribute() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<div id='&eacute;'></div>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        Assert.assertArrayEquals(new int[]{'\u00E9'}, codepoints);

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserInvalidInput() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<!DOCTYPE HTML>invalid".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // consume DOCTYPE

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character 'i' in input state [DOCTYPE]", error.getMessage());
    }

    @Test
    public void testTokeniserEOFError() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // consume XML declaration

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Misc);

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
    }

    @Test
    public void testTokeniserHandlesEmptyInput() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[0]));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.read(); // consume EOF

        Assert.assertEquals(0, errors.size());
    }

    @Test
    public void testTokeniserHandlesEmptyInputWithEOFError() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[0]));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Misc);
        tokeniser.acknowledgeSelfClosingFlag();

        Assert.assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
    }

    @Test
    public void testTokeniserHandlesEmptyInputWithEOFErrorAndConsumedChar() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(new byte[]{'<'}));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        tokeniser.transition(TokeniserState.Misc);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.consume();

        Assert.assertEquals(0, errors.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testTokeniserThrowsExceptionWhenReadingWithoutInitializing() throws IOException {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(null, errors); // null reader

        tokeniser.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTokeniserThrowsExceptionWhenInitializingWithNullErrors() {
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
        ParseErrorList errors = null;
        Tokeniser tokeniser = new Tokeniser(reader, errors);
    }
}