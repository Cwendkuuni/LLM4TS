import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokeniserTest {

 private CharacterReader characterReader = new CharacterReader(); // for testing purposes
 private ParseErrorList errors = new ParseErrorList();
 private Tokeniser tokeniser;

 @Test
 public void testReadEmptyStream() {
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @Test
 public void testReadWhitespace() {
 characterReader.init(" \t\n\r\f ");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Whitespace, token.type);
 }

 @Test
 public void testReadCharacter() {
 characterReader.init("a");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals("a", token.data());
 }

 @Test
 public void testReadString() {
 characterReader.init("abcd");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("abcd", token.data());
 }

 @Test
 public void testReadSelfClosingTag() {
 characterReader.init("<img src='image.png' />");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("img", token.tagName());
 assertTrue(token.isSelfClosing());
 }

 @Test
 public void testReadStartTagWithAttributes() {
 characterReader.init("<div class='red' id='box'></div>");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName());
 assertEquals("red", token.attributes().get("class"));
 assertEquals("box", token.attributes().get("id"));
 }

 @Test
 public void testReadEndTag() {
 characterReader.init("<div><p></p></div>");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName());
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("div", token.tagName());
 }

 @Test
 public void testReadCharacterReference() {
 characterReader.init("&amp;");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("&amp;", token.data());
 }

 @Test
 public void testReadNamedEntity() {
 characterReader.init("&copy;");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("©", token.data());
 }

 @Test
 public void testReadInvalidCharacterReference() {
 characterReader.init("&invalid");
 tokeniser = new Tokeniser(characterReader, errors);
 tokeniser.read(); // consume &
 assertEquals(ParseError.class, errors.getError(0).getClass());
 }

 @Test
 public void testReadComment() {
 characterReader.init("<!-- This is a comment -->");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("This is a comment", token.data());
 }

 @Test
 public void testReadScriptTag() {
 characterReader.init("<script type='text/javascript'></script>");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("script", token.tagName());
 assertEquals("text/javascript", token.attributes().get("type"));
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("script", token.tagName());
 }

 @Test
 public void testReadDoctypeDeclaration() {
 characterReader.init("<!DOCTYPE html>");
 tokeniser = new Tokeniser(characterReader, errors);
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 assertEquals("html", token.data());
 }

 @Test(expected = IllegalStateException.class)
 public void testReadAfterEOF() {
 characterReader.init("abcd");
 tokeniser = new Tokeniser(characterReader, errors);
 tokeniser.read(); // read "a"
 tokeniser.read(); // read "b"
 characterReader.setEmpty();
 tokeniser.read(); // should throw exception
 }
}