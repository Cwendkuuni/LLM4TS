import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype token = new Doctype();
 token.getName().equals("doctype");
 assertEquals("", token.getPublicIdentifier());
 assertEquals("", token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeTokenWithName() {
 Doctype token = new Doctype().name("html");
 token.getName().equals("doctype-html");
 }

 @Test
 public void testDoctypeTokenWithNameAndPublicIdentifier() {
 Doctype token = new Doctype().name("html").publicIdentifier("_xml");
 token.getName().equals("doctype-html");
 token.getPublicIdentifier().equals("_xml");
 }

 @Test
 public void testDoctypeTokenWithNamePublicIdentifierAndSystemIdentifier() {
 Doctype token = new Doctype().name("html")
 .publicIdentifier("_xml")
 .systemIdentifier("/path/to/myfile.html");
 token.getName().equals("doctype-html");
 token.getPublicIdentifier().equals("_xml");
 token.getSystemIdentifier().equals("/path/to/myfile.html");
 }

 @Test
 public void testDoctypeTokenWithNamePublicIdentifierSystemIdentifierAndForceQuirks() {
 Doctype token = new Doctype().name("html")
 .publicIdentifier("_xml")
 .systemIdentifier("/path/to/myfile.html")
 .forceQuirks(true);
 token.getName().equals("doctype-html");
 token.getPublicIdentifier().equals("_xml");
 token.getSystemIdentifier().equals("/path/to/myfile.html");
 assertTrue(token.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag token = new StartTag().name("p");
 token.getName().equals("p");
 assertTrue(token.isStartTag());
 }

 @Test
 public void testStartTagTokenWithNameAndAttributes() {
 StartTag token = new StartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "username"));
 token.getName().equals("input");
 assertTrue(token.isStartTag());
 assertEquals("text", token.getAttributes().get("type").getValue());
 assertEquals("username", token.getAttributes().get("name").getValue());
 }

 @Test
 public void testEndTagToken() {
 EndTag token = new EndTag().name("p");
 token.getName().equals("p");
 assertTrue(token.isEndTag());
 }

 @Test
 public void testCommentToken() {
 Comment token = new Comment().data("This is a comment.");
 token.getData().equals("This is a comment.");
 assertTrue(token.isComment());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterTokenWithNullCharacter() {
 Character token = new Character(null);
 }

 @Test
 public void testCharacterToken() {
 Character token = new Character("Hello World!");
 token.getData().equals("Hello World!");
 assertTrue(token.isCharacter());
 }

 @Test
 public void testEOFToken() {
 EOF token = new EOF();
 assertTrue(token.isEOF());
 }
}