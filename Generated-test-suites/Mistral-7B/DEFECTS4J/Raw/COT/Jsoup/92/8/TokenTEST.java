import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;
import static org.jsoup.parser.Token.TokenType.*;

public class TokenTest {

 @Test
 public void testNewDoctypeToken() {
 Doctype token = new Doctype();
 assertEquals(TokenType.Doctype, token.type);
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype token = new Doctype();
 token.getName().equals("html"); // assume initial value
 token.reset();
 assertEquals(null, token.getName());
 }

 @Test
 public void testNewStartTagToken() {
 StartTag token = new StartTag();
 assertEquals(TokenType.StartTag, token.type);
 }

 @Test
 public void testNewEndTagToken() {
 EndTag token = new EndTag();
 assertEquals(TokenType.EndTag, token.type);
 }

 @Test
 public void testResetTagToken() {
 StartTag token = new StartTag();
 token.name().equals("div"); // assume initial value
 token.reset();
 assertEquals(null, token.name());
 }

 @Test
 public void testNewCommentToken() {
 Comment token = new Comment();
 assertEquals(TokenType.Comment, token.type);
 }

 @Test
 public void testResetCommentToken() {
 Comment token = new Comment();
 token.getData().equals("--comment"); // assume initial value
 token.reset();
 assertEquals(null, token.getData());
 }

 @Test
 public void testNewCharacterToken() {
 Character token = new Character();
 assertEquals(TokenType.Character, token.type);
 }

 @Test
 public void testResetCharacterToken() {
 Character token = new Character();
 token.getData().equals("a"); // assume initial value
 token.reset();
 assertEquals(null, token.getData());
 }

 @Test
 public void testNewCDataToken() {
 CData token = new CData("CDATA[abc]");
 assertEquals(TokenType.Character, token.type);
 assertEquals("CDATA[abc]", token.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidDoctypeName() {
 Doctype token = new Doctype();
 token.name("invalidName");
 }

 @Test
 public void testDoctypeWithAttributes() {
 Doctype token = new Doctype();
 token.name("html");
 token.pubSysKey("PSID");
 token.publicIdentifier("PUBID");
 token.systemIdentifier("SYSTEMID");
 token.forceQuirks(true);
 assertEquals("html", token.getName());
 assertEquals("PSID", token.getPubSysKey());
 assertEquals("PUBID", token.getPublicIdentifier());
 assertEquals("SYSTEMID", token.getSystemIdentifier());
 assertEquals(true, token.isForceQuirks());
 }

 @Test
 public void testResetDoctypeTokenWithAttributes() {
 Doctype token = new Doctype();
 token.name("html");
 token.pubSysKey("PSID");
 token.publicIdentifier("PUBID");
 token.systemIdentifier("SYSTEMID");
 token.forceQuirks(true);
 token.reset();
 assertEquals(null, token.getName());
 assertEquals(null, token.getPubSysKey());
 assertEquals(null, token.getPublicIdentifier());
 assertEquals(null, token.getSystemIdentifier());
 assertEquals(false, token.isForceQuirks());
 }

 @Test
 public void testNewStartTagWithAttributes() {
 StartTag token = new StartTag();
 token.name("div").nameAttr("id", "id1").nameAttr("class", "class1");
 assertEquals("div", token.name());
 assertEquals("id1", token.attributes().get("id"));
 assertEquals("class1", token.attributes().get("class"));
 }

 @Test
 public void testResetStartTagToken() {
 StartTag token = new StartTag();
 token.name("div").nameAttr("id", "id1").nameAttr("class", "class1");
 token.reset();
 assertEquals(null, token.name());
 assertEquals(null, token.getAttributes());
 }

 @Test
 public void testFinaliseTag() {
 StartTag token = new StartTag();
 token.name("div").nameAttr("id", "id1").nameAttr("class", "class1");
 token.finaliseTag();
 assertEquals("div", token.name());
 assertEquals("id1", token.getAttributes().get("id"));
 assertEquals("class1", token.getAttributes().get("class"));
 assertEquals(null, token.getAttributes().get("nonExistentAttr"));
 }

 @Test
 public void testGetAttributesEmpty() {
 StartTag token = new StartTag();
 assertEquals(null, token.getAttributes());
 }

 @Test
 public void testGetAttributesNonEmpty() {
 StartTag token = new StartTag().name("div").nameAttr("id", "id1").nameAttr("class", "class1");
 Attributes attributes = token.getAttributes();
 assertEquals("id1", attributes.get("id"));
 assertEquals("class1", attributes.get("class"));
 }

 @Test
 public void testResetTagTokenWithAttributes() {
 StartTag token = new StartTag().name("div").nameAttr("id", "id1").nameAttr("class", "class1");
 token.reset();
 assertEquals(null, token.name());
 assertEquals(null, token.getAttributes());
 }

 @Test
 public void testNewEndTag() {
 EndTag token = new EndTag();
 assertEquals(TokenType.EndTag, token.type);
 }

 @Test
 public void testResetEndTagToken() {
 EndTag token = new EndTag();
 token.name().equals("/div"); // assume initial value
 token.reset();
 assertEquals(null, token.name());
 }

 @Test
 public void testNewCharacterTokenWithData() {
 Character token = new Character().data("abc");
 assertEquals("abc", token.getData());
 }

 @Test
 public void testResetCharacterTokenWithData() {
 Character token = new Character().data("abc");
 token.reset();
 assertEquals(null, token.getData());
 }

 @Test
 public void testNewCDataTokenWithData() {
 CData token = new CData("<![CDATA[abc]]>");
 assertEquals("<![CDATA[abc]]>", token.toString());
 }

 @Test
 public void testResetCDataTokenWithData() {
 CData token = new CData("<![CDATA[abc]]>");
 token.reset();
 assertEquals(null, token.getData());
 }

 @Test
 public void testNewEOFToken() {
 EOF token = new EOF();
 assertEquals(TokenType.EOF, token.type);
 }

 @Test
 public void testResetEOFToken() {
 EOF token = new EOF();
 token.reset();
 assertEquals(null, token.type);
 }
}