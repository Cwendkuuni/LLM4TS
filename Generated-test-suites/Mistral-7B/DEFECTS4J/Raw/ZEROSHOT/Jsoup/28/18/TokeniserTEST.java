import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() throws IOException {
 reader = Mockito.mock(CharacterReader.class);
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testReadCharacter() throws IOException {
 byte[] input = "<html><head></head><body><p>Hello World</p></body></html>".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("H"));
 }

 @Test
 public void testReadCharacterReference() throws IOException {
 byte[] input = "&apos; &amp; &eacute; &nbsp; &ndash; &rsquo; &rsquo; &rsquo; &rsquo;".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\''"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("&"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("#x00A1"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\u00A0"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("-"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\'"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\'"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\'"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\'"));
 }

 @Test(expected = Exception.class)
 public void testReadCharacterNotFound() throws IOException {
 byte[] input = "&foo;".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 tokeniser.read();
 tokeniser.read();

 tokeniser.advanceTransition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(null, false);
 }

 @Test
 public void testReadWhitespace() throws IOException {
 byte[] input = "<html><head></head><body><p >\tHello World</p></body></html>".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Whitespace));
 assertThat(token.data, is(" "));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Whitespace));
 assertThat(token.data, is("\t"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("H"));
 }

 @Test
 public void testReadSelfClosingTag() throws IOException {
 byte[] input = "<img src=\"image.jpg\" alt=\"Image\" />".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("img"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("src"));
 assertThat(token.value, is("image.jpg"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("="));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\""));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("\""));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(" "));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.EndTag));
 assertThat(token.tagName, is("img"));
 }

 @Test
 public void testReadStartTag() throws IOException {
 byte[] input = "<p class=\"red\">Hello World</p>".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("p"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("class"));
 assertThat(token.value, is("red"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(">"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("H"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("e"));

 //...

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("l"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("l"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is("o"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(" "));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Text));
 assertThat(token.data, is("World"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.EndTag));
 assertThat(token.tagName, is("p"));
 }

 @Test
 public void testReadEndTag() throws IOException {
 byte[] input = "<p class=\"red\">Hello World</p>\n<p class=\"blue\">Hi There</p>".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("p"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("class"));
 assertThat(token.value, is("red"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(">"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Text));
 assertThat(token.data, is("Hello World"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.EndOfDocument));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("p"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("class"));
 assertThat(token.value, is("blue"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(">"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Text));
 assertThat(token.data, is("Hi There"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.EndTag));
 assertThat(token.tagName, is("p"));
 }

 @Test(expected = Exception.class)
 public void testReadEndTagMismatch() throws IOException {
 byte[] input = "<p class=\"red\">Hello World</p>\n<p class=\"blue\">Hi There</p>\n<p class=\"green\">Test</p>".getBytes();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
 Mockito.when(reader.read()).thenReturn(inputStream);

 Token token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("p"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("class"));
 assertThat(token.value, is("red"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(">"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Text));
 assertThat(token.data, is("Hello World"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.EndOfDocument));

 token = tokeniser.read(); // should be <p class="green">

 assertThat(token.type, is(Token.TokenType.StartTag));
 assertThat(token.tagName, is("p"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Attribute));
 assertThat(token.name, is("class"));
 assertThat(token.value, is("green"));

 token = tokeniser.read();

 assertThat(token.type, is(Token.TokenType.Character));
 assertThat(token.data, is(">"));

 token = tokeniser.read(); // should be <p>

 assertThat(token.type, is(Token.TokenType.EndTag));
 assertThat(token.tagName, is("p"));
 }
}