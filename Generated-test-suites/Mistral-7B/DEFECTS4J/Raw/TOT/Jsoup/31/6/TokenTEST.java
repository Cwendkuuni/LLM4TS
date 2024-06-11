import org.junit.Assert;
 import org.junit.Test;
 import org.jsoup.parser.Token;
 import org.jsoup.parser.TokenType;
 import org.jsoup.parser.Doctype;
 import org.jsoup.parser.StartTag;
 import org.jsoup.parser.EndTag;
 import org.jsoup.parser.Comment;
 import org.jsoup.parser.Character;
 import org.jsoup.parser.EOF;

 public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public id");
 doctype.systemIdentifier.append("system id");
 doctype.isForceQuirks(true);

 Token expected = new Doctype();
 expected.name.append("html");
 expected.publicIdentifier.append("public id");
 expected.systemIdentifier.append("system id");
 expected.isForceQuirks(true);

 Assert.assertEquals(expected, doctype);
 }

 @Test
 public void testStartTagWithAttributes() {
 StartTag tag = new StartTag("div").name("div").newAttribute().name("class").newAttribute().value("test");

 Token expected = new StartTag("div").name("div").getAttributes().put(new Attribute("class", "test"));

 Assert.assertEquals(expected, tag);
 }

 @Test
 public void testStartTagWithoutAttributes() {
 StartTag tag = new StartTag("p");

 Token expected = new StartTag("p");

 Assert.assertEquals(expected, tag);
 }

 @Test
 public void testStartTagSelfClosing() {
 StartTag tag = new StartTag("img").isSelfClosing(true);

 Token expected = new StartTag("img").isSelfClosing(true);

 Assert.assertEquals(expected, tag);
 }

 @Test
 public void testEndTag() {
 EndTag tag = new EndTag("body");

 Token expected = new EndTag("body");

 Assert.assertEquals(expected, tag);
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 Token expected = new Comment();
 expected.getData().append("This is a comment");

 Assert.assertEquals(expected, comment);
 }

 @Test
 public void testCharacter() {
 Character character = new Character("&lt;");

 Token expected = new Character("&lt;");

 Assert.assertEquals(expected, character);
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 Token expected = new EOF();

 Assert.assertEquals(expected, eof);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidDoctype() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(true);
 doctype.name.append("html");

 doctype.newAttribute(); // should throw an error as this is not allowed for Doctype objects
 }

 @Test(expected = AssertionError.class)
 public void testStartTagWithInvalidAttributeName() {
 StartTag tag = new StartTag("div");
 tag.newAttribute().name("foo:bar"); // invalid attribute name should throw an error
 }
 }