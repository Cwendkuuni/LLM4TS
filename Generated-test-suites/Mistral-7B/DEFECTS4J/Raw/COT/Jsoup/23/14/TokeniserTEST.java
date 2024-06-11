package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;

public class TokeniserTest {

 private ByteArrayInputStream inputStream;
 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("test data".getBytes());
 errors = new ArrayList<>();
 tokeniser = new Tokeniser(new CharReader() {
 @Override
 public int read() throws IOException {
 return inputStream.read();
 }

 @Override
 public boolean isEmpty() {
 return inputStream.available() <= 0;
 }

 @Override
 public char current() throws IOException {
 return (char) inputStream.read();
 }

 @Override
 public void mark(int readAheadLimit) {
 inputStream.mark(readAheadLimit);
 }

 @Override
 public void rewind(int readAheadLimit) throws IOException {
 inputStream.rewind(readAheadLimit);
 }

 @Override
 public boolean matchesAny(char... chars) {
 for (char c : chars) {
 if (current() == c) {
 inputStream.read();
 return true;
 }
 }
 return false;
 }

 @Override
 public boolean matchesConsume(char c) throws IOException {
 if (current() == c) {
 inputStream.read();
 return true;
 }
 return false;
 }

 @Override
 public boolean matches(String regex) {
 return inputStream.matches(regex);
 }

 @Override
 public boolean matchesLetter() {
 return isAlphabetic((char) inputStream.read());
 }

 @Override
 public boolean matchesDigit() {
 return isDigit((char) inputStream.read());
 }

 @Override
 public boolean matchesIgnoreCase(String pattern) {
 return inputStream.matchesIgnoreCase(pattern);
 }

 @Override
 public int consumeLetterSequence() throws IOException {
 StringBuilder sb = new StringBuilder();
 while (isAlphabetic(current())) {
 sb.append(current());
 inputStream.read();
 }
 return sb.toString().length();
 }

 @Override
 public int consumeDigitSequence() throws IOException {
 StringBuilder sb = new StringBuilder();
 while (isDigit(current())) {
 sb.append(current());
 inputStream.read();
 }
 return Integer.parseInt(sb.toString());
 }

 @Override
 public int consumeHexSequence() throws IOException {
 StringBuilder sb = new StringBuilder();
 boolean isHex = false;
 while (true) {
 char c = inputStream.current();
 if (c >= '0' && c <= '9') {
 sb.append(c);
 inputStream.read();
 } else if (c >= 'A' && c <= 'F') {
 sb.append((char) ((c & 0x20) + (isHex ? 0x0 : 7)));
 inputStream.read();
 isHex = true;
 } else if (c >= 'a' && c <= 'f') {
 sb.append((char) ((c & 0x20) + (isHex ? 0x0 : 7)));
 inputStream.read();
 isHex = true;
 } else {
 break;
 }
 }
 return Integer.parseInt(sb.toString(), 16);
 }

 @Override
 public int consumeWhiteSpace() throws IOException {
 StringBuilder sb = new StringBuilder();
 while (inputStream.matchesAny(' ')) {
 sb.append(inputStream.current());
 inputStream.read();
 }
 return sb.toString().length();
 }

 @Override
 public boolean isAlphabetic(char c) {
 return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
 }

 @Override
 public boolean isDigit(char c) {
 return c >= '0' && c <= '9';
 }
 }, errors);
 }

 @After
 public void tearDown() {
 inputStream.close();
}

 // Test methods for Tokeniser class

 @Test
 public void testConstructor() {
 tokeniser = new Tokeniser(null, null);
 assertNull(tokeniser.reader);
 assertNull(tokeniser.errors);
 }

 @Test
 public void testReadCharacter() {
 char expected = 'a';
 char actual = tokeniser.read().getData();
 assertEquals("Unexpected character", expected, actual);
 }

 @Test
 public void testReadEmptyStream() {
 assertFalse("Stream should be empty", tokeniser.read() != null);
 }

 @Test
 public void testEmitToken() {
 // Given
 String expectedTagName = "p";
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.tagName = expectedTagName;
 tokeniser.emitTagPending();
 Token actualToken = tokeniser.read();

 // When
 // Then
 assertEquals("Unexpected tag type", Token.TokenType.StartTag, actualToken.type);
 assertEquals("Unexpected tag name", expectedTagName, actualToken.tagName.tagName);
 }

 @Test
 public void testEmitString() {
 // Given
 String expectedString = "test";
 tokeniser.emit("test");

 // When
 Token actualToken = tokeniser.read();

 // Then
 assertEquals("Unexpected token type", Token.TokenType.Character, actualToken.type);
 assertTrue("Unexpected string data", actualToken.getData().equals(expectedString));
 }

 @Test
 public void testEmitCharacter() {
 // Given
 char expectedChar = 'A';
 tokeniser.emit(expectedChar);

 // When
 Token actualToken = tokeniser.read();

 // Then
 assertEquals("Unexpected token type", Token.TokenType.Character, actualToken.type);
 assertEquals("Unexpected character data", expectedChar, actualToken.getData());
 }

 @Test
 public void testGetState() {
 TokeniserState expectedState = TokeniserState.Data;
 TokeniserState actualState = tokeniser.getState();
 assertEquals("Unexpected state", expectedState, actualState);
 }

 @Test
 public void testTransition() {
 // Given
 TokeniserState expectedState = TokeniserState.Script;
 tokeniser.transition(expectedState);

 // When
 TokeniserState actualState = tokeniser.getState();

 // Then
 assertEquals("Unexpected state", expectedState, actualState);
 }

 @Test
 public void testAdvanceTransition() {
 // Given
 TokeniserState expectedState = TokeniserState.Script;
 tokeniser.advanceTransition(expectedState);
 char expectedChar = '(';
 char actualChar = tokeniser.reader.current();

 // When
 tokeniser.reader.consume();

 // Then
 assertEquals("Unexpected character", expectedChar, actualChar);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse("Self closing flag not acknowledged", tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue("Self closing flag not acknowledged", tokeniser.selfClosingFlagAcknowledged);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithNullCharsAllowed() {
 char additionalAllowedCharacter = '\u0000';
 tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithInvalidCharacter() {
 char additionalAllowedCharacter = '\u0001';
 tokeniser.consumeCharacterReference(additionalAllowedCharacter, false);
 }

 @Test
 public void testConsumeCharacterReferenceWithEmptyStream() {
 assertNull("Unexpected character reference returned", tokeniser.consumeCharacterReference(null, false));
 }

 @Test
 public void testConsumeCharacterReferenceWithInvalidCharacterSequence() {
 tokeniser.mark(10);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.rewind(10);
 }

 @Test
 public void testConsumeCharacterReferenceWithNamedEntity() throws IOException {
 tokeniser.mark(10);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.rewind(10);
 String expectedEntityName = "amp";
 String actualEntityName = Entities.getCharacterByName(tokeniser.reader.consumeLetterSequence()).toString();
 assertEquals("Unexpected entity name", expectedEntityName, actualEntityName);
 tokeniser.consume(";");
 }

 @Test
 public void testConsumeCharacterReferenceWithNamedEntityWithAttribute() throws IOException {
 tokeniser.mark(10);
 String expectedAttributeName = "class";
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.rewind(10);
 String actualEntityName = Entities.getCharacterByName(tokeniser.reader.consumeLetterSequence()).toString();
 assertEquals("Unexpected entity name", expectedAttributeName, actualEntityName);
 assertTrue("Unexpected attribute name presence", tokeniser.reader.matchesLetter() || tokeniser.reader.matchesDigit() || tokeniser.reader.matchesAny('=', '-', '_'));
 tokeniser.consume(";");
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithInvalidSemicolonPosition() throws IOException {
 tokeniser.mark(10);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consume(";");
 tokeniser.rewind(10);
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag expectedTag = new Token.StartTag();
 Token.Tag actualTag = tokeniser.createTagPending(true);
 assertEquals("Unexpected tag type", expectedTag.getClass(), actualTag.getClass());
 }

 @Test
 public void testCreateEndTagPending() {
 Token.Tag expectedTag = new Token.EndTag();
 Token.Tag actualTag = tokeniser.createTagPending(false);
 assertEquals("Unexpected tag type", expectedTag.getClass(), actualTag.getClass());
 }

 @Test
 public void testEmitTagPending() {
 // Given
 String expectedTagName = "p";
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.tagName = expectedTagName;
 tokeniser.emitTagPending();

 // When
 Token actualToken = tokeniser.read();

 // Then
 assertEquals("Unexpected tag type", Token.TokenType.StartTag, actualToken.type);
 assertEquals("Unexpected tag name", expectedTagName, actualToken.tagName.tagName);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token actualToken = tokeniser.read();
 assertEquals("Unexpected token type", Token.TokenType.Comment, actualToken.type);
 }

 @Test
 public void testEmitCommentPending() {
 // Given
 String expectedComment = "test comment";
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();

 // When
 Token actualToken = tokeniser.read();

 // Then
 assertEquals("Unexpected token type", Token.TokenType.Comment, actualToken.type);
 assertEquals("Unexpected comment data", expectedComment, actualToken.getData());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token actualToken = tokeniser.read();
 assertEquals("Unexpected token type", Token.TokenType.Doctype, actualToken.type);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token actualToken = tokeniser.read();
 assertEquals("Unexpected token type", Token.TokenType.Doctype, actualToken.type);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 // Given
 String expectedTagName = "p";
 Token.StartTag startTag = new Token.StartTag();
 startTag.tagName = expectedTagName;
 tokeniser.emitTagPending();
 Token actualToken = tokeniser.read();

 // When

 // Then
 assertTrue("Unexpected end tag presence", tokeniser.isAppropriateEndTagToken());
 assertEquals("Unexpected end tag name", expectedTagName, tokeniser.appropriateEndTagName());
 }

 @Test
 public void testError() {
 // Given
 String expectedErrorMessage = "Unexpected character '%s' in input state [Data]";
 String actualErrorMessage = "";

 // When
 tokeniser.error(TokeniserState.Data);
 actualErrorMessage += tokeniser.errors.get(0).getMessage();
 tokeniser.errors.clear();
 tokeniser.error(TokeniserState.Data);
 actualErrorMessage += tokeniser.errors.get(0).getMessage();

 // Then
 assertEquals("Unexpected error message", String.format(expectedErrorMessage, tokeniser.reader.current()), actualErrorMessage);
 }

 @Test
 public void testEofError() {
 // Given
 String expectedErrorMessage = "Unexpectedly reached end of file (EOF) in input state [Data]";
 String actualErrorMessage = "";

 // When
 tokeniser.eofError(TokeniserState.Data);
 actualErrorMessage += tokeniser.errors.get(0).getMessage();
 tokeniser.errors.clear();
 tokeniser.eofError(TokeniserState.Data);
 actualErrorMessage += tokeniser.errors.get(0).getMessage();

 // Then
 assertEquals("Unexpected error message", expectedErrorMessage, actualErrorMessage);
 }

 @Test
 public void testCharacterReferenceError() {
 // Given
 String expectedErrorMessage = "Invalid character reference: %s";
 String actualErrorMessage = "";

 // When
 tokeniser.characterReferenceError("numeric reference with no numerals");
 actualErrorMessage += tokeniser.errors.get(0).getMessage();
 tokeniser.errors.clear();
 tokeniser.characterReferenceError("numeric reference with no numerals");
 actualErrorMessage += tokeniser.errors.get(0).getMessage();

 // Then
 assertEquals("Unexpected error message", expectedErrorMessage, actualErrorMessage);
 }
}