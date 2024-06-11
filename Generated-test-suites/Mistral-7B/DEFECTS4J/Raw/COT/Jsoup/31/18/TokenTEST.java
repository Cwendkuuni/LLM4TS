import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testTokenTypeDoctype() {
 Token token = new Token.Doctype();
 assertEquals(TokenType.Doctype, token.type);
 }

 @Test
 public void testDoctypeGetName() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.name.append("html");
 assertEquals("html", doctype.getName());
 }

 @Test
 public void testDoctypeGetPublicIdentifier() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.publicIdentifier.append("_xml");
 assertEquals("_xml", doctype.getPublicIdentifier());
 }

 @Test
 public void testDoctypeGetSystemIdentifier() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.systemIdentifier.append("/index.html");
 assertEquals("/index.html", doctype.getSystemIdentifier());
 }

 @Test
 public void testDoctypeIsForceQuirks() {
 Token.Doctype doctype = new Token.Doctype();
 doctype.forceQuirks = true;
 assertTrue(doctype.isForceQuirks());
 }

 @Test(expected = AssertionError.class)
 public void testNewDoctypeWithoutType() {
 new Token.Doctype(); // should throw an AssertionError as the constructor is private
 }

 @Test
 public void testTagGetName() {
 Token.Tag tag = new Token.StartTag("div");
 assertEquals("div", tag.name());
 }

 @Test
 public void testTagName() {
 Token.StartTag tag = new Token.StartTag();
 tag.name("p");
 assertEquals("p", tag.name());
 }

 @Test
 public void testTagIsSelfClosing() {
 Token.StartTag tag = new Token.StartTag("img");
 assertFalse(tag.isSelfClosing());
 tag.selfClosing = true;
 assertTrue(tag.isSelfClosing());
 }

 @Test
 public void testTagGetAttributes() {
 Token.StartTag tag = new Token.StartTag("input", new Attributes().put("type", "text"));
 assertNotNull(tag.getAttributes());
 }

 @Test
 public void testTagFinaliseTag() {
 Token.StartTag tag = new Token.StartTag("a");
 tag.finaliseTag();
 assertNull(tag.pendingAttributeName);
 assertNull(tag.pendingAttributeValue);
 }

 @Test
 public void testCommentGetData() {
 Token.Comment comment = new Token.Comment();
 comment.data.append("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 @Test
 public void testCharacterGetData() {
 Token.Character character = new Token.Character("A");
 assertEquals("A", character.getData());
 }

 @Test
 public void testTokenIsEOF() {
 Token eofToken = new Token.EOF();
 assertTrue(eofToken.isEOF());
 }

 @Test
 public void testTokenIsStartTag() {
 Token startTagToken = new Token.StartTag("div");
 assertTrue(startTagToken.isStartTag());
 }

 @Test
 public void testTokenIsEndTag() {
 Token endTagToken = new Token.EndTag("p");
 assertTrue(endTagToken.isEndTag());
 }

 @Test
 public void testTokenIsComment() {
 Token commentToken = new Token.Comment();
 assertTrue(commentToken.isComment());
 }

 @Test
 public void testTokenIsCharacter() {
 Token characterToken = new Token.Character("!");
 assertTrue(characterToken.isCharacter());
 }

 @Test
 public void testTokenIsDoctype() {
 Token doctypeToken = new Token.Doctype();
 assertFalse(doctypeToken.isDoctype());
 }
}