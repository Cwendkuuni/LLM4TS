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

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private CharacterReader characterReader;
 private Tokeniser tokeniser;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 characterReader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ArrayList<>();
 tokeniser = new Tokeniser(characterReader);
 }

 @Test
 public void testReadEmptyStream() {
 Token token = tokeniser.read();
 assertNull(token);
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadCharacter() {
 characterReader.mark(3);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Char);
 tokeniser.read();
 Token token = tokeniser.read();
 Character characterToken = (Character) token;
 assertEquals("t", characterToken.data);
 characterReader.mark(3);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadTokenWithBuffer() {
 characterReader.mark(6);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // read 't'
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.transition(TokeniserState.Char);
 tokeniser.read(); // read ' '
 Token token = tokeniser.read();
 assertEquals("test", token.data);
 characterReader.mark(6);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadStartTag() {
 characterReader.mark(12);
 characterReader.reset();
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 StartTag startTagToken = (StartTag) token;
 assertEquals("test", startTagToken.tagName);
 characterReader.mark(12);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadEndTag() {
 characterReader.mark(7);
 characterReader.reset();
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('/');
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 EndTag endTagToken = (EndTag) token;
 assertEquals("test", endTagToken.tagName);
 characterReader.mark(7);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadComment() {
 characterReader.mark(8);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read(); // read '<'
 tokeniser.emit('/');
 tokeniser.emit('c');
 tokeniser.emit('o');
 tokeniser.emit('m'); tokeniser.emit('m'); tokeniser.emit('e'); tokeniser.emit('n');
 tokeniser.emit('t'); tokeniser.emit('>');
 Token token = tokeniser.read();
 Comment commentToken = (Comment) token;
 assertEquals("<!--comment-->", commentToken.data);
 characterReader.mark(8);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadDoctype() {
 characterReader.mark(14);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read(); // read '<'
 tokeniser.emit('d'); tokeniser.emit('o'); tokeniser.emit('c'); tokeniser.emit('t'); tokeniser.emit('y'); tokeniser.emit('p'); tokeniser.emit('e'); tokeniser.emit('>');
 Token token = tokeniser.read();
 Doctype doctypeToken = (Doctype) token;
 assertNull(doctypeToken.data);
 characterReader.mark(14);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadError() {
 characterReader.mark(1);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // read unexpected character
 tokeniser.error(TokeniserState.Data);
 Token token = tokeniser.read();
 assertNull(token);
 characterReader.mark(1);
 characterReader.reset();
 assertTrue(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpected character in input", error.getMessage());
 assertEquals(characterReader.pos(), error.getPos());
 assertEquals(characterReader.current(), error.getCharacter());
 }

 @Test
 public void testReadEOFError() {
 characterReader.mark(1);
 characterReader.reset();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // read to EOF
 tokeniser.eofError(TokeniserState.Data);
 Token token = tokeniser.read();
 assertNull(token);
 characterReader.mark(1);
 characterReader.reset();
 assertTrue(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 assertEquals(1, errors.size());
 ParseError error = errors.get(0);
 assertEquals("Unexpectedly reached end of file (EOF)", error.getMessage());
 assertEquals(Character.MIN_VALUE, error.getCharacter());
 }

 @Test
 public void testConsumeCharacterReference() {
 characterReader.mark(3);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Data);
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertNull(character);
 characterReader.mark(3);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference(null, true);
 assertNull(character);
 characterReader.mark(3);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("#x1F", false);
 assertNull(character);
 characterReader.mark(3);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("#x1F", true);
 assertNull(character);
 characterReader.mark(3);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("ampersand", false);
 assertNull(character);
 characterReader.mark(3);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("ampersand", true);
 assertNull(character);
 characterReader.mark(5);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("#41;", false);
 assertEquals('A', character.getData());
 characterReader.mark(5);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("#x33;", false);
 assertEquals('3', character.getData());
 characterReader.mark(6);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("nbsp", false);
 assertEquals('\u00A0', character.getData());
 characterReader.mark(6);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("nbsp", true);
 assertEquals('\u00A0', character.getData());
 characterReader.mark(7);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("nbsp#x3;", false);
 assertEquals('\u00A0', character.getData());
 characterReader.mark(7);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("nbsp#x3;", true);
 assertEquals('\u00A0', character.getData());
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x33;", false);
 assertEquals('3', character.getData());
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x33;", true);
 assertEquals('3', character.getData());
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x1F;", false);
 assertNull(character);
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x1F;", true);
 assertNull(character);
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x1FFF;", false);
 assertNull(character);
 characterReader.mark(2);
 characterReader.reset();
 character = tokeniser.consumeCharacterReference("&#x1FFF;", true);
 assertNull(character);
 }

 @Test
 public void testCreateTagPending() {
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.createTagPending(true);
 StartTag startTag = (StartTag) tokeniser.tagPending;
 assertNull(startTag.tagName);
 tokeniser.createTagPending(false);
 EndTag endTag = (EndTag) tokeniser.tagPending;
 assertNull(endTag.tagName);
 }

 @Test
 public void testEmitTagPending() {
 characterReader.mark(4);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('t'); tokeniser.emit('e'); tokeniser.emit('s'); tokeniser.emit('>');
 tokeniser.emitTagPending();
 StartTag startTag = (StartTag) tokeniser.tagPending;
 assertEquals("test", startTag.tagName);
 characterReader.mark(4);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testCreateCommentPending() {
 characterReader.mark(7);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read(); // read '<'
 tokeniser.emit('/'); tokeniser.emit('c'); tokeniser.emit('o'); tokeniser.emit('m'); tokeniser.emit('m'); tokeniser.emit('e'); tokeniser.emit('n'); tokeniser.emit('t'); tokeniser.emit('>');
 tokeniser.emitCommentPending();
 Comment comment = (Comment) tokeniser.commentPending;
 assertEquals("<!--comment-->", comment.data);
 characterReader.mark(7);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testCreateDoctypePending() {
 characterReader.mark(13);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read(); // read '<'
 tokeniser.emit('d'); tokeniser.emit('o'); tokeniser.emit('c'); tokeniser.emit('t'); tokeniser.emit('y'); tokeniser.emit('p'); tokeniser.emit('e'); tokeniser.emit('>');
 tokeniser.emitDoctypePending();
 Doctype doctype = (Doctype) tokeniser.doctypePending;
 assertNull(doctype.data);
 characterReader.mark(13);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testIsAppropriateEndTagToken() {
 characterReader.mark(15);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('t'); tokeniser.emit('e'); tokeniser.emit('s'); tokeniser.emit('>');
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('/'); tokeniser.emit('t'); tokeniser.emit('e'); tokeniser.emit('s'); tokeniser.emit('>');
 assertTrue(tokeniser.isAppropriateEndTagToken());
 characterReader.mark(15);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testIsAppropriateEndTagTokenFalse() {
 characterReader.mark(15);
 characterReader.reset();
 Tokeniser tokeniser = new Tokeniser(characterReader);
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('t'); tokeniser.emit('e'); tokeniser.emit('s'); tokeniser.emit('>');
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.read(); // read '<'
 tokeniser.emit('/'); tokeniser.emit('h'); tokeniser.emit('t'); tokeniser.emit('m'); tokeniser.emit('l'); tokeniser.emit('>');
 assertFalse(tokeniser.isAppropriateEndTagToken());
 characterReader.mark(15);
 characterReader.reset();
 assertFalse(tokeniser.isTrackErrors());
 assertSame(TokeniserState.Data, tokeniser.getState());
 }
}