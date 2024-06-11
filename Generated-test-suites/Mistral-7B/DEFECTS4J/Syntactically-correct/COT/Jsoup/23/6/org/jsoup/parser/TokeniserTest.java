import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream(new byte[]{}));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testTokeniserConstructor() {
 // Test the constructor
 Tokeniser tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 // Test reading a character
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(Token.Character.valueOf("A"));
 tokeniser.emitPending();
 assertEquals("A", tokeniser.read().getText());
 }

 @Test
 public void testReadEmptyCharacterSequence() {
 // Test reading an empty character sequence
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertNull(tokeniser.read());
 }

 @Test
 public void testReadSelfClosingTag() {
 // Test reading a self-closing tag
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<img src='image.jpg' />"));
 tokeniser.emitPending();
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertEquals(Token.TagType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testReadStartTagWithoutEndTag() {
 // Test reading a start tag without an end tag
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<div"));
 tokeniser.emitPending();
 assertSame(tokeniser.lastStartTag, tokeniser.read());
 }

 @Test(expected = AssertionError.class)
 public void testReadEndTagWithoutStartTag() {
 // Test reading an end tag without a corresponding start tag
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 tokeniser.emit(Token.EndTag.valueOf("</div>"));
 tokeniser.emitPending();
 }

 @Test
 public void testReadStartTagWithEndTag() {
 // Test reading a start tag with a corresponding end tag
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<div"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.EndTagOpen);
 tokeniser.emit(Token.EndTag.valueOf("</div>"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertSame(tokeniser.lastStartTag, tokeniser.read());
 assertSame(tokeniser.read(), tokeniser.read());
 }

 @Test
 public void testReadString() {
 // Test reading a string
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 tokeniser.emit("Hello, World!");
 tokeniser.emitPending();
 assertEquals("Hello, World!", tokeniser.read().getText());
 }

 @Test
 public void testReadCharacterReference() throws IOException {
 // Test reading a character reference
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 reader.setCurrent('&');
 reader.mark();
 tokeniser.consumeCharacterReference(null, false);
 reader.unconsume();
 reader.setCurrent('a');
 reader.mark();
 tokeniser.consumeCharacterReference(null, false);
 reader.unconsume();
 reader.setCurrent(';');
 reader.mark();
 tokeniser.consumeCharacterReference(null, false);
 reader.unconsume();
 assertEquals(Character.toChars(97)[0], tokeniser.read().getText().charAt(0));
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidCharacterReference() throws IOException {
 // Test reading an invalid character reference
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 reader.setCurrent('&');
 reader.mark();
 tokeniser.consumeCharacterReference(Character.MAX_VALUE, false);
 reader.unconsume();
 reader.setCurrent(';');
 reader.mark();
 tokeniser.consumeCharacterReference(Character.MAX_VALUE, false);
 reader.unconsume();
 }

 @Test(expected = AssertionError.class)
 public void testReadNamedCharacterReferenceWithSemicolon() throws IOException {
 // Test reading a named character reference with a semicolon
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 reader.setCurrent('&');
 reader.mark();
 tokeniser.consumeCharacterReference("ampersand", true);
 reader.unconsume();
 reader.setCurrent(';');
 reader.mark();
 tokeniser.consumeCharacterReference("ampersand", true);
 reader.unconsume();
 }

 @Test(expected = AssertionError.class)
 public void testReadNamedCharacterReferenceWithoutSemicolon() throws IOException {
 // Test reading a named character reference without a semicolon
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 reader.setCurrent('&');
 reader.mark();
 tokeniser.consumeCharacterReference("ampersand", false);
 reader.unconsume();
 }

 @Test
 public void testCreateTagPending() {
 // Test creating a tag pending
 tokeniser.createTagPending(true);
 assertEquals(Token.TagType.StartTag, tokeniser.tagPending.getType());
 }

 @Test
 public void testEmitTagPending() {
 // Test emitting a tag pending
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertEquals(tokeniser.tagPending, tokeniser.read());
 }

 @Test
 public void testCreateCommentPending() {
 // Test creating a comment pending
 tokeniser.createCommentPending();
 assertEquals(Token.TagType.Comment, tokeniser.commentPending.getType());
 }

 @Test
 public void testEmitCommentPending() {
 // Test emitting a comment pending
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertEquals(tokeniser.commentPending, tokeniser.read());
 }

 @Test
 public void testCreateDoctypePending() {
 // Test creating a doctype pending
 tokeniser.createDoctypePending();
 assertEquals(Token.TagType.Doctype, tokeniser.doctypePending.getType());
 }

 @Test
 public void testEmitDoctypePending() {
 // Test emitting a doctype pending
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 assertEquals(tokeniser.doctypePending, tokeniser.read());
 }

 @Test
 public void testCreateTempBuffer() {
 // Test creating a temp buffer
 tokeniser.createTempBuffer();
 assertEquals(tokeniser.dataBuffer.length(), 0);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 // Test checking if the current token is an appropriate end tag token
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<div"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.EndTagOpen);
 tokeniser.emit(Token.EndTag.valueOf("</div>"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_false() {
 // Test checking if the current token is not an appropriate end tag token
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<div"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 // Test getting the appropriate end tag name
 tokeniser.transition(TokeniserState.StartTagOpen);
 tokeniser.emit(Token.StartTag.valueOf("<div"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.EndTagOpen);
 tokeniser.emit(Token.EndTag.valueOf("</div>"));
 tokeniser.emitPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 assertEquals("div", tokeniser.appropriateEndTagName());
 }

 @Test
 public void testError() {
 // Test throwing an error
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testEofError() {
 // Test throwing an EOF error
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test
 public void testCharacterReferenceError() {
 // Test throwing a character reference error
 tokeniser.characterReferenceError("numeric reference with no numerals");
 }

 @Test
 public void testErrorMessage() {
 // Test getting the error message
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPending();
 tokeniser.error(TokeniserState.Data);
 assertEquals("Unexpected character '\\u0000' in input state [Data]", tokeniser.errors.get(0).getMessage());
 }
}