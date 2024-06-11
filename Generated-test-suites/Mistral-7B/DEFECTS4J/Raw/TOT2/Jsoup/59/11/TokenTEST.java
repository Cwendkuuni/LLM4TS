import org.junit.Test;
import org.junit.Assert.*;
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
 doctype.getName() = "html";
 doctype.setForceQuirks(true);
 Doctype expectedDoctype = new Doctype();
 expectedDoctype.getName() = "html";
 expectedDoctype.setForceQuirks(true);
 assertEquals(expectedDoctype, doctype);
 }

 // Test for reset method of Doctype token
 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.setName("html");
 doctype.setForceQuirks(true);
 doctype.reset();
 Doctype resetDoctype = new Doctype();
 assertTrue(resetDoctype.reset() instanceof Doctype);
 assertNull(resetDoctype.getName());
 assertFalse(resetDoctype.isForceQuirks());
 }

 // Test for Getter methods of Doctype token
 @Test
 public void testDoctypeGetters() {
 Doctype doctype = new Doctype();
 doctype.setName("html");
 doctype.setForceQuirks(true);
 String name = doctype.getName();
 assertTrue(name.equals("html"));
 boolean quirks = doctype.isForceQuirks();
 assertTrue(quirks);
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.setAttributes(new Attributes());
 StartTag expectedTag = new StartTag();
 expectedTag.name("html");
 assertEquals(expectedTag, tag);
 }

 // Test for reset method of StartTag token
 @Test
 public void testStartTagReset() {
 Tag tag = new StartTag();
 tag.name("html");
 tag.setAttributes(new Attributes());
 tag.reset();
 Tag resetTag = new Tag();
 assertTrue(resetTag.reset() instanceof Tag);
 assertNull(resetTag.tagName);
 assertNull(resetTag.normalName);
 assertNull(resetTag.pendingAttributeName);
 assertNull(resetTag.pendingAttributeValue);
 assertNull(resetTag.pendingAttributeValueS);
 assertFalse(resetTag.hasEmptyAttributeValue);
 assertFalse(resetTag.hasPendingAttributeValue);
 assertFalse(resetTag.selfClosing);
 assertNull(resetTag.attributes);
 }

 // Test for Getter methods of StartTag token
 @Test
 public void testStartTagGetters() {
 StartTag tag = new StartTag();
 tag.name("html").setAttributes(new Attributes());
 String name = tag.name();
 assertTrue(name.equals("html"));
 String normalName = tag.normalName();
 assertTrue(normalName.equalsIgnoreCase("html"));
 Attributes attributes = tag.getAttributes();
 assertEquals(attributes.size(), 0);
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 Tag endTag = new EndTag();
 endTag.name("html");
 EndTag expectedEndTag = new EndTag();
 expectedEndTag.name("html");
 assertEquals(expectedEndTag, endTag);
 }

 // Test for reset method of EndTag token
 @Test
 public void testEndTagReset() {
 Tag endTag = new EndTag();
 endTag.name("html");
 endTag.reset();
 Tag resetEndTag = new Tag();
 assertTrue(resetEndTag.reset() instanceof Tag);
 assertNull(resetEndTag.tagName);
 assertNull(resetEndTag.normalName);
 assertNull(resetEndTag.pendingAttributeName);
 assertNull(resetEndTag.pendingAttributeValue);
 assertNull(resetEndTag.pendingAttributeValueS);
 assertFalse(resetEndTag.hasEmptyAttributeValue);
 assertFalse(resetEndTag.hasPendingAttributeValue);
 assertFalse(resetEndTag.selfClosing);
 assertNull(resetEndTag.attributes);
 }

 // Test for Getter methods of EndTag token
 @Test
 public void testEndTagGetters() {
 EndTag endTag = new EndTag();
 endTag.name("html");
 String name = endTag.name();
 assertTrue(name.equals("html"));
 String normalName = endTag.normalName();
 assertTrue(normalName.equalsIgnoreCase("html"));
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Token comment = new Comment();
 comment.getData().append("This is a comment");
 Comment expectedComment = new Comment();
 expectedComment.getData().append("This is a comment");
 assertEquals(expectedComment, comment);
 }

 // Test for reset method of Comment token
 @Test
 public void testCommentReset() {
 Token comment = new Comment();
 comment.getData().append("This is a comment");
 comment.reset();
 Token resetComment = new Token();
 assertTrue(resetComment.reset() instanceof Token);
 assertNull(resetComment.tokenType());
 }

 // Test for Getter methods of Comment token
 @Test
 public void testCommentGetters() {
 Comment comment = new Comment();
 comment.getData().append("This is a comment");
 String data = comment.getData().toString();
 assertEquals(data, "This is a comment");
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("A");
 Character expectedCharacter = new Character();
 expectedCharacter.data("A");
 assertEquals(expectedCharacter, character);
 }

 // Test for reset method of Character token
 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.data("A");
 character.reset();
 Character resetCharacter = new Character();
 assertTrue(resetCharacter.reset() instanceof Character);
 assertNull(resetCharacter.getData());
 }

 // Test for Getter methods of Character token
 @Test
 public void testCharacterGetters() {
 Character character = new Character();
 character.data("A");
 String data = character.getData();
 assertEquals(data, "A");
 }

 // Test for EOF token
 @Test
 public void testEOFToken() {
 Token eof = new EOF();
 Token expectedEof = new EOF();
 assertEquals(expectedEof, eof);
 }

 // Test for reset method of EOF token
 @Test
 public void testEOFReset() {
 Token eof = new EOF();
 eof.reset();
 Token resetEof = new Token();
 assertTrue(resetEof.reset() instanceof Token);
 assertNull(resetEof.tokenType());
 }

 // Test for TokenType enum
 @Test
 public void testTokenTypeEnum() {
 TokenType tokenType = TokenType.StartTag;
 TokenType expectedTokenType = TokenType.StartTag;
 assertEquals(expectedTokenType, tokenType);
 }

 // Test for Token.isDoctype() method
 @Test
 public void testIsDoctypeMethod() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 Token nonDoctypeToken = new StartTag();
 assertFalse(nonDoctypeToken.isDoctype());
 }

 // Test for Doctype.asDoctype() method
 @Test
 public void testAsDoctypeMethod() {
 Token token = new Doctype();
 Doctype doctype = token.asDoctype();
 assertEquals(token, doctype);
 }

 // Test for Token.isStartTag() method
 @Test
 public void testIsStartTagMethod() {
 Token token = new StartTag();
 assertTrue(token.isStartTag());
 Token nonStartTagToken = new Comment();
 assertFalse(nonStartTagToken.isStartTag());
 }

 // Test for StartTag.asStartTag() method
 @Test
 public void testAsStartTagMethod() {
 Token token = new StartTag();
 StartTag startTag = token.asStartTag();
 assertEquals(token, startTag);
 }

 // Test for Token.isEndTag() method
 @Test
 public void testIsEndTagMethod() {
 Token token = new EndTag();
 assertTrue(token.isEndTag());
 Token nonEndTagToken = new StartTag();
 assertFalse(nonEndTagToken.isEndTag());
 }

 // Test for EndTag.asEndTag() method
 @Test
 public void testAsEndTagMethod() {
 Token token = new EndTag();
 EndTag endTag = token.asEndTag();
 assertEquals(token, endTag);
 }

 // Test for Token.isComment() method
 @Test
 public void testIsCommentMethod() {
 Token token = new Comment();
 assertTrue(token.isComment());
 Token nonCommentToken = new StartTag();
 assertFalse(nonCommentToken.isComment());
 }

 // Test for Comment.asComment() method
 @Test
 public void testAsCommentMethod() {
 Token token = new Comment();
 Comment comment = token.asComment();
 assertEquals(token, comment);
 }

 // Test for Token.isCharacter() method
 @Test
 public void testIsCharacterMethod() {
 Token token = new Character();
 assertTrue(token.isCharacter());
 Token nonCharacterToken = new StartTag();
 assertFalse(nonCharacterToken.isCharacter());
 }

 // Test for Character.asCharacter() method
 @Test
 public void testAsCharacterMethod() {
 Token token = new Character();
 Character character = token.asCharacter();
 assertEquals(token, character);
 }

 // Test for Token.isEOF() method
 @Test
 public void testIsEOFMethod() {
 Token token = new EOF();
 assertTrue(token.isEOF());
 Token nonEOFToken = new StartTag();
 assertFalse(nonEOFToken.isEOF());
 }

 // Test for EOF.asEOF() method
 @Test
 public void testAsEOFMethod() {
 Token token = new EOF();
 EOF eof = token.asEOF();
 assertEquals(token, eof);
 }

 // Test for Edge cases and exception handling scenarios
 @Test(expected = IllegalStateException.class)
 public void testIllegalStateException() {
 StartTag startTag = new StartTag();
 startTag.newAttribute();
 startTag.newAttribute();
 }

 @Test
 public void testSelfClosingTag() {
 StartTag selfClosingTag = new StartTag();
 selfClosingTag.name("img").isSelfClosing(true);
 assertTrue(selfClosingTag.isSelfClosing());
 }

 @Test
 public void testEmptyStringAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeValue("");
 startTag.newAttribute();
 assertTrue(startTag.hasEmptyAttributeValue);
 }

 @Test
 public void testStringAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeValue("value");
 startTag.newAttribute();
 assertTrue(startTag.hasPendingAttributeValue);
 assertEquals("value", startTag.pendingAttributeValue.toString());
 }

 @Test
 public void testStringArrayAttributeValue() {
 StartTag startTag = new StartTag();
 char[] attributeValues = {'a', 'b', 'c'};
 startTag.appendAttributeValue(attributeValues);
 startTag.newAttribute();
 assertTrue(startTag.hasPendingAttributeValue);
 assertEquals("abc", startTag.pendingAttributeValue.toString());
 }

 @Test
 public void testIntArrayAttributeValue() {
 StartTag startTag = new StartTag();
 int[] attributeValues = {1, 2, 3};
 startTag.appendAttributeValue(attributeValues);
 startTag.newAttribute();
 assertTrue(startTag.hasPendingAttributeValue);
 assertEquals("1 2 3", startTag.pendingAttributeValue.toString());
 }

 @Test
 public void testNullAttributeName() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeName(null);
 startTag.newAttribute();
 assertNull(startTag.pendingAttributeName);
 }

 @Test
 public void testNullAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.appendAttributeValue(null);
 startTag.newAttribute();
 assertNull(startTag.pendingAttributeValue);
 }

 @Test
 public void testNullInput() {
 Token token = new Token();
 token.reset();
 assertNull(token.tokenType());
 }
}