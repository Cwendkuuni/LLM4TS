package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 private ParseErrorList mockParseErrorList;
 private Tokeniser tokeniser;

 @Before
 public void setup() {
 mockCharacterReader = new MockCharacterReader();
 mockParseErrorList = new ParseErrorList();
 tokeniser = new Tokeniser(mockCharacterReader, mockParseErrorList);
 }

 @Test
 public void testRead_StartTag() {
 mockCharacterReader.setInput("<html>");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("html", token.tagName);
 }

 @Test
 public void testRead_EndTag() {
 mockCharacterReader.setInput("</html>");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("html", token.tagName);
 }

 @Test
 public void testRead_StartTagWithSelfClosingFlag() {
 mockCharacterReader.setInput("<img src=\"image.jpg\" />");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("img", token.tagName);
 assertTrue(token.selfClosing);
 }

 @Test
 public void testRead_Character() {
 mockCharacterReader.setInput("abc");
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("abc", token.data);
 }

 @Test
 public void testRead_EmptyString() {
 mockCharacterReader.setInput("");
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test(expected = IllegalStateException.class)
 public void testRead_SelfClosingFlagNotAcknowledged() {
 mockCharacterReader.setInput("</html>"); // self closing tag
 tokeniser.read(); // read past start tag
 tokeniser.read(); // should acknowledge self closing flag before reading end tag
 tokeniser.read(); // read end tag
 }

 @Test
 public void testRead_DataBuffer() {
 mockCharacterReader.setInput("<script type=\"text/javascript\">function test(){alert(\"Hello\")}</script>");
 tokeniser.transition(TokeniserState.ScriptData);
 tokeniser.read(); // read <script>
 tokeniser.read(); // read type="text/javascript"
 tokeniser.read(); // read >
 tokeniser.read(); // read script data
 tokeniser.emit(tokeniser.dataBuffer.toString()); // emit script data as one token
 }

 @Test
 public void testRead_ErrorHandling() {
 mockCharacterReader.setInput("<html><script>error();</script>");
 tokeniser.read(); // read <html>
 tokeniser.read(); // read <script>
 tokeniser.error(TokeniserState.Script);
 tokeniser.read(); // read >
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testConsumeCharacterReference_DigitSequence() {
 mockCharacterReader.setInput("&#123;");
 int[] result = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, result.length);
 assertEquals(123, result[0]);
 }

 @Test
 public void testConsumeCharacterReference_HexSequence() {
 mockCharacterReader.setInput("&#x123;");
 int[] result = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, result.length);
 assertEquals(123, result[0]);
 }

 @Test
 public void testConsumeCharacterReference_NamedEntity() {
 mockCharacterReader.setInput("&copy;");
 int[] result = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, result.length);
 assertEquals(169, result[0]);
 }

 @Test
 public void testConsumeCharacterReference_NamedEntityWithSemicolon() {
 mockCharacterReader.setInput("&amp;");
 int[] result = tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, result.length);
 assertEquals(38, result[0]);
 }

 @Test
 public void testConsumeCharacterReference_NamedEntityInAttribute() {
 mockCharacterReader.setInput("<a href=\"&nbsp;\">Link</a>");
 int[] result = tokeniser.consumeCharacterReference(32, true);
 assertEquals(1, result.length);
 assertEquals(160, result[0]);
 }

 @Test
 public void testConsumeCharacterReference_InvalidReference() {
 mockCharacterReader.setInput("<p>&#%&;"); // invalid characters in reference
 tokeniser.consumeCharacterReference(null, false);
 assertTrue(mockParseErrorList.contains("Invalid character reference: missing semicolon"));
 }

 @Test
 public void testConsumeCharacterReference_OutOfRangeCharacter() {
 mockCharacterReader.setInput("<p>&#100000;</p>"); // character out of range
 tokeniser.consumeCharacterReference(null, false);
 assertTrue(mockParseErrorList.contains("Character outside of valid range"));
 }

 @Test
 public void testCreateTagPending() {
 Tag tagPending = tokeniser.createTagPending(true);
 assertEquals(StartTag.class, tagPending.getClass());
 }

 @Test
 public void testCreateEndTagPending() {
 EndTag endTagPending = tokeniser.createEndTagPending();
 assertEquals(EndTag.class, endTagPending.getClass());
 }

 @Test
 public void testCreateDoctypePending() {
 Doctype doctypePending = tokeniser.createDoctypePending();
 assertEquals(Doctype.class, doctypePending.getClass());
 }

 @Test
 public void testEmitTagPending() {
 mockCharacterReader.setInput("<html>");
 StartTag tagPending = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 mockCharacterReader.setInput("</html>");
 EndTag endTagPending = tokeniser.createEndTagPending();
 tokeniser.emitTagPending();
 assertEquals(2, mockParseErrorList.size());
 }

 @Test
 public void testErrorHandling() {
 mockCharacterReader.setInput("<html><script>error();</script>"); // error within script tag
 tokeniser.read(); // read <html>
 tokeniser.read(); // read <script>
 tokeniser.error(TokeniserState.Script);
 tokeniser.read(); // read >
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testErrorHandling_EOF() {
 mockCharacterReader.setInput("<html>"); // error before script tag
 tokeniser.read(); // read <html>
 tokeniser.transition(TokeniserState.Script);
 tokeniser.eofError(TokeniserState.Script);
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testCharacterReferenceError() {
 mockCharacterReader.setInput("<p>&#%&;"); // invalid characters in reference
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testCharacterReferenceError_MissingSemicolon() {
 mockCharacterReader.setInput("&amp;"); // missing semicolon
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testCharacterReferenceError_InvalidNamedEntity() {
 mockCharacterReader.setInput("&invalidEntity;"); // invalid named entity
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, mockParseErrorList.size());
 }

 @Test
 public void testCharacterReferenceError_OutOfRangeCharacter() {
 mockCharacterReader.setInput("<p>&#100000;</p>"); // character out of range
 tokeniser.consumeCharacterReference(null, false);
 assertEquals(1, mockParseErrorList.size());
 }
}