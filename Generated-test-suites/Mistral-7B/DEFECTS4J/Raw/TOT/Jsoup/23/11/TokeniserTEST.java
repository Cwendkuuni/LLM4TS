import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private TokeniserState state;
 private ParseErrorList errors;
 private List<ParseError> errorList;

 @Before
 public void setUp() {
 reader = Mockito.mock(CharacterReader.class);
 tokeniser = new Tokeniser(reader, null);
 state = TokeniserState.Data;
 errors = new ParseErrorList();
 errorList = new ArrayList<>();
 errors.setErrors(errorList);
 tokeniser.setErrors(errors);
 }

 @Test
 public void testRead_DataState_Character() {
 char expectedChar = 'a';
 Mockito.when(reader.current()).thenReturn(expectedChar);
 Mockito.when(reader.read()).thenReturn(expectedChar);

 Token expectedToken = new Token.Character(String.valueOf(expectedChar));

 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(state); // Data state remains unchanged

 ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);
 Mockito.verify(errors).add(Mockito.argThat(e -> e.toString().equals(String.format("Unexpected character '%c' in input state [%s]", expectedChar, state))));
 Mockito.verify(tokeniser).emit(tokenCaptor.capture());
 assertEquals(expectedToken, tokenCaptor.getValue());
 }

 @Test
 public void testRead_DataState_SelfClosingTag() {
 String expectedTagName = "img";
 Mockito.when(reader.current()).thenReturn('<');
 tokeniser.transition(state);
 tokeniser.read();
 Mockito.when(reader.current()).thenReturn('/');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(state);
 tokeniser.transition(TokeniserState.SelfClosingStartTag);

 ArgumentCaptor<Token.StartTag> startTagCaptor = ArgumentCaptor.forClass(Token.StartTag.class);
 ArgumentCaptor<String> tagNameCaptor = ArgumentCaptor.forClass(String.class);
 Mockito.verify(tokeniser).createTagPending(true);
 Mockito.verify(tokeniser).emitTagPending();
 Mockito.verify(tokeniser).acknowledgeSelfClosingFlag();
 Mockito.verify(tokeniser).error(TokeniserState.SelfClosingStartTag);
 Mockito.verify(tokeniser).createTempBuffer();
 Mockito.verify(tokeniser).emit(startTagCaptor.capture());
 assertEquals(expectedTagName, startTagCaptor.getValue().tagName);
 }

 @Test
 public void testRead_DataState_EndTag() {
 String expectedTagName = "div";
 Mockito.when(reader.current()).thenReturn('<."]');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.EndTag);

 ArgumentCaptor<Token.EndTag> endTagCaptor = ArgumentCaptor.forClass(Token.EndTag.class);
 Mockito.verify(tokeniser).createTempBuffer();
 Mockito.verify(tokeniser).emitEndTagName(expectedTagName);
 Mockito.verify(tokeniser).emitDoctypePending();
 Mockito.verify(tokeniser).emitCommentPending();
 Mockito.verify(tokeniser).emitTagPending();
 Mockito.verify(tokeniser).emit(endTagCaptor.capture());
 assertEquals(expectedTagName, endTagCaptor.getValue().tagName);
 }

 @Test
 public void testRead_ErrorState_EOF() {
 Mockito.when(reader.current()).thenReturn('\n');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.Error);
 tokeniser.acknowledgeSelfClosingFlag();
 Mockito.when(reader.isEmpty()).thenReturn(true);

 Mockito.verify(tokeniser).eofError(TokeniserState.Error);
 }

 @Test
 public void testRead_ErrorState_CharacterReference() {
 Mockito.when(reader.current()).thenReturn('#');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.Error);

 ArgumentCaptor<ParseError> parseErrorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(tokeniser).characterReferenceError("missing semicolon");
 Mockito.verify(errors).add(parseErrorCaptor.capture());
 assertEquals("Unexpected character '#' in input state [Data]", parseErrorCaptor.getValue().getMessage());
 }

 @Test
 public void testRead_ErrorState_NamedCharacterReference() {
 String expectedEntityName = "amp";
 Mockito.when(reader.current()).thenReturn('&');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.Error);

 ArgumentCaptor<ParseError> parseErrorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(tokeniser).characterReferenceError("invalid named referenece '&amp'");
 Mockito.verify(errors).add(parseErrorCaptor.capture());
 assertEquals("Unexpected character '&' in input state [Data]", parseErrorCaptor.getValue().getMessage());
 }

 @Test
 public void testRead_DataState_EOF() {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 tokeniser.transition(state);

 ArgumentCaptor<ParseError> parseErrorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(tokeniser).eofError(state);
 Mockito.verify(errors).add(parseErrorCaptor.capture());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", parseErrorCaptor.getValue().getMessage());
 }

 @Test
 public void testRead_DataState_Comment() {
 Mockito.when(reader.current()).thenReturn('*');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(state);
 tokeniser.transition(TokeniserState.Comment);

 ArgumentCaptor<Token.Comment> commentCaptor = ArgumentCaptor.forClass(Token.Comment.class);
 Mockito.verify(tokeniser).createCommentPending();
 Mockito.verify(tokeniser).emitCommentPending();
 Mockito.verify(tokeniser).emit(commentCaptor.capture());
 }

 @Test
 public void testRead_DataState_Script() {
 Mockito.when(reader.current()).thenReturn('<');
 tokeniser.transition(state);
 tokeniser.read();
 Mockito.when(reader.current()).thenReturn('/');
 tokeniser.transition(state);
 tokeniser.read();
 Mockito.when(reader.current()).thenReturn('s');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.Script);

 ArgumentCaptor<Token.StartTag> startTagCaptor = ArgumentCaptor.forClass(Token.StartTag.class);
 ArgumentCaptor<Token.Script> scriptCaptor = ArgumentCaptor.forClass(Token.Script.class);
 Mockito.verify(tokeniser).createTagPending(true);
 Mockito.verify(tokeniser).emitTagPending();
 Mockito.verify(tokeniser).createTempBuffer();
 Mockito.verify(tokeniser).emit(startTagCaptor.capture());
 assertEquals("script", startTagCaptor.getValue().tagName);
 Mockito.verify(tokeniser).emit(scriptCaptor.capture());
 }

 @Test
 public void testRead_DataState_Doctype() {
 Mockito.when(reader.current()).thenReturn('<');
 tokeniser.transition(state);
 tokeniser.read();
 Mockito.when(reader.current()).thenReturn('!');
 tokeniser.transition(state);
 tokeniser.read();
 Mockito.when(reader.current()).thenReturn('D');
 tokeniser.transition(state);
 tokeniser.read();
 tokeniser.transition(TokeniserState.Doctype);

 ArgumentCaptor<Token.Doctype> doctypeCaptor = ArgumentCaptor.forClass(Token.Doctype.class);
 Mockito.verify(tokeniser).createDoctypePending();
 Mockito.verify(tokeniser).emitDoctypePending();
 Mockito.verify(tokeniser).emit(doctypeCaptor.capture());
 }

 @Test
 public void testRead_DataState_CharacterEscapes() {
 String input = "&amp;&lt;&gt;";
 InputStream inputStream = new ByteArrayInputStream(input.getBytes());
 tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return inputStream.read();
 }

 @Override
 public char current() {
 return (char) inputStream.read();
 }

 @Override
 public boolean isEmpty() {
 return inputStream.available() == 0;
 }

 @Override
 public void mark(int readlimit) {
 // no implementation
 }

 @Override
 public void reset() {
 // no implementation
 }

 @Override
 public void advance() throws IOException {
 inputStream.read();
 }
 }, errors);

 tokeniser.transition(state);
 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.Error) {
 tokens.add(tokeniser.read());
 }

 ArgumentCaptor<Token.Character> characterCaptor = ArgumentCaptor.forClass(Token.Character.class);
 Token expectedToken1 = new Token.Character("&");
 Token expectedToken2 = new Token.Character("a");
 Token expectedToken3 = new Token.Character("m");
 Token expectedToken4 = new Token.Character("p");
 Token expectedToken5 = new Token.Character("&");
 Token expectedToken6 = new Token.Character("<");
 Token expectedToken7 = new Token.Character("/");
 Token expectedToken8 = new Token.Character("g");
 Token expectedToken9 = new Token.Character("t");

 assertEquals(8, tokens.size());
 assertEquals(expectedToken1, tokens.get(0));
 assertEquals(expectedToken2, tokens.get(1));
 assertEquals(expectedToken3, tokens.get(2));
 assertEquals(expectedToken4, tokens.get(3));
 assertEquals(expectedToken5, tokens.get(4));
 assertEquals(expectedToken6, tokens.get(5));
 assertEquals(expectedToken7, tokens.get(6));
 assertEquals(expectedToken8, tokens.get(7));
 assertEquals(expectedToken9, tokens.get(8));
 }

 @Test
 public void testRead_DataState_CharacterEscapes_InvalidCharacterReference() {
 String input = "&xyz;";
 InputStream inputStream = new ByteArrayInputStream(input.getBytes());
 tokeniser = new Tokeniser(new CharacterReader() {
 @Override
 public int read() throws IOException {
 return inputStream.read();
 }

 @Override
 public char current() {
 return (char) inputStream.read();
 }

 @Override
 public boolean isEmpty() {
 return inputStream.available() == 0;
 }

 @Override
 public void mark(int readlimit) {
 // no implementation
 }

 @Override
 public void reset() {
 // no implementation
 }

 @Override
 public void advance() throws IOException {
 inputStream.read();
 }
 }, errors);

 tokeniser.transition(state);
 List<Token> tokens = new ArrayList<>();
 while (tokeniser.getState() != TokeniserState.Error) {
 tokens.add(tokeniser.read());
 }

 ArgumentCaptor<ParseError> parseErrorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(tokeniser).characterReferenceError("character outside of valid range");
 Mockito.verify(errors).add(parseErrorCaptor.capture());
 assertEquals("Unexpected character 'x' in input state [Data]", parseErrorCaptor.getValue().getMessage());
 }
}