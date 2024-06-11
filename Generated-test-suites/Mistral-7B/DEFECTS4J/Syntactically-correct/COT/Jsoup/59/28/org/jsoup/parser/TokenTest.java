import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testTokenType() {
 Token token = new Token.Doctype();
 assertEquals("Doctype", token.tokenType());

 token = new Token.StartTag();
 assertEquals("StartTag", token.tokenType().name());

 token = new Token.EndTag();
 assertEquals("EndTag", token.tokenType().name());

 token = new Token.Comment();
 assertEquals("Comment", token.tokenType().name());

 token = new Token.Character();
 assertEquals("Character", token.tokenType().name());

 token = new Token.EOF();
 assertEquals("EOF", token.tokenType().name());
 }

 @Test
 public void testResetDoctype() {
 Token doctype = new Token.Doctype();
 doctype.newName("html");
 doctype.setPubSysKey("PSK-123");
 doctype.setPublicIdentifier("PUBID-456");
 doctype.setSystemIdentifier("SYSID-789");
 doctype.setForceQuirks(true);

 doctype.reset();

 assertNull(doctype.getName());
 assertNull(doctype.getPubSysKey());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testResetTag() {
 Token tag = new Token.StartTag();
 tag.name("div");
 tag.newAttribute("class", "class1 class2");
 tag.newAttribute("id", "id1");
 tag.setSelfClosing(true);

 tag.reset();

 assertNull(tag.tagName);
 assertNull(tag.normalName);
 assertNull(tag.pendingAttributeName);
 assertNull(tag.pendingAttributeValue);
 assertNull(tag.pendingAttributeValueS);
 assertFalse(tag.hasEmptyAttributeValue);
 assertFalse(tag.hasPendingAttributeValue);
 assertFalse(tag.isSelfClosing());
 assertNull(tag.attributes);
 }

 @Test
 public void testResetComment() {
 Token comment = new Token.Comment();
 comment.data("This is a comment.");

 comment.reset();

 assertNull(comment.getData());
 assertFalse(comment.isBogus());
 }

 @Test(expected = AssertionError.class)
 public void testResetCharacter() {
 Token character = new Token.Character();
 character.data("A");
 character.reset();

 // This assertion should fail as reset method does not exist for Character token
 assertNull(character.getData());
 }

 @Test(expected = AssertionError.class)
 public void testResetEOF() {
 Token eof = new Token.EOF();
 eof.reset();

 // This assertion should fail as reset method does not exist for EOF token
 assertSame(TokenType.EOF, eof.type);
 }
}