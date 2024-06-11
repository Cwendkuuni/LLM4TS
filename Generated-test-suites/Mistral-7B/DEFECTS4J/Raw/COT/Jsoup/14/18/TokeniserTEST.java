package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser createTokeniser(String input) {
 CharArrayReader reader = new CharArrayReader(input.toCharArray());
 return new Tokeniser(reader);
 }

 @Test
 public void testRead_EmptyInput() {
 Tokeniser tokeniser = createTokeniser("");
 Token token = tokeniser.read();
 Assert.assertNull(token);
 }

 @Test
 public void testRead_SingleCharacter() {
 Tokeniser tokeniser = createTokeniser("A");
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.Character);
 Assert.assertEquals('A', token.asCharacter().charAt(0));
 }

 @Test
 public void testRead_SingleWhiteSpace() {
 Tokeniser tokeniser = createTokeniser(" ");
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.Character);
 Assert.assertEquals(' ', token.asCharacter().charAt(0));
 }

 @Test
 public void testRead_SingleTag() {
 Tokeniser tokeniser = createTokeniser("<tag>");
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.StartTag);
 }

 @Test(expected = AssertionError.class)
 public void testRead_InvalidCharacterReference() {
 Tokeniser tokeniser = createTokeniser("&#xABCD;");
 tokeniser.consumeCharacterReference((char) 0xA, false);
 }

 @Test
 public void testRead_ValidCharacterReference() {
 Tokeniser tokeniser = createTokeniser("&#x00E9;");
 Character character = tokeniser.consumeCharacterReference(null, false);
 Validate.isNotNull(character);
 Assert.assertEquals('é', character.charValue());
 }

 @Test
 public void testRead_ValidCharacterReferenceInAttribute() {
 Tokeniser tokeniser = createTokeniser("<tag attr=\"#x00E9;\">");
 Character character = tokeniser.consumeCharacterReference(null, true);
 Validate.isNotNull(character);
 Assert.assertEquals('é', character.charValue());
 }

 @Test(expected = AssertionError.class)
 public void testRead_MissingEndTag() {
 Tokeniser tokeniser = createTokeniser("<tag>");
 tokeniser.read(); // read <tag>
 tokeniser.read(); // expect <tag/>
 tokeniser.read(); // should throw an error
 }

 @Test
 public void testRead_SelfClosingTag() {
 Tokeniser tokeniser = createTokeniser("<img src=\"image.png\" />");
 Token tag = tokeniser.read();
 Validate.isTrue(tag.type == TokenType.StartTag);
 Validate.isTrue(tag.isSelfClosing());
 }

 @Test
 public void testRead_MultipleCharacters() {
 Tokeniser tokeniser = createTokeniser("AB");
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.Character);
 Assert.assertEquals("AB", token.asCharacter().toString());
 }

 @Test
 public void testRead_EmptyScriptTag() {
 Tokeniser tokeniser = createTokeniser("<script></script>");
 Token startTag = tokeniser.read();
 Validate.isTrue(startTag.type == TokenType.StartTag);
 Token endTag = tokeniser.read();
 Validate.isTrue(endTag.type == TokenType.EndTag);
 }

 @Test
 public void testRead_ScriptTagWithContent() {
 Tokeniser tokeniser = createTokeniser("<script>function test(){\n alert(\"Hello World\");\n}</script>");
 Token startTag = tokeniser.read();
 Validate.isTrue(startTag.type == TokenType.StartTag);
 Token doctype = tokeniser.read();
 Validate.isTrue(doctype.type == TokenType.Doctype);
 Token script = tokeniser.read();
 Validate.isTrue(script.type == TokenType.Character);
 Validate.assertEquals("script", script.asCharacter().toString());
 Token endTag = tokeniser.read();
 Validate.isTrue(endTag.type == TokenType.EndTag);
 }

 @Test
 public void testRead_InvalidInput() {
 Tokeniser tokeniser = createTokeniser("&");
 Token token = tokeniser.read();
 Assert.assertNull(token); // should have thrown an error
 }

 @Test(expected = AssertionError.class)
 public void testRead_MissingEndTag_TagPending() {
 Tokeniser tokeniser = createTokeniser("<tag>");
 tokeniser.read(); // read <tag>
 tokeniser.read(); // should throw an error
 }

 @Test(expected = AssertionError.class)
 public void testRead_InvalidEndTag() {
 Tokeniser tokeniser = createTokeniser("<tag><//tag>");
 tokeniser.read(); // read <tag>
 tokeniser.read(); // read </tag
 tokeniser.read(); // should throw an error
 }

 @Test
 public void testRead_MismatchedEndTags() {
 Tokeniser tokeniser = createTokeniser("<p><b></r></p>");
 List<ParseError> errors = new ArrayList<ParseError>();
 tokeniser.setTrackErrors(true);
 tokeniser.read(); // read <p>
 Token startTag = tokeniser.read(); // read <b>
 Token endTag = tokeniser.read(); // read </r> (should be </b>)
 tokeniser.read(); // read </p>
 Assert.assertEquals(1, errors.size());
 Assert.assertEquals("Mismatched end tag: expected \"END_TAG_B\", but found \"END_TAG_R\"", errors.get(0).getMessage());
 }

 @Test
 public void testRead_NestedTags() {
 Tokeniser tokeniser = createTokeniser("<p><b><i>Hello World</i></b></p>");
 Token startTag = tokeniser.read(); // read <p>
 Token startTagB = tokeniser.read(); // read <b>
 Token startTagI = tokeniser.read(); // read <i>
 Token endTagI = tokeniser.read(); // read </i>
 Token content = tokeniser.read(); // read "Hello World"
 Token endTagB = tokeniser.read(); // read </b>
 Token endTagP = tokeniser.read(); // read </p>
 }

 @Test
 public void testRead_NestedScriptTags() {
 Tokeniser tokeniser = createTokeniser("<script><script>function test(){\n alert(\"Hello World\");\n}</script></script>");
 Token startTag1 = tokeniser.read(); // read <script>
 Token doctype1 = tokeniser.read(); // read <!DOCTYPE script PUBLIC>
 Token startTag2 = tokeniser.read(); // read <script>
 Token script1 = tokeniser.read(); // read "script"
 Token endTag2 = tokeniser.read(); // read </script>
 Token endTag1 = tokeniser.read(); // read </script>
 }

 @Test
 public void testRead_NestedCommentTags() {
 Tokeniser tokeniser = createTokeniser("<!-- -- ><!-- comment -- >");
 Token comment1 = tokeniser.read(); // read <!-- -- >
 Token comment2 = tokeniser.read(); // read <!-- comment -->
 }

 @Test
 public void testRead_NestedCommentAndScriptTags() {
 Tokeniser tokeniser = createTokeniser("<script><!-- comment --></script>");
 Token startTag = tokeniser.read(); // read <script>
 Token doctype = tokeniser.read(); // read <!DOCTYPE script PUBLIC>
 Token comment = tokeniser.read(); // read <!-- comment -->
 Token endTag = tokeniser.read(); // read </script>
 }

 @Test
 public void testRead_NestedCommentAndScriptTagsWithScriptContent() {
 Tokeniser tokeniser = createTokeniser("<script><!-- comment --><script>function test(){\n alert(\"Hello World\");\n}</script></script>");
 Token startTag = tokeniser.read(); // read <script>
 Token doctype = tokeniser.read(); // read <!DOCTYPE script PUBLIC>
 Token comment = tokeniser.read(); // read <!-- comment -->
 Token startTag2 = tokeniser.read(); // read <script>
 Token script = tokeniser.read(); // read "script"
 Token endTag2 = tokeniser.read(); // read </script>
 Token endTag = tokeniser.read(); // read </script>
 }

 @Test
 public void testRead_NestedCommentAndScriptTagsWithCommentContent() {
 Tokeniser tokeniser = createTokeniser("<script><!-- comment --><!-- comment --></script>");
 Token startTag = tokeniser.read(); // read <script>
 Token doctype = tokeniser.read(); // read <!DOCTYPE script PUBLIC>
 Token comment1 = tokeniser.read(); // read <!-- comment -->
 Token comment2 = tokeniser.read(); // read <!-- comment -->
 Token endTag = tokeniser.read(); // read </script>
 }

 @Test
 public void testRead_NestedScriptTagsWithScriptContent() {
 Tokeniser tokeniser = createTokeniser("<script><script>function test1(){\n alert(\"Hello World 1\");\n}</script><script>function test2(){\n alert(\"Hello World 2\");\n}</script></script>");
 Token startTag1 = tokeniser.read(); // read <script>
 Token doctype1 = tokeniser.read(); // read <!DOCTYPE script PUBLIC>
 Token startTag2 = tokeniser.read(); // read <script>
 Token script1 = tokeniser.read(); // read "script"
 Token endTag2 = tokeniser.read(); // read </script>
 Token script2 = tokeniser.read(); // read "script"
 Token endTag1 = tokeniser.read(); // read </script>
 }

 @Test
 public void testRead_InvalidInputStream() {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{});
 Tokeniser tokeniser = new Tokeniser(new InputStreamReader(inputStream));
 tokeniser.read(); // should throw an error
 }

 @Test
 public void testRead_EOF() {
 InputStream inputStream = new ByteArrayInputStream("abc".getBytes());
 Tokeniser tokeniser = new Tokeniser(new InputStreamReader(inputStream));
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.Character);
 Assert.assertEquals('a', token.asCharacter().charAt(0));
 Token token2 = tokeniser.read();
 Validate.isTrue(token2.type == TokenType.Character);
 Assert.assertEquals('b', token2.asCharacter().charAt(0));
 Token token3 = tokeniser.read();
 Validate.isNotNull(token3); // should be EOF
 }

 @Test
 public void testRead_EOF_WithTrackErrors() {
 InputStream inputStream = new ByteArrayInputStream("abc".getBytes());
 Tokeniser tokeniser = new Tokeniser(new InputStreamReader(inputStream));
 tokeniser.setTrackErrors(true);
 Token token = tokeniser.read();
 Validate.isTrue(token.type == TokenType.Character);
 Assert.assertEquals('a', token.asCharacter().charAt(0));
 Token token2 = tokeniser.read();
 Validate.isTrue(token2.type == TokenType.Character);
 Assert.assertEquals('b', token2.asCharacter().charAt(0));
 Token token3 = tokeniser.read();
 Assert.assertEquals(1, tokeniser.getErrors().size());
 Assert.assertEquals("Unexpectedly reached end of file (EOF)", tokeniser.getErrors().get(0).getMessage());
 }
}

 class CharArrayReader {
 private char[] buffer;
 private int pos;

 public CharArrayReader(char[] buffer) {
 this.buffer = buffer;
 this.pos = 0;
 }

 public char current() {
 return buffer[pos];
 }

 public void mark() {
 // no-op
 }

 public void rewindToMark() {
 pos--;
 }

 public boolean matches(char c) {
 return pos < buffer.length && buffer[pos] == c;
 }

 public boolean matchesAny(char... allowedChars) {
 for (char ch : allowedChars) {
 if (matches(ch)) {
 return true;
 }
 }
 return false;
 }

 public boolean matchesIgnoreCase(char c) {
 return (pos < buffer.length && Character.toLowerCase(buffer[pos]) == Character.toLowerCase(c));
 }

 public boolean matchesLetter() {
 return isAlpha((char) current());
 }

 public boolean matchesDigit() {
 return isDigit((char) current());
 }

 public void consume() {
 pos++;
 }

 public void consumeCharacterReference() {
 consumeCharacterReference(null, false);
 }

 public void consumeDigitSequence() {
 int start = pos;
 while (matchesDigit()) {
 consume();
 }
 consumeLetter();
 consume();
 }

 public void consumeHexSequence() {
 int start = pos;
 consumeLetter(); // consume "0x" or "0X"
 boolean isHexMode = matchesIgnoreCase('X');
 consumeLetter(); // consume hex digit
 while (matchesLetter() || matchesDigit()) {
 consume();
 }
 consume(); // consume semicolon
 int hexValue = Integer.parseInt(new String(buffer, start, pos - start), 16);
 consumeLetter(); // consume ">" or "/"
 }

 public void consumeLetterSequence() {
 int start = pos;
 while (matchesLetter()) {
 consume();
 }
 consume(); // consume following character
 String letterSequence = new String(buffer, start, pos - start);
 consume(); // consume following character
 }

 public void consumeWhiteSpace() {
 int start = pos;
 while (matchesWhiteSpace()) {
 consume();
 }
 consume(); // consume following character
 }

 public boolean isAlpha(char c) {
 return (Character.isLetter(c) || Character.isDigit(c) || c == '_');
 }

 public boolean isDigit(char c) {
 return Character.isDigit(c);
 }

 public boolean matchesWhiteSpace() {
 return isSpace(current()) || isTab(current()) || isNewline(current()) || isFormFeed(current());
 }

 public boolean isSpace(char c) {
 return Character.isWhitespace(c);
 }

 public boolean isTab(char c) {
 return c == '\t';
 }

 public boolean isNewline(char c) {
 return c == '\n';
 }

 public boolean isFormFeed(char c) {
 return c == '\f';
 }
 }