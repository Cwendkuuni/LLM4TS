import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Error;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 // Basic functionality test
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);

 // Edge cases and exception handling tests
 tokeniser.transition(TokeniserState.Script);
 when(reader.current()).thenReturn('%');
 token = tokeniser.read(); // Should throw an error
 assertEquals(Error.class, token.getClass());
 assertEquals("Unexpected character '%%' in input state [Script]", token.toString());
 when(reader.current()).thenReturn('&');
 token = tokeniser.read(); // Should consume & and return a character reference
 assertEquals(Character.class, token.getClass());
 assertEquals('&', token.charValue());
 when(reader.current()).thenReturn('#');
 token = tokeniser.read(); // Should throw an error
 assertEquals(Error.class, token.getClass());
 assertEquals("Unexpected character '#' in input state [Script]", token.toString());
 }

 @Test
 public void testEmit() {
 // Basic functionality test
 tokeniser.emit(new Character("a"));
 Token expectedToken = new Character("a");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);

 // Edge cases and exception handling tests
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emit(new StartTag("script"));
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emit(new EndTag("script"));
 tokeniser.error("Unexpected EndTag without matching StartTag"); // Should throw an error
 assertNull(tokeniser.read());
 }

 @Test
 public void testEmitString() {
 // Basic functionality test
 tokeniser.emit("Hello World");
 Token expectedToken = new Character("H");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 for (int i = 1; i < "Hello World".length(); i++) {
 expectedToken = new Character(String.valueOf(i < "Hello World".length() ? "Hello World".charAt(i) : '\u0000'));
 actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }
 // Edge cases and exception handling tests
 when(reader.current()).thenReturn('<');
 tokeniser.emit("Hello < World");
 tokeniser.error("Invalid character in string");
 assertNull(tokeniser.read());
 }

 @Test
 public void testEmitCharacter() {
 // Basic functionality test
 tokeniser.emit('a');
 Token expectedToken = new Character("a");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 // Edge cases and exception handling tests
 when(reader.current()).thenReturn('\uFFFD');
 tokeniser.emit('\\uFFFD');
 Token actualToken2 = tokeniser.read();
 Token expectedToken2 = new Character("\uFFFD");
 assertEquals(expectedToken2, actualToken2);
 }

 @Test
 public void testGetState() {
 // Basic functionality test
 assertEquals(TokeniserState.Data, tokeniser.getState());
 // State transition test
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testTransition() {
 // Basic functionality test
 tokeniser.transition(TokeniserState.Script);
 // Edge cases and exception handling tests
 tokeniser.transition(null);
 when(reader.current()).thenReturn('>');
 tokeniser.transition(TokeniserState.TagName);
 assertEquals(TokeniserState.TagName, tokeniser.getState());
 assertEquals("Unexpected '>' in input state [Script]", tokeniser.errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testAdvanceTransition() {
 // Basic functionality test
 tokeniser.advanceTransition(TokeniserState.TagName);
 // Edge cases and exception handling tests
 tokeniser.transition(TokeniserState.Script);
 when(reader.current()).thenReturn('>');
 tokeniser.advanceTransition(TokeniserState.TagName);
 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 assertEquals("Unexpected '>' in input state [Script]", tokeniser.errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 // Basic functionality test
 tokeniser.acknowledgeSelfClosingFlag();
 // Edge cases and exception handling tests
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.Script);
 when(reader.current()).thenReturn('>');
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals(TokeniserState.Script, tokeniser.getState());
 assertEquals("Self closing flag not acknowledged", tokeniser.errors.getErrors().get(0).getMessage());
 }

 @Test
 public void testConsumeCharacterReference() {
 // Basic functionality test
 Character expectedCharacter = new Character("&a;");
 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedCharacter, actualCharacter);
 // Edge cases and exception handling tests
 tokeniser.transition(TokeniserState.TagName);
 when(reader.current()).thenReturn('&');
 Character actualCharacter2 = tokeniser.consumeCharacterReference('a', false);
 assertNull(actualCharacter2);
 tokeniser.transition(TokeniserState.AttributeValue);
 when(reader.current()).thenReturn('&');
 Character actualCharacter3 = tokeniser.consumeCharacterReference(null, true);
 assertNull(actualCharacter3);
 }

 @Test
 public void testCreateTagPending() {
 // Basic functionality test
 tokeniser.createTagPending(true);
 assertEquals(StartTag.class, tokeniser.tagPending.getClass());
 // Edge cases and exception handling tests
 tokeniser.createTagPending(false);
 assertEquals(EndTag.class, tokeniser.tagPending.getClass());
 }

 @Test
 public void testEmitTagPending() {
 // Basic functionality test
 tokeniser.emitTagPending();
 Token expectedToken = new StartTag("tagName");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 // Edge cases and exception handling tests
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.error("Unexpected StartTag without matching EndTag");
 assertNull(tokeniser.read());
 }

 @Test
 public void testCreateCommentPending() {
 // Basic functionality test
 tokeniser.createCommentPending();
 assertEquals(Comment.class, tokeniser.commentPending.getClass());
 // Edge cases and exception handling tests
 tokeniser.emitCommentPending();
 tokeniser.error("Unexpected Comment without matching EndComment");
 assertNull(tokeniser.read());
 }

 @Test
 public void testEmitDoctypePending() {
 // Basic functionality test
 tokeniser.emitDoctypePending();
 Token expectedToken = new Doctype("doctypeName");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 // Edge cases and exception handling tests
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 tokeniser.error("Unexpected Doctype without matching EndDoctype");
 assertNull(tokeniser.read());
 }

 @Test
 public void testCreateTempBuffer() {
 // Basic functionality test
 tokeniser.createTempBuffer();
 // Edge cases and exception handling tests
 tokeniser.emit("data");
 tokeniser.error("Unexpected data after Doctype");
 assertNull(tokeniser.read());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 // Basic functionality test
 tokeniser.emit(new StartTag("div"));
 tokeniser.createTagPending(false);
 Token expectedToken = new EndTag("div");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 // Edge cases and exception handling tests
 tokeniser.emit(new StartTag("span"));
 tokeniser.createTagPending(false);
 assertEquals(Error.class, tokeniser.read().getClass());
 }

 @Test
 public void testError() {
 // Basic functionality test
 tokeniser.error("Unexpected character");
 assertEquals(1, tokeniser.errors.getErrors().size());
 // Edge cases and exception handling tests
 tokeniser.errors.clear();
 tokeniser.transition(TokeniserState.Script);
 when(reader.current()).thenReturn('>');
 tokeniser.error("Unexpected '>' in input state [Script]");
 assertEquals(1, tokeniser.errors.getErrors().size());
 }

 @Test
 public void testEofError() {
 // Basic functionality test
 tokeniser.eofError(TokeniserState.Script);
 assertEquals(1, tokeniser.errors.getErrors().size());
 // Edge cases and exception handling tests
 tokeniser.errors.clear();
 tokeniser.transition(TokeniserState.Script);
 tokeniser.reader.setLength(0);
 tokeniser.eofError(TokeniserState.Script);
 assertEquals(1, tokeniser.errors.getErrors().size());
 }

 @Test
 public void testCharacterReferenceError() {
 // Basic functionality test
 tokeniser.characterReferenceError("Named reference with no numerals");
 assertEquals(1, tokeniser.errors.getErrors().size());
 // Edge cases and exception handling tests
 tokeniser.errors.clear();
 tokeniser.transition(TokeniserState.AttributeValue);
 when(reader.current()).thenReturn("&amp;");
 tokeniser.characterReferenceError("Named reference missing semicolon");
 assertEquals(1, tokeniser.errors.getErrors().size());
 }
}