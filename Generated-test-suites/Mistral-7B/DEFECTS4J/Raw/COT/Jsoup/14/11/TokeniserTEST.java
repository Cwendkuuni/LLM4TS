import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader("test.html"); // Assuming a valid CharacterReader implementation
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testRead_ReturnsExpectedToken() {
 Token expectedToken = new Token.Character("A");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testEmit_EmitsExpectedToken() {
 Character charToken = new Character("B");
 tokeniser.emit(charToken);
 Token expectedToken = new Token.Character("B");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testEmit_EmitsStartTag() {
 StartTag startTag = new StartTag("div", null, null, null, null, false);
 tokeniser.emit(startTag);
 Token expectedToken = startTag;
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testConsumeCharacterReference_ValidCharacterReference() {
 Character expectedToken = new Character('é');
 tokeniser.emit("&#233;");
 Character actualToken = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference() {
 Character expectedToken = Tokeniser.replacementChar;
 tokeniser.emit("&#x10000;");
 Character actualToken = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReferenceWithAttribute() {
 tokeniser.emit("class=\"test\" ");
 tokeniser.emit("&#x10000;");
 tokeniser.consume(1); // consume space
 Character expectedToken = Tokeniser.replacementChar;
 Character actualToken = tokeniser.consumeCharacterReference(null, true);
 assertEquals(expectedToken, actualToken);
 }

 @Test(expected = AssertionError.class)
 public void testConsumeCharacterReference_InvalidCharacterReferenceMissingSemicolon() {
 tokeniser.emit("&amp;");
 tokeniser.consumeCharacterReference(null, false); // consume "&"
 tokeniser.consumeCharacterReference(null, false); // try to consume "amp"
 }

 @Test
 public void testCreateTagPending_ReturnExpectedTag() {
 StartTag startTag = tokeniser.createTagPending(true);
 assertEquals(startTag.getClass(), Token.StartTag.class);
 EndTag endTag = tokeniser.createTagPending(false);
 assertEquals(endTag.getClass(), Token.EndTag.class);
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsTrueForCorrectEndTag() {
 StartTag startTag = new StartTag("div", null, null, null, null, false);
 EndTag endTag = new EndTag("div");
 tokeniser.emit(startTag);
 tokeniser.emit(endTag);
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_ReturnsFalseForIncorrectEndTag() {
 StartTag startTag = new StartTag("div", null, null, null, null, false);
 EndTag endTag = new EndTag("p");
 tokeniser.emit(startTag);
 tokeniser.emit(endTag);
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testError_AddsParseErrorToList() {
 ParseError expectedError = new ParseError("Expected 'EOF' but was 'A'", 1, TokeniserState.Data, 1);
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, tokeniser.getErrors().size());
 assertEquals(expectedError, tokeniser.getErrors().get(0));
 }

 @Test
 public void testEofError_AddsParseErrorToList() {
 ParseError expectedError = new ParseError("Unexpectedly reached end of file (EOF)", null, TokeniserState.Data, -1);
 tokeniser.eofError(TokeniserState.Data);
 assertEquals(1, tokeniser.getErrors().size());
 assertEquals(expectedError, tokeniser.getErrors().get(0));
 }

 @Test
 public void testCharacterReferenceError_AddsParseErrorToList() {
 tokeniser.emit("&");
 tokeniser.characterReferenceError();
 assertEquals(1, tokeniser.getErrors().size());
 assertTrue(tokeniser.getErrors().get(0).getMessage().contains("Invalid character reference"));
 }
}