import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 // Test for Doctype token
 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("DOCTYPE");
 }

 // Test for reset method of Doctype token
 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 String name = doctype.getName();
 doctype.reset();
 assertNotEquals(name, doctype.getName());
 }

 // Test for Getter methods of Doctype token
 @Test
 public void testDoctypeGetters() {
 Doctype doctype = new Doctype();
 doctype.getName("testName");
 doctype.setName("testName2");
 doctype.getName().equals("testName2");
 doctype.getPubSysKey("testPubSysKey");
 doctype.setPubSysKey("testPubSysKey2");
 doctype.getPubSysKey().equals("testPubSysKey2");
 doctype.getPublicIdentifier().equals("");
 doctype.setPublicIdentifier("testPublicIdentifier");
 doctype.getPublicIdentifier().equals("testPublicIdentifier");
 doctype.getSystemIdentifier().equals("");
 doctype.setSystemIdentifier("testSystemIdentifier");
 doctype.getSystemIdentifier().equals("testSystemIdentifier");
 doctype.isForceQuirks();
 doctype.setForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 // Test for reset method of Tag token
 @Test
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

 // Test for newAttribute method of Tag token
 @Test
 public void testTagNewAttribute() {
 Tag tag = new StartTag();
 tag.newAttribute();
 assertNotNull(tag.attributes);
 }

 // Test for finaliseTag method of Tag token
 @Test
 public void testTagFinaliseTag() {
 Tag tag = new StartTag();
 tag.newAttribute();
 tag.finaliseTag();
 assertNotNull(tag.attributes);
 }

 // Test for name method of Tag token
 @Test
 public void testTagName() {
 Tag tag = new StartTag();
 tag.name("testName");
 tag.name("testName2");
 tag.name().equals("testName2");
 }

 // Test for isSelfClosing method of Tag token
 @Test
 public void testTagIsSelfClosing() {
 Tag tag = new StartTag();
 tag.isSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 // Test for getAttributes method of Tag token
 @Test
 public void testTagGetAttributes() {
 Tag tag = new StartTag();
 tag.newAttribute();
 tag.newAttribute();
 tag.finaliseTag();
 Assert.assertEquals(2, tag.getAttributes().size());
 }

 // Test for EOF token
 @Test
 public void testEOF() {
 EOF eof = new EOF();
 eof.isEOF();
 assertTrue(eof.isEOF());
 }

 // Test for reset method of EOF token
 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 assertFalse(eof.isEOF());
 }
}