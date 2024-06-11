import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testTokenTypeDoctype() {
 Token token = new Token.Doctype();
 assertEquals(TokenType.Doctype, token.type);
 }

 @Test
 public void testDoctypeGetters() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public");
 doctype.systemIdentifier.append("/path/to/file.html");
 doctype.forceQuirks = true;

 assertEquals("html", doctype.getName());
 assertEquals("public", doctype.getPublicIdentifier());
 assertEquals("/path/to/file.html", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeGettersInvalidState() {
 Token.Doctype doctype = new Token.Doctype();
 assertEquals("html", doctype.getName()); // This should fail as name is null initially
 }

 @Test
 public void testTokenTypeStartTag() {
 Token token = new Token.StartTag();
 assertEquals(TokenType.StartTag, token.type);
 }

 @Test
 public void testStartTagGettersInitialState() {
 Token.StartTag startTag = new Token.StartTag();
 assertNull(startTag.tagName);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }

 @Test
 public void testStartTagGettersSetValues() {
 Token.StartTag startTag = new Token.StartTag();
 startTag.tagName = "div";
 startTag.selfClosing = true;
 startTag.newAttribute();
 startTag.appendAttributeName("class");
 startTag.appendAttributeValue("red");
 startTag.newAttribute(); // This creates a new Attributes object
 startTag.appendAttributeName("style");
 startTag.appendAttributeValue("background:red;");

 assertEquals("div", startTag.name());
 assertTrue(startTag.isSelfClosing());
 assertEquals("class", startTag.getAttributes().getClass().getSimpleName());
 assertEquals("red", startTag.getAttributes().getClass().getSimpleName() + ".get(\"class\")");
 assertEquals("style", startTag.getAttributes().getClass().getSimpleName() + ".getClassNames()[0]");
 assertEquals("background:red;", startTag.getAttributes().getClass().getSimpleName() + ".get(\"style\")");
 }

 @Test
 public void testTokenTypeEndTag() {
 Token token = new Token.EndTag();
 assertEquals(TokenType.EndTag, token.type);
 }

 @Test
 public void testEndTagGettersInitialState() {
 Token.EndTag endTag = new Token.EndTag();
 assertNull(endTag.tagName);
 }

 @Test
 public void testEndTagGettersSetValues() {
 Token.EndTag endTag = new Token.EndTag();
 endTag.tagName = "div";
 assertEquals("div", endTag.name());
 }

 @Test
 public void testTokenTypeComment() {
 Token token = new Token.Comment();
 assertEquals(TokenType.Comment, token.type);
 }

 @Test
 public void testCommentGettersInitialState() {
 Token.Comment comment = new Token.Comment();
 assertNull(comment.getData());
 }

 @Test
 public void testCommentGettersSetValues() {
 Token.Comment comment = new Token.Comment();
 comment.data.append("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testTokenTypeCharacter() {
 Token token = new Token.Character("");
 assertEquals(TokenType.Character, token.type);
 }

 @Test
 public void testCharacterGettersInitialState() {
 Token.Character character = new Token.Character("");
 assertNull(character.getData());
 }

 @Test
 public void testCharacterGettersSetValues() {
 Token.Character character = new Token.Character("Hello World!");
 assertEquals("Hello World!", character.getData());
 }

 @Test
 public void testTokenTypeEOF() {
 Token token = new Token.EOF();
 assertEquals(TokenType.EOF, token.type);
 }

}