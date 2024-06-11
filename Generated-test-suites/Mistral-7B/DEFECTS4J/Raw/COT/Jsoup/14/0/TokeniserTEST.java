import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setup() {
 reader = new CharacterReader(new ByteArrayInputStream("Test Input".getBytes()));
 tokeniser = new Tokeniser(reader);
 errors = tokeniser.getErrors();
 }

 @Test
 public void testReadBasic() {
 Token token = tokeniser.read();
 Assert.assertNotNull(token);
 }

 @Test
 public void testReadCharacter() {
 tokeniser.emit("A");
 Token token = tokeniser.read();
 Assert.assertEquals("A", token.getData());
 }

 @Test
 public void testReadStartTag() {
 tokeniser.createTagPending(true);
 tokeniser.emit("div");
 tokeniser.emit('>');
 Token token = tokeniser.read();
 Assert.assertTrue(token instanceof StartTag);
 Assert.assertEquals("div", ((StartTag) token).tagName);
 }

 @Test
 public void testReadEndTag() {
 tokeniser.createTagPending(false);
 tokeniser.emit("/div");
 tokeniser.emit('>');
 Token token = tokeniser.read();
 Assert.assertTrue(token instanceof EndTag);
 Assert.assertEquals("div", ((EndTag) token).tagName);
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.createTagPending(true);
 tokeniser.emit("img");
 tokeniser.emit('/');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 Assert.assertTrue(token instanceof StartTag);
 Assert.assertEquals("img", ((StartTag) token).tagName);
 Assert.assertTrue(token.isSelfClosing());
 }

 @Test
 public void testReadComment() {
 tokeniser.createCommentPending();
 tokeniser.emit("<!--");
 tokeniser.emit('>');
 Token token = tokeniser.read();
 Assert.assertTrue(token instanceof Comment);
 }

 @Test
 public void testReadDoctype() {
 tokeniser.createDoctypePending();
 tokeniser.emit("<!DOCTYPE html");
 tokeniser.emit('>');
 Token token = tokeniser.read();
 Assert.assertTrue(token instanceof Doctype);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWhenPendingTokenIsPresent() {
 tokeniser.read(); // consume a token
 tokeniser.emit(new StartTag("div"));
 tokeniser.emit(">");
 Token token = tokeniser.read();
 // this test should fail as we have a pending token
 }

 @Test
 public void testReadWhenNoTokenIsPresent() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.BeforeDoctype);
 tokeniser.read(); // consume the doctype token
 tokeniser.advanceTransition(TokeniserState.AfterDoctype);
 tokeniser.read(); // consume the first tag
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testReadCharacterReference() {
 tokeniser.emit('&');
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals((char) 38, character.charValue()); // '&'
 tokeniser.emit('#');
 character = tokeniser.consumeCharacterReference('x', false);
 Assert.assertEquals((char) 66, character.charValue()); // 'B'
 tokeniser.emit('#');
 character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals((char) 32, character.charValue()); // ' '
 tokeniser.emit(';');
 }

 @Test
 public void testReadNamedCharacterReference() {
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit(';');
 Character character = tokeniser.read().getDataAsCharacter();
 Assert.assertEquals((char) 61, character.charValue()); // '='
 }

 @Test(expected =IllegalArgumentException.class)
 public void testReadCharacterReferenceWithInvalidCharacter() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('A', false); // no error
 tokeniser.emit('A'); // consume 'A'
 tokeniser.emit(';'); // consume ';'
 tokeniser.consumeCharacterReference('B', true); // should throw an exception
 }

 @Test
 public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
 tokeniser.emit('&');
 tokeniser.emit('#');
 tokeniser.consumeCharacterReference('x', 'x');
 tokeniser.emit(';');
 }

 @Test(expected =IllegalStateException.class)
 public void testReadCharacterReferenceWithDigitOrLetterPresent() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('a', false);
 tokeniser.emit('#');
 Assert.assertFalse(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 Assert.assertTrue(reader.matchConsume(";")); // should throw an exception
 }

 @Test
 public void testReadCharacterReferenceWithInvalidCharacterSequence() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('a', false);
 tokeniser.emit('#');
 tokeniser.emit('X');
 Assert.assertFalse(reader.matchConsume(";")); // should throw an exception
 }

 @Test
 public void testReadCharacterReferenceWithInvalidNamedEntity() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('A', false);
 tokeniser.emit(';');
 tokeniser.emit('A');
 Assert.assertFalse(reader.matchConsume(";")); // should throw an exception
 }

 @Test
 public void testReadCharacterReferenceWithMissingSemicolon() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('A', false);
 Assert.assertFalse(reader.matchConsume(";")); // should throw an exception
 }

 @Test
 public void testReadCharacterReferenceWithExtraCharacter() {
 tokeniser.emit('&');
 tokeniser.consumeCharacterReference('A', false);
 tokeniser.emit(';');
 tokeniser.consumeCharacterReference('B', false); // should be ignored
 }

 @Test
 public void testGetErrors() {
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, errors.size());
 }

 @Test
 public void testGetErrorsAfterReset() {
 tokeniser = new Tokeniser(reader);
 Assert.assertEquals(0, errors.size());
 }

 @Test(expected =IllegalArgumentException.class)
 public void testTransitionWithInvalidState() {
 tokeniser.transition(TokeniserState.BeforeDoctype);
 }

 @Test(expected =IllegalArgumentException.class)
 public void testAdvanceTransitionWithInvalidState() {
 tokeniser.advanceTransition(TokeniserState.BeforeDoctype);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.createTagPending(true);
 tokeniser.emit("div");
 tokeniser.emit('>');
 tokeniser.createTagPending(false);
 tokeniser.emit("/div");
 tokeniser.emit('>');
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagTokenWithMismatchedTags() {
 tokeniser.createTagPending(true);
 tokeniser.emit("div");
 tokeniser.emit('>');
 tokeniser.createTagPending(false);
 tokeniser.emit("/span"); // wrong tag name
 tokeniser.emit('>');
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }
}