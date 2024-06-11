import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 Token expectedToken = new Doctype();
 expectedToken.type = TokenType.Doctype;
 Assert.assertEquals(expectedToken, doctype);
 }

 @Test
 public void testDoctypeSettingName() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 Token expectedToken = new Doctype();
 expectedToken.type = TokenType.Doctype;
 expectedToken.getClass().getDeclaredField("name").set(expectedToken, "html");
 Assert.assertEquals(expectedToken, doctype);
 }

 @Test
 public void testDoctypeSettingPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.publicIdentifier.append("public");
 Token expectedToken = new Doctype();
 expectedToken.type = TokenType.Doctype;
 expectedToken.getClass().getDeclaredField("publicIdentifier").set(expectedToken, "public");
 Assert.assertEquals(expectedToken, doctype);
 }

 @Test
 public void testDoctypeSettingSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.systemIdentifier.append("/index.html");
 Token expectedToken = new Doctype();
 expectedToken.type = TokenType.Doctype;
 expectedToken.getClass().getDeclaredField("systemIdentifier").set(expectedToken, "/index.html");
 Assert.assertEquals(expectedToken, doctype);
 }

 @Test
 public void testDoctypeSettingForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks = true;
 Token expectedToken = new Doctype();
 expectedToken.type = TokenType.Doctype;
 expectedToken.getClass().getDeclaredField("forceQuirks").set(expectedToken, true);
 Assert.assertEquals(expectedToken, doctype);
 }

 // Add test cases for other Token subclasses here

 @Test
 public void testTokenToString() {
 Token token = new StartTag("div");
 String expected = "<div>";
 Assert.assertEquals(expected, token.toString());
 }

 @Test
 public void testStartTagNameSetting() {
 StartTag startTag = new StartTag();
 startTag.name("p");
 String expected = "p";
 Assert.assertEquals(expected, startTag.name());
 }

 @Test
 public void testStartTagAttributesSetting() {
 StartTag startTag = new StartTag();
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("button");
 Attributes expectedAttributes = new Attributes();
 expectedAttributes.put(new Attribute("class", "button"));
 Assert.assertEquals(expectedAttributes, startTag.getAttributes());
 }

 @Test
 public void testStartTagFinaliseTag() {
 StartTag startTag = new StartTag();
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("button");
 startTag.finaliseTag();
 Attributes attributes = startTag.getAttributes();
 Assert.assertEquals(attributes.size(), 1);
 }

 @Test(expected = AssertionError.class)
 public void testStartTagFinaliseTagWithPendingAttributeName() {
 StartTag startTag = new StartTag();
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 Assert.assertNotNull(startTag.getAttributes()); // This should not happen
 }

 @Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag();
 Token expectedToken = new EndTag();
 expectedToken.type = TokenType.EndTag;
 Assert.assertEquals(expectedToken, endTag);
 }

 @Test
 public void testEndTagNameSetting() {
 EndTag endTag = new EndTag();
 endTag.name("body");
 Token expectedToken = new EndTag();
 expectedToken.type = TokenType.EndTag;
 expectedToken.getClass().getDeclaredField("tagName").set(expectedToken, "body");
 Assert.assertEquals(expectedToken, endTag);
 }

 // Add test cases for other methods in the Token class and its subclasses here

 @Test
 public void testGetTokenType() {
 Token token = new StartTag("div");
 TokenType expectedType = TokenType.StartTag;
 Assert.assertEquals(expectedType, token.isStartTag());
 }

 @Test
 public void testGetTokenTypeDoctype() {
 Doctype doctype = new Doctype();
 TokenType expectedType = TokenType.Doctype;
 Assert.assertEquals(expectedType, doctype.isDoctype());
 }

 @Test
 public void testGetTokenTypeComment() {
 Comment comment = new Comment();
 TokenType expectedType = TokenType.Comment;
 Assert.assertEquals(expectedType, comment.isComment());
 }

 @Test
 public void testGetTokenTypeCharacter() {
 Character character = new Character("a");
 TokenType expectedType = TokenType.Character;
 Assert.assertEquals(expectedType, character.isCharacter());
 }

 @Test
 public void testGetTokenTypeEOF() {
 EOF eof = new EOF();
 TokenType expectedType = TokenType.EOF;
 Assert.assertEquals(expectedType, eof.isEOF());
 }
}