import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TokeniserTest {

 private InputStream inputStream;
 private Tokeniser tokeniser;
 private ParseErrorList errors;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("test data".getBytes());
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(new CharInputStream(inputStream), errors);
 }

 @Test
 public void testRead() {
 Token token;
 // Test reading the first character.
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("t", token.data);

 // Test reading the second character.
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("e", token.data);

 // Test reading the third character.
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("s", token.data);

 // Test reading an empty string as a token.
 token = tokeniser.read();
 assertEquals(Token.TokenType.Empty, token.getType());
 assertNull(token.data);

 // Test reading the EOF token.
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndOfFile, token.getType());
 assertNull(token.data);
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Character("t"));
 tokeniser.emit(new Character("e"));
 tokeniser.emit(new Character("s"));

 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("t", token.data);

 tokeniser.emit(new Character(" "));
 tokeniser.emit(new Character("t"));

 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 assertEquals("t", token.data);

 tokeniser.emit(new Character('\n'));
 token = tokeniser.read();
 assertEquals(Token.TokenType.Empty, token.getType());
 assertNull(token.data);
 }

 @Test
 public void testTransitions() {
 tokeniser.transition(TokeniserState.Script);
 Assertions.assertEquals(TokeniserState.Script, tokeniser.getState());

 tokeniser.advanceTransition(TokeniserState.StartTagBegin);
 Assertions.assertEquals(TokeniserState.StartTagBegin, tokeniser.getState());

 tokeniser.advanceTransition(TokeniserState.StartTagName);
 Assertions.assertEquals(TokeniserState.StartTagName, tokeniser.getState());

 tokeniser.advanceTransition(TokeniserState.StartTagAttributes);
 Assertions.assertEquals(TokeniserState.StartTagAttributes, tokeniser.getState());

 tokeniser.advanceTransition(TokeniserState.StartTagEnd);
 Assertions.assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testCharacterReference() {
 tokeniser.emit(new Character('&'));
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertNull(character);
 tokeniser.emit(new Character('#'));
 character = tokeniser.consumeCharacterReference(null, false);
 assertNull(character);
 tokeniser.emit(new Character('A'));
 character = tokeniser.consumeCharacterReference(null, false);
 assertNull(character);
 tokeniser.emit(new Character(';'));
 assertEquals(Token.TokenType.EndOfFile, tokeniser.read().getType());

 tokeniser.reset(); // reset the stream
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('#'));
 tokeniser.emit(new Character('A'));
 tokeniser.emit(new Character(';'));
 character = tokeniser.consumeCharacterReference(null, false);
 assertNotNull(character);
 assertEquals(Character.AMPERSAND, character.getValue());
 }

 @Test
 public void testCharacterReferenceWithNumber() {
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('#'));
 tokeniser.emit(new Character('x'));
 tokeniser.emit(new Character('3'));
 tokeniser.emit(new Character(';'));
 Character character = tokeniser.consumeCharacterReference(Character.DECIMAL_DIGIT, false);
 assertNotNull(character);
 assertEquals(Character.EXCLAMATION_MARK, character.getValue());
 }

 @Test
 public void testCharacterReferenceWithName() {
 tokeniser.emit(new Character('&'));
 tokeniser.emit(new Character('#'));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('m'));
 tokeniser.emit(new Character('p'));
 tokeniser.emit(new Character(';'));
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertNotNull(character);
 assertEquals(Character.AMPERSAND, character.getValue());
 }

 @Test
 public void testCreateTagPending() {
 Token.StartTag startTag = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 startTag = tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 }

 @Test
 public void testCreateCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 }

 @Test
 public void testCreateDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 }

 @Test
 public void testErrorHandling() {
 tokeniser = new Tokeniser(new CharInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()), errors);
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emit(new Character('<'));
 tokeniser.emit(new Character('?'));
 tokeniser.emit(new Character('x'));
 tokeniser.emit(new Character('m'));
 tokeniser.emit(new Character('l'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('v'));
 tokeniser.emit(new Character('e'));
 tokeniser.emit(new Character('r'));
 tokeniser.emit(new Character('s'));
 tokeniser.emit(new Character('i'));
 tokeniser.emit(new Character('o'));
 tokeniser.emit(new Character('n'));
 tokeniser.emit(new Character('='));
 tokeniser.emit(new Character('1'));
 tokeniser.emit(new Character('.'));
 tokeniser.emit(new Character('0'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('e'));
 tokeniser.emit(new Character('n'));
 tokeniser.emit(new Character('c'));
 tokeniser.emit(new Character('o'));
 tokeniser.emit(new Character('d'));
 tokeniser.emit(new Character('i'));
 tokeniser.emit(new Character('n'));
 tokeniser.emit(new Character('g'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('U'));
 tokeniser.emit(new Character('T'));
 tokeniser.emit(new Character('F'));
 tokeniser.emit(new Character('-'));
 tokeniser.emit(new Character('8'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('E'));
 tokeniser.emit(new Character('N'));
 tokeniser.emit(new Character('C'));
 tokeniser.emit(new Character('o'));
 tokeniser.emit(new Character('d'));
 tokeniser.emit(new Character('i'));
 tokeniser.emit(new Character('n'));
 tokeniser.emit(new Character('g'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('C'));
 tokeniser.emit(new Character('h'));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('r'));
 tokeniser.emit(new Character('a'));
 tokeniser.emit(new Character('c'));
 tokeniser.emit(new Character('t'));
 tokeniser.emit(new Character('e'));
 tokeniser.emit(new Character('r'));
 tokeniser.emit(new Character(' '));
 tokeniser.emit(new Character('<'));
 tokeniser.emit(new Character('/'));
 tokeniser.emit(new Character('d'));
 tokeniser.emit(new Character('o'));
 tokeniser.emit(new Character('c'));
 tokeniser.emit(new Character('t'));
 tokeniser.emit(new Character('y'));
 tokeniser.emit(new Character('p'));
 tokeniser.emit(new Character('e'));
 tokeniser.emit(new Character('>'));
 tokeniser.emit(new Character(';'));

 List<ParseError> errorsList = errors.getErrors();
 assertEquals(1, errorsList.size());
 Assertions.assertEquals("Unexpected character '?' in doctype state", errorsList.get(0).getMessage());
 }
}