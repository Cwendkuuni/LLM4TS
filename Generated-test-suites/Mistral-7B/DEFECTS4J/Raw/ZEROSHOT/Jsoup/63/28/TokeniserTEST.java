package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 characterReader = new MockCharacterReader();
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testReadEmptyInput() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacter() {
 characterReader.append("a");
 characterReader.advance();
 Token token = tokeniser.read();
 assertEquals("Character", token.type.toString());
 assertEquals('a', token.charSequence().charAt(0));
 }

 @Test
 public void testReadMultipleCharacters() {
 characterReader.append("ab");
 characterReader.advance();
 characterReader.advance();
 Token token = tokeniser.read();
 assertEquals("Data", token.type.toString());
 assertEquals("ab", token.charSequence().toString());
 }

 @Test
 public void testReadSelfClosingTag() {
 characterReader.append("<img src=\"image.jpg\" />");
 characterReader.advance();
 characterReader.advance();
 Token token = tokeniser.read();
 assertEquals("StartTag", token.type.toString());
 assertEquals("img", token.tagName());
 assertTrue(token.selfClosing());
 }

 @Test
 public void testReadTagWithAttributes() {
 characterReader.append("<div id=\"div1\" class=\"class1\" style=\"color: red;\">Content</div>");
 characterReader.advance();
 characterReader.advance();
 Token token = tokeniser.read();
 assertEquals("StartTag", token.type.toString());
 assertEquals("div", token.tagName());
 assertNotNull(token.tag().tagName());
 assertEquals("div1", token.tag().attr("id").get());
 assertEquals("class1", token.tag().attr("class").get());
 assertEquals("color: red;", token.tag().attr("style").get());
 }

 @Test
 public void testReadComment() {
 characterReader.append("<!-- This is a comment -->");
 characterReader.advance();
 characterReader.advance(3);
 Token token = tokeniser.read();
 assertEquals("Comment", token.type.toString());
 assertEquals("This is a comment ", token.charSequence().toString());
 }

 @Test
 public void testReadScriptTag() {
 characterReader.append("<script type=\"text/javascript\">console.log('Hello World')</script>");
 characterReader.advance();
 characterReader.advance();
 tokeniser.transition(TokeniserState.SCRIPT);
 tokeniser.emit('S');
 tokeniser.emit('C');
 tokeniser.emit('R');
 tokeniser.emit('I');
 tokeniser.emit('P');
 tokeniser.emit(' ');
 tokeniser.emit('T');
 tokeniser.emit(' ');
 tokeniser.emit('t');
 tokeniser.emit('e');
 tokeniser.emit('x');
 tokeniser.emit('t');
 tokeniser.emit('/');
 tokeniser.emit('J');
 tokeniser.emit('a');
 tokeniser.emit('v');
 tokeniser.emit('a');
 tokeniser.emit('S');
 tokeniser.emit('c');
 tokeniser.emit('r');
 tokeniser.emit('i');
 tokeniser.emit('p');
 tokeniser.emit('t');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 assertEquals("EndTag", token.type.toString());
 assertEquals("script", token.tagName());
 }

 @Test
 public void testReadDoctype() {
 characterReader.append("<!DOCTYPE html>");
 characterReader.advance();
 characterReader.advance();
 characterReader.advance();
 tokeniser.transition(TokeniserState.DOCTYPE);
 tokeniser.emit('D');
 tokeniser.emit('O');
 tokeniser.emit('C');
 tokeniser.emit('T');
 tokeniser.emit('Y');
 tokeniser.emit('P');
 tokeniser.emit('E');
 tokeniser.emit(' ');
 tokeniser.emit('H');
 tokeniser.emit('T');
 tokeniser.emit('M');
 tokeniser.emit('L');
 tokeniser.emit(' ');
 tokeniser.emit('html');
 tokeniser.emit('>');
 Token token = tokeniser.read();
 assertEquals("Doctype", token.type.toString());
 assertEquals("html", token.tagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadAfterError() {
 characterReader.append("invalid input");
 characterReader.advance();
 tokeniser.error("Invalid character 'i' in input state [Data]");
 tokeniser.read();
 }

 @Test
 public void testReadWithSelfClosingFlagAcknowledged() {
 characterReader.append("<p></p>");
 characterReader.advance();
 characterReader.advance();
 tokeniser.read(); // read start tag
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.read(); // read end tag
 assertTrue(tokeniser.selfClosingFlagAcknowledged());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&amp; &gt; &lt; &nbsp; &amp;nbsp; &quot; &apos; &eacute; &egrave; &iexcl; &cent; &#x1F600;";
 String expected = "&amp; &gt; &lt; &nbsp; &nbsp; \" ' é egrave í ! &#x1F600;";
 String output = tokeniser.unescapeEntities(false);
 assertEquals(expected, output);
 }

 @Test
 public void testReadAttributeWithCharacterReference() {
 characterReader.append("<div id=\"test\" class=\"test &aacute; class\" />");
 characterReader.advance();
 characterReader.advance();
 Token token = tokeniser.read();
 assertEquals("StartTag", token.type.toString());
 assertEquals("div", token.tagName());
 assertEquals("test", token.tag().attr("id").get());
 assertEquals("test &aacute; class", token.tag().attr("class").get());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReferenceWithInvalidCharacter() {
 characterReader.append("&a");
 characterReader.advance();
 tokeniser.consumeCharacterReference('b', false);
 }

 @Test
 public void testConsumeCharacterReferenceWithHexCodePoint() {
 characterReader.append("&#x1F600;");
 characterReader.advance();
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, codePoints.length);
 assertEquals(0x1F600, codePoints[0]);
 }

 @Test
 public void testConsumeCharacterReferenceWithNamedEntity() {
 characterReader.append("&nbsp;");
 characterReader.advance();
 int[] codePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, codePoints.length);
 assertEquals(32, codePoints[0]); // space character
 }

 @Test
 public void testConsumeCharacterReferenceWithNamedEntityInAttribute() {
 characterReader.append("<div class=\"test &eacute; test\" />");
 characterReader.advance();
 characterReader.advance();
 tokeniser.consumeCharacterReference(null, true);
 String attribute = tokeniser.tag().attr("class").get();
 assertEquals("test é test", attribute);
 }

 @Test
 public void testTransitionAndAdvance() {
 characterReader.append("<html><head><title>Test</title></head><body><p>Hello World</p></body></html>");
 characterReader.advance();
 characterReader.advance();
 tokeniser.transition(TokeniserState.START_TAG);
 tokeniser.advanceTransition(TokeniserState.TAG_NAME);
 assertEquals("html", tokeniser.tagPending.tagName());
 tokeniser.transition(TokeniserState.TAG_NAME);
 tokeniser.advanceTransition(TokeniserState.TAG_OVER);
 tokeniser.transition(TokeniserState.EMPTY_TAG);
 tokeniser.transition(TokeniserState.DATA);
 tokeniser.transition(TokeniserState.SCRIPT);
 }
}