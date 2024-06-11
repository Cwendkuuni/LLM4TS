package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.entities.Entities;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private List<Token> parsedTokens;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 parsedTokens = new ArrayList<>();
 }

 @Test
 public void testReadEmptyStream() {
 tokeniser.read();
 assertEquals(0, parsedTokens.size());
 }

 @Test
 public void testBasicReadCharacter() {
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');

 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("test", token.string());

 parsedTokens.add(token);
 }

 @Test
 public void testReadCharacterReference() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference('&');
 tokeniser.consumeCharacterReference('#');
 tokeniser.consume('x');
 tokeniser.consumeCharacterReference(';');

 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals('\u003C', token.charAt(0));

 parsedTokens.add(token);
 }

 @Test
 public void testReadNamedCharacterReference() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consume('&');
 tokeniser.consumeLetterSequence("ampersand");
 tokeniser.consumeCharacterReference(';');

 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals('\u0026', token.charAt(0));

 parsedTokens.add(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testReadCharacterReferenceInvalidSequence() throws IOException {
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference('&');
 tokeniser.consumeCharacterReference(';');

 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadMultipleCharacters() {
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');

 Token firstToken = tokeniser.read();
 assertEquals(TokenType.Character, firstToken.type);
 assertEquals("test", firstToken.string());
 parsedTokens.add(firstToken);

 tokeniser.emit(' ');
 tokeniser.emit('h');
 tokeniser.emit('i');
 tokeniser.emit('j');

 Token secondToken = tokeniser.read();
 assertEquals(TokenType.Character, secondToken.type);
 assertEquals(" hij", secondToken.string());
 parsedTokens.add(secondToken);
 }

 @Test
 public void testReadStartTag() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume('>');

 Token tag = tokeniser.read();
 Token.StartTag startTag = (Token.StartTag) tag;
 assertEquals("div", startTag.tagName());
 assertNull(startTag.attributes());

 parsedTokens.add(tag);
 }

 @Test
 public void testReadStartTagWithAttributes() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume(' ');
 tokeniser.consumeLetterSequence("class");
 tokeniser.consume('=');
 tokeniser.consume('"');
 tokeniser.consume('b');
 tokeniser.consume('"');
 tokeniser.consume('>');

 Token tag = tokeniser.read();Token.StartTag startTag = (Token.StartTag) tag;
 assertEquals("div", startTag.tagName());
 assertEquals(Arrays.asList(new Token.Attribute("class", "b")), startTag.attributes());

 parsedTokens.add(tag);
 }

 @Test(expected = ParseError.class)
 public void testReadStartTagInvalidAttributeName() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume(' ');
 tokeniser.consumeLetterSequence("class=");
 tokeniser.consume('x'); // invalid char
 tokeniser.consume('>');

 Token tag = tokeniser.read();
 assertNull(tag);

 assertEquals(1, errors.size());
 assertEquals("Unexpected character 'x' in input state [AttributeNameState]", errors.get(0).getMessage());
 }

 @Test(expected = ParseError.class)
 public void testReadStartTagMissingEndTag() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume('>');

 Token tag = tokeniser.read();
 assertEquals(TokenType.StartTag, tag.type);
 assertSame(tag, parsedTokens.get(parsedTokens.size()-1));

 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [StartTagState]", errors.get(0).getMessage());
 }

 @Test
 public void testReadEndTag() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume(' ');
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume('>');

 Token tag = tokeniser.read();
 Token.EndTag endTag = (Token.EndTag) tag;
 assertEquals("div", endTag.tagName());
 assertNull(endTag.attributes());

 parsedTokens.add(tag);
 }

 @Test
 public void testReadMultipleTokens() throws IOException {
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('s');
 tokeniser.emit('t');
 tokeniser.emit(' ');
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume(' ');
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume(' ');
 tokeniser.consumeLetterSequence("class");
 tokeniser.consume('=');
 tokeniser.consume('"');
 tokeniser.consumeLetterSequence("b");
 tokeniser.consume('"');
 tokeniser.consume(' ');
 tokeniser.consume('>');
 tokeniser.emit(' ');
 tokeniser.emit('h');
 tokeniser.emit('i');
 tokeniser.emit('j');

 List<Token> tokens = Arrays.asList(
 new Token.Character("test"),
 new Token.StartTag("div", Arrays.asList(new Token.Attribute("class", "b"))),
 new Token.Character(" hij")
 );

 for (int i = 0; i < tokens.size(); i++) {
 Token expectedToken = tokens.get(i);
 Token actualToken = tokeniser.read();
 assertEquals(expectedToken.type, actualToken.type);
 assertEquals(expectedToken.string(), actualToken.string());
 parsedTokens.add(actualToken);
 }
 }

 @Test(expected = ParseError.class)
 public void testReadEndTagWithoutStartTag() throws IOException {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.consume('<');
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.consume('>');

 Token tag = tokeniser.read();
 assertNull(tag);

 assertEquals(1, errors.size());
 assertEquals("Unexpected character '/' in input state [StartTagState]", errors.get(0).getMessage());
 }

 @Test
 public void testReadEndTagWithAppropriateStartTag() throws IOException {
 String input = "<div class=\"b\"></div>";
 setUpWithInput(input);

 Token tag = tokeniser.read();
 Token.EndTag endTag = (Token.EndTag) tag;
 assertEquals("div", endTag.tagName());
 assertNull(endTag.attributes());

 parsedTokens.add(tag);
 }

 @Test
 public void testReadEndTagWithInappropriateStartTag() throws IOException {
 String input = "<div class=\"b\"></p>";
 setUpWithInput(input);

 assertEquals(1, errors.size());
 assertEquals("Attributes incorrectly present on end tag", errors.get(0).getMessage());
 }

 private void setUpWithInput(String input) {
 reader = new CharacterReader(new ByteArrayInputStream(input.getBytes()));
 tokeniser = new Tokeniser(reader, errors);
 parsedTokens.clear();
 }

 @After
 public void tearDown() {
 reader.close();
 errors.clear();
 }

}