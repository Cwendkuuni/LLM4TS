import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 private Token createToken(TokenType tokenType) {
 switch (tokenType) {
 case Doctype:
 return new Token.Doctype();
 case StartTag:
 return new Token.StartTag();
 case EndTag:
 return new Token.EndTag();
 case Comment:
 return new Token.Comment();
 case Character:
 return new Token.Character();
 case EOF:
 return new Token.EOF();
 default:
 throw new IllegalArgumentException("Unsupported token type: " + tokenType);
 }
 }

 @org.junit.Test
 public void testTokenType() {
 Token token = createToken(TokenType.StartTag);
 assertEquals("StartTag", token.tokenType());
 }

 @org.junit.Test
 public void testReset_Doctype() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.newAttribute();
 doctypeToken.getName(); // tests name() method
 doctypeToken.reset();
 assertNull(doctypeToken.getName());
 }

 @org.junit.Test
 public void testReset_StartTag() {
 Token startTagToken = new Token.StartTag();
 startTagToken.name("test");
 startTagToken.reset();
 assertNull(startTagToken.tagName);
 }

 @org.junit.Test
 public void testReset_EndTag() {
 Token endTagToken = new Token.EndTag();
 endTagToken.name("test");
 endTagToken.reset();
 assertNull(endTagToken.tagName);
 }

 @org.junit.Test
 public void testReset_Comment() {
 Token commentToken = new Token.Comment();
 commentToken.getData("test");
 commentToken.reset();
 assertNull(commentToken.getData());
 }

 @org.junit.Test
 public void testReset_Character() {
 Token characterToken = new Token.Character();
 characterToken.data("test");
 characterToken.reset();
 assertNull(characterToken.getData());
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testReset_EOF() {
 Token eofToken = new Token.EOF();
 eofToken.reset();
 fail("EOF token should not be resettable");
 }

 @org.junit.Test
 public void testDoctype_getName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.getName();
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testDoctype_getName_NullName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.name(null);
 }

 @org.junit.Test
 public void testDoctype_getPubSysKey() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.setPubSysKey("test");
 assertEquals("test", doctypeToken.getPubSysKey());
 }

 @org.junit.Test
 public void testDoctype_getPublicIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.setPublicIdentifier("test");
 assertEquals("test", doctypeToken.getPublicIdentifier());
 }

 @org.junit.Test
 public void testDoctype_getSystemIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.setSystemIdentifier("test");
 assertEquals("test", doctypeToken.getSystemIdentifier());
 }

 @org.junit.Test
 public void testDoctype_isForceQuirks() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.setForceQuirks(true);
 assertTrue(doctypeToken.isForceQuirks());
 }

 @org.junit.Test
 public void testTag_name() {
 Token tagToken = new Token.StartTag();
 tagToken.name("test");
 assertEquals("test", tagToken.name());
 }

 @org.junit.Test(expected = NullPointerException.class)
 public void testTag_name_NullName() {
 Token tagToken = new Token.StartTag();
 tagToken.name(null);
 }

 @org.junit.Test
 public void testTag_normalName() {
 Token tagToken = new Token.StartTag();
 tagToken.name("Test");
 assertEquals("test", tagToken.normalName());
 }

 @org.junit.Test
 public void testTag_isSelfClosing() {
 Token tagToken = new Token.StartTag();
 tagToken.isSelfClosing(true);
 assertTrue(tagToken.isSelfClosing());
 }

 @org.junit.Test
 public void testTag_getAttributes() {
 Token tagToken = new Token.StartTag();
 tagToken.newAttribute();
 tagToken.newAttribute();
 Attributes attributes = tagToken.getAttributes();
 assertNotNull(attributes);
 assertTrue(attributes.size() > 0);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTag_newAttribute_NoPendingAttributeName() {
 Token tagToken = new Token.StartTag();
 tagToken.newAttribute();
 tagToken.newAttribute(); // second call should throw exception
 }

 @org.junit.Test
 public void testTag_appendAttributeName() {
 Token tagToken = new Token.StartTag();
 tagToken.appendAttributeName("test");
 tagToken.appendAttributeName(" attribute");
 assertEquals("test attribute", tagToken.pendingAttributeName);
 }

 @org.junit.Test
 public void testTag_appendAttributeValue() {
 Token tagToken = new Token.StartTag();
 tagToken.appendAttributeValue("value");
 assertEquals("value", tagToken.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTag_appendAttributeValue_EmptyString() {
 Token tagToken = new Token.StartTag();
 tagToken.setEmptyAttributeValue();
 assertEquals("", tagToken.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTag_appendAttributeValue_Char() {
 Token tagToken = new Token.StartTag();
 tagToken.appendAttributeValue('&');
 assertEquals("&", tagToken.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTag_appendAttributeValue_CharArray() {
 Token tagToken = new Token.StartTag();
 tagToken.appendAttributeValue(new char[]{'<', '&'});
 assertEquals("&<", tagToken.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTag_appendAttributeValue_IntArray() {
 Token tagToken = new Token.StartTag();
 tagToken.appendAttributeValue(new int[]{'<', '&'});
 assertEquals("&<", tagToken.pendingAttributeValue.toString());
 }

 @org.junit.Test
 public void testTag_finaliseTag() {
 Token tagToken = new Token.StartTag();
 tagToken.newAttribute();
 tagToken.finaliseTag();
 assertTrue(tagToken.hasPendingAttributeValue());
 }

 @org.junit.Test
 public void testTag_finaliseTag_NullPendingAttributeValue() {
 Token tagToken = new Token.StartTag();
 tagToken.newAttribute();
 tagToken.finaliseTag();
 assertFalse(tagToken.hasPendingAttributeValue());
 }

 @org.junit.Test
 public void testTag_finaliseTag_PendingAttributeValueSet() {
 Token tagToken = new Token.StartTag();
 tagToken.newAttribute();
 tagToken.finaliseTag();
 tagToken.newAttribute();
 tagToken.appendAttributeValue("value");
 tagToken.finaliseTag();
 assertFalse(tagToken.hasPendingAttributeValue());
 }

 @org.junit.Test
 public void testComment_getData() {
 Token commentToken = new Token.Comment();
 commentToken.getData("test");
 assertEquals("test", commentToken.getData());
 }

 @org.junit.Test(expected = AssertionError.class)
 public void testComment_getData_EmptyString() {
 Token commentToken = new Token.Comment();
 commentToken.getData("");
 fail("Empty string should not be allowed as comment data");
 }

 @org.junit.Test
 public void testComment_getData_Bogus() {
 Token commentToken = new Token.Comment();
 commentToken.setBogus(true);
 assertTrue(commentToken.isBogus());
 }
}