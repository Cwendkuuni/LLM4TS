import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.ParseError;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

 private Tokeniser createTokeniser(String input) {
 CharArrayReader reader = new CharArrayReader(input.toCharArray());
 ParseErrorList errors = new ArrayList<>();
 return new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() {
 Tokeniser tokeniser = createTokeniser("hello world");
 Token token;
 for (int i = 0; i < 12; i++) {
 token = tokeniser.read();
 System.out.println("Token type: " + token.type);
 System.out.println("Value: " + token.toString());
 Assert.assertNotNull(token);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testReadWithoutInitialRead() {
 Tokeniser tokeniser = createTokeniser("hello world");
 Token token = tokeniser.read();
 tokeniser.read(); // skip an initial read
 token = tokeniser.read();
 }

 @Test
 public void testEmit() {
 Tokeniser tokeniser = createTokeniser("hello<b>world</b>");
 tokeniser.emit(new Token.Character('h'));
 tokeniser.emit(new Token.Character('e'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('o'));

 Token expectedStartTag = new Token.StartTag("b");
 tokeniser.emit(expectedStartTag);
 Token actualStartTag = tokeniser.read();
 Assert.assertEquals(expectedStartTag, actualStartTag);

 Token expectedEndTag = new Token.EndTag("b");
 tokeniser.emit(expectedEndTag);
 tokeniser.emit(new Token.Character('w'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('d'));

 Token actualEndTag = tokeniser.read();
 Assert.assertEquals(expectedEndTag, actualEndTag);
 }

 @Test
 public void testEmitString() {
 Tokeniser tokeniser = createTokeniser("hello<b>world</b>");
 tokeniser.emit("hello");
 Token expectedStartTag = new Token.StartTag("b");
 tokeniser.emit(expectedStartTag);
 Token actualStartTag = tokeniser.read();
 Assert.assertEquals(expectedStartTag, actualStartTag);

 Token expectedEndTag = new Token.EndTag("b");
 tokeniser.emit("world");
 tokeniser.emit(expectedEndTag);
 tokeniser.emit(new Token.Character(' ')); // consume whitespace
 Token actualEndTag = tokeniser.read();
 Assert.assertEquals(expectedEndTag, actualEndTag);
 }

 @Test
 public void testEmitCharacter() {
 Tokeniser tokeniser = createTokeniser("hello<b>world</b>");
 tokeniser.emit("hello");
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emit("world");
 tokeniser.emit(new Token.Character(' ')); // consume whitespace
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('/'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emit(new Token.Character(' ')); // consume whitespace
 Token expectedEndTag = new Token.EndTag("b");
 tokeniser.emit(expectedEndTag);
 Token actualEndTag = tokeniser.read();
 Assert.assertEquals(expectedEndTag, actualEndTag);
 }

 @Test
 public void testErrorHandling() {
 Tokeniser tokeniser = createTokeniser("hello<b>world<script>foo</script></b>");
 tokeniser.emit("hello");
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('b'));
 tokeniser.emit(new Token.Character('>'));
 tokeniser.emit(new Token.Character('w'));
 tokeniser.emit(new Token.Character('o'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('l'));
 tokeniser.emit(new Token.Character('d'));
 tokeniser.emit(new Token.Character('<'));
 tokeniser.emit(new Token.Character('/'));
 tokeniser.emit(new Token.Character('s'));
 tokeniser.emit(new Token.Character('c'));
 tokeniser.emit(new Token.Character('r'));
 tokeniser.emit(new Token.Character('i'));
 tokeniser.emit(new Token.Character('p'));
 tokeniser.emit(new Token.Character('t'));
 tokeniser.emit(new Token.Character('>')); // missing '>' after script tag

 // Verify that error was thrown
 Assert.assertEquals(1, tokeniser.errors.size());
 Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.errors.get(0).getMessage());
 }

 @Test
 public void testCharacterReference() {
 Tokeniser tokeniser = createTokeniser("&gt;");
 Character actualChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('>', actualChar);

 tokeniser = createTokeniser("&#x26;");
 actualChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('&', actualChar);

 tokeniser = createTokeniser("&#10;");
 actualChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('\n', actualChar);

 tokeniser = createTokeniser("&#x20AC;"); // Euro sign
 actualChar = tokeniser.consumeCharacterReference(null, false);
 Assert.assertEquals('\u20AC', (int) actualChar);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceWithInvalidCharacter() {
 Tokeniser tokeniser = createTokeniser("&");
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeCharacterReferenceInAttribute() {
 Tokeniser tokeniser = createTokeniser("<a href=\"hello&world\">");
 tokeniser.consumeCharacterReference(null, true);
 }
}