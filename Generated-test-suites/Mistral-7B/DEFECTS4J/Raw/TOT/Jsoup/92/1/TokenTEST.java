import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("DOCTYPE");
 doctype.reset();
 assertEquals(null, doctype.getName());
 }

 @Test
 public void testDoctypeTokenWithAttributes() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("DOCTYPE");
 doctype.getPublicIdentifier().equals("public identifier");
 doctype.getSystemIdentifier().equals("system identifier");
 doctype.setForceQuirks(true);
 doctype.reset();
 assertEquals(null, doctype.getName());
 assertEquals(null, doctype.getPublicIdentifier());
 assertEquals(null, doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name().equals("START_TAG");
 startTag.reset();
 assertEquals(null, startTag.name());
 }

 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("tag");
 startTag.attributes().put("attr1", "value1");
 startTag.attributes().put("attr2", "value2");
 startTag.reset();
 assertEquals(null, startTag.name());
 assertNull(startTag.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name().equals("END_TAG");
 endTag.reset();
 assertEquals(null, endTag.name());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().equals("COMMENT");
 comment.reset();
 assertEquals(null, comment.getData());
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("character");
 character.reset();
 assertEquals(null, character.getData());
 }

 @Test
 public void testCDataToken() {
 CData cData = new CData("<![CDATA[CDATA]]>");
 cData.getData().equals("<![CDATA[CDATA]]>");
 cData.reset();
 assertEquals(null, cData.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 assertTrue(eof.isEOF());
 eof.reset();
 assertFalse(eof.isEOF());
 }

 @Test
 public void testTokenTypeIdentification() {
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
}