import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctype_getName() {
 Doctype token = new Doctype();
 token.getName(); // no exception, so test is passed
 }

 @Test(expected = IllegalStateException.class)
 public void testDoctype_getName_before_initialized() {
 Doctype token = new Doctype();
 token.getName(); // should throw IllegalStateException
 }

 @Test
 public void testDoctype_getPublicIdentifier() {
 Doctype token = new Doctype();
 token.setPublicIdentifier("publicID");
 assertEquals("publicID", token.getPublicIdentifier());
 }

 @Test
 public void testDoctype_getSystemIdentifier() {
 Doctype token = new Doctype();
 token.setSystemIdentifier("systemID");
 assertEquals("systemID", token.getSystemIdentifier());
 }

 @Test
 public void testDoctype_isForceQuirks() {
 Doctype token = new Doctype();
 token.setForceQuirks(true);
 assertTrue(token.isForceQuirks());
 }

 @Test
 public void testDoctype_isForceQuirks_false() {
 Doctype token = new Doctype();
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testTag_getName() {
 StartTag token = new StartTag();
 token.name("tagName");
 assertEquals("tagName", token.name());
 }

 @Test
 public void testTag_name_null() {
 StartTag token = new StartTag();
 assertSame(token, token.name(null));
 assertNull(token.name());
 }

 @Test
 public void testTag_newAttribute() {
 StartTag token = new StartTag();
 token.newAttribute();
 assertNotNull(token.getAttributes());
 }

 @Test
 public void testTag_newAttribute_withValue() {
 StartTag token = new StartTag();
 token.newAttribute("name", "value");
 assertEquals("value", token.getAttributes().get("name").getValue());
 }

 @Test
 public void testTag_finaliseTag() {
 StartTag token = new StartTag();
 token.newAttribute("name", "value");
 token.finaliseTag();
 assertSame(token, token.getAttributes().get("name").getToken());
 }

 @Test
 public void testTag_isSelfClosing() {
 StartTag token = new StartTag();
 token.isSelfClosing(); // no exception, so test is passed
 }

 @Test(expected = IllegalStateException.class)
 public void testTag_isSelfClosing_before_initialized() {
 StartTag token = new StartTag();
 token.isSelfClosing(); // should throw IllegalStateException
 }

 @Test
 public void testTag_getAttributes() {
 StartTag token = new StartTag();
 token.newAttribute("name1", "value1");
 token.newAttribute("name2", "value2");
 Attributes attributes = token.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("value1", attributes.get("name1").getValue());
 assertEquals("value2", attributes.get("name2").getValue());
 }

 @Test
 public void testTag_appendTagName() {
 StartTag token = new StartTag();
 token.appendTagName("tagName");
 assertEquals("tagName", token.name());
 }

 @Test
 public void testTag_appendTagName_null() {
 StartTag token = new StartTag();
 token.appendTagName(null);
 assertNull(token.name());
 }

 @Test
 public void testTag_appendAttributeName() {
 StartTag token = new StartTag();
 token.appendAttributeName("name");
 assertEquals("name", token.pendingAttributeName);
 }

 @Test
 public void testTag_appendAttributeName_null() {
 StartTag token = new StartTag();
 token.appendAttributeName(null);
 assertNull(token.pendingAttributeName);
 }

 @Test
 public void testTag_appendAttributeValue() {
 StartTag token = new StartTag();
 token.appendAttributeValue("value");
 assertEquals("value", token.pendingAttributeValue.toString());
 }

 @Test
 public void testTag_appendAttributeValue_null() {
 StartTag token = new StartTag();
 token.appendAttributeValue(null);
 assertNull(token.pendingAttributeValue);
 }

 @Test
 public void testTokenType_getValue() {
 TokenType type = TokenType.StartTag;
 assertEquals("StartTag", type.getValue());
 }

 @Test
 public void testToken_isDoctype() {
 Doctype token = new Doctype();
 assertTrue(token.isDoctype());
 }

 @Test
 public void testToken_isStartTag() {
 StartTag token = new StartTag();
 assertTrue(token.isStartTag());
 }

 @Test
 public void testToken_isEndTag() {
 EndTag token = new EndTag();
 assertTrue(token.isEndTag());
 }

 @Test
 public void testToken_isComment() {
 Comment token = new Comment();
 assertTrue(token.isComment());
 }

 @Test
 public void testToken_isCharacter() {
 Character token = new Character("char");
 assertTrue(token.isCharacter());
 }

 @Test
 public void testToken_isEOF() {
 EOF token = new EOF();
 assertTrue(token.isEOF());
 }
}