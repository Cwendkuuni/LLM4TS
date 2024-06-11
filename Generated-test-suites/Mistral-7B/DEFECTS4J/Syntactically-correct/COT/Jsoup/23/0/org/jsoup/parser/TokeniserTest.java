import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader characterReader = new CharacterReader();
 private ParseErrorList parseErrorList = new ParseErrorList();
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testRead_InitialState() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_Character() {
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertEquals(new Character("A"), token);
 }

 @Test
 public void testRead_SelfClosingTag() {
 tokeniser.emit(new StartTag("img", null, null, null, null, true));
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.advanceTransition(TokeniserState.SelfClosingStartTag);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_EndTag() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.emit(new EndTag("div"));
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_String() {
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('B');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('C');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit(' '); // whitespace
 tokeniser.emit('D');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('E');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertEquals(new Token.Text("ABCD"), token);
 }

 @Test
 public void testRead_ControlCharacter() {
 tokeniser.emit((char) 0x02); // form feed
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test(expected = AssertionError.class)
 public void testRead_UnexpectedCharacter() {
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 fail("Unexpected character > should have thrown an error.");
 }

 @Test
 public void testRead_CharacterReference() {
 tokeniser.emit('&');
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.advanceTransition(TokeniserState.CharacterReference);

 Character c = tokeniser.consumeCharacterReference('a', false);
 assertNull(c);

 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 c = tokeniser.consumeCharacterReference('#', false);
 assertNull(c);

 tokeniser.consume(';'); // consume semicolon
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 c = tokeniser.consumeCharacterReference("amp", false);
 assertNull(c);

 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 c = tokeniser.consumeCharacterReference("apos", false);
 assertEquals(new Character("'"), c);

 tokeniser.consume(';'); // consume semicolon
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 c = tokeniser.consumeCharacterReference("quot", false);
 assertEquals(new Character("\""), c);

 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 }

 @Test
 public void testRead_CharacterReference_InvalidCharacter() {
 tokeniser.emit('&');
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.advanceTransition(TokeniserState.CharacterReference);

 Character c = tokeniser.consumeCharacterReference('x', false);
 assertNull(c);

 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 c = tokeniser.consumeCharacterReference("xxx", false);
 assertNull(c);

 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 }

 @Test
 public void testRead_CharacterReference_InvalidNamedEntity() {
 tokeniser.emit('&');
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.advanceTransition(TokeniserState.CharacterReference);

 tokeniser.consumeLetterSequence();
 tokeniser.consume(';');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 tokeniser.expectParseError();
 }

 @Test
 public void testRead_CharacterReference_MissingSemicolon() {
 tokeniser.emit('&');
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.advanceTransition(TokeniserState.CharacterReference);

 tokeniser.consumeLetterSequence();
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.advanceTransition(TokeniserState.CharacterReference);

 tokeniser.expectParseError();
 }

 @Test
 public void testRead_Comment() {
 tokeniser.emit('<!--');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.emit('--');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_Doctype() {
 tokeniser.emit('<!DOCTYPE');
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.emit('html');
 tokeniser.transition(TokeniserState.DoctypePublic);
 tokeniser.advanceTransition(TokeniserState.DoctypePublic);
 tokeniser.emit('>');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_EndOfFile() {
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('B');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('C');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('D');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('E');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('\u0000'); // null character, end of file
 tokeniser.transition(TokeniserState.EndOfFile);
 tokeniser.advanceTransition(TokeniserState.EndOfFile);

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.emitTagPending();

 tokeniser.expect(new StartTag("div"));
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.emit('<!--');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.emitCommentPending();

 tokeniser.expect(new Comment());
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.emit('<!DOCTYPE');
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.emitDoctypePending();

 tokeniser.expect(new Doctype());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('B');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('C');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('D');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.emit('E');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);

 String data = tokeniser.dataBuffer.toString();
 assertEquals("ABCDE", data);
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.emit(new EndTag("div"));
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 tokeniser.emit(new StartTag("p"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);

 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_InvalidEndTag() {
 tokeniser.emit(new StartTag("div"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 tokeniser.emit(new EndTag("p"));
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.advanceTransition(TokeniserState.EndTag);
 tokeniser.emit(new StartTag("p"));
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.advanceTransition(TokeniserState.StartTag);

 assertFalse(tokeniser.isAppropriateEndTagToken());
 }
}