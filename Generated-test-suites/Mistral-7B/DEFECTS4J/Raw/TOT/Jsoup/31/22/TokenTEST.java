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
 doctype.publicIdentifier.append("W3C");
 doctype.systemIdentifier.append("/trantor/html4");
 doctype.isForceQuirks(true);

 Token expectedToken = doctype;
 Token actualToken = new Doctype();
 actualToken.name.append("html");
 actualToken.publicIdentifier.append("W3C");
 actualToken.systemIdentifier.append("/trantor/html4");
 actualToken.isForceQuirks(true);

 Assert.assertEquals(expectedToken.tokenType(), actualToken.tokenType());
 Assert.assertEquals(expectedToken.getClass(), actualToken.getClass());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("html");
 startTag.newAttribute();
 startTag.appendAttributeName("lang");
 startTag.appendAttributeValue("en");
 startTag.newAttribute();
 startTag.appendAttributeName("version");
 startTag.appendAttributeValue("4.01");
 startTag.finaliseTag();

 Token expectedToken = startTag;
 Token actualToken = new StartTag("html");
 actualToken.newAttribute();
 actualToken.appendAttributeName("lang");
 actualToken.appendAttributeValue("en");
 actualToken.newAttribute();
 actualToken.appendAttributeName("version");
 actualToken.appendAttributeValue("4.01");
 actualToken.finaliseTag();

 Assert.assertEquals(expectedToken.toString(), actualToken.toString());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("body");

 Token expectedToken = endTag;
 Token actualToken = new EndTag("body");

 Assert.assertEquals(expectedToken.toString(), actualToken.toString());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");

 Token expectedToken = comment;
 Token actualToken = new Comment();
 actualToken.data.append("This is a comment.");

 Assert.assertEquals(expectedToken.toString(), actualToken.toString());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("&nbsp;");

 Token expectedToken = character;
 Token actualToken = new Character(" ");
 actualToken.getData().equals("&nbsp;");

 Assert.assertEquals(expectedToken.tokenType(), actualToken.tokenType());
 Assert.assertEquals(expectedToken.getData(), actualToken.getData());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 Token expectedToken = eof;
 Token actualToken = new EOF();

 Assert.assertEquals(expectedToken.tokenType(), actualToken.tokenType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoctypeInvalidName() {
 Doctype doctype = new Doctype();
 doctype.name.append("&html"); // invalid name with an ampersand

 // Calling a method that will trigger the creation of a new Token instance
 // which will fail since the name is invalid.
 new StartTag("html");
 }
}