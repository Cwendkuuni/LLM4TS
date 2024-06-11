import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

 @Test
 public void testToken_Doctype() {
 Doctype doctype = new Doctype();
 Assert.assertEquals(TokenType.Doctype, doctype.type);
 doctype.reset();
 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPubSysKey());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testToken_Doctype_withData() {
 Doctype doctype = new Doctype();
 doctype.getName().append("html");
 Assert.assertEquals("html", doctype.getName());
 doctype.setPubSysKey("pubsyskey");
 Assert.assertEquals("pubsyskey", doctype.getPubSysKey());
 doctype.getPublicIdentifier().append("publicid");
 Assert.assertEquals("publicid", doctype.getPublicIdentifier().toString());
 doctype.getSystemIdentifier().append("systemid");
 Assert.assertEquals("systemid", doctype.getSystemIdentifier().toString());
 doctype.isForceQuirks(true);
 Assert.assertTrue(doctype.isForceQuirks());
 doctype.reset();
 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPubSysKey());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testToken_StartTag() {
 StartTag startTag = new StartTag();
 Assert.assertEquals(TokenType.StartTag, startTag.type);
 startTag.name("div");
 Assert.assertEquals("div", startTag.name());
 startTag.name("DIV");
 Assert.assertEquals("div", startTag.name());
 startTag.tagName = "p";
 Assert.assertEquals("p", startTag.name());
 startTag.reset();
 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.normalName);
 Assert.assertNull(startTag.getAttributes());
 }

 @Test
 public void testToken_StartTag_withAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("input");
 Attribute attr = new Attribute("type", "text");
 startTag.attributes.put(attr);
 Attribute attr2 = new Attribute("name", "username");
 startTag.attributes.put(attr2);
 startTag.newAttribute();
 Assert.assertNotNull(startTag.getAttributes());
 Assert.assertEquals(2, startTag.getAttributes().size());
 Assert.assertEquals("text", startTag.attributes.get("type").getValue());
 Assert.assertEquals("username", startTag.attributes.get("name").getValue());
 startTag.reset();
 Assert.assertNull(startTag.getAttributes());
 }

 @Test
 public void testToken_EndTag() {
 EndTag endTag = new EndTag();
 Assert.assertEquals(TokenType.EndTag, endTag.type);
 endTag.name("p");
 Assert.assertEquals("p", endTag.name());
 endTag.reset();
 Assert.assertNull(endTag.tagName);
 Assert.assertNull(endTag.normalName);
 Assert.assertNull(endTag.getAttributes());
 }

 @Test
 public void testToken_Comment() {
 Comment comment = new Comment();
 Assert.assertEquals(TokenType.Comment, comment.type);
 comment.getData().append("This is a comment");
 Assert.assertEquals("This is a comment", comment.getData().toString());
 comment.bogus = true;
 Assert.assertTrue(comment.bogus);
 comment.reset();
 Assert.assertNull(comment.getData());
 Assert.assertFalse(comment.bogus);
 }

 @Test
 public void testToken_Character() {
 Character character = new Character();
 Assert.assertEquals(TokenType.Character, character.type);
 character.data("Hello World!");
 Assert.assertEquals("Hello World!", character.getData());
 character.reset();
 Assert.assertNull(character.getData());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToken_InvalidCharacter() {
 Character character = new Character();
 character.appendAttributeValue('\u0000');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToken_InvalidAttributeName() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName('\u0000');
 }

 @Test
 public void testToken_EOF() {
 EOF eof = new EOF();
 Assert.assertEquals(TokenType.EOF, eof.type);
 eof.reset();
 }
}