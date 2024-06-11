import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
doctype.getPublicIdentifier().equals("");
doctype.getSystemIdentifier().equals("");
doctype.isForceQuirks(); //false by default
 }

 @Test
 public void testDoctypeTokenWithPublicIdAndSystemId() {
 Doctype doctype = new Doctype();
 doctype.getName().append("html");
 doctype.getPublicIdentifier().append("PublicID: ");
 doctype.getPublicIdentifier().append("publicid");
 doctype.getSystemIdentifier().append("SystemID: ");
 doctype.getSystemIdentifier().append("systemid");
 doctype.isForceQuirks(); //false by default
 }

 @Test
 public void testDoctypeTokenWithForceQuirksTrue() {
 Doctype doctype = new Doctype();
 doctype.getName().append("html");
 doctype.isForceQuirks(); //false by default
 doctype.isForceQuirks() = true;
 doctype.isForceQuirks(); //true
 }

 @Test(expected = AssertionError.class)
 public void testInvalidDoctypeName() {
 Doctype doctype = new Doctype();
 doctype.getName().append("invalid");
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag("div");
 startTag.name().equals("div");
 startTag.isSelfClosing(); // false by default
 }

 @Test
 public void testStartTagTokenWithSelfClosingTrue() {
 StartTag startTag = new StartTag("img").name("img").isSelfClosing(true);
 startTag.isSelfClosing(); // true
 }

 @Test
 public void testStartTagTokenWithAttributes() {
 StartTag startTag = new StartTag("a").name("a").newAttribute().appendAttributeName("href").appendAttributeValue("http://example.com/");
 startTag.getAttributes().get("href").equals("http://example.com/");
 }

 @Test(expected = AssertionError.class)
 public void testInvalidStartTagName() {
 StartTag startTag = new StartTag();
 startTag.name().append("invalid");
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag("div");
 endTag.name().equals("div");
 }

 @Test(expected = AssertionError.class)
 public void testInvalidEndTagName() {
 EndTag endTag = new EndTag();
 endTag.name().append("invalid");
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.getData().isEmpty();
 comment.getData().append("This is a comment.");
 comment.getData().equals("This is a comment.");
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character("A");
 character.getData().equals("A");
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();
 eof.isEOF(); // true
 }
}