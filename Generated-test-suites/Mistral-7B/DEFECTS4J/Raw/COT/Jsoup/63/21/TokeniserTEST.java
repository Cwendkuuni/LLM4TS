package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader mockCharacterReader;
 private ParseErrorList errorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockCharacterReader = new MockCharacterReader();
 errorList = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, errorList);
 }

 @Test
 public void testRead_EmptyInput_ReturnsNull() {
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_SingleCharacter_ReturnsCharacterToken() {
 mockCharacterReader.feed('a');
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testRead_String_ReturnsCharacterToken() {
 mockCharacterReader.feed('a');
 tokeniser.emit("b");
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 }

 @Test
 public void testRead_StringWithMultipleCharacters_ReturnsCharacterTokenWithMultipleCharacters() {
 mockCharacterReader.feed('a');
 tokeniser.emit("bc");
 assertNotNull(tokeniser.read());
 Token charToken = tokeniser.read();
 assertEquals(Token.TokenType.Character, charToken.type);
 assertEquals("bc", charToken.data());
 }

 @Test
 public void testRead_SelfClosingTag_ReturnsCharacterTokenWithTagNameAndSelfClosingFlagSet() {
 mockCharacterReader.feed('<', 'i', 'f');
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.emit("iframe");
 tokeniser.acknowledgeSelfClosingFlag();
 assertNotNull(tokeniser.read());
 Token tagToken = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tagToken.type);
 assertEquals("iframe", tagToken.tagName());
 assertTrue(tagToken.selfClosing());
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingTagWithError_ThrowsIllegalStateException() {
 mockCharacterReader.feed('<', 'i', 'f');
 tokeniser.transition(TokeniserState.InTagName);
 tokeniser.emit("iframe");
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.error(TokeniserState.InTag);
 assertThrows(IllegalStateException.class, tokeniser::read);
 }

 @Test
 public void testRead_EndTag_ReturnsEndTagToken() {
 mockCharacterReader.feed('<', '/', 'i', 'f');
 tokeniser.transition(TokeniserState.InEndTagName);
 tokeniser.emit("iframe");
 assertNotNull(tokeniser.read());
 Token endTagToken = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, endTagToken.type);
 assertEquals("iframe", endTagToken.tagName());
 }

 @Test
 public void testRead_ScriptTag_ReadsWithDataUntilEndTag() {
 mockCharacterReader.feed('<', 's', 'c', 'r', 'i', 'p', 't', ' ', 's', 'c', 'r', 'i', 'p', 't', ' ', '>', 's', 'c', 'r', 'i', 'p', 't', ' ', 's', 'c', 'r', 'i', 'p', '<', '/', 's', 'c', 'r', 'i', 'p', 't', '>', '>');
 tokeniser.transition(TokeniserState.Script);
 assertNull(tokeniser.read()); // consume <script>
 assertNotNull(tokeniser.read()); // consume script data
 Token scriptToken = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, scriptToken.type);
 assertEquals("script", scriptToken.tagName());
 }

 @Test
 public void testRead_Doctype_ReadsWithPublicIdentifierAndSystemIdentifier() {
 mockCharacterReader.feed("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 assertNull(tokeniser.read()); // consume <!DOCTYPE
 tokeniser.transition(TokeniserState.Doctype);
 assertNotNull(tokeniser.read()); // consume PUBLIC "
 Token doctypeToken = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, doctypeToken.type);
 assertEquals("html", doctypeToken.publicIdentifier());
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctypeToken.systemIdentifier());
 }

 @Test
 public void testRead_InvalidCharacterReference_ReturnsReplacementCharacterToken() {
 mockCharacterReader.feed('#');
 tokeniser.error("character reference with no numerals");
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals(Character.replacementChar, tokeniser.read().data());
 }

 @Test
 public void testRead_InvalidNamedCharacterReference_ReturnsNull() {
 mockCharacterReader.feed('#', 'x');
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_NamedCharacterReferenceWithSemicolon_ReturnsCharacterTokenWithCorrespondingCodePoint() {
 mockCharacterReader.feed('#', 'A', ';');
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals("\u0041", tokeniser.read().data());
 }

 @Test
 public void testRead_InvalidNamedCharacterReferenceWithoutSemicolon_ReturnsNull() {
 mockCharacterReader.feed('#', 'A');
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_NextCharacterIsNotExpectedCharacterReference_ReturnsNull() {
 mockCharacterReader.feed('#', 'x');
 tokeniser.error("invalid named referenece '%s'", "x");
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_NextCharacterIsNotExpectedCharacterReferenceAndIsALetter_ReturnsCharacterTokenWithCharacter() {
 mockCharacterReader.feed('#', 'x', 'a');
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals("a", tokeniser.read().data());
 }

 @Test
 public void testRead_NextCharacterIsNotExpectedCharacterReferenceAndIsADigit_ReturnsCharacterTokenWithCharacter() {
 mockCharacterReader.feed('#', 'x', '1');
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals("1", tokeniser.read().data());
 }

 @Test
 public void testRead_NextCharacterIsNotExpectedCharacterReferenceAndIsANonCharacterReferenceCharacter_ReturnsCharacterTokenWithCharacter() {
 mockCharacterReader.feed('#', 'x', '\t');
 assertNotNull(tokeniser.read());
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals("\t", tokeniser.read().data());
 }

 @Test
 public void testRead_WithErrorState_ReturnsNull() {
 mockCharacterReader.feed('a');
 tokeniser.error("Unexpected character '%s' in input state [%s]", 'a', TokeniserState.Data);
 assertNull(tokeniser.read());
 }

 @Test
 public void testRead_WithEOFState_ThrowsIllegalStateException() {
 mockCharacterReader.feed('a');
 tokeniser.error("Unexpectedly reached end of file (EOF) in input state [%s]", TokeniserState.Data);
 assertThrows(IllegalStateException.class, tokeniser::read);
 }

 @Test
 public void testRead_WithDataBuffer_ReturnsCharacterTokenWithDataFromBuffer() {
 StringBuilder dataBuffer = new StringBuilder("abc");
 tokeniser.createTempBuffer();
 tokeniser.emit(dataBuffer.toString());
 assertNull(tokeniser.read()); // consume <script>
 tokeniser.transition(TokeniserState.Script);
 assertNotNull(tokeniser.read()); // consume script data from buffer
 assertEquals(Token.TokenType.Character, tokeniser.read().type);
 assertEquals("a", tokeniser.read().data());
 }

 @Test
 public void testCreateTagPending_StartTag_ReturnsStartTagPendingToken() {
 Token.StartTag startTagPending = tokeniser.createTagPending(true);
 assertEquals("", startTagPending.tagName());
 assertNull(startTagPending.attributes());
 }

 @Test
 public void testCreateTagPending_EndTag_ReturnsEndTagPendingToken() {
 Token.EndTag endTagPending = tokeniser.createTagPending(false);
 assertEquals("", endTagPending.tagName());
 assertNull(endTagPending.attributes());
 }

 @Test
 public void testEmitTagPending_StartTag_EmitsStartTagToken() {
 Token.StartTag startTagPending = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 assertNotNull(tokeniser.read());
 Token startTagToken = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, startTagToken.type);
 assertEquals(startTagPending.tagName(), startTagToken.tagName());
 assertNull(startTagToken.attributes());
 }

 @Test
 public void testEmitTagPending_EndTag_EmitsEndTagToken() {
 Token.EndTag endTagPending = tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 assertNotNull(tokeniser.read());
 Token endTagToken = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, endTagToken.type);
 assertEquals(endTagPending.tagName(), endTagToken.tagName());
 assertNull(endTagToken.attributes());
 }

 @Test
 public void testCreateCommentPending_ReturnsCommentPendingToken() {
 Token.Comment commentPending = tokeniser.createCommentPending();
 assertEquals("", commentPending.data());
 }

 @Test
 public void testEmitCommentPending_EmitsCommentToken() {
 Token.Comment commentPending = tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertNotNull(tokeniser.read());
 Token commentToken = tokeniser.read();
 assertEquals(Token.TokenType.Comment, commentToken.type);
 assertEquals(commentPending.data(), commentToken.data());
 }

 @Test
 public void testCreateDoctypePending_ReturnsDoctypePendingToken() {
 Token.Doctype doctypePending = tokeniser.createDoctypePending();
 assertEquals("", doctypePending.publicIdentifier());
 assertEquals("", doctypePending.systemIdentifier());
 }

 @Test
 public void testEmitDoctypePending_EmitsDoctypeToken() {
 Token.Doctype doctypePending = tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 assertNotNull(tokeniser.read());
 Token doctypeToken = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, doctypeToken.type);
 assertEquals(doctypePending.publicIdentifier(), doctypeToken.publicIdentifier());
 assertEquals(doctypePending.systemIdentifier(), doctypeToken.systemIdentifier());
 }
}