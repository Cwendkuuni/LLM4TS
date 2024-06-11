import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @org.junit.Test
 public void testDoctypeToken() {
 Doctype docType = new Doctype();
 assertEquals("Doctype", docType.tokenType());
 // Add assertions for name, publicIdentifier, systemIdentifier, and forceQuirks fields if necessary
 }

 @org.junit.Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag("html");
 assertEquals("StartTag", startTag.tokenType().name());
 assertEquals("html", startTag.name());
 assertFalse(startTag.isSelfClosing());
 assertNull(startTag.getAttributes());

 StartTag tagWithAttributes = new StartTag("img").name("src", "image.jpg").name("alt", "Alternate text");
 assertEquals("StartTag", tagWithAttributes.tokenType().name());
 assertEquals("img", tagWithAttributes.name());
 assertFalse(tagWithAttributes.isSelfClosing());
 assertNotNull(tagWithAttributes.getAttributes());
 assertEquals("src", tagWithAttributes.getAttributes().keySet().iterator().next().toString());
 assertEquals("image.jpg", tagWithAttributes.getAttributes().value("src"));
 assertEquals("alt", tagWithAttributes.getAttributes().keySet().iterator().next());
 assertEquals("Alternate text", tagWithAttributes.getAttributes().value("alt"));
 }

 @org.junit.Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag("body");
 assertEquals("EndTag", endTag.tokenType().name());
 assertEquals("body", endTag.name());
 }

 @org.junit.Test
 public void testCommentToken() {
 Comment comment = new Comment();
 assertEquals("Comment", comment.tokenType().name());
 assertEquals("", comment.getData());
 // Add assertions for data field if necessary
 }

 @org.junit.Test
 public void testCharacterToken() {
 Character character = new Character("A");
 assertEquals("Character", character.tokenType().name());
 assertEquals("A", character.getData());
 }

 @org.junit.Test
 public void testEOFToken() {
 EOF eof = new EOF();
 assertEquals("EOF", eof.tokenType().name());
 }

 @org.junit.Test
 public void testTokenTypeEnum() {
 TokenType[] tokenTypes = TokenType.values();
 for (TokenType tokenType : tokenTypes) {
 assertEquals(tokenType.name().toLowerCase(), tokenType.name());
 }
 }
}