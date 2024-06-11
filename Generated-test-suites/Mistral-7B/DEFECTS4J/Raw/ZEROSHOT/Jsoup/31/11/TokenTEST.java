import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;
import static org.jsoup.parser.Token.*;

public class TokenTest {

 @Test
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 Assert.assertEquals(TokenType.Doctype, doctype.type);
 Assert.assertNull(doctype.getName());
 Assert.assertNull(doctype.getPublicIdentifier());
 Assert.assertNull(doctype.getSystemIdentifier());
 Assert.assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeSetting() {
 Doctype doctype = new Doctype();
 doctype.getName().append("html");
 doctype.getPublicIdentifier().append("public");
 doctype.getSystemIdentifier().append("system.html");
 doctype.isForceQuirks(true);

 Assert.assertEquals("html", doctype.getName());
 Assert.assertEquals("public", doctype.getPublicIdentifier());
 Assert.assertEquals("system.html", doctype.getSystemIdentifier());
 Assert.assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagCreation() {
 StartTag startTag = new StartTag();
 Assert.assertEquals(TokenType.StartTag, startTag.type);
 Assert.assertNull(startTag.tagName);
 Assert.assertNull(startTag.attributes);
 Assert.assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagSetting() {
 StartTag startTag = new StartTag();
 startTag.tagName = "div";
 startTag.selfClosing = true;

 Assert.assertEquals("div", startTag.tagName);
 Assert.assertNull(startTag.attributes);
 Assert.assertTrue(startTag.isSelfClosing());

 StartTag startTagWithAttributes = new StartTag("div");
 startTagWithAttributes.attributes.put(new Attribute("class", "class1"));
 startTagWithAttributes.attributes.put(new Attribute("id", "id1"));

 Assert.assertEquals("div", startTagWithAttributes.tagName);
 Assert.assertNotNull(startTagWithAttributes.attributes);
 Assert.assertTrue(startTagWithAttributes.isSelfClosing());
 }

 @Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag();
 Assert.assertEquals(TokenType.EndTag, endTag.type);
 Assert.assertNull(endTag.tagName);
 }

 @Test
 public void testEndTagSetting() {
 EndTag endTag = new EndTag();
 endTag.tagName = "div";

 Assert.assertEquals("div", endTag.tagName);
 }

 @Test
 public void testCommentCreation() {
 Comment comment = new Comment();
 Assert.assertEquals(TokenType.Comment, comment.type);
 Assert.assertNull(comment.getData());
 }

 @Test
 public void testCommentSetting() {
 Comment comment = new Comment();
 comment.getData().append("This is a comment.");

 Assert.assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCharacterCreation() {
 Character character = new Character("A");
 Assert.assertEquals(TokenType.Character, character.type);
 Assert.assertEquals("A", character.getData());
 }

 @Test
 public void testCharacterSetting() {
 Character character = new Character("A");

 Assert.assertEquals("A", character.getData());
 }

 @Test
 public void testEOFCreation() {
 EOF eof = new EOF();
 Assert.assertEquals(TokenType.EOF, eof.type);
 }

 @Test
 public void testTokenTypeIdentification() {
 Token token = new Doctype();
 Assert.assertTrue(token.isDoctype());
 Token doctypeToken = new Doctype();
 Token startTagToken = new StartTag();
 Token endTagToken = new EndTag();
 Token commentToken = new Comment();
 Token characterToken = new Character("A");
 Token eofToken = new EOF();

 Assert.assertFalse(startTagToken.isDoctype());
 Assert.assertFalse(startTagToken.isEndTag());
 Assert.assertFalse(startTagToken.isComment());
 Assert.assertFalse(startTagToken.isCharacter());
 Assert.assertFalse(startTagToken.isEOF());

 Assert.assertFalse(endTagToken.isDoctype());
 Assert.assertFalse(endTagToken.isStartTag());
 Assert.assertTrue(endTagToken.isEndTag());
 Assert.assertFalse(endTagToken.isComment());
 Assert.assertFalse(endTagToken.isCharacter());
 Assert.assertFalse(endTagToken.isEOF());

 Assert.assertFalse(commentToken.isDoctype());
 Assert.assertFalse(commentToken.isStartTag());
 Assert.assertFalse(commentToken.isEndTag());
 Assert.assertTrue(commentToken.isComment());
 Assert.assertFalse(commentToken.isCharacter());
 Assert.assertFalse(commentToken.isEOF());

 Assert.assertFalse(characterToken.isDoctype());
 Assert.assertFalse(characterToken.isStartTag());
 Assert.assertFalse(characterToken.isEndTag());
 Assert.assertFalse(characterToken.isComment());
 Assert.assertTrue(characterToken.isCharacter());
 Assert.assertFalse(characterToken.isEOF());

 Assert.assertFalse(eofToken.isDoctype());
 Assert.assertFalse(eofToken.isStartTag());
 Assert.assertFalse(eofToken.isEndTag());
 Assert.assertFalse(eofToken.isComment());
 Assert.assertFalse(eofToken.isCharacter());
 Assert.assertTrue(eofToken.isEOF());
 }

}