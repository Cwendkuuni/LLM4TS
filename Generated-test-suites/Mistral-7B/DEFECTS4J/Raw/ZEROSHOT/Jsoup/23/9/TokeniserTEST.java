package org.jsoup.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokeniserState;
import org.jsoup.parser.Tokeniser.ParseError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testNullReader() {
 assertNull(new Tokeniser(null, errors));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullErrorList() {
 new Tokeniser(reader, null);
 }

 @Test
 public void testGetState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.Comment);
 assertEquals(TokeniserState.Comment, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() throws IOException {
 tokeniser.transition(TokeniserState.Data);
 reader.advance();
 tokeniser.advanceTransition(TokeniserState.Script);
 assertEquals(TokeniserState.Script, tokeniser.getState());
 }

 @Test
 public void testConsumeCharacterReference_ValidNumericReference() throws IOException {
 tokeniser.emit('&');
 tokeniser.emit('A');
 tokeniser.emit('n');
 tokeniser.emit('d');
 tokeniser.emit(';');
 ArgumentCaptor<Character> characterCaptor = ArgumentCaptor.forClass(Character.class);
 verify(reader, times(4)).current();
 verify(reader, times(1)).consume(characterCaptor.capture());
 assertEquals((char) 65, characterCaptor.getValue());
 assertEquals((char) 68, reader.current());
 assertEquals((char) 68, reader.current());
 assertEquals((char) 69, reader.current());
 assertEquals((char) 59, reader.current());
 verifyNoMoreInteractions(reader);
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 }

 @Test
 public void testConsumeCharacterReference_InvalidNumericReference() throws IOException {
 tokeniser.emit('&');
 tokeniser.emit('A');
 tokeniser.emit('d');
 tokeniser.emit(';');
 ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
 verify(errors, times(1)).add(errorCaptor.capture());
 assertEquals("invalid character reference: invalid numeric reference with no numerals", errorCaptor.getValue().getMessage());
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamedReference() throws IOException {
 tokeniser.emit('&');
 tokeniser.emit('A');
 tokeniser.emit('b');
 tokeniser.emit(';');
 ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
 verify(errors, times(1)).add(errorCaptor.capture());
 assertEquals("invalid named reference 'Ab'", errorCaptor.getValue().getMessage());
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @Test
 public void testConsumeCharacterReference_NamedReferenceWithSemicolon() throws IOException {
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit('m');
 tokeniser.emit('p');
 tokeniser.emit(';');
 ArgumentCaptor<Character> characterCaptor = ArgumentCaptor.forClass(Character.class);
 verify(reader, times(3)).current();
 verify(reader, times(1)).consume(characterCaptor.capture());
 assertEquals('a', characterCaptor.getValue());
 assertEquals('m', reader.current());
 assertEquals('p', reader.current());
 assertEquals(';', reader.current());
 verifyNoMoreInteractions(reader);
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 }

 @Test
 public void testConsumeCharacterReference_NamedReferenceWithInAttribute() throws IOException {
 tokeniser.emit('&');
 tokeniser.emit('a');
 tokeniser.emit('m');
 tokeniser.emit('p');tokeniser.emit(' ');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('t');
 tokeniser.emit('p');
 tokeniser.emit('=');
 tokeniser.emit(' ');
 tokeniser.emit('"');
 tokeniser.emit('h');
 tokeniser.emit('t');
 tokeniser.emit('t');tokeniser.emit('p');
 tokeniser.emit('"');
 tokeniser.emit(';');
 ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
 verify(errors, times(1)).add(errorCaptor.capture());
 assertEquals("invalid named reference 'amp' in attribute value", errorCaptor.getValue().getMessage());
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @Test
 public void testCreateTagPending() {
 Token.Tag tag = tokeniser.createTagPending(true);
 assertNotNull(tag);
 assertEquals(StartTag.class, tag.getClass());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 Token.Tag tag = tokeniser.createTagPending(false);
 assertNotNull(tag);
 assertEquals(EndTag.class, tag.getClass());
 }

 @Test
 public void testEmitTagPending() {
 StartTag startTag = new StartTag("div");
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 verify(reader, times(1)).consume('d');
 verify(reader, times(1)).consume('i');
 verify(reader, times(1)).consume('v');
 verify(reader, times(1)).consume('>');
 assertEquals(startTag, tokeniser.read());
 }

 @Test
 public void testCreateCommentPending() {
 Comment comment = new Comment("-- test comment");
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertEquals(comment, tokeniser.read());
 }

 @Test
 public void testCreateDoctypePending() {
 Doctype doctype = new Doctype("html", new ArrayList<Attribute>(), null);
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 verify(reader, times(3)).consume('D');
 verify(reader, times(1)).consume('O');
 verify(reader, times(1)).consume('C');
 verify(reader, times(1)).consume('T');
 verify(reader, times(1)).consume('Y');
 verify(reader, times(1)).consume('P');
 verify(reader, times(1)).consume('E');
 verify(reader, times(1)).consume('>');
 assertEquals(doctype, tokeniser.read());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 StartTag startTag = new StartTag("p");
 EndTag endTag = new EndTag("p");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser.transition(TokeniserState.Comment);
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }

 @Test
 public void testAppropriateEndTagName() {
 StartTag startTag = new StartTag("p");
 EndTag endTag = new EndTag("p");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertEquals("p", tokeniser.appropriateEndTagName());
 tokeniser.transition(TokeniserState.Comment);
 assertNull(tokeniser.appropriateEndTagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testIsAppropriateEndTagToken_MismatchedTags() {
 StartTag startTag = new StartTag("p");
 EndTag endTag = new EndTag("div");
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitTagPending();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testError() {
 ParseError error = new ParseError(1, "Unexpected character 'x' in state Data");
 tokeniser.error(TokeniserState.Data);
 assertEquals(error, tokeniser.read());
 }

 @Test
 public void testEofError() throws IOException {
 tokeniser.transition(TokeniserState.Script);
 reader.mark();
 reader.advanceToEnd();
 tokeniser.emit('&');
 tokeniser.eofError(TokeniserState.Script);
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @Test
 public void testCharacterReferenceError() throws IOException {
 tokeniser.transition(TokeniserState.Data);
 reader.mark();
 reader.advance();
 reader.consume('#');
 tokeniser.characterReferenceError("numeric reference with no numerals");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }
}