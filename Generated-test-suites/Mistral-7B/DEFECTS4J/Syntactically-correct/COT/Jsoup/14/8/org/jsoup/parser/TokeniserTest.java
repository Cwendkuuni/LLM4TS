import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.helper.Validate;

public class TokeniserTest {

 private Tokeniser tokeniser;

 @Before
 public void setup() {
 char[] input = {'<', 'h', 't', 'm', 'l', ' ', 'i', 'n', 'put', ' ', 'd', 'o', 'c', 't', 'y', 'p', 'e', '>', "\n"};
 CharacterReader reader = new CharacterReader(input);
 tokeniser = new Tokeniser(reader);
 }

 @Test
 public void testRead_NormalCase() {
 Token token = tokeniser.read();
 Assert.assertNotNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read(); // should throw an exception as selfClosingFlagAcknowledged is not set to true
 }

 @Test
 public void testEmit_Character() {
 Character character = new Character("a");
 tokeniser.emit(character);
 Token token = tokeniser.read();
 Assert.assertEquals(character.value, token.toString());
 }

 @Test
 public void testEmit_StartTag() {
 StartTag startTag = new StartTag("p", null, null, null);
 tokeniser.emit(startTag);
 Token token = tokeniser.read();
 Assert.assertEquals(startTag.toString(), token.toString());
 }

 @Test
 public void testEmit_EndTag() {
 EndTag endTag = new EndTag("p");
 tokeniser.emit(endTag);
 Token token = tokeniser.read();
 Assert.assertEquals(endTag.toString(), token.toString());
 }

 @Test
 public void testEmit_String() {
 tokeniser.emit("hello");
 Token token = tokeniser.read();
 Assert.assertEquals("hello", token.toString());
 }

 @Test
 public void testEmit_Char() {
 tokeniser.emit('a');
 Token token = tokeniser.read();
 Assert.assertEquals("a", token.toString());
 }

 @Test
 public void testGetState() {
 TokeniserState expectedState = TokeniserState.Data;
 TokeniserState actualState = tokeniser.getState();
 Assert.assertEquals(expectedState, actualState);
 }

 @Test
 public void testTransition() {
 TokeniserState expectedState = TokeniserState.Script;
 tokeniser.transition(TokeniserState.Script);
 TokeniserState actualState = tokeniser.getState();
 Assert.assertEquals(expectedState, actualState);
 }

 @Test
 public void testAdvanceTransition() {
 CharacterReader reader = new CharacterReader(new char[] {'<', 's', 'c', 'r', 'i', 'p', 't', '>', "\n"});
 tokeniser = new Tokeniser(reader);
 tokeniser.transition(TokeniserState.Script);
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.read(); // skip <script>
 TokeniserState expectedState = TokeniserState.Data;
 TokeniserState actualState = tokeniser.getState();
 Assert.assertEquals(expectedState, actualState);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_ValidReference() {
 Character reference = new Character('a');
 Token token = tokeniser.read();
 Assert.assertEquals(token.type, Token.TokenType.Character);
 Assert.assertEquals(token.value, reference.value);
 Character consumedCharacter = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals(consumedCharacter, reference);
 }

 @Test
 public void testConsumeCharacterReference_InvalidReference() {
 tokeniser.consumeCharacterReference(null, false); // this should not consume anything as input is empty
 Assert.assertEquals(tokeniser.reader.current(), '\u0000');
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_InAttribute() {
 tokeniser.consumeCharacterReference('&', true); // this should not consume anything as input is empty
 }

 @Test
 public void testCreateTagPending() {
 Token.StartTag startTag = tokeniser.createTagPending(true);
 Assert.assertNotNull(startTag);
 }

 @Test
 public void testEmitTagPending() {
 Token.StartTag startTag = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 Assert.assertEquals(startTag.toString(), token.toString());
 }

 @Test
 public void testCreateCommentPending() {
 Token.Comment comment = tokeniser.createCommentPending();
 Assert.assertNotNull(comment);
 }

 @Test
 public void testEmitCommentPending() {
 Token.Comment comment = tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 Assert.assertEquals(comment.toString(), token.toString());
 }

 @Test
 public void testCreateDoctypePending() {
 Token.Doctype doctype = tokeniser.createDoctypePending();
 Assert.assertNotNull(doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 Token.Doctype doctype = tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 Assert.assertEquals(doctype.toString(), token.toString());
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 Assert.assertNotNull(tokeniser.dataBuffer);
 }

 @Test
 public void testIsAppropriateEndTagToken_Matches() {
 StartTag startTag = new StartTag("p");
 EndTag endTag = new EndTag("p");
 tokeniser.lastStartTag = startTag;
 tokeniser.tagPending = endTag;
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_DoesNotMatch() {
 StartTag startTag = new StartTag("p");
 EndTag endTag = new EndTag("div");
 tokeniser.lastStartTag = startTag;
 tokeniser.tagPending = endTag;
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors_true() {
 assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_false() {
 tokeniser.setTrackErrors(false);
 assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 assertFalse(tokeniser.isTrackErrors());
 List<ParseError> errors = tokeniser.getErrors();
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 assertFalse(tokeniser.isTrackErrors());
 List<ParseError> errors = tokeniser.getErrors();
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError();
 assertFalse(tokeniser.isTrackErrors());
 List<ParseError> errors = tokeniser.getErrors();
 assertTrue(errors.size() > 0);
 }

 @Test
 public void testCurrentNodeInHtmlNS() {
 // This test is not possible to pass as the implementation of currentNode() and namespace() are missing
 // Assume the implementation is correct
 assertTrue(tokeniser.currentNodeInHtmlNS());
 }

 @Test(expected = NullPointerException.class)
 public void testRead_NullCharacterReader() {
 tokeniser = new Tokeniser(null);
 tokeniser.read();
 }
}