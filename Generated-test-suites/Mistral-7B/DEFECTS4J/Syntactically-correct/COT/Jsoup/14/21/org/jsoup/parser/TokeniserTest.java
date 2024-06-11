import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;

 @Before
 public void setUp() {
 charReader = new CharacterReader("Test Input");
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testRead_whenCalledFirstTime_returnsCharacterToken() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_whenCalledMultipleTimes_returnsCorrectOrderOfTokens() {
 List<Token> expectedTokens = new ArrayList<>();
 expectedTokens.add(new Character("T"));
 expectedTokens.add(new Character("e"));
 expectedTokens.add(new Character("s"));
 expectedTokens.add(new Character("t"));
 expectedTokens.add(new StartTag("div"));
 expectedTokens.add(new Character(' '));
 expectedTokens.add(new Character("I"));
 expectedTokens.add(new Character("s"));
 expectedTokens.add(new Character(" "));
 expectedTokens.add(new Character("t"));
 expectedTokens.add(new Character(" "));
 expectedTokens.add(new Character("s"));
 expectedTokens.add(new Character(" "));
 expectedTokens.add(new Character('>'));

 for (int i = 0; i < expectedTokens.size(); i++) {
 Token token = tokeniser.read();
 assertEquals(expectedTokens.get(i), token);
 }
 }

 @Test
 public void testEmit_whenCalledWithCharacterToken_emitsCharacterToken() {
 tokeniser.emit(new Character('T'));
 Token expectedToken = new Character("T");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testEmit_whenCalledWithStartTagToken_emitsStartTagToken() {
 tokeniser.emit(new StartTag("div"));
 Token expectedToken = new StartTag("div");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testEmitPending_whenCalledWithCharacterToken_setsPendingTokenAsCharacter() {
 tokeniser.emitPending = new Character('T');
 tokeniser.read();
 Token expectedToken = new Character("T");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testEmitPending_whenCalledWithStartTagToken_setsPendingTokenAsStartTag() {
 tokeniser.emitPending = new StartTag("div");
 tokeniser.read();
 Token expectedToken = new StartTag("div");
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken, actualToken);
 }

 @Test
 public void testError_whenCalledWithInvalidCharacterReference_addsErrorToErrorList() {
 tokeniser.error("Unexpected character in input");
 assertEquals(1, tokeniser.errors.size());
 }

 @Test
 public void testError_whenCalledWithInvalidCharacterReference_throwsException() {
 try {
 tokeniser.consumeCharacterReference('X', true);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConsumeCharacterReference_whenCalledWithValidCharacterReference_returnsCorrespondingCharacter() {
 Character expectedCharacter = 'A';
 Tokeniser tokeniser = new Tokeniser(new CharacterReader("&#65;"));
 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedCharacter, actualCharacter);
 }

 @Test
 public void testConsumeCharacterReference_whenCalledWithNamedCharacterReference_returnsCorrespondingCharacter() {
 Character expectedCharacter = '&deg;';
 Tokeniser tokeniser = new Tokeniser(new CharacterReader("&deg;"));
 Character actualCharacter = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedCharacter, actualCharacter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_whenCalledWithInvalidNamedCharacterReference_throwsException() {
 tokeniser.consumeCharacterReference("InvalidEntity", false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_whenCalledWithCharacterReferenceOutsideAttribute_throwsException() {
 characterReader.setCurrent('&');
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testCreateTagPending_whenCalled_createsNewTagPendingObject() {
 Token.StartTag expectedTag = new Token.StartTag();
 Token.StartTag actualTag = tokeniser.createTagPending(true);
 assertSame(expectedTag, actualTag);
 }

 @Test
 public void testEmitTagPending_whenCalled_emitsTagPendingObject() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token expectedTag = tokeniser.read();
 Token actualTag = tokeniser.read();
 assertEquals(expectedTag, actualTag);
 }

 @Test
 public void testEmitCommentPending_whenCalled_emitsCommentPendingObject() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token expectedComment = tokeniser.read();
 Token actualComment = tokeniser.read();
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testEmitDoctypePending_whenCalled_emitsDoctypePendingObject() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token expectedDoctype = tokeniser.read();
 Token actualDoctype = tokeniser.read();
 assertEquals(expectedDoctype, actualDoctype);
 }

 @Test
 public void testIsAppropriateEndTagToken_whenCalledWithSameStartTagAndEndTag_returnsTrue() {
 tokeniser.createTagPending(true);
 tokeniser.createTagPending(false);
 tokeniser.transition(TokeniserState.StartTagEnd);
 tokeniser.emitTagPending();
 tokeniser.emitTagPending();
 Boolean expectedBoolean = true;
 Boolean actualBoolean = tokeniser.isAppropriateEndTagToken();
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testIsAppropriateEndTagToken_whenCalledWithDifferentStartTagAndEndTag_returnsFalse() {
 tokeniser.createTagPending(true);
 tokeniser.createTagPending(false);
 tokeniser.transition(TokeniserState.StartTagEnd);
 tokeniser.emitTagPending();
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Boolean expectedBoolean = false;
 Boolean actualBoolean = tokeniser.isAppropriateEndTagToken();
 assertEquals(expectedBoolean, actualBoolean);
 }
}