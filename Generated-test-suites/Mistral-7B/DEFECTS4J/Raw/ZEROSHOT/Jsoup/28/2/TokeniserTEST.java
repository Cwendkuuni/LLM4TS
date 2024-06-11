import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 @Test
 public void testEmptyInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader(""), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 // nothing to test
 }
 }

 @Test
 public void testBasicCharacterInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("abc"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 assertEquals("a", token.getData());
 assertEquals(Token.TokenType.Character, token.getType());
 token = tokeniser.read();
 assertEquals("b", token.getData());
 assertEquals(Token.TokenType.Character, token.getType());
 token = tokeniser.read();
 assertEquals("c", token.getData());
 assertEquals(Token.TokenType.Character, token.getType());
 }
 }

 @Test
 public void testCharacterReferenceInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("&#123;"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof Character) {
 assertEquals('\u007B', token.getData().charAt(0));
 break;
 }
 }

 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testNamedCharacterReferenceInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("&copy;"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof Character) {
 assertEquals('\u00A9', token.getData().charAt(0));
 break;
 }
 }

 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testEmptyTagInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("<tag/>"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof EndTag) {
 assertEquals("tag", token.getData());
 assertFalse(token.hasAttributes());
 break;
 }
 }
 }

 @Test
 public void testTagWithAttributesInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("<tag attr='value' attr2='value2' />"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof EndTag) {
 assertEquals("tag", token.getData());
 List<Attribute> attributes = token.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("attr", attributes.get(0).getName());
 assertEquals("value", attributes.get(0).getValue());
 assertEquals("attr2", attributes.get(1).getName());
 assertEquals("value2", attributes.get(1).getValue());
 break;
 }
 }
 }

 @Test
 public void testScriptTagInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("<script type='text/javascript'>alert('Hello World!');</script>"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof StartTag) {
 assertEquals("script", token.getData());
 assertEquals(TokeniserState.Script, tokeniser.getState());
 assertSame(token, tokeniser.lastStartTag);
 break;
 }
 }

 token = tokeniser.read(); // script data
 assertTrue(tokeniser.dataBuffer.toString().contains("alert('Hello World!');"));

 token = tokeniser.read(); // script closing tag
 assertEquals("script", token.getData());
 assertFalse(token.hasAttributes());
 assertSame(token, tokeniser.lastStartTag);
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test(expected = IllegalStateException.class)
 public void testMultipleScriptTagsWithoutEndingClosingTag() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("<script type='text/javascript'>alert('Hello World!');<script>"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 // nothing to test
 }
 }

 @Test
 public void testScriptTagWithAttributesInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("<script type='text/javascript' src='script.js'>"), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 if (token instanceof StartTag) {
 assertEquals("script", token.getData());
 List<Attribute> attributes = token.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("type", attributes.get(0).getName());
 assertEquals("text/javascript", attributes.get(0).getValue());
 assertEquals("src", attributes.get(1).getName());
 assertEquals("script.js", attributes.get(1).getValue());
 break;
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharacterReferenceInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("&#notValid;"), new ArrayList<ParseError>());
 }

 @Test
 public void testInvalidNamedCharacterReferenceInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader("&notValid;"), new ArrayList<ParseError>());
 }

 @Test
 public void testEOFInput() {
 Tokeniser tokeniser = new Tokeniser(new TestCharacterReader(""), new ArrayList<ParseError>());

 Token token = null;
 while ((token = tokeniser.read()) != null) {
 // nothing to test
 }
 tokeniser.eofError(TokeniserState.AfterStartTag);
 }
}

class TestCharacterReader implements CharacterReader {
 private final StringBuilder input;
 private final int startPos;

 public TestCharacterReader(String input) {
 this.input = new StringBuilder(input);
 this.startPos = 0;
 }

 @Override
 public int pos() {
 return startPos;
 }

 @Override
 public int mark() {
 return startPos;
 }

 @Override
 public void rewindToMark() {
 startPos = Math.max(0, startPos - 1);
 }

 @Override
 public char current() {
 return input.charAt(startPos);
 }

 @Override
 public boolean matches(char expected) {
 return current() == expected;
 }

 @Override
 public boolean matchesAny(char... expected) {
 for (char c : expected) {
 if (matches(c)) {
 return true;
 }
 }
 return false;
 }

 @Override
 public boolean matchesIgnoreCase(char expected) {
 return (current() == expected || current() == Character.toUpperCase(expected) || current() == Character.toLowerCase(expected));
 }

