import org.jsoup.parser.Attribute;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().append("html");
 doctype.getPublicIdentifier().append("Public ID");
 doctype.getSystemIdentifier().append("System ID");
 doctype.isForceQuirks(true);

 Token expectedToken = new Doctype();
 expectedToken.asDoctype().getName().append("html");
 expectedToken.asDoctype().getPublicIdentifier().append("Public ID");
 expectedToken.asDoctype().getSystemIdentifier().append("System ID");
 expectedToken.asDoctype().isForceQuirks(true);

 assertEquals(expectedToken, doctype);
 }

 // Test for StartTag token with attributes
 @Test
 public void testStartTagTokenWithAttributes() {
 String tagName = "div";
 Attributes attributes = new Attributes();
 attributes.put(new Attribute("id", "123"));
 attributes.put(new Attribute("class", "class1 class2"));

 StartTag startTag = new StartTag(tagName).attributes(attributes);

 Token expectedToken = new StartTag(tagName).name("div").getAttributes();
 expectedToken.asStartTag().getAttributes().put(new Attribute("id", "123"));
 expectedToken.asStartTag().getAttributes().put(new Attribute("class", "class1 class2"));

 assertEquals(expectedToken, startTag);
 }

 // Test for StartTag token without attributes
 @Test
 public void testStartTagTokenWithoutAttributes() {
 String tagName = "p";
 StartTag startTag = new StartTag(tagName);

 Token expectedToken = new StartTag(tagName).name("p");

 assertEquals(expectedToken, startTag);
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 String tagName = "ul";
 EndTag endTag = new EndTag(tagName);

 Token expectedToken = new EndTag(tagName).name("ul");

 assertEquals(expectedToken, endTag);
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 StringBuilder data = new StringBuilder("This is a comment");
 Comment comment = new Comment();
 comment.getData().append(data);

 Token expectedToken = new Comment().getData().append("This is a comment");

 assertEquals(expectedToken, comment);
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character("&hellip;");

 Token expectedToken = new Character("&hellip;");

 assertEquals(expectedToken, character);
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 Token eofToken = new Token.EOF();

 Token expectedToken = new Token.EOF();

 assertEquals(expectedToken, eofToken);
 }

 // Test for isDoctype method
 @Test
 public void testIsDoctypeMethod() {
 Token token = new Doctype();

 assertTrue(token.isDoctype());
 }

 // Test for isStartTag method
 @Test
 public void testIsStartTagMethod() {
 Token token = new StartTag("p");

 assertTrue(token.isStartTag());
 }

 // Test for isEndTag method
 @Test
 public void testIsEndTagMethod() {
 Token token = new EndTag("ul");

 assertTrue(token.isEndTag());
 }

 // Test for isComment method
 @Test
 public void testIsCommentMethod() {
 Token token = new Comment();

 assertTrue(token.isComment());
 }

 // Test for isCharacter method
 @Test
 public void testIsCharacterMethod() {
 Token token = new Character("a");

 assertTrue(token.isCharacter());
 }

 // Test for isEOF method
 @Test
 public void testIsEOFMethod() {
 Token token = new Token.EOF();

 assertTrue(token.isEOF());
 }
}