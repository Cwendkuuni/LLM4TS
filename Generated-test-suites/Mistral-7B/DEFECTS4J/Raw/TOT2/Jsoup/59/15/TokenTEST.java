import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 // Test for Token class constructor
 @Test
 public void testTokenConstructor() {
 Token token = new Token();
 Assert.assertEquals(TokenType.TokenType.DOCTYPE, token.isDoctype());
 }

 // Test for Doctype class constructor and methods
 @Test
 public void testDoctype() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD HTML 4.01 Transitional");
 doctype.systemIdentifier("/dtd/html_transitional.dtd");
 doctype.forceQuirks(true);

 Token resetDoctype = doctype.reset();
 Assert.assertEquals("html", resetDoctype.getName());
 Assert.assertNull(resetDoctype.getPubSysKey());
 Assert.assertNull(resetDoctype.getPublicIdentifier());
 Assert.assertNull(resetDoctype.getSystemIdentifier());
 Assert.assertFalse(resetDoctype.isForceQuirks());
 }

 // Test for StartTag class constructor and methods
 @Test
 public void testStartTag() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.name("html");
 Token resetStartTag = startTag.reset();
 Assert.assertEquals("html", resetStartTag.getName());
 Assert.assertNull(resetStartTag.getAttributes());
 }

 // Test for StartTag name and getAttributes methods with valid inputs
 @Test
 public void testStartTagNameAndAttributes() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.name("html");
 startTag.newAttribute().name("id").value("test");
 startTag.newAttribute().name("class").value("testClass");

 Token resetStartTag = startTag.reset();
 Assert.assertEquals("html", resetStartTag.getName());
 Assert.assertEquals("id=\"test\" class=\"testClass\"", resetStartTag.toString());
 }

 // Test for StartTag name and getAttributes methods with empty string value
 @Test
 public void testStartTagNameAndEmptyAttributes() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.name("html").newAttribute().name("class").setEmptyAttributeValue();

 Token resetStartTag = startTag.reset();
 Assert.assertEquals("html", resetStartTag.getName());
 Assert.assertNull(resetStartTag.toString());
 }

 // Test for StartTag name and getAttributes methods with null value
 @Test(expected = NullPointerException.class)
 public void testStartTagNameAndNullAttributes() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.name("html").attributes(null);

 Token resetStartTag = startTag.reset();
 Assert.assertEquals("html", resetStartTag.getName());
 }

 // Test for StartTag finaliseTag method
 @Test
 public void testStartTagFinaliseTag() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.name("html").newAttribute().name("id").value("test");
 startTag.newAttribute().name("class").value("testClass");
 startTag.finaliseTag();

 Token resetStartTag = startTag.reset();
 Assert.assertEquals("html", resetStartTag.getName());
 Assert.assertNull(resetStartTag.getAttributes());
 }

 // Test for EndTag class constructor and methods
 @Test
 public void testEndTag() {
 Token.EndTag endTag = new Token.EndTag();
 endTag.name("html");

 Token resetEndTag = endTag.reset();
 Assert.assertEquals("html", resetEndTag.getName());
 }

 // Test for Comment class constructor and methods
 @Test
 public void testComment() {
 Token.Comment comment = new Token.Comment();
 comment.data("This is a comment");

 Token resetComment = comment.reset();
 Assert.assertEquals("!--This is a comment-->", resetComment.toString());
 }

 // Test for Character class constructor and methods
 @Test
 public void testCharacter() {
 Token.Character character = new Token.Character();
 character.data("A");

 Token resetCharacter = character.reset();
 Assert.assertEquals("A", resetCharacter.getData());
 }

 // Test for EOF class constructor and methods
 @Test
 public void testEOF() {
 Token.EOF eof = new Token.EOF();

 Token resetEOF = eof.reset();
 // EOF token should not have any type or data
 Assert.assertFalse(resetEOF.isDoctype());
 Assert.assertFalse(resetEOF.isStartTag());
 Assert.assertFalse(resetEOF.isEndTag());
 Assert.assertFalse(resetEOF.isComment());
 Assert.assertFalse(resetEOF.isCharacter());
 Assert.assertFalse(resetEOF.isEOF());
 }
}