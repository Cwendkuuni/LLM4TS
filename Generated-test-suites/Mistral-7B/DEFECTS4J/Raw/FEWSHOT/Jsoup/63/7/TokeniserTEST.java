package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.jsoup.nodes.Entities;

public class TokeniserTest {

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        Token.StartTag startTag = tokeniser.createTagPending(true);
        Token.EndTag endTag = tokeniser.createTagPending(false);
        assertEquals(Token.TagType.StartTag, startTag.getType());
        assertEquals(Token.TagType.EndTag, endTag.getType());
        assertNull(startTag.getEndTag());
        assertNull(endTag.getStartTag());
    }

    @Test
    public void testEmitTagPending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        Token.StartTag startTag = tokeniser.createTagPending(true);
        Token.EndTag endTag = tokeniser.createTagPending(false);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertSame(startTag, token);
        tokeniser.emitTagPending();
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertSame(endTag, token);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.createCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.getType());
        assertNull(token.getComment().getBody());
    }

    @Test
    public void testEmitCommentPending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.getType());
        assertNull(token.getComment().getBody());
        String body = "Test Comment";
        tokeniser.emit(body);
        token = tokeniser.read();
        assertEquals(Token.TokenType.Comment, token.getType());
        assertEquals(body, token.getComment().getBody());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.createDoctypePending();
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.getType());
        assertNull(token.getDoctype().getPublicId());
        assertNull(token.getDoctype().getSystemId());
        assertNull(token.getDoctype().getName());
    }

    @Test
    public void testEmitDoctypePending() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.createDoctypePending();
        String publicId = "Test Public ID";
        String systemId = "Test System ID";
        String name = "Test Name";
        tokeniser.emitDoctypePending();
        tokeniser.emit(publicId);
        tokeniser.emit(systemId);
        tokeniser.emit(name);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Doctype, token.getType());
        assertEquals(publicId, token.getDoctype().getPublicId());
        assertEquals(systemId, token.getDoctype().getSystemId());
        assertEquals(name, token.getDoctype().getName());
    }

    @Test
    public void testReadEmpty() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Eof, token.getType());
    }

    @Test
    public void testReadSingleCharacter() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.emit("a");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("a", token.getCharacter().getData());
    }

    @Test
    public void testReadMultipleCharacters() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.emit("ab");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("a", token.getCharacter().getData());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("b", token.getCharacter().getData());
    }

    @Test
    public void testReadWhitespace() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.emit(" \t\n ");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Whitespace, token.getType());
        assertEquals(" ", token.getWhitespace().getData());
    }

    @Test
    public void testReadStartTag() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        String tagName = "TestTag";
        tokeniser.emit("<" + tagName);
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals(tagName, token.getStartTag().getName());
        assertNull(token.getStartTag().getAttributes());
    }

    @Test
    public void testReadStartTagWithAttributes() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        String tagName = "TestTag";
        tokeniser.emit("<" + tagName + " id='test' class='testClass'");
        tokeniser.emit("/>"); // self-closing
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals(tagName, token.getStartTag().getName());
        assertEquals("test", token.getStartTag().getAttribute("id"));
        assertEquals("testClass", token.getStartTag().getAttribute("class"));
        assertNull(token.getStartTag().getEndTag());
    }

    @Test
    public void testReadEndTag() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        String tagName = "TestTag";
        tokeniser.emit("<" + tagName);
        tokeniser.emit(">"); // open without />
        tokeniser.emit("<" + "/" + tagName + ">");
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertEquals(tagName, token.getEndTag().getName());
        assertNull(token.getEndTag().getStartTag());
    }

    @Test
    public void testReadMixedContent() {
        Tokeniser tokeniser = new Tokeniser(new DummyCharacterReader(), new ParseErrorList());
        tokeniser.emit("<html><head><title>Test Title</title></head><body>");
        tokeniser.emit("Test Content <script src=\"test.js\"></script> <a href=\"test.html\">Link to test</a>");
        tokeniser.emit("</body></html>");

        // Read and consume <html>, <head>, <title>, <body> and whitespace
        Token token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("html", token.getStartTag().getName());
        assertNull(token.getStartTag().getAttributes());
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("head", token.getStartTag().getName());
        assertNull(token.getStartTag().getAttributes());
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("title", token.getStartTag().getName());
        assertNull(token.getStartTag().getAttributes());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("Test Title", token.getCharacter().getData());
        token = tokeniser.read();
        assertEquals(Token.TokenType.Whitespace, token.getType());
        assertEquals("", token.getWhitespace().getData());

        // Read and consume <body>
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("body", token.getStartTag().getName());
        assertNull(token.getStartTag().getAttributes());

        // Read and consume Test Content
        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.getType());
        assertEquals("Test Content ", token.getCharacter().getData());

        // Read and consume <script> and its attributes
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("script", token.getStartTag().getName());
        assertEquals("src=\"test.js\"", token.getStartTag().getAttributes().toString());

        // Read and consume <a> and its attributes
        token = tokeniser.read();
        assertEquals(Token.TokenType.StartTag, token.getType());
        assertEquals("a", token.getStartTag().getName());
        assertEquals("href=\"test.html\"", token.getStartTag().getAttributes().toString());

        // Read and consume </a>
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertEquals("a", token.getEndTag().getName());
        assertNull(token.getEndTag().getStartTag());

        // Read and consume </body> and </html>
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertEquals("body", token.getEndTag().getName());
        assertNull(token.getEndTag().getStartTag());
        token = tokeniser.read();
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertEquals("html", token.getEndTag().getName());
        assertNull(token.getEndTag().getStartTag());

        // Read and consume EOF
        token = tokeniser.read();
        assertEquals(Token.TokenType.Eof, token.getType());
    }
}

