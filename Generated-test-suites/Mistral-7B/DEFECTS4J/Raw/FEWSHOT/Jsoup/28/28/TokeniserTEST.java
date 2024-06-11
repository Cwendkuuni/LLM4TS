package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Tokenizer.TokeniserState;
import org.jsoup.parser.CharacterReference;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTokeniserConstruction() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertEquals(reader, tokeniser.reader);
        assertEquals(errors, tokeniser.errors);
    }

    @Test
    public void testReadEmptyInput() {
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadWhiteSpaceOnlyInput() {
        CharacterReader reader = createReaderWithString(" \t\r\n");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(TokenType.Whitespace, token.type);
        assertEquals("", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Whitespace, token.type);
        assertEquals("\t", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Whitespace, token.type);
        assertEquals("\r", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Whitespace, token.type);
        assertEquals("\n", token.text);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterInput() {
        CharacterReader reader = createReaderWithString("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("a", token.text);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceInput() {
        CharacterReader reader = createReaderWithString("&amp;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("&", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("a", token.text);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadNamedCharacterReferenceInput() {
        CharacterReader reader = createReaderWithString("&copy;");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("&copy;", token.text);

        token = tokeniser.read();
        assertEquals(Character.COPYRIGHT, token.charValue);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadCharacterReferenceWithExtraCharsInput() {
        CharacterReader reader = createReaderWithString("&amp;foo");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("&", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Character, token.type);
        assertEquals("a", token.text);

        token = tokeniser.read();
        assertEquals(TokenType.Text, token.type);
        assertEquals("foo", token.text);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test(expected = IOException.class)
    public void testReaderThrowsIOException() throws IOException {
        CharacterReader reader = mock(CharacterReader.class);
        when(reader.current()).thenThrow(new IOException("Mock IoException"));
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read();
    }

    @Test
    public void testReadInputWithSelfClosingTag() {
        CharacterReader reader = createReaderWithString("<img src=\"foo.jpg\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("img", tagOpen.tagName);
        assertEquals(true, tagOpen.selfClosing);

        Token tagClose = tokeniser.read();
        assertEquals(TokenType.EndTag, tagClose.type);
        assertEquals("img", tagClose.tagName);

        Token image = tokeniser.read();
        assertEquals(TokenType.Text, image.type);
        assertEquals("foo.jpg", image.text);

        tokeniser.read(); // consume '>'

        Token text = tokeniser.read();
        assertEquals(TokenType.Text, text.type);
        assertEquals("", text.text);

        tokeniser.read(); // consume '/'

        tokeniser.acknowledgeSelfClosingFlag();

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithSelfClosingTagAndText() {
        CharacterReader reader = createReaderWithString("<img src=\"foo.jpg\" alt=\"Bar\" />");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("img", tagOpen.tagName);
        assertEquals(true, tagOpen.selfClosing);

        Attribute attribute = tokeniser.read();
        assertEquals(TokenType.Attribute, attribute.type);
        assertEquals("alt", attribute.name);
        assertEquals("Bar", attribute.value);

        tokeniser.read(); // consume '='

        tokeniser.read(); // consume '>'

        tokeniser.acknowledgeSelfClosingFlag();

        Token text = tokeniser.read();
        assertEquals(TokenType.Text, text.type);
        assertEquals("", text.text);

        tokeniser.read(); // consume '/'

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithEndTag() {
        CharacterReader reader = createReaderWithString("</body>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.EndTag, tagOpen.type);
        assertEquals("body", tagOpen.tagName);

        tokeniser.acknowledgeSelfClosingFlag();

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithStartTagAndEndTag() {
        CharacterReader reader = createReaderWithString("<p><em>This is an example</em></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("p", tagOpen.tagName);

        tokeniser.read(); // consume '<'

        tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("em", tagOpen.tagName);

        Attributes attributes = tokeniser.readAttributes();
        List<Attribute> attributeList = attributes.asList();
        assertEquals(1, attributeList.size());
        Attribute attribute = attributeList.get(0);
        assertEquals("", attribute.name);
        assertEquals("", attribute.value);

        tokeniser.read(); // consume '>'

        Token text = tokeniser.read();
        assertEquals(TokenType.Text, text.type);
        assertEquals("This is an example", text.text);

        tagOpen = tokeniser.read();
        assertEquals(TokenType.EndTag, tagOpen.type);
        assertEquals("em", tagOpen.tagName);

        tokeniser.read(); // consume '<'

        tagOpen = tokeniser.read();
        assertEquals(TokenType.EndTag, tagOpen.type);
        assertEquals("p", tagOpen.tagName);

        tokeniser.acknowledgeSelfClosingFlag();

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithStartTagAndEndTagWithAttributes() {
        CharacterReader reader = createReaderWithString("<a href=\"http://www.example.com\" target=\"_blank\">Link</a>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("a", tagOpen.tagName);

        Attributes attributes = tokeniser.readAttributes();
        List<Attribute> attributeList = attributes.asList();
        assertEquals(2, attributeList.size());
        Attribute hrefAttribute = attributeList.get(0);
        assertEquals("href", hrefAttribute.name);
        assertEquals("http://www.example.com", hrefAttribute.value);

        Attribute targetAttribute = attributeList.get(1);
        assertEquals("target", targetAttribute.name);
        assertEquals("_blank", targetAttribute.value);

        tokeniser.read(); // consume '>'

        Token text = tokeniser.read();
        assertEquals(TokenType.Text, text.type);
        assertEquals("Link", text.text);

        tagOpen = tokeniser.read();
        assertEquals(TokenType.EndTag, tagOpen.type);
        assertEquals("a", tagOpen.tagName);

        tokeniser.acknowledgeSelfClosingFlag();

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithScriptTag() {
        CharacterReader reader = createReaderWithString("<script src=\"foo.js\"></script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token tagOpen = tokeniser.read();
        assertEquals(TokenType.StartTag, tagOpen.type);
        assertEquals("script", tagOpen.tagName);

        Attributes attributes = tokeniser.readAttributes();
        List<Attribute> attributeList = attributes.asList();
        assertEquals(1, attributeList.size());
        Attribute srcAttribute = attributeList.get(0);
        assertEquals("src", srcAttribute.name);
        assertEquals("foo.js", srcAttribute.value);

        tokeniser.read(); // consume '>'

        tokeniser.createTempBuffer();

        Token scriptText = tokeniser.read();
        assertEquals(TokenType.Text, scriptText.type);
        assertNull(scriptText.text); // script text is stored in dataBuffer

        tokeniser.emitCommentPending();

        Token comment = tokeniser.read();
        assertEquals(TokenType.Comment, comment.type);
        assertNull(comment.text);

        tokeniser.read(); // consume '-->'

        tokeniser.emitDoctypePending();

        Doctype doctype = tokeniser.read();
        assertEquals(Doctype.emptyDoctype(), doctype);

        tokeniser.createTempBuffer();

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadInputWithError() {
        CharacterReader reader = createReaderWithString("<script src=\"foo.js");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read(); // consume '<'
        tokeniser.read(); // consume 's'
        tokeniser.read(); // consume 'c'
        tokeniser.read(); // consume 'r'
        tokeniser.read(); // consume 'i'
        tokeniser.read(); // consume 'p'
        tokeniser.read(); // consume ' '
        tokeniser.error(TokeniserState.AfterScriptStartTag);

        token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpected character 's' in input state [AfterScriptStartTag]", error.getMessage());
    }

    @Test
    public void testReadInputWithUnexpectedEndOfFile() {
        CharacterReader reader = createReaderWithString("<script src=\"foo.js");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.read(); // consume '<'
        tokeniser.read(); // consume 's'
        tokeniser.read(); // consume 'c'
        tokeniser.read(); // consume 'r'
        tokeniser.read(); // consume 'i'
        tokeniser.read(); // consume 'p'
        tokeniser.read(); // consume ' '

        tokeniser.eofError(TokeniserState.AfterScriptStartTag);

        token = tokeniser.read();
        assertNull(token);

        assertEquals(1, errors.size());
        ParseError error = errors.get(0);
        assertEquals("Unexpectedly reached end of file (EOF) in input state [AfterScriptStartTag]", error.getMessage());
    }

    private CharacterReader createReaderWithString(String input) {
        ByteArrayReader reader = new ByteArrayReader(input.getBytes());
        when(reader.current()).then((ignored) -> (char) input.charAt(0));
        when(reader.markSupported()).thenReturn(true);
        when(reader.mark(1)).thenReturn(reader);
        when(reader.mark(input.length()+1)).thenReturn(reader);
        when(reader.matchConsume(String.format("%[%s]", input))).thenReturn(input.length() == input.length());
        when(reader.matchConsumeIgnoreCase(String.format("%[%s]", input))).thenReturn(input.length() == input.length());
        when(reader.match(String.format("%[%s]", input))).thenReturn(input.equals(input.substring(0, reader.pos())));
        when(reader.matches(String.format("%[%s]", input))).thenReturn(input.matches(input.substring(0, reader.pos())));
        when(reader.matchesAny(String.valueOf(input.charAt(0)).toCharArray())).thenReturn(input.charAt(0) == reader.current());
        when(reader.matchesLetter()).thenReturn(Character.isLetter(reader.current()));
        when(reader.matchesDigit()).thenReturn(Character.isDigit(reader.current()));
        when(reader.consumeCharacterReference(null, false)).thenCallRealMethod();
        when(reader.consumeCharacterReference(Character.class, false)).thenCallRealMethod();
        when(reader.consumeCharacterReference(Character.class, true)).thenCallRealMethod();
        when(reader.consumeDigitSequence()).thenReturn(input.substring(reader.pos(), reader.pos()+Integer.parseInt(input.substring(reader.pos()))+1));
        when(reader.consumeLetterThenDigitSequence()).thenReturn(input.substring(reader.pos()));
        return reader;
    }
}