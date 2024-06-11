import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setup() {
 charReader = new MockCharacterReader();
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacter() {
 characterReader.append('a');
 characterReader.next();
 Token token = tokeniser.read();
 assertEquals("Character token expected, got: null", new Token.Character("a"), token);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorNullReader() {
 new Tokeniser(null);
 }

 @Test
 public void testEmitToken() {
 Token token = new Token.Character('a');
 tokeniser.emit(token);
 Token emittedToken = tokeniser.read();
 assertEquals("Unexpected token type, expected: CHAR, got: ", token.type, emittedToken.type);
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("hello");
 Token token = tokeniser.read();
 assertEquals("Character token expected, got: ", new Token.TextNode("hello"), token);
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.advanceTransition(TokeniserState.StartTagName);
 assertSame("Unexpected state, expected: START_TAG_NAME, got: ", TokeniserState.StartTagName, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse("Self closing flag should be false after acknowledging", tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference() {
 characterReader.append("#x41");
 assertTrue("Unexpected character reference error, expected: true, got: false", tokeniser.consumeCharacterReference(null, false) != null);
 assertEquals("Unexpected character reference, expected: 'A', got: ", 'A', tokeniser.consumeCharacterReference(null, false).charValue());
 characterReader.append("&amp;");
 assertEquals("Unexpected character reference error, expected: '&amp;', got: null", "&amp;", tokeniser.consumeCharacterReference(null, false).getEntityName());
 }

 @Test
 public void testCreateTagPending() {
 Token.StartTag tag = tokeniser.createTagPending(true);
 tag.tagName = "div";
 assertSame("Unexpected tag type, expected: START_TAG, got: ", Token.StartTag.class, tag.getClass());
 assertEquals("Unexpected tag name, expected: div, got: ", "div", tag.tagName);
 }

 @Test
 public void testEmitTagPending() {
 Token.StartTag startTag = tokeniser.createTagPending(true);
 startTag.tagName = "div";
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertSame("Unexpected token type, expected: START_TAG, got: ", startTag, token);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token comment = tokeniser.read();
 assertEquals("Unexpected token type, expected: COMMENT, got: ", new Token.Comment(), comment);
 }

 @Test
 public void testEmitCommentPending() {
 Token comment = tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertSame("Unexpected token type, expected: COMMENT, got: ", comment, token);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token doctype = tokeniser.read();
 assertEquals("Unexpected token type, expected: DOCTYPE, got: ", new Token.Doctype(), doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 Token doctype = tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertSame("Unexpected token type, expected: DOCTYPE, got: ", doctype, token);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadAfterEmitPending() {
 tokeniser.emitPending = new Token.Character('a');
 tokeniser.isEmitPending = true;
 tokeniser.read();
 }

 @Test
 public void testTrackErrors() {
 characterReader.append("«tag"); // invalid character
 tokeniser.read();
 assertTrue("No error thrown, expected an error", tokeniser.isTrackErrors());
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals("Unexpected number of errors, expected: 1, got: ", 1, errors.size());
 assertEquals("Unexpected error message, expected: " + "Unexpected character in input, got: ", "Unexpected character in input", errors.get(0).getMessage());
 assertSame("Unexpected position, expected: 1, got: ", 1, errors.get(0).getPosition());
 assertSame("Unexpected state, expected: DONE, got: ", TokeniserState.DONE, errors.get(0).getState());
 }

 @Test
 public void testGetState() {
 tokeniser.transition(TokeniserState.StartTagName);
 assertEquals("Unexpected state, expected: START_TAG_NAME, got: ", TokeniserState.StartTagName, tokeniser.getState());
 }
}

class MockCharacterReader implements CharacterReader {
 private StringBuilder stringBuilder = new StringBuilder();
 private int position = 0;

 @Override
 public boolean isEmpty() {
 return stringBuilder.length() == 0;
 }

 @Override
 public int pos() {
 return position;
 }

 @Override
 public char current() {
 return stringBuilder.length() > 0 ? stringBuilder.charAt(position) : '\u0000';
 }

 @Override
 public void mark() {
 // no-op
 }

 @Override
 public void mark(int readAheadLimit) {
 // no-op
 }

 @Override
 public void rewindToMark() {
 if (stringBuilder.length() > position)
 stringBuilder.delete(position + 1, stringBuilder.length());
 }

 @Override
 public boolean matches(char expected) {
 return stringBuilder.length() > position && stringBuilder.charAt(position) == expected;
 }

 @Override
 public boolean matchesIgnoreCase(char expected) {
 return matches(expected);
 }

 @Override
 public boolean matchesAny(char... expected) {
 for (char c : expected)
 if (matches(c))
 return true;
 return false;
 }

 @Override
 public boolean matchesLetter() {
 return isAlpha((char) current());
 }

 @Override
 public boolean matchesDigit() {
 return isDigit((char) current());
 }

 @Override
 public boolean matches(String expected) {
 return stringBuilder.toString().startsWith(expected, position);
 }

 @Override
 public void consume(int n) {
 stringBuilder.delete(position, position + n);
 position += n;
 }

 @Override
 public void consume(char expected) {
 if (matches(expected)) {
 consume(1);
 } else {
 throw new IllegalStateException("Unexpected character encountered, expected: " + expected);
 }
 }

 @Override
 public void consumeDigitSequence() {
 int startPos = position;
 while (matchesDigit())
 consume(1);
 assertEquals("Unexpected number of digits consumed, expected: " + (position - startPos), startPos + 1, position);
 }

 @Override
 public void consumeLetterSequence() {
 int startPos = position;
 while (matchesLetter())
 consume(1);
 assertEquals("Unexpected number of letters consumed, expected: " + (position - startPos), startPos + 1, position);
 }

 @Override
 public void consumeHexSequence() {
 int startPos = position;
 consumeLetterSequence();
 consume(0x22); // ';'
 consumeLetterSequence();
 consume(0x22); // ';'
 assertEquals("Unexpected number of hex digits consumed, expected: 6, got: " + (position - startPos), startPos + 6, position);
 }

 @Override
 public char next() {
 if (isEmpty())
 throw new IllegalStateException("No character to read from input");
 char c = current();
 consume(1);
 return c;
 }

 @Override
 public void append(char c) {
 stringBuilder.append(c);
 }

 @Override
 public void append(String s) {
 stringBuilder.append(s);
 }

 private boolean isAlpha(char c) {
 return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
 }

 private boolean isDigit(char c) {
 return c >= '0' && c <= '9';
 }
}