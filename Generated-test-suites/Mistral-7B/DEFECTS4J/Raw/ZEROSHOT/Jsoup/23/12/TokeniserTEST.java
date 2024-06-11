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

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private static InputStream inputStream;
 private static ParseErrorList errors;
 private static Tokeniser tokeniser;

 @BeforeClass
 public static void setUp() {
 inputStream = new ByteArrayInputStream("test".getBytes());
 errors = new ArrayList<>();
 tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
 }

 @Test
 public void testRead_stateIsData_readsCharacter() {
 char c = tokeniser.read().asCharacter();
 assertEquals('t', c);
 }

 @Test
 public void testRead_stateIsData_readsCharacterReference() throws IOException {
 char ref = tokeniser.consumeCharacterReference('a', false).asCharacter();
 assertEquals('&#x61;', ref.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_stateIsData_readsSelfClosingTag() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.read();
 }

 @Test
 public void testRead_stateIsData_readsStartTag() {
 StartTag tag = tokeniser.read().asStartTag();
 assertEquals("tag", tag.tagName);
 }

 @Test
 public void testRead_stateIsData_readsEndTag() {
 EndTag tag = tokeniser.read().asEndTag();
 assertEquals("tag", tag.tagName);
 }

 @Test
 public void testRead_stateIsData_readsComment() {
 Comment comment = tokeniser.read().asComment();
 assertEquals("<!--", comment.asString());
 }

 @Test
 public void testRead_stateIsData_readsDoctype() {
 Doctype doctype = tokeniser.read().asDoctype();
 assertEquals("DOCTYPE", doctype.asString());
 }

 @Test
 public void testRead_stateIsData_readsInvalidCharacter() {
 char c = tokeniser.read().asCharacter();
 assertNull(c);
 }

 @Test
 public void testRead_stateIsData_readsUnexpectedEndOfInput() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.consume(1);
 tokeniser.reader.close();
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testEmit_characterIsEmitted() {
 tokeniser.emit(new Character("t"));
 Character c = tokeniser.read().asCharacter();
 assertEquals('t', c);
 }

 @Test
 public void testEmit_startTagIsEmitted() {
 StartTag startTag = new StartTag("tag");
 tokeniser.emit(startTag);
 StartTag tag = tokeniser.read().asStartTag();
 assertEquals("tag", tag.tagName);
 }

 @Test
 public void testEmit_endTagIsEmitted() {
 EndTag endTag = new EndTag("tag");
 tokeniser.emit(endTag);
 EndTag tag = tokeniser.read().asEndTag();
 assertEquals("tag", tag.tagName);
 }

 @Test
 public void testEmit_stringIsEmittedAsCharacter() {
 tokeniser.emit("test");
 Character c = tokeniser.read().asCharacter();
 assertEquals("test", c.toString());
 }

 @Test
 public void testEmit_selfClosingTagIsSetAsLastStartTag() {
 StartTag startTag = new StartTag("tag") {
 @Override public boolean isSelfClosing() {
 return true;
 }
 };
 tokeniser.emit(startTag);
 assertEquals("tag", tokeniser.lastStartTag.tagName);
 }

 @Test
 public void testCreateTagPending_startTagIsCreated() {
 StartTag tag = tokeniser.createTagPending(true);
 assertEquals(Token.TokenType.StartTag, tag.type);
 }

 @Test
 public void testCreateTagPending_endTagIsCreated() {
 EndTag tag = tokeniser.createTagPending(false);
 assertEquals(Token.TokenType.EndTag, tag.type);
 }

 @Test
 public void testCreateCommentPending_commentIsCreated() {
 Comment comment = tokeniser.createCommentPending();
 assertEquals(Token.TokenType.Comment, comment.type);
 }

 @Test
 public void testCreateDoctypePending_doctypeIsCreated() {
 Doctype doctype = tokeniser.createDoctypePending();
 assertEquals(Token.TokenType.Doctype, doctype.type);
 }

 @Test
 public void testTransitionToDataState() {
 tokeniser.transition(TokeniserState.Data);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testTransitionToSelfClosingStartTagState() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 assertTrue(tokeniser.getState().equals(TokeniserState.SelfClosingStartTag));
 }

 @Test
 public void testTransitionToStartTagState() {
 tokeniser.transition(TokeniserState.StartTag);
 assertTrue(tokeniser.getState().equals(TokeniserState.StartTag));
 }

 @Test
 public void testTransitionToEndTagState() {
 tokeniser.transition(TokeniserState.EndTag);
 assertTrue(tokeniser.getState().equals(TokeniserState.EndTag));
 }

 @Test
 public void testTransitionToDoctypeState() {
 tokeniser.transition(TokeniserState.Doctype);
 assertTrue(tokeniser.getState().equals(TokeniserState.Doctype));
 }

 @Test
 public void testTransitionToCommentState() {
 tokeniser.transition(TokeniserState.Comment);
 assertTrue(tokeniser.getState().equals(TokeniserState.Comment));
 }

 @Test
 public void testTransitionToCharState() {
 tokeniser.transition(TokeniserState.Char);
 assertTrue(tokeniser.getState().equals(TokeniserState.Char));
 }

 @Test
 public void testAdvanceTransitionToDataState() {
 tokeniser.advanceTransition(TokeniserState.Data);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToSelfClosingStartTagState() {
 tokeniser.advanceTransition(TokeniserState.SelfClosingStartTag);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToStartTagState() {
 tokeniser.advanceTransition(TokeniserState.StartTag);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToEndTagState() {
 tokeniser.advanceTransition(TokeniserState.EndTag);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToDoctypeState() {
 tokeniser.advanceTransition(TokeniserState.Doctype);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToCommentState() {
 tokeniser.advanceTransition(TokeniserState.Comment);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAdvanceTransitionToCharState() {
 tokeniser.advanceTransition(TokeniserState.Char);
 assertTrue(tokeniser.getState().equals(TokeniserState.Data));
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testIsAppropriateEndTagToken_true() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.createEndTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_false() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.createCommentPending();
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_null() {
 assertNull(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testCreateTempBuffer() {
 StringBuilder buffer = tokeniser.createTempBuffer();
 assertSame(buffer, tokeniser.dataBuffer);
 }

 @Test
 public void testCreateTempBuffer_dataIsBuffered() {
 tokeniser.createTempBuffer();
 tokeniser.emit("test");
 assertEquals("test", tokeniser.dataBuffer.toString());
 }

 @Test
 public void testError_validErrorIsCreated() {
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 }

 @Test
 public void testError_invalidCharacterReferenceErrorIsCreated() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.advanceTransition(1);
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 assertEquals("Unexpected character '!' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testError_EOFErrorIsCreated() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.reader.consume(1);
 tokeniser.reader.close();
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testError_characterReferenceErrorIsCreated() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error("Unexpected character '!' in input state [Data]");
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: Unexpected character '!' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testError_invalidEndTagErrorIsCreated() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag("tag");
 tokeniser.error(TokeniserState.EndTag);
 assertEquals(1, errors.size());
 assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());
 }

 @Test
 public void testError_stateIsUnknown() {
 tokeniser.error("Unknown state");
 assertEquals(1, errors.size());
 assertEquals("Unknown state", errors.get(0).getMessage());
 }

 @Test
 public void testCurrentNodeInHtmlNS_true() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.createStartTagPending("html");
 tokeniser.emitTagPending();
 assertTrue(tokeniser.currentNodeInHtmlNS());
 }

 @Test
 public void testCurrentNodeInHtmlNS_false() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.createStartTagPending("other");
 tokeniser.emitTagPending();
 assertFalse(tokeniser.currentNodeInHtmlNS());
 }
}