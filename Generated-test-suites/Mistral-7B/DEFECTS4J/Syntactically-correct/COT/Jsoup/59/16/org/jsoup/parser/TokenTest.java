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
 throw new IllegalArgumentException("Invalid token type.");
 }
 }

 @org.junit.Test
 public void testTokenType() {
 Token token = createToken(TokenType.StartTag);
 assertEquals(TokenType.StartTag, token.tokenType());
 }

 @org.junit.Test
 public void testResetDoctype() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.getName().append("html");
 doctypeToken.newAttribute().put("xmlns:html", "http://www.w3.org/1999/xhtml");
 doctypeToken.newAttribute().put("version", "5.0");
 doctypeToken.isForceQuirks = true;
 doctypeToken.reset();
 assertEquals(TokenType.Doctype, doctypeToken.tokenType());
 assertEquals("", doctypeToken.getName().toString());
 assertNull(doctypeToken.getPubSysKey());
 assertNull(doctypeToken.getPublicIdentifier().toString());
 assertNull(doctypeToken.getSystemIdentifier().toString());
 assertFalse(doctypeToken.isForceQuirks());
 }

 @org.junit.Test
 public void testResetStartTag() {
 Token startTagToken = new Token.StartTag();
 startTagToken.name("div");
 startTagToken.newAttribute().put("id", "container");
 startTagToken.newAttribute().put("class", "myClass");
 startTagToken.selfClosing = true;
 startTagToken.reset();
 assertEquals(TokenType.StartTag, startTagToken.tokenType());
 assertNull(startTagToken.tagName);
 assertNull(startTagToken.attributes);
 assertFalse(startTagToken.isSelfClosing());
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testNewAttributeWithPendingAttributeNameAndValue() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.appendAttributeValue("myClass");
 tagToken.newAttribute();
 }

 @org.junit.Test
 public void testNewAttributeWithPendingAttributeNameAndEmptyValue() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.setEmptyAttributeValue();
 tagToken.newAttribute();
 assertEquals("", tagToken.getAttributes().getClass().getName());
 }

 @org.junit.Test
 public void testNewAttributeWithPendingAttributeNameAndValueS() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.appendAttributeValueS("myClass");
 tagToken.newAttribute();
 assertEquals("class", tagToken.getAttributes().keySet().iterator().next().toString());
 assertEquals("myClass", tagToken.getAttributes().get("class").getValue());
 }

 @org.junit.Test
 public void testFinaliseTag() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.appendAttributeValueS("myClass");
 tagToken.finaliseTag();
 assertNotNull(tagToken.getAttributes());
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testNewAttributeWithPendingAttributeNameAndValueAfterFinalisedTag() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.appendAttributeValueS("myClass");
 tagToken.finaliseTag();
 tagToken.appendAttributeName("data-test");
 tagToken.appendAttributeValue("passed");
 tagToken.newAttribute();
 }

 @org.junit.Test
 public void testEmptyAttributeValue() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("class", "");
 tagToken.finaliseTag();
 assertEquals("class", tagToken.getAttributes().keySet().iterator().next().toString());
 assertEquals("", tagToken.getAttributes().get("class").getValue());
 }

 @org.junit.Test
 public void testFinaliseTagAfterAttributeAdded() {
 Token tagToken = new Token.StartTag();
 tagToken.name("div");
 tagToken.newAttribute().put("id", "container");
 tagToken.appendAttributeName("class");
 tagToken.appendAttributeValueS("myClass");
 tagToken.finaliseTag();
 tagToken.newAttribute().put("data-test", "passed");
 assertEquals("data-test", tagToken.getAttributes().keySet().iterator().next().toString());
 assertEquals("passed", tagToken.getAttributes().get("data-test").getValue());
 }

}