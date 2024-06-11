import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.read();
 assertEquals("test", tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadEmpty() {
 tokeniser.read();
 assertNull(tokeniser.charBuffer.toString());
 }

 @Test
 public void testReadEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
 }

 @Test
 public void testReadStartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.StartTag, tokeniser.emitPending.type);
 }

 @Test
 public void testReadComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.Comment, tokeniser.emitPending.type);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.Doctype, tokeniser.emitPending.type);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("img", null, true));
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.EndTag, tokeniser.emitPending.type);
 }

 @Test
 public void testReadData() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("test data");
 tokeniser.read();
 assertNotNull(tokeniser.emitPending);
 assertEquals(TokenType.Character, tokeniser.emitPending.type);
 }

 @Test(expected = AssertionError.class)
 public void testReadInvalidStartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new Token.StartTag("test", null, false));
 tokeniser.read();
 }

 @Test
 public void testReadInvalidEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 }

 @Test
 public void testReadInvalidCharacter() {
 tokeniser.transition(TokeniserState.Data);
 reader.mark(1);
 reader.consume('&');
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("#x26");
 tokeniser.read();
 assertEquals('&', tokeniser.charBuffer.charAt(0));
 }

 @Test
 public void testReadCharacterReferenceNamed() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("#amp;");
 tokeniser.read();
 assertEquals('&', tokeniser.charBuffer.charAt(0));
 }

 @Test
 public void testReadCharacterReferenceInvalid() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("#abc");
 tokeniser.read();
 assertNull(tokeniser.emitPending);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.transition(TokeniserState.Data);
 reader.mark(1);
 reader.consume('#');
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testEOFError() {
 reader.mark(1);
 reader.consume('\n'); // consume eof
 tokeniser.read();
 tokeniser.error(TokeniserState.Data);
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testStateTransition() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.transition(TokeniserState.EndTag);
 assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 assertEquals(TokeniserState.EndTag, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }
}