class DummyCharacterReader implements CharacterReader {
    private int index = 0;
    private String data = "";

    @Override
    public int read() throws IOException {
        if (index >= data.length()) {
            return -1;
        }
        return (int) data.charAt(index++);
    }

    @Override
    public boolean isEmpty() {
        return data.length() == 0;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        // Dummy
    }

    @Override
    public void mark(int readAheadLimit, long pos) throws IOException {
        // Dummy
    }

    @Override
    public void reset() throws IOException {
        index = 0;
    }

    @Override
    public void close() throws IOException {
        // Dummy
    }

    @Override
    public boolean matches(String regex) {
        return data.matches(regex);
    }

    @Override
    public boolean matches(String regex, int index) {
        return data.regionMatches(index, regex);
    }

    @Override
    public boolean matchesAny(char[] chars) {
        return data.matches(String.valueOf(chars));
    }

    @Override
    public boolean matchesAny(char[] chars, int index, int len) {
        return data.regionMatches(index, chars, 0, len);
    }

    @Override
    public boolean matchesAnySorted(char[] charsSorted) {
        for (char c : charsSorted) {
            if (data.matches(Character.toString(c))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void markConsume(int ch) throws IOException {
        data += (char) ch;
    }

    @Override
    public void consume(int ch) throws IOException {
        data += (char) ch;
    }

    @Override
    public void consume(char c) throws IOException {
        data += c;
    }

    @Override
    public void consumeLetterThenDigitSequence() {
        // Dummy
    }

    @Override
    public void consumeDigitSequence() {
        // Dummy
    }

    @Override
    public void consumeHexSequence() {
        // Dummy
    }

    @Override
    public void advance() throws IOException {
        index++;
    }

    @Override
    public void consume(int numChars) throws IOException {
        data = data.substring(numChars);
        index -= numChars;
    }

    @Override
    public void consume(int[] codepoints) throws IOException {
        int len = codepoints.length;
        for (int i = 0; i < len; i++) {
            consume((int) codepoints[i]);
        }
    }

    @Override
    public void consume(String s) throws IOException {
        data += s;
    }

    @Override
    public void consumeTo(char c) throws IOException {
        int index = 0;
        while (index < data.length() && data.charAt(index) != c) {
            consume((int) data.charAt(index++));
        }
    }

    @Override
    public int position() {
        return index;
    }

    @Override
    public void setData(String data) {
        this.data = data;
        index = 0;
    }
}

class ParseErrorList {
    private int maxErrors = 1024;
    private int errorCount = 0;
    private ParseError[] errors = new ParseError[maxErrors];

    public boolean canAddError() {
        return errorCount < maxErrors;
    }

    public void add(ParseError error) {
        if (errorCount >= maxErrors) {
            throw new RuntimeException("Error list is full.");
        }
        errors[errorCount++] = error;
    }
}

class ParseError {
    final int pos;
    final String message;

    ParseError(int pos, String message) {
        this.pos = pos;
        this.message = message;
    }
}