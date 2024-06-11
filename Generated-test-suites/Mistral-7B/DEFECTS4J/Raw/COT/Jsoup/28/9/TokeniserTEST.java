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
import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private CharacterReader characterReader;

 @Before
 public void setup() {
 charReader = new CharacterReader("Test input stream");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testRead() {
 Token token = tokeniser.read();
 Assert.assertNotNull(token);
 }

 @Test(expected = AssertionError.class)
 public void testReadUnexpectedError() {
 tokeniser.error("Unexpected error");
 Token token = tokeniser.read();
 }

 @Test
 public void testEmit() {
 Token token = new Token.Character("A");
 tokeniser.emit(token);
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(token, emittedToken);
 }

 @Test
 public void testEmitStartTag() {
 Token startTag = new Token.StartTag("div", null);
 tokeniser.emit(startTag);
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(startTag, emittedToken);
 }

 @Test
 public void testEmitEndTag() {
 Token endTag = new Token.EndTag("div");
 tokeniser.emit(endTag);
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(endTag, emittedToken);
 }

 @Test
 public void testEmitString() {
 tokeniser.emit("Test String");
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(new Token.Text("Test String"), emittedToken);
 }

 @Test
 public void testEmitCharacter() {
 tokeniser.emit('A');
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(new Token.Character("A"), emittedToken);
 }

 @Test
 public void testGetState() {
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Script);
 TokeniserState newState = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Script, newState);
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.advanceTransition(TokeniserState.Script);
 tokeniser.advanceTransition(TokeniserState.Data);
 TokeniserState newState = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, newState);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReferenceNull() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNull(character);
 }

 @Test
 public void testConsumeCharacterReferenceValid() {
 Character character = tokeniser.consumeCharacterReference('&', false);
 Assert.assertNull(character);
 character = tokeniser.consumeCharacterReference("A", false);
 Assert.assertEquals(new Character("A"), character);
 }

 @Test
 public void testConsumeCharacterReferenceInAttribute() {
 characterReader.mark();
 characterReader.consumeLetterThenDigitSequence();
 characterReader.unconsume();
 Assert.assertFalse(tokeniser.consumeCharacterReference(null, true).isPresent());
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 Assert.assertNotNull(tag);
 Assert.assertTrue(tag instanceof StartTag);
 }

 @Test
 public void testEmitTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token emittedToken = tokeniser.read();
 Assert.assertEquals(tag, emittedToken);
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Token comment = tokeniser.read();
 Assert.assertEquals(new Comment(), comment);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token emittedComment = tokeniser.read();
 Assert.assertEquals(new Comment(), emittedComment);
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Token doctype = tokeniser.read();
 Assert.assertEquals(new Doctype(), doctype);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token emittedDoctype = tokeniser.read();
 Assert.assertEquals(new Doctype(), emittedDoctype);
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser.createTempBuffer();
 Assert.assertNotNull(tokeniser.dataBuffer);
 }

 @Test
 public void testIsAppropriateEndTagTokenTrue() {
 Token startTag = new Token.StartTag("div", null);
 Token endTag = new Token.EndTag("div");
 tokeniser.emit(startTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenFalse() {
 Token startTag = new Token.StartTag("div", null);
 tokeniser.emit(startTag);
 tokeniser.createTagPending(false);
 tokeniser.createTagPending(false); // different tag
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test(expected = AssertionError.class)
 public void testError() {
 tokeniser.error("Unexpected error");
 Token token = tokeniser.read();
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 Token token = tokeniser.read();
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError("Invalid character reference");
 Token token = tokeniser.read();
 }

 @Test
 public void testCurrentNodeInHtmlNS() {
 Assert.assertTrue(tokeniser.currentNodeInHtmlNS());
 }

}