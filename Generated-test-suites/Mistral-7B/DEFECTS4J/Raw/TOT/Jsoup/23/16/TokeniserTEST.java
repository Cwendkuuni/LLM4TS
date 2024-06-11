import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
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
 private CharacterReader characterReader;
 private ParseErrorList errorList;

 @Before
 public void setUp() {
 errorList = new ParseErrorList();
 characterReader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
 tokeniser = new Tokeniser(characterReader, errorList);
 }

 @Test
 public void testEmptyInput() {
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.emit("a");
 tokeniser.emit('b');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Char);

 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("a", token.data);

 token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.Character, token.type);
 Assert.assertEquals("b", token.data);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 tokeniser.emit('<');
 tokeniser.emit('t');
 tokeniser.emit('h');
 tokeniser.emit(' ');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('>');

 Token token = tokeniser.read();
 Token.StartTag startTag = (Token.StartTag) token;
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("div", startTag.tagName);
 Assert.assertNull(startTag.attributes);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 tokeniser.emit('<');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('i');
 tokeniser.emit('m');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('>');

 Token token = tokeniser.read();
 Token.StartTag startTag = (Token.StartTag) token;
 Assert.assertEquals(Token.TokenType.StartTag, token.type);
 Assert.assertEquals("img", startTag.tagName);
 Assert.assertNotNull(startTag.attributes);
 Assert.assertEquals(1, startTag.attributes.size());
 Assert.assertEquals("src", startTag.attributes.get(0).key);
 Assert.assertNull(startTag.attributes.get(0).value);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 tokeniser.emit('<');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('a');
 tokeniser.emit('g');
 tokeniser.emit(' ');
 tokeniser.emit('/');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit(' ');
 tokeniser.emit('>');

 Token token = tokeniser.read();
 Assert.assertEquals(Token.TokenType.EndTag, token.type);
 Assert.assertEquals("div", token.data);
 }

 @Test
 public void testReadError() {
 characterReader = new CharacterReader(new ByteArrayInputStream("<".getBytes()));
 tokeniser = new Tokeniser(characterReader, errorList);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 tokeniser.emit('<');

 Assert.assertEquals(1, errorList.size());
 TokenError error = errorList.get(0);
 Assert.assertEquals(ParseError.ErrorType.EXPECTED_SEMICOLON, error.type);
 Assert.assertEquals(1, error.lineNumber);
 Assert.assertEquals(1, error.columnNumber);
 Assert.assertEquals("<", error.message);
 }

 @Test
 public void testReadEof() throws IOException {
 characterReader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
 tokeniser = new Tokeniser(characterReader, errorList);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 tokeniser.emit('<');
 tokeniser.emit('t');
 tokeniser.emit('h');
 tokeniser.emit(' ');
 tokeniser.emit('d');
 tokeniser.emit('i');
 tokeniser.emit('v');
 tokeniser.emit(' ');
 tokeniser.emit('>');
 tokeniser.read(); // consume start tag
 characterReader.close();

 Assert.assertEquals(1, errorList.size());
 Assert.assertEquals(ParseError.ErrorType.EOF, errorList.get(0).type);
 }
}