package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser createTokeniser(String input) {
 char[] buffer = input.toCharArray();
 ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
 return new Tokeniser(new CharacterReader(stream));
 }

 @Test
 public void testRead_DataState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 Token token = tokeniser.read();

 Assert.assertEquals(TokenType.StartTag, token.type);
 Assert.assertEquals("html", token.tagName);

 token = tokeniser.read();
 Assert.assertEquals(TokenType.EndTag, token.type);
 Assert.assertEquals("html", token.tagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.read(); // read first start tag
 tokeniser.read(); // read first end tag
 tokeniser.read(); // read second start tag without acknowledging self-closing flag
 tokeniser.read(); // should throw IllegalStateException
 }

 @Test
 public void testRead_CharacterState() {
 Tokeniser tokeniser = createTokeniser("abcdef");
 Token token = tokeniser.read();

 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("a", token.getData());

 token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("b", token.getData());

 token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("c", token.getData());

 token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("d", token.getData());

 token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("e", token.getData());

 token = tokeniser.read();
 Assert.assertEquals(TokenType.Character, token.type);
 Assert.assertEquals("f", token.getData());

 token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitPending_UnexpectedEmission() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.read(); // read first start tag
 tokeniser.emit(new Token.Character('a')); // should throw IllegalStateException
 tokeniser.read(); // read first end tag
 }

 @Test
 public void testEmit_StartTag() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 Token token = tokeniser.read();
 tokeniser.emit(token);

 Token expectedToken = new Token.StartTag("html");
 Assert.assertEquals(expectedToken, tokeniser.read());
 }

 @Test
 public void testEmit_EndTag() {
 Tokeniser tokeniser = createTokeniser("<html><body></body></html>");
 Token token = tokeniser.read();
 tokeniser.emit(token);
 token = tokeniser.read();
 tokeniser.emit(token);

 Token expectedToken = new Token.EndTag("html");
 Assert.assertEquals(expectedToken, tokeniser.read());
 }

 @Test
 public void testEmit_String() {
 Tokeniser tokeniser = createTokeniser("<html><body>Hello, World!</body></html>");
 tokeniser.emit("Hello, World!");

 Token expectedToken = new Token.TextNode("Hello, World!");
 Assert.assertEquals(expectedToken.getData(), tokeniser.read().getData());
 }

 @Test
 public void testEmit_Character() {
 Tokeniser tokeniser = createTokeniser("<html><body>Hello, World!</body></html>");
 tokeniser.emit('H');
 tokeniser.emit('e');
 tokeniser.emit('l');
 tokeniser.emit('l');
 tokeniser.emit('o');
 tokeniser.emit(' ');
 tokeniser.emit('W');
 tokeniser.emit('o');
 tokeniser.emit('r');
 tokeniser.emit('l');
 tokeniser.emit('d');
 tokeniser.emit('!');

 Token expectedToken = new Token.TextNode("Hello, World!");
 Assert.assertEquals(expectedToken.getData(), tokeniser.read().getData());
 }

 @Test
 public void testGetState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Data, state);
 }

 @Test
 public void testTransition() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.transition(TokeniserState.Character);
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Character, state);
 }

 @Test
 public void testAdvanceTransition() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.advanceTransition(TokeniserState.Character);
 tokeniser.advance();
 TokeniserState state = tokeniser.getState();
 Assert.assertEquals(TokeniserState.Character, state);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.acknowledgeSelfClosingFlag();
 TokeniserState state = tokeniser.getState();
 Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_EmptyInput() {
 Tokeniser tokeniser = createTokeniser("");
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testConsumeCharacterReference_Numbered() {
 Tokeniser tokeniser = createTokeniser("&#10;");
 Character result = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('\n', result.charValue());
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 Tokeniser tokeniser = createTokeniser("&copy;");
 Character result = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('©', result.charValue());
 }

 @Test
 public void testConsumeCharacterReference_NamedWithAttribute() {
 Tokeniser tokeniser = createTokeniser("<img src=\"image.png\" alt=\"&copy; 2023 Example Corp.\">&copy; 2023 Example Corp.");
 Character result = tokeniser.consumeCharacterReference(null, true);
 Assert.assertEquals('©', result.charValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReference_NamedWithAttribute_InvalidState() {
 Tokeniser tokeniser = createTokeniser("<img src=\"image.png\" alt=\"&copy; 2023 Example Corp.\">&copy; 2023 Example Corp.");
 tokeniser.consumeCharacterReference(null, true);
 tokeniser.consumeCharacterReference(null, true); // read first character reference
 tokeniser.consumeCharacterReference(null, true); // read second character reference
 tokeniser.consumeCharacterReference(null, true); // should throw IllegalStateException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateTagPending_InvalidState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.createTagPending(false); // create end tag
 tokeniser.createTagPending(true); // should throw IllegalArgumentException
 }

 @Test
 public void testEmitTagPending() {
 Tokeniser tokeniser = createTokeniser("<html><head><title>Test</title></head><body></body></html>");
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Token expectedStartTag = new Token.StartTag("html");
 Token expectedHeadTag = new Token.StartTag("head");
 Token expectedTitleTag = new Token.StartTag("title");
 Token expectedEndTitleTag = new Token.EndTag("title");
 Token expectedEndHeadTag = new Token.EndTag("head");
 Token expectedBodyTag = new Token.StartTag("body");
 Token expectedEndBodyTag = new Token.EndTag("body");
 Token expectedEndHtmlTag = new Token.EndTag("html");

 Assert.assertEquals(expectedStartTag, tokeniser.read());
 Assert.assertEquals(expectedHeadTag, tokeniser.read());
 Assert.assertEquals(expectedTitleTag, tokeniser.read());
 Assert.assertEquals(expectedEndTitleTag, tokeniser.read());
 Assert.assertEquals(expectedEndHeadTag, tokeniser.read());
 Assert.assertEquals(expectedBodyTag, tokeniser.read());
 Assert.assertEquals(expectedEndBodyTag, tokeniser.read());
 Assert.assertEquals(expectedEndHtmlTag, tokeniser.read());
 }

 @Test
 public void testCreateCommentPending() {
 Tokeniser tokeniser = createTokeniser("<html><!-- Comment --></html>");
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();

 Token expectedCommentToken = new Token.Comment("-- Comment --");
 Assert.assertEquals(expectedCommentToken, tokeniser.read());
 }

 @Test
 public void testCreateDoctypePending() {
 Tokeniser tokeniser = createTokeniser("<!DOCTYPE html>");
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();

 Token expectedDoctypeToken = new Token.Doctype("html");
 Assert.assertEquals(expectedDoctypeToken, tokeniser.read());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 Tokeniser tokeniser = createTokeniser("<html><body><div></div></body></html>");
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();

 Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsAppropriateEndTagToken_InvalidState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testIsTrackErrors() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 Assert.assertTrue(tokeniser.isTrackErrors());
 }

 @Test
 public void testSetTrackErrors() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.setTrackErrors(false);
 Assert.assertFalse(tokeniser.isTrackErrors());
 }

 @Test
 public void testError_DataState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }

 @Test
 public void testEofError_DataState() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.eofError(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }

 @Test
 public void testCharacterReferenceError() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.characterReferenceError();
 List<ParseError> errors = tokeniser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }

 @Test
 public void testHasErrors() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.error(TokeniserState.Data);
 Assert.assertTrue(tokeniser.hasErrors());
 }

 @Test
 public void testClearErrors() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.error(TokeniserState.Data);
 tokeniser.clearErrors();
 Assert.assertFalse(tokeniser.hasErrors());
 }

 @Test
 public void testGetErrors() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.error(TokeniserState.Data);
 List<ParseError> errors = tokeniser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }

 @Test
 public void testThrowParseError() {
 Tokeniser tokeniser = createTokeniser("<html></html>");
 tokeniser.throwParseError();
 List<ParseError> errors = tokeniser.getErrors();
 Assert.assertFalse(errors.isEmpty());
 }
}