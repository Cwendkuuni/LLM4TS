package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private byte[] input = "<html><head><title>Test</title></head><body><script>alert('Hello!');</script></body></html>".getBytes();
 private ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 private List<Token> expectedTokens = new ArrayList<>();

 @Before
 public void setUp() {
 expectedTokens.add(new Token.StartTag("html", null, null));
 expectedTokens.add(new Token.StartTag("head", null, null));
 expectedTokens.add(new Token.StartTag("title", null, null));
 expectedTokens.add(new Token.Character("Test"));
 expectedTokens.add(new Token.EndTag("title"));
 expectedTokens.add(new Token.StartTag("body", null, null));
 expectedTokens.add(new Token.StartTag("script", null, null));
 expectedTokens.add(new Token.Character("alert"));
 expectedTokens.add(new Token.Character("("));
 expectedTokens.add(new Token.Character("H"));
 expectedTokens.add(new Token.Character("e"));
 expectedTokens.add(new Token.Character("l"));
 expectedTokens.add(new Token.Character("l"));
 expectedTokens.add(new Token.Character("o"));
 expectedTokens.add(new Token.Character("!"));
 expectedTokens.add(new Token.Character(")"));
 expectedTokens.add(new Token.Character(";"));
 expectedTokens.add(new Token.EndTag("script"));
 expectedTokens.add(new Token.EndTag("body"));
 expectedTokens.add(new Token.EndTag("html"));
 }

 @Test
 public void testTokeniser() throws IOException {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return (char) (inputStream.read() & 0xFF);
 }

 @Override
 public int read(char[] c, int off, int len) throws IOException {
 inputStream.read(c, off, len);
 return len;
 }

 @Override
 public void close() throws IOException {
 inputStream.close();
 }
 }, new ParseErrorList());

 Token token;
 for (int i = 0; i < expectedTokens.size(); i++) {
 token = tokeniser.read();
 assertEquals(expectedTokens.get(i).toString(), token.toString());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testUnreadTokenPending() throws IOException {
 tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return (char) (input.length > 0 ? input[0] : -1);
 }

 @Override
 public int read(char[] c, int off, int len) throws IOException {
 inputStream.read(c, off, len);
 return len;
 }

 @Override
 public void close() throws IOException {
 inputStream.close();
 }
 }, new ParseErrorList());

 tokeniser.read(); // read first token
 tokeniser.emit(expectedTokens.get(0)); // manually emit the first token

 // Simulate reading a character that is not the expected next character.
 // This should result in an error being thrown.
 tokeniser.reader.advance();
 tokeniser.reader.current('Z');

 tokeniser.read(); // read next token, but the pending token should still be emitted
 assertEquals(expectedTokens.get(0).toString(), tokeniser.emitPending.toString());
 assertNull(tokeniser.emitPending);
 }

 @Test
 public void testSelfClosingFlag() throws IOException {
 input = "<img src=\"image.png\"".getBytes();
 inputStream = new ByteArrayInputStream(input);
 tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return (char) (inputStream.read() & 0xFF);
 }

 @Override
 public int read(char[] c, int off, int len) throws IOException {
 inputStream.read(c, off, len);
 return len;
 }

 @Override
 public void close() throws IOException {
 inputStream.close();
 }
 }, new ParseErrorList());

 tokeniser.read(); // img
 tokeniser.emit(tokeniser.read()); // src
 tokeniser.consumeCharacterReference((char) '='); // '='
 tokeniser.consumeCharacterReference(null, true); // "
 tokeniser.consumeCharacterReference(null, false); // i
 tokeniser.consumeCharacterReference(null, false); // m
 tokeniser.consumeCharacterReference(null, false); // g
 tokeniser.consumeCharacterReference(null, false); // s
 tokeniser.consumeCharacterReference(null, false); // p
 tokeniser.consumeCharacterReference(null, false); // >
 tokeniser.acknowledgeSelfClosingFlag();

 token = tokeniser.read();
 assertEquals(Token.Tag.Img.toString(), token.tagName);
 assertTrue(token.isSelfClosing());
 }
}