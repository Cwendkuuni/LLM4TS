package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 characterReader = new CharacterReader("test");
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testRead_EmptyInput() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_Whitespace() {
 tokeniser.emit(" \t\n\r\f ");
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("\u0020", token.data());
 }

 @Test
 public void testRead_SingleCharacter() {
 tokeniser.emit('A');
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("A", token.data());
 }

 @Test
 public void testRead_MultipleCharacters() {
 tokeniser.emit("ABC");
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.Data, token.type);
 assertEquals("ABC", token.data());
 }

 @Test
 public void testRead_SelfClosingTag() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.emitStartTag("img");
 tokeniser.emitEndTag("img");
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("img", token.data());
 }

 @Test
 public void testRead_TagWithAttributes() {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.emitStartTag("img", true, null);
 tokeniser.emitAttribute("src", "image.png");
 tokeniser.emitAttribute("alt", "Alternate text");
 tokeniser.emitEndTag("img");
 token = tokeniser.read();
 assertNotNull(token);
 assertTrue(token.attributes().hasKey("src"));
 assertEquals("image.png", token.attributes().getValue("src"));
 assertTrue(token.attributes().hasKey("alt"));
 assertEquals("Alternate text", token.attributes().getValue("alt"));
 }

 @Test
 public void testRead_CharRef() {
 tokeniser.emit("#&x26;"); // "&amp;"
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("&amp;", token.data());
 }

 @Test
 public void testRead_NamedEntity() {
 tokeniser.emit("#entname;"); // "<entname>"
 token = tokeniser.read();
 assertNotNull(token);
 assertEquals(Token.TokenType.Data, token.type);
 assertEquals("<entname>", token.data());
 }

 @Test(expected = AssertionError.class)
 public void testRead_InvalidCharRef() {
 tokeniser.emit("#&X;");
 tokeniser.read();
 fail("Expected an AssertionError but did not receive one");
 }

 @Test(expected = AssertionError.class)
 public void testRead_InvalidNamedEntity() {
 tokeniser.emit("#invalidEntity;");
 tokeniser.read();
 fail("Expected an AssertionError but did not receive one");
 }

 @Test(expected = ParseError.class)
 public void testRead_ParseError_UnexpectedCharacter() {
 tokeniser.emit(">");
 tokeniser.read();
 assertTrue(parseErrorList.getErrors().size() > 0);
 assertEquals("Unexpected character '>' in state [Data]", parseErrorList.getErrors().get(0).getMessage());
 }

 @Test(expected = ParseError.class)
 public void testRead_ParseError_EOF() {
 tokeniser.emit("AB");
 tokeniser.read();
 tokeniser.read();
 tokeniser.read(); // empty
 assertTrue(parseErrorList.getErrors().size() > 0);
 assertEquals("Unexpectedly reached end of file (EOF) in state [Data]", parseErrorList.getErrors().get(0).getMessage());
 }

 @Test
 public void testEmit() {
 tokeniser.emit(new Token.Character('A'));
 tokeniser.emit(new Token.Character('B'));
 tokeniser.emit(new Token.Data("C"));
 Token[] tokens = tokeniser.getReadTokens(3);
 assertEquals("ABC", tokens[0].data());
 assertEquals(Token.TokenType.Character, tokens[1].type);
 assertEquals('B', tokens[1].data().charAt(0));
 assertEquals(Token.TokenType.Data, tokens[2].type);
 assertEquals("C", tokens[2].data());
 }

 @Test
 public void testGetState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testTransition() {
 tokeniser.transition(TokeniserState.StartTagBegin);
 assertEquals(TokeniserState.StartTagBegin, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() {
 tokeniser.transition(TokeniserState.StartTagBegin);
 tokeniser.advanceTransition(TokeniserState.StartTag);
 assertEquals(TokeniserState.StartTag, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 tokeniser.emitStartTag("img");
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.emitEndTag("img");
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }
}