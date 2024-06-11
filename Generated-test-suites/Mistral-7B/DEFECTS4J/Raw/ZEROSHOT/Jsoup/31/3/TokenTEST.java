import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype token = new Doctype();
 assertEquals("Doctype", token.tokenType());
 assertNull(token.getName());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeWithName() {
 Doctype token = new Doctype();
 token.getName().append("html");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNameAndPublicId() {
 Doctype token = new Doctype();
 token.getName().append("html");
 token.getPublicIdentifier().append("publicId");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertEquals("publicId", token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNamePublicIdAndSystemId() {
 Doctype token = new Doctype();
 token.getName().append("html");
 token.getPublicIdentifier().append("publicId");
 token.getSystemIdentifier().append("systemId");
 assertEquals("Doctype", token.tokenType());
 assertEquals("html", token.getName());
 assertEquals("publicId", token.getPublicIdentifier());
 assertEquals("systemId", token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeWithForceQuirks() {
 Doctype token = new Doctype();
 token.isForceQuirks(true);
 assertEquals("Doctype", token.tokenType());
 assertFalse(token.isForceQuirks()); // default value is false
 assertTrue(token.isForceQuirks()); // but we just set it to true
 }

 @Test
 public void testStartTag() {
 StartTag token = new StartTag();
 assertEquals("StartTag", token.tokenType());
 assertNull(token.tagName);
 assertNull(token.getAttributes());
 assertFalse(token.isSelfClosing());
 }

 @Test
 public void testStartTagWithName() {
 StartTag token = new StartTag("div");
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertNull(token.getAttributes());
 assertFalse(token.isSelfClosing());
 }

 @Test
 public void testStartTagWithNameAndAttributes() {
 StartTag token = new StartTag("div");
 token.newAttribute();
 token.appendAttributeName("class");
 token.appendAttributeValue("test");
 token.newAttribute();
 token.finaliseTag();
 assertEquals("StartTag", token.tokenType());
 assertEquals("div", token.tagName);
 assertEquals("class=test", token.toString());
 }

 @Test
 public void testStartTagWithNameAndAttributesAndSelfClosing() {
 StartTag token = new StartTag("img");
 token.selfClosing = true;
 token.newAttribute();
 token.appendAttributeName("src");
 token.appendAttributeValue("test.jpg");
 token.newAttribute();
 token.appendAttributeName("alt");
 token.appendAttributeValue("Test Image");
 token.finaliseTag();
 assertEquals("StartTag", token.tokenType());
 assertEquals("img", token.tagName);
 assertEquals("<img src=\"test.jpg\" alt=\"Test Image\" />", token.toString());
 }

 @Test
 public void testEndTag() {
 EndTag token = new EndTag();
 assertEquals("EndTag", token.tokenType());
 assertNull(token.tagName);
 assertEquals(0, token.getAttributes().size());
 }

 @Test
 public void testEndTagWithName() {
 EndTag token = new EndTag("p");
 assertEquals("EndTag", token.tokenType());
 assertEquals("p", token.tagName);
 assertEquals(0, token.getAttributes().size());
 }

 @Test
 public void testComment() {
 Comment token = new Comment();
 assertEquals("Comment", token.tokenType());
 assertNull(token.getData());
 }

 @Test
 public void testCommentWithData() {
 Comment token = new Comment();
 token.getData().append("This is a comment.");
 assertEquals("Comment", token.tokenType());
 assertEquals("This is a comment.", token.getData().toString());
 }

 @Test
 public void testCharacter() {
 Character token = new Character("A");
 assertEquals("Character", token.tokenType());
 assertEquals("A", token.getData());
 }

 @Test
 public void testCharacterWithMultiByte() {
 Character token = new Character("\u4F60"); // 中文字符
 assertEquals("Character", token.tokenType());
 assertEquals("\u4F60", token.getData());
 }

 @Test
 public void testEOF() {
 EOF token = new EOF();
 assertEquals("EOF", token.tokenType());
 }
}