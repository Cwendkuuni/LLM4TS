import org.jsoup.parser.Attribute;
import org.jsoup.parser.Attributes;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Character;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.setName("html");
 assertEquals("html", doctype.getName());
 doctype.setPublicIdentifier("_public");
 assertEquals("_public", doctype.getPublicIdentifier());
 doctype.setSystemIdentifier("_system");
 assertEquals("_system", doctype.getSystemIdentifier());
 doctype.setForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testInvalidDoctype() {
 Doctype doctype = new Doctype();
 try {
 doctype.setName(null);
 } catch (NullPointerException e) {
 // expected
 }

 try {
 doctype.setPublicIdentifier(null);
 } catch (NullPointerException e) {
 // expected
 }

 try {
 doctype.setSystemIdentifier(null);
 } catch (NullPointerException e) {
 // expected
 }

 try {
 doctype.setForceQuirks(-1);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("div");
 startTag.name().equals("div");
 startTag.name("p");
 assertEquals("p", startTag.name());
 startTag.selfClosing = true;
 assertTrue(startTag.isSelfClosing());

 Attributes attributes = new Attributes();
 attributes.put(new Attribute("class", "test"));
 startTag.attributes = attributes;
 assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testInvalidStartTag() {
 StartTag startTag = new StartTag();
 try {
 startTag.name = null;
 } catch (NullPointerException e) {
 // expected
 }

 try {
 startTag.tagName = null;
 } catch (NullPointerException e) {
 // expected
 }

 try {
 startTag.attributes = null;
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("div");
 endTag.name().equals("div");
 }

 @Test
 public void testInvalidEndTag() {
 EndTag endTag = new EndTag();
 try {
 endTag.tagName = null;
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.getData().equals("");
 comment.appendAttributeName("test");
 comment.appendAttributeValue("test");
 assertEquals("test", comment.getData());
 }

 @Test
 public void testCharacter() {
 Character character = new Character("test");
 assertEquals("test", character.getData());
 character.appendAttributeName("test");
 assertNull(character.getData());
 }

 @Test
 public void testInvalidCharacter() {
 Character character = new Character();
 try {
 character.data = null;
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testTokenIdentification() {
 Token token = new Doctype();
 assertTrue(token.isDoctype());
 token = new StartTag("div");
 assertTrue(token.isStartTag());
 token = new EndTag("div");
 assertTrue(token.isEndTag());
 token = new Comment();
 assertTrue(token.isComment());
 token = new Character('a');
 assertTrue(token.isCharacter());
 token = new TokenType.EOF();
 assertTrue(token.isEOF());
 }

 @Test
 public void testTokenConversion() {
 Token token = new StartTag("div");
 assertTrue(token.isStartTag());
 Doctype doctype = (Doctype) token;
 assertFalse(doctype != null);
 }

 @Test(expected = ClassCastException.class)
 public void testInvalidTokenConversion() {
 Token token = new Character('a');
 StartTag startTag = (StartTag) token;
 }
}