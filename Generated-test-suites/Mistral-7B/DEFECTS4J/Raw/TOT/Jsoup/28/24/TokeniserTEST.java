import org.jsoup.helper.CharsetUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParserUtils;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader reader;
 private List<ParseError> errors;

 @Before
 public void setUp() {
 reader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()), CharsetUtil.UTF_8);
 errors = new ArrayList<>();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() {
 // Test reading characters
 tokeniser.transition(TokeniserState.Data);
 assertEquals('t', tokeniser.read().toString());
 assertEquals('e', tokeniser.read().toString());
 assertEquals('s', tokeniser.read().toString());
 assertEquals(' ', tokeniser.read().toString());
 assertEquals('\n', tokeniser.read().toString());
 assertEquals(TokenType.EOF, tokeniser.read().type);
 }

 @Test
 public void testReadCharacterWithSelfClosingFlag() {
 // Test reading characters with self-closing flag
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 assertEquals('t', tokeniser.read().toString());
 assertEquals('e', tokeniser.read().toString());
 assertEquals('s', tokeniser.read().toString());
 assertEquals('>', tokeniser.read().toString());
 assertEquals(TokenType.EOF, tokeniser.read().type);
 }

 @Test
 public void testReadString() {
 // Test reading strings
 tokeniser.emit("test");
 tokeniser.transition(TokeniserState.Data);
 assertEquals("test", tokeniser.read().toString());
 }

 @Test
 public void testReadStartTag() {
 // Test reading start tags
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("<tag name=\"test\" attr1=\"value1\" attr2=\"value2\">");
 assertEquals(TokenType.StartTag, tokeniser.read().type);
 Token startTag = (Token.StartTag) tokeniser.read();
 assertEquals("tag", startTag.tagName);
 assertEquals(2, startTag.attributes.size());
 assertEquals("value1", startTag.attributes.get("attr1"));
 assertEquals("value2", startTag.attributes.get("attr2"));
 }

 @Test
 public void testReadEndTag() {
 // Test reading end tags
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("<tag name=\"test\"></tag>");
 assertEquals(TokenType.EndTag, tokeniser.read().type);
 Token endTag = (Token.EndTag) tokeniser.read();
 assertEquals("tag", endTag.tagName);
 }

 @Test
 public void testReadComment() {
 // Test reading comments
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("<!-- comment -->");
 assertEquals(TokenType.Comment, tokeniser.read().type);
 Token comment = (Token.Comment) tokeniser.read();
 assertEquals("comment", comment.toString());
 }

 @Test
 public void testReadUnknown() {
 // Test reading unknown tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&nbsp;");
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals("nbsp", tokeniser.read().toString());
 tokeniser.acknowledgeSelfClosingFlag();
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&lt;");
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals("lt", tokeniser.read().toString());
 }

 @Test
 public void testErrorHandling() {
 // Test error handling
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("<tag name=\"test\" attr1=\"value1\"");
 tokeniser.error("Missing '>' in start tag");
 assertNull(tokeniser.read());
 assertEquals(1, errors.size());
 }

 @Test
 public void testParsingXML() {
 // Test parsing XML
 Document doc = ParserUtils.parseXml("<test xmlns:x=\"http://www.w3.org/2001/XMLSchema\"><x:foo/></test>");
 InputStream input = new ByteArrayInputStream(doc.html().getBytes(CharsetUtil.UTF_8));
 Tokeniser xmlTokeniser = new Tokeniser(new CharacterReader(input, CharsetUtil.UTF_8), errors);
 tokeniser = xmlTokeniser;
 tokeniser.transition(TokeniserState.Data);
 Token tag = tokeniser.read();
 assertEquals(TokenType.StartTag, tag.type);
 assertEquals("test", tag.tagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testTransitionToInvalidState() {
 // Test transitioning to an invalid state
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.SelfClosingStartTag);
 }

 @Test
 public void testTransitionToSameState() {
 // Test transitioning to the same state
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.Data);
 }

 @Test
 public void testConsumeCharacterReference() {
 // Test consuming character references
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&#160;");
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals(" ", tokeniser.read().toString());
 }

 @Test
 public void testConsumeNamedCharacterReference() {
 // Test consuming named character references
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&nbsp;");
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals("nbsp", tokeniser.read().toString());
 }

 @Test
 public void testConsumeInvalidCharacterReference() {
 // Test consuming invalid character references
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("&foo;");
 assertNull(tokeniser.read());
 assertEquals(1, errors.size());
 assertEquals("Invalid named reference 'foo'", errors.get(0).toString());
 }

 @Test
 public void testConsumeCharacterReferenceWithAttribute() {
 // Test consuming character references with attributes
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("<tag attr=\"&#160;\" />");
 tokeniser.transition(TokeniserState.Data);
 assertEquals(TokenType.Character, tokeniser.read().type);
 assertEquals(" ", tokeniser.read().toString());
 }

 @Test
 public void testCreateTagPending() {
 // Test creating tag tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createTagPending(true);
 assertEquals(TokenType.StartTag, tokeniser.read().type);
 Token startTag = (Token.StartTag) tokeniser.read();
 assertTrue(startTag.isStartTag());
 }

 @Test
 public void testEmitTagPending() {
 // Test emitting tag tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitTagPending();
 assertEquals("tag", tokeniser.lastStartTag.tagName);
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitTagPendingNotCreated() {
 // Test emitting tag tokens before they're created
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitTagPending();
 }

 @Test
 public void testCreateCommentPending() {
 // Test creating comment tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createCommentPending();
 assertEquals(TokenType.Comment, tokeniser.read().type);
 Token comment = (Token.Comment) tokeniser.read();
 assertTrue(comment.isComment());
 }

 @Test
 public void testEmitCommentPending() {
 // Test emitting comment tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCommentPending();
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitCommentPendingNotCreated() {
 // Test emitting comment tokens before they're created
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCommentPending();
 }

 @Test
 public void testCreateDoctypePending() {
 // Test creating doctype tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.createDoctypePending();
 assertEquals(TokenType.Doctype, tokeniser.read().type);
 Token doctype = (Token.Doctype) tokeniser.read();
 assertEquals("<!DOCTYPE", doctype.toString());
 }

 @Test
 public void testEmitDoctypePending() {
 // Test emitting doctype tokens
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitDoctypePending();
 }

 @Test(expected = IllegalStateException.class)
 public void testEmitDoctypePendingNotCreated() {
 // Test emitting doctype tokens before they're created
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitDoctypePending();
 }
}