import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

 private Tokeniser tokeniser;
 private CharacterReader characterReader;
 private ParseErrorList parseErrorList;

 @Before
 public void setup() {
 charReader = new CharacterReader("Test");
 parseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(characterReader, parseErrorList);
 }

 @Test
 public void testRead_DataState_ReturnsCharacterToken() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.getType());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedCharacter() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals("A", tokeniser.charBuffer.toString());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedCharacterWhenMultipleCharacters() {
 tokeniser.emit("AB");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals("AB", tokeniser.charBuffer.toString());
 }

 @Test
 public void testRead_DataState_ReturnsExpectedCharacterWithEscapedSequence() {
 tokeniser.emit("&#123;");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals("\\u007B", tokeniser.charBuffer.toString());
 }

 @Test(expected = AssertionError.class)
 public void testRead_DataState_ThrowsErrorOnUnexpectedCharacter() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 }

 @Test
 public void testRead_TagOpeningState_ReturnsEndTag() {
 tokeniser.emit("/html");
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_TagOpeningState_ReturnsExpectedStartTag() {
 tokeniser.emit("<html");
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_TagOpeningState_ReturnsExpectedStartTagWithAttributes() {
 tokeniser.emit("<html id='test'>");
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 Token startTag = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, startTag.getType());
 assertEquals("html", startTag.tagName);
 assertEquals("test", startTag.attributes.get("id").value);
 }

 @Test
 public void testRead_TagOpeningState_ReturnsExpectedStartTagWithSelfClosingTag() {
 tokeniser.emit("<img src='image.png' />");
 tokeniser.transition(TokeniserState.TagOpening);
 tokeniser.read();
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 tokeniser.read();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testRead_TagClosingState_ReturnsExpectedEndTag() {
 tokeniser.emit("/html");
 tokeniser.transition(TokeniserState.TagClosing);
 tokeniser.read();
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 }

 @Test
 public void testRead_ScriptState_ReturnsExpectedScriptToken() {
 tokeniser.createTempBuffer();
 tokeniser.emit("<script>");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 assertEquals(Token.TokenType.Script, tokeniser.read().getType());
 tokeniser.emitScriptData("alert('Hello World')");
 tokeniser.emit("</script>");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_ScriptState_ReturnsExpectedScriptData() {
 tokeniser.createTempBuffer();
 tokeniser.emit("<script>");
 tokeniser.transition(TokeniserState.Script);
 tokeniser.read();
 assertEquals(Token.TokenType.Script, tokeniser.read().getType());
 tokeniser.read(); // consume "<script>"
 tokeniser.read(); // consume ">"
 assertEquals(tokeniser.dataBuffer.toString(), "alert('Hello World')");
 tokeniser.emit("</script>");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read(); // consume "</script>"
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertFalse(tokeniser.isEmitPending());
 }

 @Test
 public void testRead_CommentState_ReturnsExpectedCommentToken() {
 tokeniser.createCommentPending();
 tokeniser.emit("<!-- Test -->");
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.read();
 assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 assertEquals("Test", tokeniser.commentPending.data);
 }

 @Test
 public void testRead_DoctypeState_ReturnsExpectedDoctypeToken() {
 tokeniser.createDoctypePending();
 tokeniser.emit("<!DOCTYPE html>");
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.read();
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 }

 @Test
 public void testTransition_DataState_ToTagOpeningState() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.transition(TokeniserState.TagOpening);
 assertEquals(TokeniserState.TagOpening, tokeniser.getState());
 }

 @Test
 public void testAcknowledgeSelfClosingFlag() {
 tokeniser.acknowledgeSelfClosingFlag();
 assertTrue(tokeniser.selfClosingFlagAcknowledged);
 }

 @Test
 public void testConsumeCharacterReference_ValidReference() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertEquals((char) 1, character.charValue());
 }

 @Test
 public void testConsumeCharacterReference_ValidNamedReference() {
 Character character = tokeniser.consumeCharacterReference("ampersand", false);
 assertEquals("&", character.charValue().toString());
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamedReference() {
 Character character = tokeniser.consumeCharacterReference("invalid", false);
 assertNull(character);
 }

 @Test
 public void testConsumeCharacterReference_InvalidNamedReferenceWithSemicolon() {
 Character character = tokeniser.consumeCharacterReference("invalid;", false);
 assertNull(character);
 }

 @Test
 public void testConsumeCharacterReference_InvalidCharacterReference() {
 Character character = tokeniser.consumeCharacterReference(null, false);
 assertNull(character);
 }

 @Test
 public void testCreateTagPending_StartTag() {
 Token.StartTag startTag = tokeniser.createTagPending(true);
 assertEquals(Token.StartTag.class, startTag.getClass());
 }

 @Test
 public void testCreateTagPending_EndTag() {
 Token.EndTag endTag = tokeniser.createTagPending(false);
 assertEquals(Token.EndTag.class, endTag.getClass());
 }

 @Test
 public void testEmitTagPending() {
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 }

 @Test
 public void testEmitCommentPending() {
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 }

 @Test
 public void testEmitDoctypePending() {
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 }
}