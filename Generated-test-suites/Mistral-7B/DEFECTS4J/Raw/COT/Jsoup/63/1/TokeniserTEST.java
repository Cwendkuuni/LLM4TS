package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokeniserTest;
import org.junit.Test;

public class TokeniserTest {

 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;
 private Tokeniser tokeniser;

 @Test
 public void testConstructor() {
 characterReader = new CharacterReader("Test");
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testRead_Data() {
 tokeniser.transition(TokeniserState.Data);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.CharacterData, token.type);
 assertEquals("T", token.data);
 }

 @Test
 public void testRead_StartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emitPendingToken();
 assertNull(tokeniser.read());

 Token startTag = tokeniser.createTagPending(true);
 startTag.tagName = "div";
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName);
 }

 @Test
 public void testRead_EndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitPendingToken();
 assertNull(tokeniser.read());

 tokeniser.createEndTagPending();
 tokeniser.emitEndTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("div", token.tagName);
 }

 @Test
 public void testRead_Character() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPendingToken();
 assertNull(tokeniser.read());

 tokeniser.emit("T");
 tokeniser.emit("e");
 tokeniser.emit("s");
 tokeniser.emit("t");
 token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals("Test", token.data);
 }

 @Test
 public void testRead_Doctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitPendingToken();
 assertNull(tokeniser.read());

 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertEquals(Doctype.class, token.getClass());
 assertSame("html", token.publicId);
 assertSame("", token.systemId);
 assertSame("HTML", token.name);
 }

 @Test
 public void testRead_Comment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitPendingToken();
 assertNull(tokeniser.read());

 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("", token.data);
 }

 @Test
 public void testConsumeCharacterReference_Valid() {
 int[] expected = {'\u00A9'};
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPendingToken();
 tokeniser.emit("&copy;");
 token = tokeniser.read();
 assertSame(Character.class, token.getClass());
 assertEquals("\u00A9", token.data);
 assertArrayEquals(expected, token.codePoints);
 }

 @Test
 public void testConsumeCharacterReference_Invalid_Named() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPendingToken();
 tokeniser.emit("&foo;");
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testConsumeCharacterReference_Invalid_Named_WithAttribute() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPendingToken();
 tokeniser.emit("&foo; class=\"bar\"");
 token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testConsumeCharacterReference_Invalid_Named_MissingSemicolon() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitPendingToken();tokeniser.emit("&foo");tokeniser.emit(" bar");token = tokeniser.read(); assertNull(token); }

 @Test public void testConsumeCharacterReference_Invalid_Named_InvalidCharacter() { // This test case requires a custom CharacterReader // that supports consuming an invalid character sequence and // returning null.
 tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.emit("&foo&#x1F0000;"); token = tokeniser.read(); assertNull(token); }

 @Test public void testConsumeCharacterReference_Invalid_Number() { tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.emit("#12345ABC"); token = tokeniser.read(); assertNull(token); }

 @Test public void testConsumeCharacterReference_Invalid_Number_InvalidChar() { tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.emit("#12345ABCx"); token = tokeniser.read(); assertNull(token); }

 @Test public void testConsumeCharacterReference_Invalid_Number_MissingSemicolon() { tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.emit("#12345ABC"); token = tokeniser.read(); assertNull(token); }

 @Test public void testCreateTagPending() { Token tag = tokeniser.createTagPending(true); assertEquals(StartTag.class, tag.getClass()); }

 @Test public void testCreateEndTagPending() { Token tag = tokeniser.createEndTagPending(); assertEquals(EndTag.class, tag.getClass()); }

 @Test public void testEmitTagPending() { StartTag startTag = tokeniser.createTagPending(true); startTag.tagName = "div"; tokeniser.emitTagPending(); token = tokeniser.read(); assertEquals(Token.TokenType.StartTag, token.type); assertEquals("div", token.tagName); }

 @Test public void testEmitEndTagPending() { EndTag endTag = tokeniser.createEndTagPending(); tokeniser.emitEndTagPending(); token = tokeniser.read(); assertEquals(Token.TokenType.EndTag, token.type); assertEquals("div", token.tagName); }

 @Test public void testEmitCommentPending() { tokeniser.createCommentPending(); tokeniser.emitCommentPending(); token = tokeniser.read(); assertEquals(Token.TokenType.Comment, token.type); assertEquals("", token.data); }

 @Test public void testError() { tokeniser.error(TokeniserState.Data); assertNull(tokeniser.read()); }

 @Test public void testEofError() { characterReader = new CharacterReader("Test"); parseErrorList = new ParseErrorList(); tokeniser = new Tokeniser(characterReader, parseErrorList); tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); characterReader.setEmpty(); token = tokeniser.read(); assertNull(token); }

 @Test public void testCharacterReferenceError() { tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.emit("&foo"); tokeniser.error(TokeniserState.Data); token = tokeniser.read(); assertNull(token); }

 @Test public void testError_State() { characterReader = new CharacterReader("Test"); parseErrorList = new ParseErrorList(); tokeniser = new Tokeniser(characterReader, parseErrorList); tokeniser.emitPendingToken(); tokeniser.transition(TokeniserState.StartTag); tokeniser.error(TokeniserState.StartTag); token = tokeniser.read(); assertNull(token); }

 @Test public void testIsAppropriateEndTagToken() { tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList()); tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.createStartTagPending("div"); tokeniser.emitTagPending(); tokeniser.createEndTagPending("div"); tokeniser.emitEndTagPending(); token = tokeniser.read(); assertEquals(Token.TokenType.CharacterData, token.type); tokeniser.transition(TokeniserState.Data); tokeniser.emitPendingToken(); tokeniser.createStartTagPending("div"); tokeniser.emitTagPending(); tokeniser.createEndTagPending("p"); tokeniser.emitEndTagPending(); token = tokeniser.read(); assertEquals(Token.TokenType.EndTag, token.type); assertEquals("div", token.tagName); }

 @Test public void testUnescapeEntities() { String input = "&copy; &eacute; &apos; &amp; &nbsp;"; String expected = "© é ’ & amp; 
r\n"; String actual = tokeniser.unescapeEntities(false).toString(); assertEquals(expected, actual); }

}