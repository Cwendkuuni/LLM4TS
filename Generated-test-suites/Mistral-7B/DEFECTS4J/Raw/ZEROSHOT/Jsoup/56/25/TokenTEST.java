import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype token = new Doctype();
 assertEquals("Doctype", token.tokenType());

 token.getName() = "html";
 assertEquals("html", token.getName());

 token.getPublicIdentifier() = "Public ID: HTML5";
 assertEquals("Public ID: HTML5", token.getPublicIdentifier());

 token.getSystemIdentifier() = "system:http://www.w3.org/TR/html5/";
 assertEquals("system:http://www.w3.org/TR/html5/", token.getSystemIdentifier());

 token.isForceQuirks() = true;
 assertEquals(true, token.isForceQuirks());

 Token resetToken = token.reset();
 assertEquals(token, resetToken);
 }

 @Test
 public void testStartTagToken() {
 StartTag token = new StartTag();
 token.name("html");
 assertEquals("html", token.name());

 Attributes attributes = new Attributes();
 attributes.put("lang", "en");
 token.attributes = attributes;

 Token resetToken = token.reset();
 assertNull(token.tagName);
 assertNull(token.attributes);
 }

 @Test
 public void testEndTagToken() {
 EndTag token = new EndTag();
 token.name("html");
 assertEquals("html", token.name());

 Token resetToken = token.reset();
 assertNull(token.tagName);
 }

 @Test
 public void testCommentToken() {
 Comment token = new Comment();
 token.data("This is a comment");
 assertEquals("This is a comment", token.getData());

 Token resetToken = token.reset();
 assertNull(token.getData());
 }

 @Test
 public void testCharacterToken() {
 Character token = new Character();
 token.data("A");
 assertEquals("A", token.getData());

 Token resetToken = token.reset();
 assertNull(token.getData());
 }

 @Test
 public void testEOFToken() {
 EOF token = new EOF();
 assertTrue(token.isEOF());

 Token resetToken = token.reset();
 assertTrue(token.isEOF());
 }

 @Test
 public void testTokenTypes() {
 Token token = new Doctype();
 assertEquals(TokenType.Doctype, token.type);

 token = new StartTag();
 assertEquals(TokenType.StartTag, token.type);

 token = new EndTag();
 assertEquals(TokenType.EndTag, token.type);

 token = new Comment();
 assertEquals(TokenType.Comment, token.type);

 token = new Character();
 assertEquals(TokenType.Character, token.type);

 token = new EOF();
 assertEquals(TokenType.EOF, token.type);
 }

}