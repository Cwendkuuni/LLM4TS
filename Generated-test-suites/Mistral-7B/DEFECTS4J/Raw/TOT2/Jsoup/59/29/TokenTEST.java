import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD HTML 4.01 Transitional");
 doctype.publicIdentifier("PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
 doctype.systemIdentifier("/html4transitional.dtd");
 doctype.forceQuirks(true);

 Token expectedDoctype = new Doctype();
 expectedDoctype.name("html");
 expectedDoctype.pubSysKey("DTD HTML 4.01 Transitional");
 expectedDoctype.publicIdentifier("PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
 expectedDoctype.systemIdentifier("/html4transitional.dtd");
 expectedDoctype.forceQuirks(true);

 Assert.assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expectedDoctype.getName(), doctype.getName());
 Assert.assertEquals(expectedDoctype.getPubSysKey(), doctype.getPubSysKey());
 Assert.assertEquals(expectedDoctype.getPublicIdentifier(), doctype.getPublicIdentifier());
 Assert.assertEquals(expectedDoctype.getSystemIdentifier(), doctype.getSystemIdentifier());
 Assert.assertEquals(expectedDoctype.isForceQuirks(), doctype.isForceQuirks());
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.attributes(new Attributes().put("xmlns:html", "http://www.w3.org/1999/xhtml"));

 Token expectedStartTag = new StartTag();
 expectedStartTag.name("html");
 expectedStartTag.getAttributes().put("xmlns:html", "http://www.w3.org/1999/xhtml");

 Assert.assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expectedStartTag.getName(), startTag.getName());
 Assert.assertEquals(expectedStartTag.getAttributes(), startTag.getAttributes());
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 Token expectedEndTag = new EndTag();
 expectedEndTag.name("html");

 Assert.assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expectedEndTag.getName(), endTag.getName());
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("--This is a comment");

 Token expectedComment = new Comment();
 expectedComment.data("--This is a comment");

 Assert.assertEquals(expectedComment.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedComment.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedComment.getData(), comment.getData());
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&amp;");

 Token expectedCharacter = new Character();
 expectedCharacter.data("&amp;");

 Assert.assertEquals(expectedCharacter.tokenType(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expectedCharacter.getData(), character.getData());
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token expectedEOF = new EOF();

 Assert.assertEquals(expectedEOF.tokenType(), eof.tokenType());
 Assert.assertEquals(expectedEOF.getClass().getSimpleName(), eof.tokenType());
 }

 // Test for reset method
 @Test
 public void testResetMethod() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD HTML 4.01 Transitional");
 doctype.publicIdentifier("PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"");
 doctype.systemIdentifier("/html4transitional.dtd");
 doctype.forceQuirks(true);

 doctype.reset();

 Assert.assertFalse(doctype.isDoctype());
 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPubSysKey());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }
}