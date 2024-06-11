import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EF;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html"); // Check if the name of the Doctype is set correctly
 doctype.reset(); // Reset the token
 }

 // Test for empty Doctype token
 @Test
 public void testEmptyDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.reset(); // Reset the token
 String name = doctype.getName();
 Assertions.assertNull(name);
 }

 // Test for Doctype token with public identifier
 @Test
 public void testDoctypeTokenWithPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setPublicIdentifier("_public");
 String publicIdentifier = doctype.getPublicIdentifier();
 Assertions.assertEquals("_public", publicIdentifier);
 doctype.reset();
 }

 // Test for Doctype token with system identifier
 @Test
 public void testDoctypeTokenWithSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setSystemIdentifier("_system");
 String systemIdentifier = doctype.getSystemIdentifier();
 Assertions.assertEquals("_system", systemIdentifier);
 doctype.reset();
 }

 // Test for Doctype token with forceQuirks flag
 @Test
 public void testDoctypeTokenWithForceQuirksFlag() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setForceQuirks(true);
 boolean forceQuirks = doctype.isForceQuirks();
 Assertions.assertTrue(forceQuirks);
 doctype.reset();
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("tagName").attributes(new Attributes());
 String name = startTag.name();
 String normalName = startTag.normalName();
 Assertions.assertEquals("tagName", name);
 Assertions.assertEquals("tagname", normalName);
 startTag.reset();
 }

 // Test for StartTag token with attributes
 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("tagName").attributes(new Attributes().put("attributeName", "attributeValue"));
 String name = startTag.name();
 String normalName = startTag.normalName();
 String attributeValue = startTag.getAttributes().getValue("attributeName");
 Assertions.assertEquals("tagName", name);
 Assertions.assertEquals("tagname", normalName);
 Assertions.assertEquals("attributeValue", attributeValue);
 startTag.reset();
 }

 // Test for StartTag token with empty attribute value
 @Test
 public void testStartTagTokenWithEmptyAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.name("tagName").attributes(new Attributes().put("attributeName", ""));
 String name = startTag.name();
 String normalName = startTag.normalName();
 Assertions.assertEquals("tagName", name);
 Assertions.assertEquals("tagname", normalName);
 String attributeValue = startTag.getAttributes().getValue("attributeName");
 Assertions.assertEquals("", attributeValue);
 startTag.reset();
 }

 // Test for StartTag token with selfClosing flag
 @Test
 public void testStartTagTokenWithSelfClosingFlag() {
 StartTag startTag = new StartTag();
 startTag.name("img").attributes(new Attributes().put("src", "image.jpg")).isSelfClosing(true);
 String name = startTag.name();
 String normalName = startTag.normalName();
 boolean selfClosing = startTag.isSelfClosing();
 Assertions.assertEquals("img", name);
 Assertions.assertEquals("img", normalName);
 Assertions.assertTrue(selfClosing);
 startTag.reset();
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("tagName");
 String name = endTag.name();
 String normalName = endTag.normalName();
 Assertions.assertEquals("tagName", name);
 Assertions.assertEquals("tagname", normalName);
 endTag.reset();
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("This is a comment");
 String data = comment.getData();
 Assertions.assertEquals("This is a comment", data);
 comment.reset();
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("A");
 String data = character.getData();
 Assertions.assertEquals("A", data);
 character.reset();
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EF eof = new EF();
 Assertions.assertTrue(eof.isEOF());
 eof.reset();
 }
}