package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 reader = new CharacterReader("Test input");
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @After
 public void teardown() {
 reader.close();
 errors.clear();
 }

 @Test
 public void testRead_InitialState() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_DataState() {
 reader.consume('a');
 reader.consume('b');
 reader.consume('c');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Character, token.type);
 assertEquals("abc", token.data);
 }

 @Test
 public void testRead_TagOpeningState() {
 reader.consume('<');
 reader.consume('t');
 reader.consume('a');
 reader.consume('g');
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("tag", token.tagName);
 }

 @Test
 public void testRead_TagClosingState() {
 reader.consume('/');
 reader.consume('t');
 reader.consume('a');
 reader.consume('g');
 tokeniser.transition(TokeniserState.TagClosing);
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.EndTag, token.type);
 assertEquals("tag", token.tagName);
 }

 @Test
 public void testRead_ScriptDataState() {
 reader.consume('<');
 reader.consume('s');
 reader.consume('c' );
 reader.consume('r' );
 reader.consume('i' );
 reader.consume('p' );
 reader.consume('t' );
 reader.consume('>');
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Character, token.type);
 }

 @Test(expected = AssertionError.class)
 public void testRead_TagNameError() {
 reader.consume('<');
 reader.consume('x');
 reader.consume('a');
 reader.consume('t');
 reader.consume('>');
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 }

 @Test
 public void testRead_CommentState() {
 reader.consume('<');
 reader.consume('/');
 reader.consume('c');
 reader.consume('o');
 reader.consume('m');
 reader.consume('m');
 reader.consume('e');
 reader.consume('n');
 reader.consume('t');
 reader.consume('>');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Comment, token.type);
 }

 @Test
 public void testRead_DoctypeState() {
 reader.consume('<');
 reader.consume('d');
 reader.consume('o');
 reader.consume('c');
 reader.consume('t');
 reader.consume('y');
 reader.consume('p');
 reader.consume('e');
 reader.consume('>');
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Doctype, token.type);
 }

 @Test
 public void testRead_ErrorState() {
 reader.consume('x');
 tokeniser.transition(TokeniserState.Error);
 tokeniser.read();
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testRead_EndOfInputState() {
 reader.consume('x');
 reader.consume(0); // EOF marker
 tokeniser.transition(TokeniserState.EndOfInput);
 tokeniser.read();
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testEmit_Character() {
 tokeniser.emit(new Character('a'));
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @Test
 public void testEmit_StartTag() {
 tokeniser.emit(new StartTag("tag"));
 tokeniser.emitTagPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.StartTag, token.type);
 assertEquals("tag", token.tagName);
 }

 @Test
 public void testEmit_EndTag() {
 tokeniser.emit(new EndTag("tag"));
 tokeniser.emitEndTagPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.EndTag, token.type);
 assertEquals("tag", token.tagName);
 }

 @Test
 public void testEmit_Comment() {
 tokeniser.emitCommentPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Comment, token.type);
 }

 @Test
 public void testEmit_Doctype() {
 tokeniser.emitDoctypePending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertSame(Token.TokenType.Doctype, token.type);
 }

 @Test(expected = AssertionError.class)
 public void testEmit_InvalidState() {
 tokeniser.emit(new Character('a'));
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.emitPending();
 }

 @Test
 public void testConsumeCharacterReference_Numeric() {
 tokeniser.emit(new Character('#'));
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(Token.TokenType.Character.toInt(), tokeniser.reader.current());
 tokeniser.reader.consume('1');
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(65, tokeniser.reader.current());
 tokeniser.reader.consume(';');
 tokeniser.reader.consume('>');
 Token token = tokeniser.read();
 assertEquals(97, token.codePointAt(0));
 }

 @Test
 public void testConsumeCharacterReference_Named() {
 tokeniser.emit(new Character('#'));
 tokeniser.consumeCharacterReference("ampersand", false);
 tokeniser.reader.consume('a');
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("semicolon", false);
 tokeniser.reader.consume('>');
 tokeniser.reader.consume('>');
 Token token = tokeniser.read();
 assertEquals("&amp;", token.data);
 }

 @Test
 public void testConsumeCharacterReference_Named_Attribute() {
 tokeniser.emit(new Character('#'));
 tokeniser.consumeCharacterReference("ampersand", true);
 tokeniser.reader.consume('a');
 tokeniser.consumeCharacterReference("quotationmark", true);
 tokeniser.consumeCharacterReference("quotationmark", true);
 tokeniser.consumeCharacterReference(" quotationmark ", true);
 tokeniser.consumeCharacterReference("ampersand", false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference(null, false);
 tokeniser.consumeCharacterReference("semicolon", false);
 tokeniser.reader.consume('>');
 tokeniser.reader.consume('>');
 Token token = tokeniser.read();
 assertEquals("&quot;&quot;&quot;amp;&quot;", token.data);
 }

 @Test
 public void testCreateTagPending() {
 Tag tag = tokeniser.createTagPending(true);
 tag.tagName = "tag";
 tag.tagNameLower = "Tag";
 tag.selfClosing = false;
 assertEquals("Tag", tag.tagNameLower);
 assertFalse(tag.selfClosing);
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.emitTagPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(Tag.class, token.classType());
 assertEquals("Tag", token.tagName);
 assertFalse(((StartTag) token).isSelfClosing());
 }

 @Test
 public void testCreateCommentPending() {
 Comment comment = tokeniser.createCommentPending();
 comment.commentText = "Test comment";
 assertEquals("Test comment", comment.commentText);
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.emitCommentPending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(Comment.class, token.classType());
 assertEquals("Test comment", token.commentText);
 }

 @Test
 public void testCreateDoctypePending() {
 Doctype doctype = tokeniser.createDoctypePending();
 doctype.publicId = "Public ID";
 doctype.systemId = "System ID";
 assertEquals("Public ID", doctype.publicId);
 assertEquals("System ID", doctype.systemId);
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.emitDoctypePending();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 Token token = tokeniser.read();
 assertEquals(Doctype.class, token.classType());
 assertEquals("Public ID", token.doctype.publicId);
 assertEquals("System ID", token.doctype.systemId);
 }

 @Test
 public void testErrorHandling() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.error(TokeniserState.TagOpening);
 assertEquals(3, errors.size());
 assertEquals("Invalid character '%s' in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testEOFHandling() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.consume(0); // EOF marker
 tokeniser.error(TokeniserState.Data);
 assertEquals(1, errors.size());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
 }

 @Test
 public void testCharacterReferenceError() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.emit(new Character('#'));
 tokeniser.error("Invalid character reference: invalid named referenece '%s'", "invalid named referenece");
 assertEquals(1, errors.size());
 assertEquals("Invalid character reference: invalid named referenece '%s'", errors.get(0).getMessage());
 }

 @Test
 public void testErrorWithState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.error(TokeniserState.TagOpening);
 assertEquals("Unexpected character '%s' in input state [TagOpening]", errors.get(0).getMessage());
 }

 @Test
 public void testUnescapeEntities() {
 String input = "&amp;lt; &gt; &amp;#039; &amp;#128;";
 String expectedOutput = "&lt; > ' ⁨";
 String output = tokeniser.unescapeEntities(false);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeEntities_Attributes() {
 String input = "&amp;lt;tag attr='&amp;quot;&#039;&#039;amp;quot;' &gt;";
 String expectedOutput = "<tag attr='\"\' ''\"'";
 String output = tokeniser.unescapeEntities(true);
 assertEquals(expectedOutput, output);
 }
}