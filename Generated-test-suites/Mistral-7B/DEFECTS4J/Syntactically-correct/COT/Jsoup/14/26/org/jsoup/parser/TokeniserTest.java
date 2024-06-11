package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import org.jsoup.parser.TokeniserTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for the Tokeniser class.
 */
public class TokeniserTest extends TokeniserTest {

 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("Test data".getBytes()))) {
 @Override
 public void error(TokeniserState state) {
 super.error(state);
 System.out.println("Error in state " + state + " at position " + this.pos);
 }
 };
 }

 /**
 * Test the constructor.
 */
 @Test
 public void testTokeniser() {
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("Test data".getBytes())));
 Assert.assertNotNull(tokeniser);
 }

 /**
 * Test the read method.
 */
 @Test
 public void testRead() {
 Token token = tokeniser.read();
 Assert.assertNotNull(token);
 }

 /**
 * Test the emit method with a character token.
 */
 @Test
 public void testEmitCharacter() {
 tokeniser.emit(new Character("T"));
 Token token = tokeniser.read();
 Assert.assertEquals(Token.Character.class, token.getClass());
 Assert.assertEquals("T", ((Character) token).data);
 }

 /**
 * Test the emit method with a start tag token.
 */
 @Test
 public void testEmitStartTag() {
 tokeniser.emit(new StartTag("p"));
 tokeniser.read(); // consume character token
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.StartTag.class, tag.getClass());
 Assert.assertEquals("p", ((StartTag) tag).tagName);
 }

 /**
 * Test the emit method with an end tag token.
 */
 @Test
 public void testEmitEndTag() {
 tokeniser.emit(new StartTag("p"));
 tokeniser.read(); // consume character token and start tag token
 tokeniser.emit(new EndTag("p"));
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.EndTag.class, tag.getClass());
 Assert.assertEquals("p", ((EndTag) tag).tagName);
 }

 /**
 * Test the emit method with a comment token.
 */
 @Test
 public void testEmitComment() {
 tokeniser.createTempBuffer();
 tokeniser.emit("<!-- Test comment -->");
 tokeniser.emitCommentPending();
 Token comment = tokeniser.read();
 Assert.assertEquals(Token.Comment.class, comment.getClass());
 Assert.assertEquals("Test comment", ((Comment) comment).data);
 }

 /**
 * Test the emit method with a doctype token.
 */
 @Test
 public void testEmitDoctype() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token doctype = tokeniser.read();
 Assert.assertEquals(Token.Doctype.class, doctype.getClass());
 }

 /**
 * Test the getState method.
 */
 @Test
 public void testGetState() {
 Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 /**
 * Test the transition method.
 */
 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.CharacterRef);
 Assert.assertEquals(TokeniserState.CharacterRef, tokeniser.getState());
 }

 /**
 * Test the acknowledgeSelfClosingFlag method.
 */
 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 /**
 * Test the consumeCharacterReference method with a valid character reference.
 */
 @Test
 public void testConsumeCharacterReferenceValid() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 Assert.assertNotNull(character);
 Assert.assertEquals("&apos;", character.data);
 }

 /**
 * Test the consumeCharacterReference method with an invalid character reference.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReferenceInvalid() {
 tokeniser.consumeCharacterReference(null, false);
 }

 /**
 * Test the consumeCharacterReference method with a character reference inside an attribute.
 */
 @Test
 public void testConsumeCharacterReferenceAttribute() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("p"));
 tokeniser.emit(" class=\"class&name;\"");
 Character character = tokeniser.consumeCharacterReference(Character.class, true);
 Assert.assertNull(character);
 }

 /**
 * Test the createTagPending method with a start tag.
 */
 @Test
 public void testCreateTagPendingStart() {
 tokeniser.createTagPending(true);
 Assert.assertNotNull(tokeniser.tagPending);
 Assert.assertTrue(tokeniser.tagPending instanceof StartTag);
 }

 /**
 * Test the createTagPending method with an end tag.
 */
 @Test
 public void testCreateTagPendingEnd() {
 tokeniser.createTagPending(false);
 Assert.assertNotNull(tokeniser.tagPending);
 Assert.assertTrue(tokeniser.tagPending instanceof EndTag);
 }

 /**
 * Test the emitTagPending method.
 */
 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.read(); // consume character token
 Token tag = tokeniser.read();
 Assert.assertEquals(Token.StartTag.class, tag.getClass());
 Assert.assertEquals("p", ((StartTag) tag).tagName);
 }

 /**
 * Test the createCommentPending method.
 */
 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 Assert.assertNotNull(tokeniser.commentPending);
 Assert.assertTrue(tokeniser.commentPending instanceof Comment);
 }

 /**
 * Test the emitCommentPending method.
 */
 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Token comment = tokeniser.read();
 Assert.assertEquals(Token.Comment.class, comment.getClass());
 Assert.assertEquals("", ((Comment) comment).data);
 }

 /**
 * Test the createDoctypePending method.
 */
 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 Assert.assertNotNull(tokeniser.doctypePending);
 Assert.assertTrue(tokeniser.doctypePending instanceof Doctype);
 }

 /**
 * Test the emitDoctypePending method.
 */
 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Token doctype = tokeniser.read();
 Assert.assertEquals(Token.Doctype.class, doctype.getClass());
 }

 /**
 * Test the isAppropriateEndTagToken method.
 */
 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser.emit(new StartTag("p"));
 tokeniser.read(); // consume character token and start tag token
 tokeniser.emit(new EndTag("p"));
 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 /**
 * Test the isAppropriateEndTagToken method with an incorrect end tag.
 */
 @Test
 public void testIsAppropriateEndTagTokenIncorrect() {
 tokeniser.emit(new StartTag("p"));
 tokeniser.read(); // consume character token and start tag token
 tokeniser.emit(new EndTag("div"));
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 /**
 * Test the isTrackErrors method.
 */
 @Test
 public void testIsTrackErrors() {
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 /**
 * Test the setTrackErrors method.
 */
 @Test
 public void testSetTrackErrors() {
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }

 /**
 * Test error handling for unexpected character in input.
 */
 @Test
 public void testUnexpectedCharacter() {
 tokeniser.error(TokeniserState.Data);
 Assert.assertEquals(1, tokeniser.errors.size());
 ParseError error = tokeniser.errors.get(0);
 Assert.assertEquals("Unexpected character in input", error.getMessage());
 Assert.assertEquals(0, tokeniser.pos);
 }

 /**
 * Test error handling for end of file (EOF).
 */
 @Test
 public void testEndOfFile() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
 Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream)) {
 @Override
 public void error(TokeniserState state) {
 super.error(state);
 System.out.println("Error in state " + state + " at position " + this.pos);
 }
 };
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.eofError(TokeniserState.CharacterRef);
 List<ParseError> errors = tokeniser.errors;
 Assert.assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
 Assert.assertEquals(-1, tokeniser.pos);
 }

 /**
 * Test error handling for invalid character reference.
 */
 @Test
 public void testCharacterReferenceError() {
 tokeniser.error(TokeniserState.CharacterRef);
 Assert.assertEquals(1, tokeniser.errors.size());
 ParseError error = tokeniser.errors.get(0);
 Assert.assertEquals("Invalid character reference", error.getMessage());
 Assert.assertEquals(0, tokeniser.pos);
 }
}