import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 private Token createToken(String tokenType) {
 try {
 Class<?> clazz = Class.forName("org.jsoup.parser." + tokenType);
 return (Token) clazz.newInstance();
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @org.junit.Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.reset();
 assertNull(doctype.pubSysKey);
 assertNull(doctype.publicIdentifier);
 assertNull(doctype.systemIdentifier);
 assertFalse(doctype.isForceQuirks());
 }

 @org.junit.Test
 public void testDoctypeGetName() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 assertEquals("html", doctype.getName());
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testDoctypeGetNameNull() {
 Doctype doctype = new Doctype();
 assertEquals("html", doctype.getName()); // this test should fail
 }

 @org.junit.Test
 public void testDoctypeIsForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 @org.junit.Test
 public void testTagReset() {
 Tag tag = new StartTag();
 tag.reset();
 assertNull(tag.tagName);
 assertNull(tag.normalName);
 assertNull(tag.pendingAttributeName);
 assertNull(tag.pendingAttributeValue);
 assertNull(tag.pendingAttributeValueS);
 assertFalse(tag.hasEmptyAttributeValue);
 assertFalse(tag.hasPendingAttributeValue);
 assertFalse(tag.selfClosing);
 assertNull(tag.attributes);
 }

 @org.junit.Test
 public void testTagNewAttribute() {
 StartTag tag = new StartTag();
 tag.newAttribute();
 assertNotNull(tag.attributes);
 }

 @org.junit.Test
 public void testTagNewAttributeValue() {
 StartTag tag = new StartTag();
 tag.appendAttributeValue("value");
 assertEquals("value", tag.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTagNewAttributeValueEmptyString() {
 StartTag tag = new StartTag();
 tag.setEmptyAttributeValue();
 assertTrue(tag.hasEmptyAttributeValue);
 assertEquals("", tag.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTagNewAttributeValueChar() {
 StartTag tag = new StartTag();
 tag.appendAttributeValue('a');
 tag.appendAttributeValue('b');
 assertEquals("ab", tag.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTagNewAttributeValueCharArray() {
 StartTag tag = new StartTag();
 char[] chars = {'a', 'b'};
 tag.appendAttributeValue(chars);
 assertEquals("ab", tag.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTagNewAttributeValueIntArray() {
 StartTag tag = new StartTag();
 int[] codepoints = {0x0061, 0x0062}; // 'a', 'b'
 tag.appendAttributeValue(codepoints);
 assertEquals("ab", tag.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTagFinaliseTag() {
 StartTag tag = new StartTag();
 tag.finaliseTag();
 assertNotNull(tag.attributes);
 }

 @org.junit.Test
 public void testTagIsSelfClosing() {
 StartTag tag = new StartTag();
 tag.isSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 @org.junit.Test
 public void testTagToString() {
 StartTag tag = new StartTag();
 tag.name("html");
 tag.isSelfClosing(true);
 tag.getAttributes().put("class", "test");
 assertEquals("<html class=\"test\"/>", tag.toString());
 }

 @org.junit.Test
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.reset();
 assertNull(endTag.tagName);
 assertNull(endTag.normalName);
 assertNull(endTag.pendingAttributeName);
 assertNull(endTag.pendingAttributeValue);
 assertNull(endTag.pendingAttributeValueS);
 assertFalse(endTag.hasEmptyAttributeValue);
 assertFalse(endTag.hasPendingAttributeValue);
 assertFalse(endTag.selfClosing);
 assertNull(endTag.attributes);
 }

 @org.junit.Test
 public void testEndTagToString() {
 EndTag endTag = new EndTag();
 endTag.name("html");
 assertEquals("</html>", endTag.toString());
 }

 @org.junit.Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.reset();
 assertNull(comment.getData());
 assertFalse(comment.bogus);
 }

 @org.junit.Test
 public void testCommentGetData() {
 Comment comment = new Comment();
 comment.data("Test Comment");
 assertEquals("Test Comment", comment.getData());
 }

 @org.junit.Test
 public void testCommentToString() {
 Comment comment = new Comment();
 comment.data("<!-- Test Comment -->");
 assertEquals("<!--Test Comment -->", comment.toString());
 }

 @org.junit.Test
 public void testCharacterReset() {
 Character character = new Character();
 character.reset();
 assertNull(character.getData());
 }

 @org.junit.Test
 public void testCharacterSetData() {
 Character character = new Character();
 character.data("Test");
 assertEquals("Test", character.getData());
 }

 @org.junit.Test
 public void testCharacterToString() {
 Character character = new Character();
 character.data("Test");
 assertEquals("Test", character.toString());
 }

 @org.junit.Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 assertTrue(eof.isEOF());
 }

 @org.junit.Test
 public void testTokenTypes() {
 Token token = createToken("Doctype");
 assertEquals(TokenType.Doctype, token.type);
 token = createToken("StartTag");
 assertEquals(TokenType.StartTag, token.type);
 token = createToken("EndTag");
 assertEquals(TokenType.EndTag, token.type);
 token = createToken("Comment");
 assertEquals(TokenType.Comment, token.type);
 token = createToken("Character");
 assertEquals(TokenType.Character, token.type);
 token = createToken("EOF");
 assertEquals(TokenType.EOF, token.type);
 }
}