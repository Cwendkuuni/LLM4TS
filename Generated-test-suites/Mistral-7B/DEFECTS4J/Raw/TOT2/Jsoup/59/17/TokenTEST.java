import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctypeToken() {
 Token doctypeToken = new Token.Doctype();
 assertEquals(TokenType.Doctype, doctypeToken.tokenType());
 }

 @Test
 public void testDoctypeReset() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.reset();
 assertNull(doctypeToken.getName());
 assertNull(doctypeToken.getPubSysKey());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());
 assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testDoctypeName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.name("html");
 assertEquals("html", doctypeToken.getName());
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeInvalidName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.name("invalidName");
 }

 @Test
 public void testDoctypeGetForceQuirks() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.isForceQuirks(true);
 assertTrue(doctypeToken.isForceQuirks());
 }

 // Test for StartTag token
 @Test
 public void testStartTagToken() {
 Token startTagToken = new Token.StartTag();
 assertEquals(TokenType.StartTag, startTagToken.tokenType());
 }

 @Test
 public void testStartTagReset() {
 Token startTagToken = new Token.StartTag();
 startTagToken.reset();
 assertNull(startTagToken.tagName);
 assertNull(startTagToken.normalName);
 assertNull(startTagToken.pendingAttributeName);
 assertNull(startTagToken.pendingAttributeValue);
 assertNull(startTagToken.pendingAttributeValueS);
 assertFalse(startTagToken.hasEmptyAttributeValue);
 assertFalse(startTagToken.hasPendingAttributeValue);
 assertFalse(startTagToken.isSelfClosing());
 assertNull(startTagToken.attributes);
 }

 @Test
 public void testStartTagName() {
 Token startTagToken = new Token.StartTag();
 startTagToken.name("div");
 assertEquals("div", startTagToken.name());
 }

 @Test
 public void testStartTagSelfClosing() {
 Token startTagToken = new Token.StartTag();
 startTagToken.isSelfClosing(true);
 assertTrue(startTagToken.isSelfClosing());
 }

 @Test(expected = AssertionError.class)
 public void testStartTagInvalidName() {
 Token startTagToken = new Token.StartTag();
 startTagToken.name("invalidName");
 }

 @Test
 public void testStartTagNewAttribute() {
 Token startTagToken = new Token.StartTag();
 startTagToken.newAttribute();
 assertEquals(1, startTagToken.attributes.size());
 }

 @Test
 public void testStartTagNewAttributeWithValue() {
 Token startTagToken = new Token.StartTag();
 startTagToken.appendAttributeName("class");
 startTagToken.appendAttributeValue("testClass");
 startTagToken.newAttribute();
 assertEquals("class", startTagToken.attributes.attribute(0).getName());
 assertEquals("testClass", startTagToken.attributes.attribute(0).getValue());
 }

 @Test
 public void testStartTagNewAttributeWithEmptyValue() {
 Token startTagToken = new Token.StartTag();
 startTagToken.appendAttributeName("class");
 startTagToken.setEmptyAttributeValue();
 startTagToken.newAttribute();
 assertEquals("class", startTagToken.attributes.attribute(0).getName());
 assertEquals("", startTagToken.attributes.attribute(0).getValue());
 }

 // Test for EndTag token
 @Test
 public void testEndTagToken() {
 Token endTagToken = new Token.EndTag();
 assertEquals(TokenType.EndTag, endTagToken.tokenType());
 }

 @Test
 public void testEndTagReset() {
 Token endTagToken = new Token.EndTag();
 endTagToken.reset();
 assertNull(endTagToken.tagName);
 assertNull(endTagToken.normalName);
 assertNull(endTagToken.pendingAttributeName);
 assertNull(endTagToken.pendingAttributeValue);
 assertNull(endTagToken.pendingAttributeValueS);
 assertFalse(endTagToken.hasEmptyAttributeValue);
 assertFalse(endTagToken.hasPendingAttributeValue);
 assertFalse(endTagToken.isSelfClosing());
 assertNull(endTagToken.attributes);
 }

 @Test
 public void testEndTagName() {
 Token endTagToken = new Token.EndTag();
 endTagToken.name("div");
 assertEquals("div", endTagToken.name());
 }

 @Test(expected = AssertionError.class)
 public void testEndTagInvalidName() {
 Token endTagToken = new Token.EndTag();
 endTagToken.name("invalidName");
 }

 // Test for Comment token
 @Test
 public void testCommentToken() {
 Token commentToken = new Token.Comment();
 assertEquals(TokenType.Comment, commentToken.tokenType());
 }

 @Test
 public void testCommentReset() {
 Token commentToken = new Token.Comment();
 commentToken.reset();
 assertNull(commentToken.getData());
 assertFalse(commentToken.bogus);
 }

 @Test
 public void testCommentData() {
 Token commentToken = new Token.Comment();
 commentToken.data("This is a comment");
 assertEquals("This is a comment", commentToken.getData());
 }

 @Test
 public void testCommentBogus() {
 Token commentToken = new Token.Comment();
 commentToken.bogus(true);
 assertTrue(commentToken.isBogus());
 }

 // Test for Character token
 @Test
 public void testCharacterToken() {
 Token characterToken = new Token.Character();
 assertEquals(TokenType.Character, characterToken.tokenType());
 }

 @Test
 public void testCharacterReset() {
 Token characterToken = new Token.Character();
 characterToken.reset();
 assertNull(characterToken.getData());
 }

 @Test
 public void testCharacterData() {
 Token characterToken = new Token.Character();
 characterToken.data("A");
 assertEquals("A", characterToken.getData());
 }

 // Test for EOF token
 @Test
 public void testEoFToken() {
 Token eofToken = new Token.EOF();
 assertEquals(TokenType.EOF, eofToken.tokenType());
 }

 @Test
 public void testEoFReset() {
 Token eofToken = new Token.EOF();
 eofToken.reset();
 }

}