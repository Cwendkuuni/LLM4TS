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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader reader;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadWhenInputStreamIsEmptyShouldReturnNull() throws IOException {
 Mockito.when(reader.isEmpty()).thenReturn(true);
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadWhenCharacterIsConsumedShouldReturnCharacterToken() throws IOException {
 Mockito.when(reader.current()).thenReturn('a');
 tokeniser.emit('a');
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @Test
 public void testReadWhenCharacterIsConsumedAndBufferIsNotEmptyShouldReturnCharacterInBuffer() throws IOException {
 charBufferSetup();
 tokeniser.emit('a');
 tokeniser.emit('b');
 tokeniser.emit('c');
 Mockito.when(reader.current()).thenReturn('a');
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @Test
 public void testReadWhenCharacterIsConsumedAndBufferIsEmptyShouldReturnPendingToken() throws IOException {
 charBufferSetup();
 tokeniser.emit('a');
 tokeniser.emit('b');
 Mockito.when(reader.current()).thenReturn(replacementChar);
 Token token = tokeniser.read();
 assertSame(emitPending, token);
 }

 @Test
 public void testReadWhenEndTagIsConsumedShouldReturnEndTagToken() throws IOException {
 createEndTagTokenSetup();
 Mockito.when(reader.current()).thenReturn('/');
 reader.matchConsume('/');
 reader.matchConsume('p');
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("p", token.tagName);
 }

 @Test
 public void testReadWhenEndTagIsConsumedWithAttributesShouldThrowError() throws IOException {
 createEndTagTokenSetup();
 Mockito.when(reader.current()).thenReturn('/');
 reader.matchConsume('/');
 reader.matchConsume('p');
 reader.matchConsume(' ');
 reader.matchConsume('a');
 reader.matchConsume('=');
 reader.matchConsume('"');
reader.matchConsume('value');
 reader.matchConsume('"');
 reader.matchConsume('>');
 tokeniser.read();
 tokeniser.error(TokeniserState.ErrorHandling);
 assertEquals(1, errors.getErrorCount());
 }

 @Test
 public void testReadWhenStartTagIsConsumedShouldReturnStartTagToken() throws IOException {
 createStartTagTokenSetup();
 Mockito.when(reader.current()).thenReturn('<');
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('p');
 reader.matchConsume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("p", token.tagName);
 }

 @Test
 public void testReadWhenStartTagIsConsumedWithAttributesShouldReturnStartTagTokenWithAttributes() throws IOException {
 createStartTagTokenSetup();
 Mockito.when(reader.current()).thenReturn('<');
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('p');
 reader.matchConsume(' ');
 reader.matchConsume('c');
 reader.matchConsume('=');
 reader.matchConsume('"');
 reader.matchConsume('c');
 reader.matchConsume('"');
 reader.matchConsume(' ');
 reader.matchConsume('a');
 reader.matchConsump('=');
 reader.matchConsume('"');
 reader.matchConsume('v');
 reader.matchConsume('"');
 reader.matchConsume('>');
 tokeniser.read();
 tokeniser.read();
 List<Token.Attribute> attributes = tokeniser.lastStartTag.attributes;
 assertEquals(2, attributes.size());
 assertEquals("c", attributes.get(0).name);
 assertEquals("c", attributes.get(0).value);
 assertEquals("a", attributes.get(1).name);
 assertEquals("v", attributes.get(1).value);
 }

 @Test
 public void testReadWhenStringIsConsumedShouldReturnStringToken() throws IOException {
 String stringToConsume = "Test";
 tokeniser.emit(stringToConsume.toCharArray());
 Mockito.when(reader.current()).thenReturn('\u0000');
 String stringTokenData = tokeniser.read().data;
 assertEquals(stringToConsume, stringTokenData);
 }

 @Test
 public void testReadWhenSelfClosingStartTagIsConsumedShouldReturnStartTagToken() throws IOException {
 createSelfClosingStartTagTokenSetup();
 Mockito.when(reader.current()).thenReturn('<');
 reader.matchConsume('<');
 reader.matchConsume('/');
 reader.matchConsume('p');
 reader.matchConsume('>');
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("p", token.tagName);
 assertTrue(token.selfClosing);
 }

 @Test
 public void testErrorHandlingWhenUnexpectedCharacterIsConsumedShouldAddParseError() throws IOException {
 Mockito.when(reader.current()).thenReturn('!');
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.getErrorCount());
 }

 @Test
 public void testErrorHandlingWhenUnexpectedEndOfFileIsConsumedShouldAddParseError() throws IOException {
 Mockito.when(reader.isEmpty()).thenReturn(false);
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
 tokeniser = new Tokeniser(new InputStreamReader(inputStream), errors);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Mockito.when(reader.isEmpty()).thenReturn(true);
 tokeniser.read();
 tokeniser.eofError(TokeniserState.ErrorHandling);
 assertEquals(1, errors.getErrorCount());
 }

 private void charBufferSetup() {
 tokeniser.emit('a');
 tokeniser.emit('b');
 tokeniser.emit('c');
 }

 private void createEndTagTokenSetup() {
 tokeniser.createEndTagPending();
 }

 private void createStartTagTokenSetup() {
 tokeniser.createTagPending(true);
 }

 private void createSelfClosingStartTagTokenSetup() {
 tokeniser.createTagPending(true);
 tokeniser.emit('/');
 }
}