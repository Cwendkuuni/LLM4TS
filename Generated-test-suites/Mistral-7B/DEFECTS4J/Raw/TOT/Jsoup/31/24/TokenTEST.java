import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("Public ID");
 doctype.systemIdentifier.append("System ID");
 doctype.forceQuirks = true;
 Token expected = new Doctype();
 expected.name.append("html");
 expected.publicIdentifier.append("Public ID");
 expected.systemIdentifier.append("System ID");
 expected.forceQuirks = true;

 Assert.assertEquals(expected.tokenType(), doctype.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), doctype.tokenType());
 Assert.assertEquals(expected.getClass(), doctype.getClass());
 Assert.assertEquals(expected.toString(), doctype.toString());
 }

 @Test
 public void testStartTag() {
 String tagName = "div";
 StartTag startTag = new StartTag(tagName);
 startTag.newAttribute();
 startTag.appendAttributeName("id");
 startTag.appendAttributeValue("test");
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("testClass");

 Token expected = new StartTag("div");
 expected.newAttribute();
 expected.appendAttributeName("id");
 expected.appendAttributeValue("test");
 expected.newAttribute();
 expected.appendAttributeName("class");
 expected.appendAttributeValue("testClass");

 Assert.assertEquals(expected.tokenType(), startTag.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), startTag.tokenType());
 Assert.assertEquals(expected.getClass(), startTag.getClass());
 Assert.assertEquals(expected.toString(), startTag.toString());
 }

 @Test
 public void testEndTag() {
 String tagName = "div";
 EndTag endTag = new EndTag(tagName);

 Token expected = new EndTag(tagName);

 Assert.assertEquals(expected.tokenType(), endTag.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), endTag.tokenType());
 Assert.assertEquals(expected.getClass(), endTag.getClass());
 Assert.assertEquals(expected.toString(), endTag.toString());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("test");

 Token expected = new Character("test");

 Assert.assertEquals(expected.tokenType(), character.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), character.tokenType());
 Assert.assertEquals(expected.getClass(), character.getClass());
 Assert.assertEquals(expected.toString(), character.toString());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 Token invalidToken = new Object();

 Assert.assertNotEquals(TokenType.Doctype, invalidToken.tokenType());
 }

 @Test(expected = AssertionError.class)
 public void testNullToken() {
 Token nullToken = null;

 Assert.assertNull(nullToken);
 Assert.assertNull(nullToken.tokenType());
 }

 @Test
 public void testEOF() {
 Token eof = new Token.EOF();

 Token expected = new Token.EOF();

 Assert.assertEquals(expected.tokenType(), eof.tokenType());
 Assert.assertEquals(expected.getClass().getSimpleName(), eof.tokenType());
 Assert.assertEquals(expected.getClass(), eof.getClass());
 Assert.assertEquals(expected.toString(), eof.toString());
 }

 @Test
 public void testStartTagWithEmptyName() {
 StartTag startTag = new StartTag();
 Assert.assertEquals(startTag.name().length(), 0);
 }
}