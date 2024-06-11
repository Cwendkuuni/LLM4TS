package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import org.junit.Test;

public class TokeniserTest {

 private ByteArrayInputStream inputStream;
 private CharacterReader reader;
 private ParseErrorList errors = new ParseErrorList();
 private Tokeniser tokeniser;

 @Test
 public void testTokeniserConstructor() {
 tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);
 }

 @Test(expected = NullPointerException.class)
 public void testTokeniserNullErrors() {
 tokeniser = new Tokeniser(new CharacterReader(inputStream), null);
 }

 @Test
 public void testReadCharacter() throws IOException {
 inputStream = new ByteArrayInputStream("abcdef".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 Character c = tokeniser.read();
 assertEquals("a", c.getData());

 c = tokeniser.read();
 assertEquals("b", c.getData());

 c = tokeniser.read();
 assertEquals("c", c.getData());

 c = tokeniser.read();
 assertEquals("d", c.getData());

 c = tokeniser.read();
 assertEquals("e", c.getData());

 c = tokeniser.read();
 assertEquals("f", c.getData());

 c = tokeniser.read();
 assertNull(c);
 }

 @Test
 public void testReadStartTag() throws IOException {
 inputStream = new ByteArrayInputStream("<html><body>".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 Token token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("html", ((StartTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("body", ((StartTag) token).tagName);

 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadEndTag() throws IOException {
 inputStream = new ByteArrayInputStream("<html><body><p></p></body></html>".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 Token token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("html", ((StartTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("body", ((StartTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("p", ((StartTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.EndTag, token.type);
 assertEquals("p", ((EndTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.EndTag, token.type);
 assertEquals("body", ((EndTag) token).tagName);

 token = tokeniser.read();
 assertSame(Token.TokenType.EndTag, token.type);
 assertEquals("html", ((EndTag) token).tagName);

 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadString() throws IOException {
 inputStream = new ByteArrayInputStream("hello world".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 String str = "";
 tokeniser.emit("hello ");
 str += "hello ";
 while (tokeniser.readType() != Token.TokenType.Character) {
 str += tokeniser.read().getData();
 }
 tokeniser.emit(tokeniser.read().getData());
 tokeniser.emit(" world");
 str += " world";
 assertEquals("hello world", str);
 }

 @Test
 public void testReadCharacterReference() throws IOException {
 inputStream = new ByteArrayInputStream("&aaa; &ccc#123; &nnn; &XXXx; &nbsp;".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 Character a = tokeniser.consumeCharacterReference(null, false);
 assertEquals('\u0061', a.getData()); // a

 Character c = tokeniser.consumeCharacterReference('c', false);
 assertEquals('\u0063', c.getData()); // c

 Character cc = tokeniser.consumeCharacterReference((char) 0x0063, false);
 assertEquals('\u0063', cc.getData()); // c

 Character d = tokeniser.consumeCharacterReference(null, true);
 assertNull(d);

 Character n = tokeniser.consumeCharacterReference(null, false);
 assertEquals('\n', n.getData()); // lf

 Character x = tokeniser.consumeCharacterReference("XXX", false);
 assertEquals('\u0021', x.getData()); // exclamation mark

 Character space = tokeniser.consumeCharacterReference(null, false);
 assertEquals(' ', space.getData()); // space
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReferenceInvalid() throws IOException {
 inputStream = new ByteArrayInputStream("&aaa; &ccc#123; &NNN; &XXXx; &nbsp;".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 tokeniser.consumeCharacterReference(null, true);
 }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError() throws IOException {
 inputStream = new ByteArrayInputStream("&aaa; &ccc#123; &NNN; &XXXx; &xnb;".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);

 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("xnb", false);
 }

 @Test
 public void testCreateTagPending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 Token tag = tokeniser.createTagPending(true);
 assertEquals(StartTag.class, tag.getClass());
 tag = tokeniser.createTagPending(false);
 assertEquals(EndTag.class, tag.getClass());
 }

 @Test
 public void testEmitTagPending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createCommentPending();
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createDoctypePending();
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 }

 @Test
 public void testCreateTempBuffer() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createTempBuffer();
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createTagPending(true).tagName = "div";
 tokeniser.createTagPending(true).tagName = "p";
 assertFalse(tokeniser.isAppropriateEndTagToken());
 tokeniser.tagPending.tagName = "div";
 assertTrue(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.createTagPending(true).tagName = "div";
 tokeniser.createTagPending(true).tagName = "p";
 assertNull(tokeniser.appropriateEndTagName());
 tokeniser.tagPending.tagName = "div";
 assertEquals("div", tokeniser.appropriateEndTagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testTransitionWithoutReaderAdvance() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.transition(TokeniserState.Script);
 }

 @Test
 public void testTransition() throws IOException {
 inputStream = new ByteArrayInputStream("<html><script>alert('hello')</script></html>".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.transition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 tokeniser.advanceTransition(TokeniserState.Data);
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTransitionInvalidState() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.transition(null);
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test(expected = IllegalStateException.class)
 public void testAcknowledgeSelfClosingFlagTwice() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.acknowledgeSelfClosingFlag();
 }

 @Test
 public void testError() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testEofError() throws IOException {
 inputStream = new ByteArrayInputStream("".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.error(TokeniserState.Script);
 tokeniser.acknowledgeSelfClosingFlag();
 inputStream.close();
 tokeniser.eofError(TokeniserState.Data);
 }

 @Test
 public void testCharacterReferenceError() throws IOException {
 inputStream = new ByteArrayInputStream("&aaa; &ccc#123; &NNN; &XXXx; &xnb;".getBytes());
 reader = new CharacterReader(inputStream);
 tokeniser = new Tokeniser(reader, errors);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("xnb", false);
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testErrorMessage() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.error(TokeniserState.Data);
 assertEquals("Unexpected character '?' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testErrorsSize() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.error(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 assertEquals(3, errors.size());
 }

 @Test
 public void testClearErrors() {
 tokeniser = new Tokeniser(new CharacterReader(new ByteArrayInputStream("".getBytes())), errors);
 tokeniser.error(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 tokeniser.clearErrors();
 assertEquals(0, errors.size());
 }
}