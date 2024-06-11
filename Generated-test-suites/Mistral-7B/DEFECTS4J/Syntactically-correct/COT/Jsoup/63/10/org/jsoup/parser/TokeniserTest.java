package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.util.ParseError;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private ParseErrorList errors;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() {
 char[] input = {'<', 'h', 't', 'm', 'l', ' ', 'i', 'n', 'p', 'u', 't', '>', '\n'};
 characterReader = new CharacterReader(Arrays.asList(input));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, errors);
 }

 @Test
 public void testRead_EmptyInput_ReturnsNull() {
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_SelfClosingFlagNotAcknowledged_ThrowsError() {
 tokeniser.selfClosingFlagAcknowledged = false;
 assertThrows(ParseError.class, () -> tokeniser.read());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.advance(); // <
 tokeniser.advanceTransition(TokeniserState.Data);
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("html", token.tagName());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedCharsWhenCharsPending() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.emit("abc");
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("abc", token.data());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedCharacterReference() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.mark();
 characterReader.consume('#');
 tokeniser.advanceTransition(TokeniserState.CharacterReference);
 int[] codePoints = tokeniser.consumeCharacterReference('\u00A9', true);
 assertEquals(codePoints[0], '©');
 tokeniser.reader.rewindToMark(); // reset reader
 }

 @Test(expected = ParseError.class)
 public void testRead_DataState_ThrowsErrorOnInvalidCharacterReference() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('#');
 characterReader.consume('x');
 tokeniser.advanceTransition(TokeniserState.CharacterReference);
 }

 @Test
 public void testRead_CharacterReferenceState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('#');
 characterReader.consume('x');
 tokeniser.advanceTransition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, true);
 Token token = tokeniser.read();
 assertEquals(TokenType.Character, token.type);
 assertEquals("\u0078", token.data()); // 'x' UTF-8 code unit
 }

 @Test
 public void testRead_TagNameState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('t');
 characterReader.consume('a');
 characterReader.consume('g');
 tokeniser.advanceTransition(TokeniserState.TagName);
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("tag", token.tagName());
 }

 @Test
 public void testRead_TagNameState_ReturnsExpectedEndTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('/');
 characterReader.consume('t');
 characterReader.consume('a');
 characterReader.consume('g');
 tokeniser.advanceTransition(TokeniserState.TagName);
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("tag", token.tagName());
 }

 @Test
 public void testRead_ScriptDataState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.createTempBuffer();
 characterReader.consume('<');
 characterReader.consume('s');
 characterReader.consume('c', 2);
 characterReader.consume('r', 2);
 characterReader.consume('i', 2);
 characterReader.consume('p', 2);
 characterReader.consume('t', 2);
 characterReader.consume('>');
 tokeniser.advanceTransition(TokeniserState.ScriptData);
 Token token = tokeniser.read();
 assertEquals(TokenType.Text, token.type);
 assertEquals("<script>", token.data());
 }

 @Test
 public void testRead_ScriptDataState_ReturnsExpectedScriptTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.createTempBuffer();
 characterReader.consume('<');
 characterReader.consume('s', 2);
 characterReader.consume('c', 2);
 characterReader.consume('r', 2);
 characterReader.consume('i', 2);
 characterReader.consume('p', 2);
 characterReader.consume('t', 2);
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.ScriptData);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("script", token.tagName());
 }

 @Test
 public void testRead_ScriptDataState_ReturnsExpectedScriptEndTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.createTempBuffer();
 characterReader.consume('/');
 characterReader.consume('s', 2);
 characterReader.consume('c', 2);
 characterReader.consume('r', 2);
 characterReader.consume('i', 2);
 characterReader.consume('p', 2);
 characterReader.consume('t', 2);
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.ScriptData);
 tokeniser.emitTagPending();
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.EndTag, token.type);
 assertEquals("script", token.tagName());
 }

 @Test
 public void testRead_ScriptDataState_ReturnsExpectedErrorOnInvalidScriptTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.createTempBuffer();
 characterReader.consume('<');
 characterReader.consume('s', 2);
 characterReader.consume('c', 2);
 characterReader.consume('r', 2);
 characterReader.consume('i', 2);
 characterReader.consume('p', 2);
 characterReader.consume('x', 2); // invalid character
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.ScriptData);
 tokeniser.error(TokeniserState.Data);
 }

 @Test
 public void testRead_DoctypeState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('<');
 characterReader.consume('d', 2);
 characterReader.consume('o', 2);
 characterReader.consume('c', 2);
 characterReader.consume('t', 2);
 characterReader.consume('y', 2);
 characterReader.consume('p', 2);
 characterReader.consume('e', 2);
 characterReader.consume('>');
 tokeniser.advanceTransition(TokeniserState.Doctype);
 Token token = tokeniser.read();
 assertEquals(TokenType.Doctype, token.type);
 assertEquals("<!DOCTYPE", token.data());
 }

 @Test
 public void testRead_DoctypeState_ReturnsExpectedDoctypeTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.createDoctypePending();
 characterReader.consume('<');
 characterReader.consume('d', 2);
 characterReader.consume('o', 2);
 characterReader.consume('c', 2);
 characterReader.consume('t', 2);
 characterReader.consume('y', 2);
 characterReader.consume('p', 2);
 characterReader.consume('e', 2);
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.Doctype);
 tokeniser.emitDoctypePending();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("!DOCTYPE", token.tagName());
 }

 @Test
 public void testRead_CommentState_ReturnsExpectedToken() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('<');
 characterReader.consume('/', 2);
 characterReader.consume('c', 2);
 characterReader.consume('o', 2);
 characterReader.consume('m', 2);
 characterReader.consume('m', 2);
 characterReader.consume('e', 2);
 characterReader.consume('n', 2);
 characterReader.consume('t', 2);
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.Comment);
 Token token = tokeniser.read();
 assertEquals(TokenType.Comment, token.type);
 assertEquals("<!--", token.data());
 }

 @Test
 public void testRead_CommentState_ReturnsExpectedCommentTag() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('<');
 characterReader.consume('/', 2);
 characterReader.consume('c', 2);
 characterReader.consume('o', 2);
 characterReader.consume('m', 2);
 characterReader.consume('m', 2);
 characterReader.consume('e', 2);
 characterReader.consume('n', 2);
 characterReader.consume('t', 2);
 characterReader.consume('>', 2);
 tokeniser.advanceTransition(TokeniserState.Comment);
 tokeniser.emitCommentPending();
 Token token = tokeniser.read();
 assertEquals(TokenType.StartTag, token.type);
 assertEquals("-->", token.tagName());
 }

 @Test
 public void testRead_ErrorState_ThrowsError() {
 tokeniser.selfClosingFlagAcknowledged = true;
 tokeniser.error(TokeniserState.Data);
 assertThrows(ParseError.class, () -> tokeniser.read());
 }

 @Test
 public void testRead_EOFState_ThrowsError() {
 tokeniser.selfClosingFlagAcknowledged = true;
 characterReader.consume('a');
 tokeniser.error(TokeniserState.Data);
 tokeniser.advanceTransition(TokeniserState.EOF);
 assertThrows(ParseError.class, () -> tokeniser.read());
 }

}