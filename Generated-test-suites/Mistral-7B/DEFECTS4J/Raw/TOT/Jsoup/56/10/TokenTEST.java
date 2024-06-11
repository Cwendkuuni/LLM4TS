import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("Public ID:");
 doctype.systemIdentifier.append("System ID:");
 doctype.forceQuirks = true;

 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("Public ID:");
 expectedDoctype.systemIdentifier.append("System ID:");
 expectedDoctype.forceQuirks = true;

 Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getType(), doctype.type);

 Assert.assertEquals("html", doctype.getName());
 Assert.assertEquals("Public ID:", doctype.getPublicIdentifier());
 Assert.assertEquals("System ID:", doctype.getSystemIdentifier());
 Assert.assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("Public ID:");
 doctype.systemIdentifier.append("System ID:");
 doctype.forceQuirks = true;
 doctype.reset();

 Assert.assertFalse(doctype.name.length() > 0);
 Assert.assertFalse(doctype.publicIdentifier.length() > 0);
 Assert.assertFalse(doctype.systemIdentifier.length() > 0);
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", null);
 startTag.attributes = new Attributes();
 startTag.attributes.put("class", "class1 class2");
 startTag.attributes.put("id", "id1");
 startTag.attributes.put("style", "color: red; background-color: blue;");

 Token expectedStartTag = new StartTag();
 expectedStartTag.tagName = "html";
 expectedStartTag.normalName = "html";
 expectedStartTag.attributes = new Attributes() {{
 put("class", new Attribute[] { new Attribute("class", "class1 class2") });
 put("id", new Attribute("id", "id1"));
 put("style", new Attribute("style", "color: red; background-color: blue;"));
 }};

 Assert.assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getType(), startTag.type);

 Assert.assertEquals("html", startTag.name());
 Assert.assertEquals("html", startTag.normalName());
 Assert.assertEquals(3, startTag.getAttributes().size());
 Assert.assertTrue(startTag.getAttributes().get("class") instanceof Attribute);
 Assert.assertEquals("class1 class2", startTag.getAttributes().get("class").toString());
 Assert.assertTrue(startTag.getAttributes().get("id") instanceof Attribute);
 Assert.assertEquals("id1", startTag.getAttributes().get("id").toString());
 Assert.assertTrue(startTag.getAttributes().get("style") instanceof Attribute);
 Assert.assertEquals("color: red; background-color: blue;", startTag.getAttributes().get("style").toString());
 }

 @Test
 public void testResetStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.tagName = "html";
 startTag.normalName = "html";
 startTag.attributes = new Attributes();
 startTag.attributes.put("class", "class1 class2");
 startTag.attributes.put("id", "id1");
 startTag.attributes.put("style", "color: red; background-color: blue;");
 startTag.reset();

 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.normalName);
 Assert.assertNull(startTag.attributes);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name = "html";
 endTag.normalName = "html";

 Token expectedEndTag = new EndTag();
 expectedEndTag.tagName = "html";
 expectedEndTag.normalName = "html";

 Assert.assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getType(), endTag.type);

 Assert.assertEquals("html", endTag.name());
 Assert.assertEquals("html", endTag.normalName());
 }

 @Test
 public void testResetEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.tagName = "html";
 endTag.normalName = "html";
 endTag.reset();

 Assert.assertNull(endTag.tagName);
 Assert.assertNull(endTag.normalName);
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("<!-- This is a comment -->");

 Token expectedComment = new Comment();
 expectedComment.data.append("This is a comment");

 Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedComment.getType(), comment.type);

 Assert.assertEquals("<!-- This is a comment -->", comment.toString());
 Assert.assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testResetCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");
 comment.reset();

 Assert.assertFalse(comment.data.length() > 0);
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&lt;");

 Token expectedCharacter = new Character();
 expectedCharacter.data("&lt;");

 Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getType(), character.type);

 Assert.assertEquals("&lt;", character.getData());
 }

 @Test
 public void testResetCharacterToken() {
 Character character = new Character();
 character.data("&lt;");
 character.reset();

 Assert.assertNull(character.data);
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token expectedEOF = new EOF();

 Assert.assertEquals(expectedEOF.tokenType(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getClass().getSimpleName(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getType(), eof.type);
 }

 @Test
 public void testResetEOFToken() {
 EOF eof = new EOF();
 eof.reset();

 Assert.assertNull(eof);
 }
}