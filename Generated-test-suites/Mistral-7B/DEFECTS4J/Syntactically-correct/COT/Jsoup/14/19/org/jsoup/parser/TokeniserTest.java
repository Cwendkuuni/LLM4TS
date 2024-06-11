package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.CharacterReference;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private InputStream inputStream;

 @Before
 public void setup() {
 inputStream = new ByteArrayInputStream("Test data".getBytes());
 tokeniser = new Tokeniser(new CharReader(inputStream));
 }

 @After
 public void tearDown() throws IOException {
 tokeniser.close();
 inputStream.close();
 }

 @Test
 public void testConstructor() {
 CharReader reader = new CharReader(new ByteArrayInputStream("Test data".getBytes()));
 Tokeniser tokeniser = new Tokeniser(reader);
 assertTrue(tokeniser instanceof Tokeniser);
 }

 @Test
 public void testRead_NormalCharacter() {
 Token token = tokeniser.read();
 assertEquals("Test data", token.getText());
 assertEquals(Token.TokenType.Character, token.type);
 }

 @Test
 public void testRead_SelfClosingFlagNotAcknowledged() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 tokeniser.error("Self closing flag not acknowledged");
 }

 @Test
 public void testRead_NoEmitPending() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_EmitPendingCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.Character("T"));
 tokeniser.emitPending = null;
 assertEquals("T", tokeniser.read().getText());
 }

 @Test
 public void testRead_EmitPendingTag() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.StartTag("p"));
 tokeniser.emitPending = null;
 assertEquals(Tag.P, tokeniser.read().getType());
 }

 @Test
 public void testRead_EmitPendingComment() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createCommentPending();
 tokeniser.emitPending = null;
 assertEquals(Comment.class, tokeniser.read().getClass());
 }

 @Test
 public void testRead_EmitPendingDoctype() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createDoctypePending();
 tokeniser.emitPending = null;
 assertEquals(Doctype.class, tokeniser.read().getClass());
 }

 @Test
 public void testRead_CharBufferNotEmpty() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(new Token.Character('T'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emitPending = null;
 assertEquals("Test", tokeniser.read().getText());
 }

 @Test
 public void testEmit_Character() {
 CharSequence text = "A";
 tokeniser.emit(new Token.Character(text.charAt(0)));
 assertEquals(text.charAt(0), tokeniser.read().getText().charAt(0));
 }

 @Test
 public void testEmit_StartTag() {
 tagPending = new Token.StartTag("p");
 tokeniser.emit(tagPending);
 assertEquals(Tag.P, tokeniser.read().getType());
 }

 @Test
 public void testEmit_EndTag() {
 tagPending = new Token.EndTag("p");
 tokeniser.emit(tagPending);
 assertEquals(Tag.P, tokeniser.read().getType());
 assertEquals(Tag.P, tokeniser.read().getType());
 assertEquals(Tag.EndTag.class, tokeniser.read().getClass());
 }

 @Test
 public void testEmit_Comment() {
 commentPending = new Token.Comment("<!-- Test -->");
 tokeniser.emit(commentPending);
 assertEquals(Comment.class, tokeniser.read().getClass());
 assertEquals("<!-- Test -->", tokeniser.read().getText());
 }

 @Test
 public void testEmit_Doctype() {
 doctypePending = new Token.Doctype();
 doctypePending.name = "html";
 doctypePending.publicId = "W3C";
 doctypePending.systemId = "http://www.w3.org/TR/html4/strict.dtd";
 tokeniser.emit(doctypePending);
 assertEquals(Doctype.class, tokeniser.read().getClass());
 assertEquals("html", tokeniser.read().getText());
 assertEquals("W3C", tokeniser.read().getText());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", tokeniser.read().getText());
 }

 @Test
 public void testTransition_Data() {
 tokeniser.transition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition_Script() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition_Data() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition_Script() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertSame('T', character);
 tokeniser.emit(character);
 assertEquals("T", tokeniser.read().getText());
 }

 @Test
 public void testConsumeCharacterReference_Invalid() {
 Character character = tokeniser.consumeCharacterReference('&', false);
 assertNull(character);
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 Character character = tokeniser.consumeCharacterReference(null, true);
 assertSame('&amp;', character.toString());
 tokeniser.emit(character);
 assertEquals("&amp;", tokeniser.read().getText());
 }

 @Test
 public void testCreateTagPending_StartTag() {
 tagPending = tokeniser.createTagPending(true);
 assertEquals(tagPending.tagName, tokeniser.read().getText());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 tagPending = tokeniser.createTagPending(false);
 assertEquals(tagPending.tagName, tokeniser.read().getText());
 assertEquals(Tag.EndTag.class, tokeniser.read().getClass());
 }

 @Test
 public void testEmitTagPending() {
 tagPending = new Token.StartTag("p");
 tokeniser.emitTagPending();
 assertEquals("p", tokeniser.read().getText());
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 assertEquals(Comment.class, tokeniser.read().getClass());
 assertEquals("<!--", tokeniser.read().getText());
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.emitCommentPending();
 assertEquals("-->", tokeniser.read().getText());
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 assertEquals(Doctype.class, tokeniser.read().getClass());
 assertEquals("<!DOCTYPE", tokeniser.read().getText());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tagPending = new Token.StartTag("p");
 tokeniser.transition(TokeniserState.HTML);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.Body);
 tokeniser.createStartTag("p", new Attributes());
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_False() {
 tagPending = new Token.StartTag("p");
 tokeniser.transition(TokeniserState.HTML);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.Head);
 tokeniser.createStartTag("p", new Attributes());
 tokeniser.emitTagPending();
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 tokeniser.setTrackErrors(false);
 assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testIsTrackErrors_True() {
 tokeniser.setTrackErrors(true);
 assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testError() {
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size());
 }

 @Test
 public void testError_NullState() {
 tokeniser.error(null);
 assertTrue(tokeniser.getErrors().isEmpty());
 }

 @Test
 public void testEofError() {
 tokeniser.eofError(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.characterReferenceError();
 List<ParseError> errors = tokeniser.getErrors();
 assertEquals(1, errors.size());
 }

 @Test
 public void testCharacterReferenceError_NullState() {
 tokeniser.characterReferenceError();
 assertTrue(tokeniser.getErrors().isEmpty());
 }
}