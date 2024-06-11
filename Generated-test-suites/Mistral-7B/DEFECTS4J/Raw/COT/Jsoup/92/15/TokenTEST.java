import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.TokenType.*;
import org.jsoup.parser.Token.*;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

 @Test
 public void testTokenDoctypeReset() {
 Doctype doctype = new Doctype();
 String expectedName = "";
 String expectedPubSysKey = null;
 String expectedPublicIdentifier = "";
 String expectedSystemIdentifier = "";
 boolean expectedForceQuirks = false;
 Doctype resetDoctype = doctype.reset();
 assertEquals(expectedName, resetDoctype.getName());
 Assert.assertNull(resetDoctype.getPubSysKey());
 Assert.assertNull(resetDoctype.getPublicIdentifier());
 Assert.assertNull(resetDoctype.getSystemIdentifier());
 Assert.assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testTokenDoctypeGetters() {
 Doctype doctype = new Doctype();
 doctype.name("testName");
 doctype.pubSysKey("testPubSysKey");
 doctype.publicIdentifier("testPublicIdentifier");
 doctype.systemIdentifier("testSystemIdentifier");
 doctype.forceQuirks(true);
 String name = doctype.getName();
 String pubSysKey = doctype.getPubSysKey();
 String publicIdentifier = doctype.getPublicIdentifier();
 String systemIdentifier = doctype.getSystemIdentifier();
 boolean forceQuirks = doctype.isForceQuirks();
 assertEquals("testName", name);
 Assert.assertEquals("testPubSysKey", pubSysKey);
 Assert.assertEquals("testPublicIdentifier", publicIdentifier);
 Assert.assertEquals("testSystemIdentifier", systemIdentifier);
 Assert.assertTrue(forceQuirks);
 }

 @Test
 public void testTagReset() {
 Tag tag = new StartTag();
 String expectedTagName = null;
 String expectedNormalName = null;
 String expectedPendingAttributeName = null;
 StringBuilder expectedPendingAttributeValue = new StringBuilder();
 boolean expectedHasEmptyAttributeValue = false;
 boolean expectedHasPendingAttributeValue = false;
 boolean expectedSelfClosing = false;
 Attributes expectedAttributes = null;
 Tag resetTag = tag.reset();
 assertEquals(expectedTagName, resetTag.tagName);
 Assert.assertEquals(expectedNormalName, resetTag.normalName);
 Assert.assertNull(resetTag.pendingAttributeName);
 assertEquals("", resetTag.pendingAttributeValue.toString());
 Assert.assertFalse(resetTag.hasEmptyAttributeValue);
 Assert.assertFalse(resetTag.hasPendingAttributeValue);
 Assert.assertFalse(resetTag.selfClosing);
 Assert.assertNull(resetTag.getAttributes());
 }

 @Test
 public void testTagNewAttribute() {
 Tag tag = new StartTag();
 tag.newAttribute();
 String expectedPendingAttributeName = "";
 String expectedPendingAttributeValue = "";
 boolean expectedHasEmptyAttributeValue = false;
 boolean expectedHasPendingAttributeValue = false;
 Attributes expectedAttributes = new Attributes();
 String attributeName = "attributeName";
 String attributeValue = "attributeValue";
 tag.appendAttributeName(attributeName);
 tag.appendAttributeValue("attributeValue");
 tag.newAttribute();
 assertEquals(expectedPendingAttributeName, tag.pendingAttributeName);
 assertEquals(expectedPendingAttributeValue.toString(), tag.pendingAttributeValue.toString());
 Assert.assertFalse(tag.hasEmptyAttributeValue);
 Assert.assertTrue(tag.hasPendingAttributeValue);
 assertEquals(attributeName, tag.pendingAttributeName);
 assertEquals("attributeValue", tag.pendingAttributeValue.toString());
 assertEquals(expectedAttributes, tag.getAttributes());
 }

 @Test
 public void testTagFinaliseTag() {
 Tag tag = new StartTag();
 tag.newAttribute();
 tag.finaliseTag();
 String expectedPendingAttributeName = null;
 StringBuilder expectedPendingAttributeValue = new StringBuilder();
 boolean expectedHasEmptyAttributeValue = false;
 boolean expectedHasPendingAttributeValue = false;
 Attributes expectedAttributes = new Attributes();
 String attributeName = "attributeName";
 String attributeValue = "attributeValue";
 tag.appendAttributeName(attributeName);
 tag.appendAttributeValue("attributeValue");
 tag.newAttribute();
 assertEquals(expectedPendingAttributeName, tag.pendingAttributeName);
 assertEquals("", tag.pendingAttributeValue.toString());
 Assert.assertFalse(tag.hasEmptyAttributeValue);
 Assert.assertFalse(tag.hasPendingAttributeValue);
 assertEquals(expectedAttributes, tag.getAttributes());
 }

 @Test
 public void testTagName() {
 StartTag tag = new StartTag();
 tag.name("testTagName");
 String expectedTagName = "testTagName";
 assertEquals(expectedTagName, tag.name());
 }

 @Test(expected = AssertionError.class)
 public void testTagNameNull() {
 StartTag tag = new StartTag();
 tag.name(null);
 }

 @Test
 public void testTagNormalName() {
 StartTag tag = new StartTag();
 tag.name("TestTagName");
 String expectedNormalName = "testtagname";
 assertEquals(expectedNormalName, tag.normalName());
 }

 @Test
 public void testTagIsSelfClosing() {
 StartTag tag = new StartTag();
 tag.selfClosing(true);
 boolean expectedSelfClosing = true;
 assertEquals(expectedSelfClosing, tag.isSelfClosing());
 }

 @Test
 public void testTagGetAttributes() {
 StartTag tag = new StartTag();
 tag.newAttribute();
 tag.appendAttributeName("attributeName");
 tag.appendAttributeValue("attributeValue");
 tag.newAttribute();
 Attributes attributes = tag.getAttributes();
 String attributeName = "attributeName";
 String attributeValue = "attributeValue";
 assertEquals(attributes.size(), 1);
 assertEquals(attributeName, attributes.get("attributeName"));
 assertEquals(attributeValue, attributes.get("attributeName"));
 }

 @Test
 public void testTagToString() {
 StartTag tag = new StartTag().name("testTagName");
 String expectedString = "<testTagName>";
 assertEquals(expectedString, tag.toString());
 }

 @Test
 public void testEndTagToString() {
 EndTag tag = new EndTag().name("testTagName");
 String expectedString = "</testTagName>";
 assertEquals(expectedString, tag.toString());
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 String expectedData = "";
 boolean expectedBogus = false;
 Comment resetComment = comment.reset();
 assertEquals(expectedData, resetComment.getData());
 Assert.assertFalse(resetComment.isBogus());
 }

 @Test
 public void testCommentgetData() {
 Comment comment = new Comment();
 comment.data("testData");
 String expectedData = "testData";
 assertEquals(expectedData, comment.getData());
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment().data("testData");
 String expectedString = "<!--testData-->";
 assertEquals(expectedString, comment.toString());
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character();
 String expectedData = "";
 Character resetCharacter = character.reset();
 assertEquals(expectedData, resetCharacter.getData());
 }

 @Test
 public void testCharacterData() {
 Character character = new Character();
 character.data("testData");
 String expectedData = "testData";
 assertEquals(expectedData, character.getData());
 }

 @Test
 public void testCharacterToString() {
 Character character = new Character().data("testData");
 String expectedString = "testData";
 assertEquals(expectedString, character.toString());
 }

 @Test
 public void testCDataReset() {
 CData cdata = new CData().data("testData");
 String expectedData = "";
 CData resetCData = cdata.reset();
 assertEquals(expectedData, resetCData.getData());
 }

 @Test
 public void testCDataData() {
 CData cdata = new CData().data("testData");
 String expectedData = "testData";
 assertEquals(expectedData, cdata.getData());
 }

 @Test
 public void testCDataToString() {
 CData cdata = new CData().data("testData");
 String expectedString = "<![CDATA[testData]]>";
 assertEquals(expectedString, cdata.toString());
 }

 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 EOF resetEof = eof.reset();
 }

 @Test
 public void testTokenTypes() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 token = new StartTag();
 assertTrue(token.isStartTag());
 token = new EndTag();
 assertTrue(token.isEndTag());
 token = new Comment();
 assertTrue(token.isComment());
 token = new Character();
 assertTrue(token.isCharacter());
 token = new CData();
 assertTrue(token.isCData());
 token = new EOF();
 assertTrue(token.isEOF());
 }

 @Test
 public void testTokenTypeMismatch() {
 Token token = new StartTag();
 assertFalse(token.isComment());
 token = new Comment();
 assertFalse(token.isStartTag());
 }

 @Test
 public void testTokenCastDoctype() {
 Token token = new Doctype();
 Doctype doctype = (Doctype) token;
 }

 @Test
 public void testTokenCastStartTag() {
 Token token = new StartTag();
 StartTag startTag = (StartTag) token;
 }

 @Test
 public void testTokenCastEndTag() {
 Token token = new EndTag();
 EndTag endTag = (EndTag) token;
 }

 @Test
 public void testTokenCastComment() {
 Token token = new Comment();
 Comment comment = (Comment) token;
 }

 @Test
 public void testTokenCastCharacter() {
 Token token = new Character();
 Character character = (Character) token;
 }

 @Test
 public void testTokenCastCData() {
 Token token = new CData();
 CData cdata = (CData) token;
 }

 @Test(expected = ClassCastException.class)
 public void testTokenCastInvalid() {
 Token token = new EOF();
 EndTag endTag = (EndTag) token; // EOF is not a subclass of EndTag
 }

}