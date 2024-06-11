import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseError;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokeniserTest {

 private CharacterReader mockReader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockReader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(mockReader, errors);
 }

 @Test
 public void testReadStartTag() {
 when(mockReader.current()).thenReturn('<').thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('l').thenReturn('>');
 tokeniser.read();
 Token expectedStartTag = new Token.StartTag("html");
 verify(mockReader).consume(4); // consume 4 characters: "<html>"
 assertEquals(expectedStartTag, tokeniser.read());
 }

 @Test
 public void testReadEndTag() {
 when(mockReader.current()).thenReturn('</').thenReturn('h').thenReturn('t').thenReturn('m').thenReturn('l').thenReturn('>');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.read();
 Token expectedEndTag = new Token.EndTag("html");
 verify(mockReader).consume(6); // consume 6 characters: "</html>"
 assertEquals(expectedEndTag, tokeniser.read());
 }

 @Test
 public void testReadCharacter() {
 when(mockReader.current()).thenReturn('a').thenReturn('b').thenReturn('c');
 tokeniser.emit(new Token.Character('a'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('c'));
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testReadUnexpectedCharacter() {
 when(mockReader.current()).thenReturn('>');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.read();
 verify(errors).add(any(ParseError.class)); // add a ParseError to the error list
 }

 @Test
 public void testReadSelfClosingTag() {
 when(mockReader.current()).thenReturn('<').thenReturn('i').thenReturn('m').thenReturn('g').thenReturn('/').thenReturn('i').thenReturn('m').thenReturn('>');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.read();
 Token expectedSelfClosingTag = new Token.StartTag("img", true);
 verify(mockReader).consume(6); // consume 6 characters: "<img>"
 assertEquals(expectedSelfClosingTag, tokeniser.read());
 }

 @Test
 public void testReadString() {
 when(mockReader.current()).thenReturn('"').thenReturn('H').thenReturn('e').thenReturn('l').thenReturn('l').thenReturn('o').thenReturn(' ').thenReturn('W').thenReturn('o').thenReturn('r').thenReturn('l').thenReturn('d').thenReturn(' ')
 .thenReturn('d').thenReturn('a').thenReturn('t').thenReturn('a').thenReturn(' ')
 .thenReturn('i').thenReturn('s').thenReturn(' ').thenReturn('a').thenReturn(' ').thenReturn('s').thenReturn(':').thenReturn(' ').thenReturn('h').thenReturn('t').thenReturn('t').thenReturn('p')
 .thenReturn(':').thenReturn('/').thenReturn('/').thenReturn('H').thenReturn('T').thenReturn('T').thenReturn('P').thenReturn('/').thenReturn(' ')
 .thenReturn('>');
 tokeniser.transition(TokeniserState.TagBody);
 tokeniser.read();
 Token expectedString = new Token.CharacterString("Hello World is a string: http://HTTP/Hello/World");
 verify(mockReader).consume(38); // consume 38 characters: "Hello World is a string: http://HTTP/Hello/World"
 assertEquals(expectedString, tokeniser.read());
 }

 @Test
 public void testReadEmptyString() {
 when(mockReader.current()).thenReturn('"');
 tokeniser.transition(TokeniserState.TagBody);
 tokeniser.read();
 Token expectedEmptyString = new Token.CharacterString("");
 verify(mockReader).consume(1); // consume 1 character: ""
 assertEquals(expectedEmptyString, tokeniser.read());
 }

 @Test
 public void testReadCharacterReference() {
 when(mockReader.current()).thenReturn('#').thenReturn('x').thenReturn('2').thenReturn('#').thenReturn('x').thenReturn('3').thenReturn(';');
 tokeniser.read(); // consume "<"
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 verify(mockReader, never()).consume(5); // do not consume the character reference itself
 assertEquals(Token.TokenType.CharacterReference, tokeniser.read().type);
 }

 @Test
 public void testReadInvalidCharacterReference() {
 when(mockReader.current()).thenReturn('#').thenReturn('x').thenReturn('A').thenReturn('#');
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 verify(errors).add(any(ParseError.class)); // add a ParseError to the error list
 }

 @Test
 public void testReadComment() {
 when(mockReader.current()).thenReturn('<!--').thenReturn('C').thenReturn('o').thenReturn('m').thenReturn('m').thenReturn('e').thenReturn('n').thenReturn('t').thenReturn(' ')
 .thenReturn('t').thenReturn('o').thenReturn(' ')
 .thenReturn('t').thenReturn('e').thenReturn('s').thenReturn('t').thenReturn(' ')
 .thenReturn('-->');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 Token expectedComment = new Token.Comment("<!-- Comment test -->");
 verify(mockReader).consume(12); // consume 12 characters: "<!-- Comment test -->"
 assertEquals(expectedComment, tokeniser.read());
 }

 @Test
 public void testReadScriptTag() {
 when(mockReader.current()).thenReturn('<').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('>');
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 tokeniser.emit(new Token.StartTag("script"));
 verify(mockReader).consume(6); // consume 6 characters: "<script>"
 }

 @Test
 public void testReadScriptTagWithError() {
 when(mockReader.current()).thenReturn('<').thenReturn('/').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t').thenReturn('>');
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 verify(errors).add(any(ParseError.class)); // add a ParseError to the error list
 }
}