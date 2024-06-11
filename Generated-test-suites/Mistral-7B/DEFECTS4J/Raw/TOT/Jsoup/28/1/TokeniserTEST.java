import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader reader;
 private Tokeniser tokeniser;
 private ParseErrorList errors;
 private List<ParseError> expectedErrors;

 @Before
 public void setUp() {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 expectedErrors = new ArrayList<>();
 }

 @Test
 public void testRead_EmptyInput() {
 givenEmptyInput();
 whenRead();
 thenNoTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_Whitespace() {
 givenInput(" ");
 whenRead();
 thenWhitespaceTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_HTMLTag() {
 givenInput("<html>");
 whenRead();
 thenStartTagHtmlTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_SelfClosingHTMLTag() {
 givenInput("<html/>");
 whenRead();
 thenStartTagHtmlTokenEmitted();
 thenEndTagHtmlTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_ScriptTag() {
 givenInput("<script>");
 whenRead();
 thenStartTagScriptTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_ScriptTagWithEndTag() {
 givenInput("<script>\n// script content\n</script>");
 whenRead();
 thenStartTagScriptTokenEmitted();
 thenCommentOrTextTokenEmitted();
 thenEndTagScriptTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_ScriptTagWithError() {
 givenInput("<script>Syntax Error");
 whenRead();
 thenError("Unexpected character 'S' in input state [Data]");
 }

 @Test
 public void testRead_ScriptTagWithEOF() {
 givenInput("<script>");
 whenRead();
 thenStartTagScriptTokenEmitted();
 eofError();
 }

 @Test
 public void testRead_Comment() {
 givenInput("<!-- comment -->");
 whenRead();
 thenCommentTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_Doctype() {
 givenInput("<!DOCTYPE html>");
 whenRead();
 thenDoctypeTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_CharacterReference() {
 givenInput("&nbsp;");
 whenRead();
 thenCharacterReferenceTokenEmitted('nbsp');
 verifyNoErrors();
 }

 @Test
 public void testRead_CharacterReferenceWithInvalidHex() {
 givenInput("&#xABC");
 whenRead();
 thenCharacterReferenceError("Invalid hexadecimal value 'ABC'");
 }

 @Test
 public void testRead_CharacterReferenceWithInvalidNamed() {
 givenInput("&foo;");
 whenRead();
 thenCharacterReferenceError("Invalid named reference 'foo'");
 }

 @Test
 public void testRead_CharacterReferenceWithInvalidNamedAndUnmatchedSemicolon() {
 givenInput("&foo");
 whenRead();
 thenCharacterReferenceError("Missing semicolon");
 }

 @Test
 public void testRead_CharacterReferenceWithInAttribute() {
 givenInput("<p class=\"class&nbsp;name\">Content</p>");
 whenRead();
 thenCharacterReferenceTokenEmitted('nbsp');
 thenCharacterReferenceTokenEmitted('amp');
 thenCharacterReferenceTokenEmitted('amp');
 thenCharacterReferenceTokenEmitted('n');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('t');
 thenCharacterReferenceTokenEmitted('e');
 thenCharacterReferenceTokenEmitted(' ');
 thenCharacterReferenceTokenEmitted('c');
 thenCharacterReferenceTokenEmitted('l');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('s');
 thenCharacterReferenceTokenEmitted('s');
 thenCharacterReferenceTokenEmitted(' ');
 thenCharacterReferenceTokenEmitted('n');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('m');
 thenCharacterReferenceTokenEmitted('e');
 thenEndTagPTagTokenEmitted();
 verifyNoErrors();
 }

 @Test
 public void testRead_CharacterReferenceWithInAttributeAndUnmatchedSemicolon() {
 givenInput("<p class=\"class&nbsp;name\"");
 whenRead();
 thenCharacterReferenceTokenEmitted('nbsp');
 thenCharacterReferenceTokenEmitted('amp');
 thenCharacterReferenceTokenEmitted('amp');
 thenCharacterReferenceTokenEmitted('n');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('t');
 thenCharacterReferenceTokenEmitted('e');
 thenCharacterReferenceTokenEmitted(' ');
 thenCharacterReferenceTokenEmitted('c');
 thenCharacterReferenceTokenEmitted('l');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('s');
 thenCharacterReferenceTokenEmitted(' ');
 thenCharacterReferenceTokenEmitted('n');
 thenCharacterReferenceTokenEmitted('a');
 thenCharacterReferenceTokenEmitted('m');
 thenCharacterReferenceTokenEmitted('e');
 thenEndTagError();
 }

 private void givenEmptyInput() {
 reader.givenEmptyInput();
 }

 private void givenInput(String input) {
 reader.givenInput(new ByteArrayInputStream(input.getBytes()));
 }

 private void whenRead() {
 Token token = tokeniser.read();
 }

 private void thenNoTokenEmitted() {
 assertNull(tokeniser.getState());
 assertEquals(0, errors.size());
 }

 private void thenWhitespaceTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Whitespace, token.type);
 assertEquals("", token.text);
 }

 private void thenStartTagHtmlTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("html", token.tagName);
 }

 private void thenStartTagScriptTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("script", token.tagName);
 }

 private void thenCommentTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 }

 private void thenCommentOrTextTokenEmitted() {
 Token token = tokeniser.read();
 if (token.type == Token.TokenType.Comment) {
 assertEquals(Token.TokenType.Comment, token.type);
 } else {
 assertEquals(Token.TokenType.Text, token.type);
 }
 }

 private void thenEndTagScriptTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("script", token.tagName);
 }

 private void thenEndTagPTagTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("p", token.tagName);
 }

 private void thenDoctypeTokenEmitted() {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 }

 private void thenCharacterReferenceTokenEmitted(char expectedCharacter) {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(String.valueOf(expectedCharacter), token.text);
 }

 private void thenCharacterReferenceTokenEmitted(String expectedName) {
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(expectedName, token.text);
 }

 private void verifyNoErrors() {
 assertTrue(errors.isEmpty());
 }

 private void eofError() {
 ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(errors).add(errorCaptor.capture());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errorCaptor.getValue().getMessage());
 }

 private void endTagError() {
 ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
 Mockito.verify(errors).add(errorCaptor.capture());
 ParseError error = errorCaptor.getValue();
 assertTrue(error.getErrorMessage().startsWith("Attributes incorrectly present on end tag"));
 }
}