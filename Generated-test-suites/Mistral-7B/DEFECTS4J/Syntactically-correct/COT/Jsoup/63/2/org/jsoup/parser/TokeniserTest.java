package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader mockReader;
 private ParseErrorList mockErrors;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockReader = new CharacterReaderMock();
 mockErrors = new ParseErrorList();
 tokeniser = new Tokeniser(mockReader, mockErrors);
 }

 @Test
 public void testRead_DataState() {
 TokeniserState dataState = TokeniserState.Data;
 tokeniser.transition(dataState);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(replacementChar, token.charPositionInLine, 0);
 assertEquals(replacementChar, token.charPositionInLine, token.text().charAt(0));
 }

 @Test
 public void testRead_HtmlState() {
 TokeniserState htmlState = TokeniserState.Html;
 tokeniser.transition(htmlState);
 tokeniser.reader.mark('&');
 tokeniser.reader.consume('#');
 tokeniser.reader.consume('x');
 tokeniser.reader.consume('r');
 tokeniser.reader.consume(';');
 tokeniser.reader.reset();
 tokeniser.read();
 Token token = tokeniser.read();
 int[] codePoints = {0x0061};
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(codePoints[0], token.charPositionInLine, 0);
 assertEquals(Character.fromCodePoint(codePoints[0]), token.text().charAt(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_PendingToken() {
 tokeniser.emit(new Token.StartTag("div"));
 tokeniser.read();
 assertFalse(tokeniser.isEmitPending());
 tokeniser.read();
 assertTrue(tokeniser.isEmitPending());
 }

 @Test
 public void testEmit_StartTag() {
 StartTag expectedStartTag = new StartTag("div");
 tokeniser.emit(expectedStartTag);
 StartTag actualStartTag = tokeniser.emitPending();
 assertEquals(expectedStartTag, actualStartTag);
 }

 @Test
 public void testEmit_EndTag() {
 EndTag expectedEndTag = new EndTag("div");
 tokeniser.emit(expectedEndTag);
 EndTag actualEndTag = tokeniser.emitPending();
 assertEquals(expectedEndTag, actualEndTag);
 }

 @Test
 public void testEmit_Character() {
 Character expectedCharacter = new Character('a');
 tokeniser.emit(expectedCharacter);
 Character actualCharacter = tokeniser.emitPending();
 assertEquals(expectedCharacter, actualCharacter);
 }

 @Test
 public void testEmit_Comment() {
 Comment expectedComment = new Comment("-- this is a comment");
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 Comment actualComment = tokeniser.emitPending();
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testEmit_Doctype() {
 Doctype expectedDoctype = new Doctype("html", Arrays.asList("HTML 4.01 Transitional"));
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 Doctype actualDoctype = tokeniser.emitPending();
 assertEquals(expectedDoctype, actualDoctype);
 }

 @Test
 public void testConsumeCharacterReference_HexMode_ValidChar() {
 mockReader.mark('&');
 mockReader.consume('#');
 mockReader.consume('X');
 mockReader.consume('3');
 mockReader.consume('A');
 mockReader.consume(';');
 mockReader.reset();
 int[] expectedCodePoints = {0x0041};
 int[] actualCodePoints = tokeniser.consumeCharacterReference(null, false);
 assertEquals(expectedCodePoints, actualCodePoints);
 }

 @Test
 public void testConsumeCharacterReference_HexMode_InvalidChar() {
 mockReader.mark('&');
 mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('F'); mockReader.consume(';'); mockReader.reset();int [] expectedCodePoints = {replacementChar};int [] actualCodePoints = tokeniser.consumeCharacterReference(null, false);assertEquals(expectedCodePoints, actualCodePoints); }

 @Test
 public void testConsumeCharacterReference_Named_ValidEntity() {
 mockReader.mark('&'); mockReader.consume('a'); mockReader.consume('m'); mockReader.consume('p'); mockReader.consume(';'); mockReader.reset(); int[] expectedCodePoints = {0x0061}; int[] actualCodePoints = tokeniser.consumeCharacterReference(null, false); assertEquals(expectedCodePoints, actualCodePoints); }

 @Test
 public void testConsumeCharacterReference_Named_InvalidEntity() {
 mockReader.mark('&'); mockReader.consume('x'); mockReader.consume('y'); mockReader.consume('z'); mockReader.consume(';'); mockReader.reset(); int[] expectedCodePoints = {replacementChar}; int[] actualCodePoints = tokeniser.consumeCharacterReference(null, false); assertEquals(expectedCodePoints, actualCodePoints); }

 @Test
 public void testConsumeCharacterReference_Named_InvalidEntityWithSemicolon() {
 mockReader.mark('&'); mockReader.consume('x'); mockReader.consume('y'); mockReader.consume('z'); mockReader.consume(';'); mockReader.consume(';'); mockReader.reset(); int[] expectedCodePoints = {replacementChar}; int[] actualCodePoints = tokeniser.consumeCharacterReference(null, false); assertEquals(expectedCodePoints, actualCodePoints); }

 @Test
 public void testConsumeCharacterReference_Named_Attributes() {
 mockReader.mark('&'); mockReader.consume('a'); mockReader.consume('m'); mockReader.consume('p'); mockReader.consume(' '); mockReader.consume(' '); mockReader.consume('='); mockReader.consume('x'); mockReader.consume(';'); mockReader.consume('&'); mockReader.consume('x'); mockReader.consume('y'); mockReader.consume('z'); mockReader.consume(';'); mockReader.reset(); int[] expectedCodePoints = {0x0061, 0x0078}; int[] actualCodePoints = tokeniser.consumeCharacterReference(null, true); assertEquals(expectedCodePoints, actualCodePoints); }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeCharacterReference_Named_Attributes_InvalidCharacter() {
 mockReader.mark('&'); mockReader.consume('a'); mockReader.consume('m'); mockReader.consume('p'); mockReader.consume(' '); mockReader.consume(' '); mockReader.consume('='); mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('A'); mockReader.consume(';'); mockReader.reset(); int[] expectedCodePoints = {replacementChar}; int[] actualCodePoints = tokeniser.consumeCharacterReference(null, true); assertEquals(expectedCodePoints, actualCodePoints); }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_InvalidCharacterReference() {
 mockReader.mark('&'); mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('F'); mockReader.consume(';'); mockReader.reset(); tokeniser.characterReferenceError("character reference with invalid characters"); }

 @Test(expected = ParseError.class)
 public void testCharacterReferenceError_MissingSemicolon() {
 mockReader.mark('&'); mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('A'); mockReader.consume(' '); mockReader.consume('a'); mockReader.consume('b'); mockReader.reset(); tokeniser.characterReferenceError("missing semicolon"); }

 @Test(expected = ParseError.class)
 public void testError_UnexpectedCharacterInState() {
 mockReader.mark('&'); mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('A'); mockReader.consume(';'); mockReader.reset(); tokeniser.error("Unexpected character in input state [Data]"); }

 @Test(expected = ParseError.class)
 public void testEofError_UnexpectedEndOfFile() {
 mockReader.mark('&'); mockReader.consume('#'); mockReader.consume('X'); mockReader.consume('3'); mockReader.consume('A'); mockReader.consume(';'); mockReader.consume(';'); mockReader.reset(); tokeniser.eofError("Unexpectedly reached end of file (EOF) in input state [Data]"); }
}