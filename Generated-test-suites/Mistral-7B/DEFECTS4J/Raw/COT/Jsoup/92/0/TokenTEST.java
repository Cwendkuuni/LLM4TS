import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import org.jsoup.parser.Token.EOF;

public class TokenTest {

 @Test
 public void testToken_Doctype_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 Assert.assertEquals(TokenType.Doctype, doctypeToken.type);
 }

 @Test
 public void testDoctype_getName_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 Assert.assertEquals("html", doctypeToken.getName());
 }

 @Test
 public void testDoctype_getPubSysKey_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.pubSysKey("DTD XHTML 1.0 Strict");
 Assert.assertEquals("DTD XHTML 1.0 Strict", doctypeToken.getPubSysKey());
 }

 @Test
 public void testDoctype_getPublicIdentifier_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.publicIdentifier("xhtml-strict.dtd");
 Assert.assertEquals("xhtml-strict.dtd", doctypeToken.getPublicIdentifier());
 }

 @Test
 public void testDoctype_getSystemIdentifier_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.systemIdentifier("xhtml1-strict.dtd");
 Assert.assertEquals("xhtml1-strict.dtd", doctypeToken.getSystemIdentifier());
 }

 @Test
 public void testDoctype_isForceQuirks_BasicFunctionality() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.forceQuirks(true);
 Assert.assertTrue(doctypeToken.isForceQuirks());
 }

 @Test(expected = IllegalStateException.class)
 public void testDoctype_getName_AfterReset() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.reset();
 doctypeToken.getName();
 }

 @Test
 public void testTag_StartTag_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 Assert.assertEquals(TokenType.StartTag, startTagToken.type);
 }

 @Test
 public void testStartTag_name_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 Assert.assertEquals("div", startTagToken.name());
 }

 @Test
 public void testStartTag_normalName_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 Assert.assertEquals("div", startTagToken.normalName());
 }

 @Test
 public void testStartTag_selfClosing_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 startTagToken.selfClosing(true);
 Assert.assertTrue(startTagToken.isSelfClosing());
 }

 @Test
 public void testStartTag_getAttributes_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 Attributes attributes = new Attributes();
 startTagToken.attributes = attributes;
 startTagToken.newAttribute();
 startTagToken.newAttribute();
 startTagToken.newAttribute("class", "test");
 Assert.assertNotNull(startTagToken.getAttributes());
 }

 @Test
 public void testStartTag_toString_BasicFunctionality() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 startTagToken.newAttribute("class", "test");
 Assert.assertEquals("<div class=\"test\"></div>", startTagToken.toString());
 }

 @Test
 public void testTag_EndTag_BasicFunctionality() {
 EndTag endTagToken = new EndTag();
 Assert.assertEquals(TokenType.EndTag, endTagToken.type);
 }

 @Test
 public void testEndTag_name_BasicFunctionality() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("div");
 Assert.assertEquals("div", endTagToken.name());
 }

 @Test
 public void testEndTag_toString_BasicFunctionality() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("div");
 Assert.assertEquals("</div>", endTagToken.toString());
 }

 @Test
 public void testToken_Comment_BasicFunctionality() {
 Comment commentToken = new Comment();
 Assert.assertEquals(TokenType.Comment, commentToken.type);
 }

 @Test
 public void testComment_getData_BasicFunctionality() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment.");
 Assert.assertEquals("This is a comment.", commentToken.getData());
 }

 @Test
 public void testComment_toString_BasicFunctionality() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment.");
 Assert.assertEquals("<!--This is a comment.-->", commentToken.toString());
 }

 @Test
 public void testToken_Character_BasicFunctionality() {
 Character characterToken = new Character();
 Assert.assertEquals(TokenType.Character, characterToken.type);
 }

 @Test
 public void testCharacter_getData_BasicFunctionality() {
 Character characterToken = new Character();
 characterToken.data("A");
 Assert.assertEquals("A", characterToken.getData());
 }

 @Test
 public void testCharacter_toString_BasicFunctionality() {
 Character characterToken = new Character();
 characterToken.data("A");
 Assert.assertEquals("A", characterToken.toString());
 }

 @Test
 public void testToken_CData_BasicFunctionality() {
 CData cdataToken = new CData("<![CDATA[This is a CData]]]>");
 Assert.assertEquals(TokenType.Character, cdataToken.type);
 }

 @Test
 public void testCData_getData_BasicFunctionality() {
 CData cdataToken = new CData("<![CDATA[This is a CData]]>");
 Assert.assertEquals("This is a CData", cdataToken.getData());
 }

 @Test
 public void testCData_toString_BasicFunctionality() {
 CData cdataToken = new CData("<![CDATA[This is a CData]]>");
 Assert.assertEquals("<![CDATA[This is a CData]]>", cdataToken.toString());
 }

 @Test
 public void testToken_EOF_BasicFunctionality() {
 EOF eofToken = new EOF();
 Assert.assertEquals(TokenType.EOF, eofToken.type);
 }

 @Test
 public void testEOF_toString_BasicFunctionality() {
 EOF eofToken = new EOF();
 Assert.assertEquals("", eofToken.toString());
 }
}