 @Override
 public boolean matchesLetter() {
 return isLetter(current());
 }

 @Override
 public boolean matchesDigit() {
 return isDigit(current());
 }

 @Override
 public boolean matchesAny(char expected1, char expected2) {
 return matches(expected1) || matches(expected2);
 }

 @Override
 public boolean isEmpty() {
 return input.length() - startPos <= 0;
 }

 @Override
 public void advance() {
 startPos++;
 }

 @Override
 public void consume(char expected) {
 if (matches(expected)) {
 advance();
 } else {
 error("Unexpected character '%s'", current());
 }
 }

 @Override
 public void consumeLetterThenDigitSequence() {
 StringBuilder buffer = new StringBuilder();
 while (isAlphaNumeric(current())) {
 buffer.append(current());
 advance();
 }
 consume(Character.isDigit(current()) ? current() : ' ');
 input.delete(startPos - buffer.length() - 1, startPos);
 setText(buffer.toString());
 }

 @Override
 public void consumeDigitSequence() {
 StringBuilder buffer = new StringBuilder();
 while (isDigit(current())) {
 buffer.append(current());
 advance();
 }
 input.delete(startPos - buffer.length() - 1, startPos);
 setText(buffer.toString());
 }

 @Override
 public void consumeHexSequence() {
 StringBuilder buffer = new StringBuilder();
 consume("0x");
 while (isHexDigit(current())) {
 buffer.append(current());
 advance();
 }
 int hexValue = Integer.parseInt(buffer.toString(), 16);
 input.delete(startPos - buffer.length() - 2, startPos);
 setText(String.format("\\u%04x", hexValue));
 }

 @Override
 public char consumeCharacterReference() {
 return consumeCharacterReference(null, false);
 }

 @Override
 public Character consumeCharacterReference(Character additionalAllowedCharacter, boolean inAttribute) {
 StringBuilder buffer = new StringBuilder();
 consume("#");
 boolean isHexMode = consumeIgnoreCase("X");
 if (isHexMode) {
 buffer.append("0x");
 consumeHexSequence();
 } else {
 consumeDigitSequence();
 }
 consume(";");
 char result = 0;
 if (buffer.length() > 0) {
 try {
 result = (char) Integer.parseInt(buffer.toString(), 10);
 } catch (NumberFormatException e) {
 // do nothing
 }
 if (result == -1) {
 return replacementChar;
 } else if ((result >= 0xD800 && result <= 0xDFFF) || result > 0x10FFFF) {
 error("Character outside of valid range");
 return replacementChar;
 }
 }
 return result;
 }

 @Override
 public boolean matchesAny(String expected) {
 for (int i = 0; i < expected.length(); i++) {
 if (matches(expected.charAt(i))) {
 continue;
 }
 return false;
 }
 return true;
 }

 @Override
 public void consumeString() {
 StringBuilder buffer = new StringBuilder();
 while (true) {
 char c = current();
 boolean isQuote = c == '\'' || c == '"';
 if (isQuote || isEOF()) {
 input.delete(startPos - buffer.length(), startPos);
 setText(buffer.toString());
 if (c == isEOF()) {
 error("Unexpected end of file (EOF) in input state [%s]", getState());
 }
 break;
 }
 buffer.append(current());
 consume();
 }
 }

 @Override
 public void setText(String text) {
 input.setLength(0);
 input.append(text);
 }

 @Override
 public String toString() {
 return input.toString();
 }

 @Override
 public void mark(int readAheadLimit) {
 // do nothing
 }

 @Override
 public int available() {
 return input.length() - startPos;
 }

 @Override
 public int read(char[] cbuf, int off, int length) {
 int charsToRead = Math.min(length, input.length() - startPos);
 input.getChars(startPos, startPos + charsToRead, cbuf, off);
 startPos += charsToRead;
 return charsToRead;
 }

 @Override
 public boolean ready() {
 return available() > 0;
 }

 @Override
 public void close() {
 // do nothing
 }

 @Override
 public void error(String message) {
 throw new RuntimeException(message);
 }

 private boolean isAlphaNumeric(char c) {
 return Character.isAlphabetic(c) || Character.isDigit(c);
 }

 private boolean isDigit(char c) {
 return Character.isDigit(c);
 }

 private boolean isHexDigit(char c) {
 return (Character.isDigit(c) || Character.isLetter(c)) && (Character.isUpperCase(c) || Character.isLowerCase(c));
 }
}