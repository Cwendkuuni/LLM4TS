import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().isEmpty();
 doctype.getSystemIdentifier().isEmpty();
 doctype.isForceQuirks() == false;
 }

 @Test
 public void testDoctypeTokenWithPublicAndSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().equals("public");
 doctype.getSystemIdentifier().equals("/my/test/page.html");
 doctype.isForceQuirks() == false;
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeTokenWithNegativeForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks() = true; // should throw an AssertionError as forceQuirks cannot be set directly
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag("div");
 startTag.name().equals("div");
 startTag.isSelfClosing() == false;
 assertTrue(startTag.getAttributes() == null);
 }

 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag("p").name("p").newAttribute().attr("id", "para1");
 startTag.name().equals("p");
 startTag.isSelfClosing() == false;
 Attribute idAttribute = startTag.getAttributes().get("id");
 idAttribute.getValue().equals("para1");
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag("p");
 endTag.name().equals("p");
 endTag.isSelfClosing() == false;
 }

 @Test(expected = AssertionError.class)
 public void testEndTagTokenWithInvalidName() {
 EndTag endTag = new EndTag("div"); // endTags cannot have names
 assertFalse(endTag.name() != null); // should throw an AssertionError
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().isEmpty();
 comment.isSelfClosing() == false;
 comment.toString().startsWith("<!--");
 }

 @Test
 public void testCommentTokenWithData() {
 Comment comment = new Comment("This is a test comment");
 comment.getData().equals("This is a test comment");
 comment.toString().startsWith("<!--This is a test comment-->");
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character("A");
 character.getData().equals("A");
 character.isSelfClosing() == false;
 character.toString().equals("A");
 }

 @Test
 public void testCharacterTokenWithMultipleCharacters() {
 Character character = new Character("ABC");
 character.getData().equals("ABC");
 character.isSelfClosing() == false;
 character.toString().equals("ABC");
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 eof.isDoctype() == false;
 eof.isStartTag() == false;
 eof.isEndTag() == false;
 eof.isComment() == false;
 eof.isCharacter() == false;
 eof.isEOF() == true;
 }
}