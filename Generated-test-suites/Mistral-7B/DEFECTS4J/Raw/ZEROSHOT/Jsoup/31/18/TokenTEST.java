import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 assertEquals("Doctype", doctypeToken.tokenType());
 }

 @Test
 public void testDoctypeName() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 assertEquals("html", doctypeToken.getName());
 }

 @Test
 public void testDoctypePublicIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.setPublicIdentifier("public id");
 assertEquals("public id", doctypeToken.getPublicIdentifier());
 }

 @Test
 public void testDoctypeSystemIdentifier() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.setSystemIdentifier("system id");
 assertEquals("system id", doctypeToken.getSystemIdentifier());
 }

 @Test
 public void testDoctypeForceQuirks() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.setForceQuirks(true);
 assertTrue(doctypeToken.isForceQuirks());
 }

 @Test(expected = IllegalStateException.class)
 public void testDoctypeNewAttribute() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.newAttribute();
 fail("Doctype object should not have attributes.");
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 assertEquals("StartTag", startTagToken.tokenType());
 }

 @Test
 public void testStartTagName() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("p");
 assertEquals("p", startTagToken.name());
 }

 @Test
 public void testStartTagSelfClosing() {
 StartTag startTagToken = new StartTag();
 startTagToken.isSelfClosing(true);
 assertTrue(startTagToken.isSelfClosing());
 }

 @Test
 public void testStartTagAttributes() {
 StartTag startTagToken = new StartTag();
 Attribute attribute = new Attribute("class", "test");
 startTagToken.newAttribute();
 startTagToken.attributes().put(attribute);
 assertNotNull(startTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 assertEquals("EndTag", endTagToken.tokenType());
 }

 @Test
 public void testEndTagName() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("p");
 assertEquals("p", endTagToken.name());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 assertEquals("Comment", commentToken.tokenType());
 }

 @Test
 public void testCommentData() {
 Comment commentToken = new Comment();
 commentToken.getData().append("This is a comment.");
 assertEquals("This is a comment.", commentToken.getData().toString());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character('A');
 assertEquals("Character", characterToken.tokenType());
 }

 @Test
 public void testCharacterData() {
 Character characterToken = new Character('A');
 assertEquals("A", characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();
 assertEquals("EOF", eofToken.tokenType());
 }

}