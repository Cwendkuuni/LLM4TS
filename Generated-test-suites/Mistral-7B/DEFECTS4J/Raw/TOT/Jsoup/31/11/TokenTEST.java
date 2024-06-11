import org.junit.jupiter.api.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Token doctypeToken = new Token.Doctype();
 assertEquals("Doctype", doctypeToken.tokenType());
 }

 @Test
 public void testDoctypeToken_getName() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().name("html");
 assertEquals("html", doctypeToken.asDoctype().getName());
 }

 @Test
 public void testDoctypeToken_getPublicIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().setPublicIdentifier("_public");
 assertEquals("_public", doctypeToken.asDoctype().getPublicIdentifier());
 }

 @Test
 public void testDoctypeToken_getSystemIdentifier() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().setSystemIdentifier("_system");
 assertEquals("_system", doctypeToken.asDoctype().getSystemIdentifier());
 }

 @Test
 public void testDoctypeToken_isForceQuirks() {
 Token doctypeToken = new Token.Doctype();
 doctypeToken.asDoctype().setForceQuirks(true);
 assertEquals(true, doctypeToken.asDoctype().isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 Token startTagToken = new Token.StartTag("div");
 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals("div", startTagToken.asStartTag().name());
 assertNull(startTagToken.asStartTag().getAttributes());
 }

 @Test
 public void testStartTagToken_withAttributes() {
 Token startTagToken = new Token.StartTag("img").name("src").attribute("src", "image.jpg");
 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals("img", startTagToken.asStartTag().name());
 assertInstanceOf(Attributes.class, startTagToken.asStartTag().getAttributes());
 assertEquals("image.jpg", startTagToken.asStartTag().getAttributes().get("src"));
 }

 @Test
 public void testEndTagToken() {
 Token endTagToken = new Token.EndTag("div");
 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("div", endTagToken.asEndTag().name());
 }

 @Test
 public void testCommentToken() {
 Token commentToken = new Token.Comment();
 assertEquals("Comment", commentToken.tokenType());
 assertEquals("", commentToken.asComment().getData());
 }

 @Test
 public void testCommentToken_withData() {
 Token commentToken = new Token.Comment("This is a comment");
 assertEquals("Comment", commentToken.tokenType());
 assertEquals("This is a comment", commentToken.asComment().getData());
 }

 @Test
 public void testCharacterToken() {
 Token characterToken = new Token.Character("\n");
 assertEquals("Character", characterToken.tokenType());
 assertEquals("\n", characterToken.asCharacter().getData());
 }

 @Test
 public void testEOFToken() {
 Token eofToken = new Token.EOF();
 assertEquals("EOF", eofToken.tokenType());
 }

}