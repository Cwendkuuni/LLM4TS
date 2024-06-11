import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 characterReader = new CharacterReader("input.txt");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 assertTrue(token == null);
 assertEquals(0, errors.size());
 }

 @Test
 public void testRead_Character() {
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 token = tokeniser.read();
 assertEquals(new Token.Character("A"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testRead_EndTag() {
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 tokeniser.acknowledgeSelfClosingFlag();
 token = tokeniser.read();
 assertEquals(new Token.EndTag("div"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testRead_StartTag() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 token = tokeniser.read();
 assertEquals(new Token.StartTag("tag"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testRead_Error() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.error(TokeniserState.Misc);
 token = tokeniser.read();
 assertTrue(token == null);
 assertEquals(1, errors.size());
 }

 @Test
 public void testRead_EOF() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.acknowledgeSelfClosingFlag();
 characterReader.setEOF();
 token = tokeniser.read();
 assertTrue(token == null);
 assertEquals(1, errors.size());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testEmit() {
 Token startTag = new Token.StartTag("tag");
 tokeniser.emit(startTag);
 token = tokeniser.read();
 assertEquals(startTag, token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmit_Character() {
 charBuffer = new StringBuilder();
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit('B');
 tokeniser.emit('C');
 token = tokeniser.read();
 assertEquals(new Token.Character("ABC"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmit_String() {
 tokeniser.emit("string");
 token = tokeniser.read();
 assertEquals(new Token.Character("string"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmit_Error() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.error(TokeniserState.Misc);
 tokeniser.emit('A');
 token = tokeniser.read();
 assertTrue(token == null);
 assertEquals(1, errors.size());
 }

 @Test
 public void testEmit_EOF() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.acknowledgeSelfClosingFlag();
 characterReader.setEOF();
 tokeniser.emit('A');
 token = tokeniser.read();
 assertTrue(token == null);
 assertEquals(1, errors.size());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testEmitPendingToken() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 tokeniser.emitPendingToken();
 token = tokeniser.read();
 assertEquals(new Token.StartTag("tag"), token);
 assertEquals(0, errors.size());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testEmitPendingToken_EndTag() {
 tokeniser.transition(TokeniserState.Misc);
 tokeniser.createTagPending(false);
 tokeniser.emit('>');
 tokeniser.emitPendingToken();
 token = tokeniser.read();
 assertEquals(new Token.EndTag("tag"), token);
 assertEquals(0, errors.size());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testConsumeCharacterReference() {
 Character ref = tokeniser.consumeCharacterReference('&', false);
 assertEquals(ref, null);
 assertEquals(0, errors.size());
 }

 @Test
 public void testConsumeCharacterReference_Hex() {
 characterReader.mark();
 characterReader.consume('&');
 characterReader.consume('x');
 characterReader.consume('41');
 characterReader.consume(';');
 Character ref = tokeniser.consumeCharacterReference(null, false);
 assertEquals(ref, '\u0041'); // A
 assertEquals(0, errors.size());
 characterReader.rewindToMark();
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 characterReader.mark();
 characterReader.consume('&');
 characterReader.consume('a');
 characterReader.consume('m');
 characterReader.consume('p');
 characterReader.consume(';');
 Character ref = tokeniser.consumeCharacterReference(null, false);
 assertEquals(ref, '&');
 assertEquals(0, errors.size());
 characterReader.rewindToMark();
 }

 @Test
 public void testConsumeCharacterReference_Error() {
 characterReader.mark();
 characterReader.consume('&');
 characterReader.consume('a');
 characterReader.consume('m');
 characterReader.consume('p');
 characterReader.consume(';');
 characterReader.consume('x');
 tokeniser.consumeCharacterReference(null, false);
 Character ref = tokeniser.consumeCharacterReference(null, false);
 assertTrue(ref == replacementChar);
 assertEquals(1, errors.size());
 characterReader.rewindToMark();
 }

 @Test
 public void testCreateTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.transition(TokeniserState.StartTag);
 token = tokeniser.read();
 assertEquals(new Token.StartTag("tag"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(false);
 tokeniser.emit('>');
 tokeniser.emitPendingToken();
 token = tokeniser.read();
 assertEquals(new Token.EndTag("tag"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals(new Token.StartTag("tag"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testEmitTagPending_EndTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals(new Token.EndTag("tag"), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertEquals(new Token.Doctype(), token);
 assertEquals(0, errors.size());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testIsAppropriateEndTagToken_Error() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.StartTag);
 assertFalse(tokeniser.isAppropriateEndTagToken());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testAppropriateEndTagName() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 assertEquals("tag", tokeniser.appropriateEndTagName());
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testAppropriateEndTagName_Error() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emit('>');
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error(TokeniserState.StartTag);
 assertNull(tokeniser.appropriateEndTagName());
 tokeniser = new Tokeniser(characterReader, errors);
 }
}