import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private Tokeniser createTestTokeniser() {
 return new Tokeniser(new MockCharacterReader(), new ArrayList<>());
 }

 @Test
 public void testRead_EmptyInput() {
 Tokeniser tokeniser = createTestTokeniser();
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_Character() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.emit("A");
 tokeniser.emit("B");
 tokeniser.emit("C");

 tokeniser.reader.mark();
 tokeniser.reader.consume('A');

 Token token = tokeniser.read();
 assertEquals("Character", token.type.toString(), token.getClass().getSimpleName());
 assertEquals("A", token.characters[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_CharacterPending() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.isEmitPending = true;
 tokeniser.emitPending = new Token.Character("A");

 tokeniser.reader.mark();
 tokeniser.reader.consume('B');

 tokeniser.read();
 }

 @Test
 public void testEmit() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.emit("A");

 Token token = tokeniser.read();
 Token expectedToken = new Token.Character("A");
 assertEquals(expectedToken, token);
 }

 @Test
 public void testTransitionToDataState() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test(expected = IllegalStateException.class)
 public void testTransitionToDataStateWhileReading() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.emit("&#x1F600;");
 Token token = tokeniser.read();
 assertEquals("Character", token.type.toString(), token.getClass().getSimpleName());
 assertEquals("\uD83C\uDF00", token.characters[0].toString());
 }

 @Test
 public void testConsumeCharacterReference_Invalid() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.emit("&#x1F600;");
 tokeniser.reader.mark();
 tokeniser.reader.consume('&');

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testCreateTagPending() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.createTagPending(true);
 Tag tag = tokeniser.tagPending;
 assertNotNull(tag);
 assertEquals(Token.TokenType.StartTag, tag.type);
 }

 @Test
 public void testCreateTagPending_EndTag() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.createTagPending(false);
 Tag tag = tokeniser.tagPending;
 assertNotNull(tag);
 assertEquals(Token.TokenType.EndTag, tag.type);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitTagPending_NotPending() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.emitTagPending();
 }

 @Test
 public void testEmitTagPending_PendingStartTag() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Token token = tokeniser.read();
 assertEquals("StartTag", token.type.toString(), token.getClass().getSimpleName());
 }

 @Test
 public void testEmitTagPending_PendingEndTag() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();

 Token token = tokeniser.read();
 assertEquals("EndTag", token.type.toString(), token.getClass().getSimpleName());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_InAttribute() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.createTagPending(true);
 tokeniser.emit("name=\"value\"");
 tokeniser.consumeCharacterReference('x', true);
 }

 @Test
 public void testError_UnexpectedCharacter() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error("Unexpected character 'X' in input state [Data]");
 }

 @Test
 public void testError_EOF() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError("Unexpectedly reached end of file (EOF) in input state [Data]");
 }

 @Test
 public void testCharacterReferenceError_InvalidReference() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.characterReferenceError("character reference with no numerals");
 }

 @Test
 public void testCharacterReferenceError_MissingSemicolon() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.mark();
 tokeniser.reader.consume("&foo;");
 tokeniser.reader.advance();
 tokeniser.characterReferenceError("missing semicolon");
 }

 @Test
 public void testCharacterReferenceError_InvalidNamedReference() {
 Tokeniser tokeniser = createTestTokeniser();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.consumeLetterSequence("FOO");
 tokeniser.reader.mark();
 tokeniser.reader.consume(';');
 tokeniser.characterReferenceError("invalid named reference 'FOO'");
 }
}