import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("HTML");
 doctypeToken.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
 doctypeToken.systemIdentifier("/my/document.dtd");
 doctypeToken.forceQuirks(true);

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("HTML", doctypeToken.getName());
 assertEquals("-//W3C//DTD HTML 4.01//EN", doctypeToken.getPublicIdentifier());
 assertEquals("/my/document.dtd", doctypeToken.getSystemIdentifier());
 assertTrue(doctypeToken.isForceQuirks());
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("HTML");
 doctypeToken.publicIdentifier("HTML");
 doctypeToken.systemIdentifier("HTML");
 doctypeToken.forceQuirks(true);
 doctypeToken.reset();

 assertEquals("Doctype", doctypeToken.tokenType());
 assertNull(doctypeToken.getName());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());
 assertFalse(doctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagTagName() {
 StartTag startTag = new StartTag();
 startTag.name("p");
 assertEquals("p", startTag.name());
 }

 @Test
 public void testStartTagNameWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("img").attr("src", "image.jpg").attr("alt", "Alternate text");
 assertEquals("img", startTag.name());
 assertEquals("img", startTag.normalName());
 assertEquals("{\"src\":\"image.jpg\",\"alt\":\"Alternate text\"}", startTag.getAttributes().toString());
 }

 @Test
 public void testStartTagEmptyAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.name("input").attr("type", "").attr("name", "username");
 assertEquals("input", startTag.name());
 assertEquals("input", startTag.normalName());
 assertEquals("{\"type\":\"\",\"name\":\"username\"}", startTag.getAttributes().toString());
 }

 @Test
 public void testStartTagIsSelfClosing() {
 StartTag startTag = new StartTag();
 startTag.name("img").selfClosing(true);
 assertTrue(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.name("p").attr("id", "id1").selfClosing(true);
 startTag.reset();
 assertEquals("", startTag.tagName);
 assertNull(startTag.normalName);
 assertNull(startTag.pendingAttributeName);
 reset(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValueS);
 assertFalse(startTag.hasEmptyAttributeValue);
 assertFalse(startTag.hasPendingAttributeValue);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }

 @Test
 public void testEndTagName() {
 EndTag endTag = new EndTag();
 endTag.name("p");
 assertEquals("p", endTag.name());
 }

 @Test
 public void testCommentData() {
 Comment comment = new Comment();
 comment.data("This is a comment.");
 assertEquals("This is a comment.", comment.getData());
 }

 @Test
 public void testCommentBogus() {
 Comment comment = new Comment();
 comment.bogus(true);
 assertTrue(comment.isBogus());
 }

 @Test
 public void testCharacterData() {
 Character character = new Character();
 character.data("Hello World!");
 assertEquals("Hello World!", character.getData());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 assertEquals(TokenType.EOF, eof.type);
 }

 @Test
 public void testResetToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("HTML");
 doctypeToken.publicIdentifier("HTML");
 doctypeToken.systemIdentifier("HTML");
 doctypeToken.forceQuirks(true);
 doctypeToken.reset();
 assertEquals("Doctype", doctypeToken.tokenType());
 assertNull(doctypeToken.getName());
 assertNull(doctypeToken.getPublicIdentifier());
 assertNull(doctypeToken.getSystemIdentifier());
 assertFalse(doctypeToken.isForceQuirks());

 StartTag startTag = new StartTag();
 startTag.name("p").attr("id", "id1").selfClosing(true);
 startTag.reset();
 assertEquals("", startTag.tagName);
 assertNull(startTag.normalName);
 assertNull(startTag.pendingAttributeName);
 reset(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValueS);
 assertFalse(startTag.hasEmptyAttributeValue);
 assertFalse(startTag.hasPendingAttributeValue);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }
}