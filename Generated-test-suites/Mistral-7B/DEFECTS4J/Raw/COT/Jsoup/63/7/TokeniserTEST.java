package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Tokenizer$Character;
import org.jsoup.parser.Tokenizer$Doctype;
import org.jsoup.parser.Tokenizer$EndTag;
import org.jsoup.parser.Tokenizer$StartTag;
import org.jsoup.parser.Tokenizer$Tag;
import org.junit.Test;

import java.util.Arrays;

/**
 * Tests for the Tokeniser class.
 */
public class TokeniserTest {

 private CharacterReader characterReaderMock;
 private ParseErrorList parseErrorListMock;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 characterReaderMock = new MockCharacterReader();
 parseErrorListMock = new ParseErrorList();
 tokeniser = new Tokeniser(characterReaderMock, parseErrorListMock);
 }

 @Test
 public void testRead_EmptyInput_ReturnsNull() {
 assertEquals(null, tokeniser.read());
 }

 @Test
 public void testRead_Whitespace_ReturnsCharacterToken() {
 characterReaderMock.feed(' ');
 assertEquals(new Tokenizer$Character(' '), tokeniser.read());
 }

 @Test
 public void testRead_NonWhitespace_ReturnsCharacterToken() {
 characterReaderMock.feed('a');
 assertEquals(new Tokenizer$Character('a'), tokeniser.read());
 }

 @Test
 public void testRead_LessThanSymbol_ReturnsStartTagToken() {
 characterReaderMock.feed('<');
 tokeniser.state = TokeniserState.TagName;
 assertEquals(new Tokenizer$StartTag("html"), tokeniser.read());
 }

 @Test
 public void testRead_SelfClosingTag_ReturnsEndTagToken() {
 characterReaderMock.feed('<img src="image.png" />');
 tokeniser.state = TokeniserState.TagName;
 assertEquals(new Tokenizer$EndTag("img"), tokeniser.read());
 }

 @Test
 public void testRead_MultipleCharacters_ReturnsCharacterToken() {
 characterReaderMock.feed('a');
 characterReaderMock.feed('b');
 assertEquals(new Tokenizer$Character('ab'), tokeniser.read());
 }

 @Test
 public void testRead_ScriptTag_BuffersDataUntilEndTag() {
 characterReaderMock.feed("<script>");
 tokeniser.state = TokeniserState.Script;
 tokeniser.emit(new Tokenizer$Tag("script"));
 characterReaderMock.feed("document.write(\"Hello, world!\");");
 tokeniser.emit(new Tokenizer$Character("document.write("));
 tokeniser.emit(new Tokenizer$Character("\"Hello, world!\""));
 characterReaderMock.feed("</script>");
 tokeniser.emitTagPending();
 tokeniser.state = TokeniserState.Data;
 assertEquals(new Tokenizer$Character("Hello, world!"), tokeniser.read());
 }

 @Test(expected = AssertionError.class)
 public void testRead_UnexpectedCharacter_ThrowsError() {
 characterReaderMock.feed('!');
 tokeniser.state = TokeniserState.Data;
 tokeniser.read();
 }

 @Test
 public void testEmit_CharacterToken() {
 tokeniser.emit(new Tokenizer$Character('a'));
 assertEquals(new Tokenizer$Character('a'), tokeniser.read());
 }

 @Test
 public void testEmit_StartTagToken() {
 tokeniser.emit(new Tokenizer$StartTag("html"));
 assertEquals(new Tokenizer$StartTag("html"), tokeniser.read());
 }

 @Test
 public void testEmit_EndTagToken() {
 tokeniser.emit(new Tokenizer$EndTag("img"));
 assertEquals(new Tokenizer$EndTag("img"), tokeniser.read());
 }

 @Test
 public void testEmit_TagPending_IsNullInput() {
 tokeniser.emitTagPending();
 assertNull(tokeniser.read());
 }

 @Test
 public void testEmit_TagPending_NonEmptyInput() {
 tokeniser.createTagPending(true).tagName("html");
 tokeniser.emitTagPending();
 assertEquals(new Tokenizer$StartTag("html"), tokeniser.read());
 }

 @Test
 public void testEmit_CommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 assertEquals(new Tokenizer$Comment(""), tokeniser.read());
 }

 @Test
 public void testEmit_DoctypePending() {
 tokeniser.createDoctypePending().publicId("DTD HTML 4.01");
 tokeniser.emitDoctypePending();
 assertEquals(new Tokenizer$Doctype("DTD HTML 4.01"), tokeniser.read());
 }

 @Test
 public void testUnescapeEntities_EmptyInput_ReturnsEmptyString() {
 assertEquals("", tokeniser.unescapeEntities(false));
 }

 @Test
 public void testUnescapeEntities_SingleCharacter_ReturnsCharacter() {
 assertEquals("&a;", tokeniser.unescapeEntities(false).toString());
 }

 @Test
 public void testUnescapeEntities_MultipleCharacters_ReturnsString() {
 assertEquals("&nbsp; &ldquo;Hello, world!&rdquo;", tokeniser.unescapeEntities(false).toString());
 }

 @Test
 public void testUnescapeEntities_InAttribute_ReturnsString() {
 assertEquals("&amp; &lt; &gt;", tokeniser.unescapeEntities(true).toString());
 }

 @Test
 public void testInvalidCharacterReference() {
 characterReaderMock.feed("#");
 tokeniser.state = TokeniserState.Data;
 tokeniser.read(); // consume &
 tokeniser.read(); // consume #
 assertFalse(tokeniser.consumeCharacterReference(null, false) == null);
 assertTrue(tokeniser.errors.hasErrors());
 }

 @Test
 public void testInvalidNamedCharacterReference() {
 characterReaderMock.feed("#abcd");
 tokeniser.state = TokeniserState.Data;
 tokeniser.read(); // consume &
 tokeniser.read(); // consume #
 tokeniser.read(); // consume a
 tokeniser.read(); // consume b
 tokeniser.read(); // consume c
 tokeniser.read(); // consume d
 assertFalse(tokeniser.consumeCharacterReference(null, false) == null);
 assertTrue(tokeniser.errors.hasErrors());
 }

 @Test
 public void testCharacterReferenceOutsideValidRange() {
 characterReaderMock.feed("#\uD800");
 tokeniser.state = TokeniserState.Data;
 tokeniser.read(); // consume &
 tokeniser.read(); // consume #
 tokeniser.read(); // consume U+DC00
 assertFalse(tokeniser.consumeCharacterReference(null, false) == null);
 assertTrue(tokeniser.errors.hasErrors());
 }

 @Test
 public void testCharacterReferenceWithExtraIllegalUnicodePoints() {
 characterReaderMock.feed("#\uD800\uDC00");
 tokeniser.state = TokeniserState.Data;
 tokeniser.read(); // consume &
 tokeniser.read(); // consume #
 tokeniser.read(); // consume U+DC00
 tokeniser.read(); // consume U+D800
 assertFalse(tokeniser.consumeCharacterReference(null, false) == null);
 assertTrue(tokeniser.errors.hasErrors());
 }

 @Test
 public void testStateTransition() {
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.state = TokeniserState.Data;
 assertEquals(TokeniserState.TagName, tokeniser.getState());
 }

 @Test
 public void testAdvanceTransition() {
 characterReaderMock.feed('<');
 tokeniser.state = TokeniserState.TagName;
 tokeniser.advanceTransition(TokeniserState.TagName);
 tokeniser.state = TokeniserState.TagName;
 reader.advance();
 assertEquals(TokeniserState.TagName, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertFalse(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

}