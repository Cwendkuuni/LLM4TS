import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("".getBytes()));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testConstructor() {
 tokeniser = new Tokeniser(reader);
 assertNotNull(tokeniser);
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorNullReader() {
 tokeniser = new Tokeniser(null);
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 assertNotNull(token);
 }

 @Test
 public void testReadEmptyStream() {
 tokeniser.reader.consume(0);
 tokeniser.reader.consume(0); // consume initial two positions (EOF)
 tokeniser.read();
 assertEquals(Token.TokenType.EndOfFile, tokeniser.getState().type);
 }

 @Test
 public void testEmit() {
 Token token = new Token.Character('A');
 tokeniser.emit(token);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals('A', token.content);
 }

 @Test
 public void testEmitTag() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.read();
 Token tag = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tag.type);
 }

 @Test
 public void testEmitEndTag() {
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.read();
 Token tag = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, tag.type);
 }

 @Test
 public void testEmitComment() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 tokeniser.read();
 Token comment = tokeniser.read();
 assertEquals(Token.TokenType.Comment, comment.type);
 }

 @Test
 public void testEmitDoctype() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 tokeniser.read();
 Token doctype = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, doctype.type);
 }

 @Test
 public void testConsumeCharacterReference() {
 tokeniser.consumeCharacterReference('&');
 assertEquals(replacementChar, tokeniser.read().content.charAt(0));
 }

 @Test
 public void testConsumeCharacterReferenceHex() {
 tokeniser.consumeCharacterReference('#', 'X');
 assertEquals('\u0026', tokeniser.read().content.charAt(0));
 }

 @Test
 public void testConsumeCharacterReferenceNamed() {
 tokeniser.consumeCharacterReference("amp;");
 assertEquals('&', tokeniser.read().content.charAt(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceInvalid() {
 tokeniser.consumeCharacterReference('#');
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testReadWithErrorsTracked() {
 tokeniser.trackErrors(false);
 tokeniser.reader.consume('A');
 tokeniser.reader.consume('B');
 tokeniser.reader.consume('C'); // EOF
 tokeniser.read();
 assertEquals(Token.TokenType.EndOfFile, tokeniser.getState().type);
 assertEquals(0, errors.size());

 tokeniser.trackErrors(true);
 tokeniser.reader.consume('A');
 tokeniser.reader.consume('B');
 tokeniser.reader.consume('C'); // EOF
 tokeniser.read();
 assertEquals(Token.TokenType.EndOfFile, tokeniser.getState().type);
 assertEquals(1, errors.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithErrorsNotTrackedAndEndOfFile() {
 tokeniser.trackErrors(false);
 tokeniser.reader.consume('A');
 tokeniser.reader.consume('B');
 tokeniser.reader.consume('C'); // EOF
 tokeniser.read();
 }

 @Test
 public void testReadWithErrorsTrackedAndUnexpectedCharacter() {
 tokeniser.trackErrors(true);
 tokeniser.reader.consume('A');
 tokeniser.reader.consume('B');
 tokeniser.reader.consume('X'); // unexpected character
 tokeniser.read();
 assertEquals(Token.TokenType.Error, tokeniser.read().type);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTagPendingInvalidArgument() {
 tokeniser.createTagPending(-1);
 }

 @Test
 public void testCreateTagPendingNull() {
 tokeniser.createTagPending(null);
 }

}