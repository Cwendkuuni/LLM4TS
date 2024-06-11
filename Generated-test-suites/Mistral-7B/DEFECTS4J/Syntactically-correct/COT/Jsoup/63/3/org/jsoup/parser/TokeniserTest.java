package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private Tokeniser tokeniser;

 @SuppressWarnings("deprecation")
 @Before
 public void setUp() {
 char[] input = {'<', 'h', 't', 'm', 'l', ' ', 't', 'a', 'g', '>', '\n'};
 StringBuilder errorList = new StringBuilder();
 tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return (input.length > 0) ? input[0] : -1;
 }

 @Override
 public int read(char[] c, int off, int len) throws IOException {
 System.arraycopy(input, 0, c, off, input.length);
 input = new char[0];
 return len;
 }

 @Override
 public void close() throws IOException {
 // no-op
 }

 @Override
 public boolean isEmpty() {
 return input.length == 0;
 }

 @Override
 public void mark(int readAheadLimit) throws IOException {
 // no-op
 }

 @Override
 public void reset() throws IOException {
 Arrays.fill(input, 0, input.length, (char) 0);
 }

 @Override
 public void advance() throws IOException {
 if (input.length > 0) {
 input = Arrays.copyOfRange(input, 1, input.length);
 }
 }
 }, new ParseErrorList() {
 @Override
 public void add(ParseError error) {
 errorList.append(error.toString());
 }
 });
 }

 @Test
 public void testReadEmpty() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.emit("<html>");
 tokeniser.transition(TokeniserState.StartTag);
 token = tokeniser.read();
 Token.StartTag expected = new Token.StartTag("html", false);
 assertEquals(expected, token);
 }

 @Test
 public void testReadStartTagSelfClosing() {
 tokeniser.emit("<img>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 Token.StartTag expected = new Token.StartTag("img", true);
 assertEquals(expected, token);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.emit("<html>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("</html>");
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 Token.EndTag expected = new Token.EndTag("html");
 assertEquals(expected, token);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.emit("abc");
 token = tokeniser.read();
 Token.Character expected = new Token.Character('a');
 assertEquals(expected, token);
 }

 @Test
 public void testReadCharacterWithEscapes() {
 tokeniser.emit("&lt;abc&gt;");
 tokeniser.transition(TokeniserState.Content);
 token = tokeniser.read();
 Token.Character expected = new Token.Character('<');
 assertEquals(expected, token);
 }

 @Test(expected = AssertionError.class)
 public void testReadUnexpectedCharacter() {
 tokeniser.emit("abc");
 tokeniser.transition(TokeniserState.Content);
 tokeniser.emit('X');
 token = tokeniser.read();
 }

 @Test
 public void testReadEmptyLine() {
 tokeniser.emit("\n");
 tokeniser.transition(TokeniserState.Whitespace);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Whitespace, token.type);
 }

 @Test
 public void testReadComment() {
 tokeniser.emit("<!-- This is a comment -->");
 tokeniser.transition(TokeniserState.Comment);
 token = tokeniser.read();
 Token.Comment expected = new Token.Comment("This is a comment");
 assertEquals(expected, token);
 }

 @Test
 public void testReadScriptTag() {
 tokeniser.emit("<script>");
 tokeniser.transition(TokeniserState.Script);
 token = tokeniser.read();
 Token.StartTag expected = new Token.StartTag("script", true);
 assertEquals(expected, token);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.emit("<!DOCTYPE html>");
 tokeniser.transition(TokeniserState.Doctype);token = tokeniser.read();
 Token.Doctype expected = new Token.Doctype("html");
 assertEquals(expected, token);
 }

 @Test(expected = AssertionError.class)
 public void testReadUnexpectedEndTag() {
 tokeniser.emit("<html>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("</body>");
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit("\n");
 tokeniser.transition(TokeniserState.Whitespace);
 token = tokeniser.read();
 }

 @Test
 public void testErrorHandling() {
 tokeniser.emit("<html>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.error(TokeniserState.StartTag);
 assertEquals("Unexpected character 'h' in input state [StartTag]", errorList.toString());
 }

 @Test
 public void testEofError() {
 tokeniser.emit("<html>");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit("</html>\0"); // adding null character to simulate EOF
 tokeniser.transition(TokeniserState.EndTag);
 assertEquals("Unexpectedly reached end of file (EOF) in input state [EndTag]", errorList.toString());
 }

 @Test
 public void testCharacterReference() {
 tokeniser.emit("&amp;");
 tokeniser.transition(TokeniserState.Content);
 Token.Character expected = new Token.Character('&');
 assertEquals(expected, tokeniser.read());
 tokeniser.emit("&lt;");
 tokeniser.transition(TokeniserState.Content);
 Token.Character lt = new Token.Character('<');
 assertEquals(lt, tokeniser.read());
 tokeniser.emit("&gt;");
 tokeniser.transition(TokeniserState.Content);
 Token.Character gt = new Token.Character('>');
 assertEquals(gt, tokeniser.read());
 }
}