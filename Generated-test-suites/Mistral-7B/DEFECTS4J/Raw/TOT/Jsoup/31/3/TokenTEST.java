import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("Public ID");
 doctype.systemIdentifier.append("System ID");
 doctype.forceQuirks = true;

 Token expectedToken = new Doctype();
 expectedToken.asDoctype().name.append("html");
 expectedToken.asDoctype().publicIdentifier.append("Public ID");
 expectedToken.asDoctype().systemIdentifier.append("System ID");
 expectedToken.asDoctype().isForceQuirks = true;

 Assert.assertEquals(expectedToken.tokenType(), doctype.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expectedToken.toString(), doctype.toString());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("html");
 startTag.newAttribute();
 startTag.appendAttributeName("lang");
 startTag.appendAttributeValue("en");
 startTag.newAttribute();
 startTag.appendAttributeName("id");
 startTag.appendAttributeValue("home");
 startTag.finaliseTag();

 Token expectedToken = new StartTag("html");
 expectedToken.asStartTag().attributes.put(new Attribute("lang", "en"));
 expectedToken.asStartTag().attributes.put(new Attribute("id", "home"));

 Assert.assertEquals(expectedToken.tokenType(), startTag.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expectedToken.toString(), startTag.toString());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("html");

 Token expectedToken = new EndTag("html");

 Assert.assertEquals(expectedToken.tokenType(), endTag.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expectedToken.toString(), endTag.toString());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");

 Token expectedToken = new Comment();
 expectedToken.asComment().data.append("This is a comment.");

 Assert.assertEquals(expectedToken.tokenType(), comment.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), comment.tokenType());
 Assert.assertEquals(expectedToken.toString(), comment.toString());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("&nbsp;");

 Token expectedToken = new Character("&nbsp;");

 Assert.assertEquals(expectedToken.tokenType(), character.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expectedToken.toString(), character.toString());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 Token expectedToken = new EOF();

 Assert.assertEquals(expectedToken.tokenType(), eof.tokenType());
 Assert.assertEquals(expectedToken.getClass().getSimpleName(), eof.tokenType());
 Assert.assertEquals(expectedToken.toString(), eof.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testIncompleteToken() {
 // This test case should throw an IllegalStateException due to missing finalisation
 StartTag startTag = new StartTag("html");
 startTag.newAttribute();
 startTag.appendAttributeName("lang");
 startTag.appendAttributeValue("en");
 }
}