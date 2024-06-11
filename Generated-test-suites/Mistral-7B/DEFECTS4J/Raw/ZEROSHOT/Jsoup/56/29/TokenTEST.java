import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 assertEquals(null, doctype.getName());
 }

 @Test
 public void testDoctypeValues() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.getPublicIdentifier().append("publicId");
 doctype.getSystemIdentifier().append("systemId");
 doctype.isForceQuirks(true);
 assertEquals("html", doctype.getName());
 assertEquals("publicId", doctype.getPublicIdentifier().toString());
 assertEquals("systemId", doctype.getSystemIdentifier().toString());
 assertTrue(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeResetValues() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.getPublicIdentifier().append("publicId");
 doctype.getSystemIdentifier().append("systemId");
 doctype.isForceQuirks(true);
 doctype.reset();
 assertEquals(null, doctype.getName());
 assertEquals(null, doctype.getPublicIdentifier());
 assertEquals(null, doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testTagReset() {
 Tag tag = new StartTag();
 tag.name("tag");
 tag.reset();
 assertEquals(null, tag.name());
 }

 @Test
 public void testTagAttributes() {
 StartTag startTag = new StartTag();
 Attribute attribute = new Attribute("name", "value");
 startTag.attributes().put(attribute);
 assertEquals(1, startTag.getAttributes().size());
 }

 @Test
 public void testTagResetAttributes() {
 StartTag startTag = new StartTag();
 Attribute attribute = new Attribute("name", "value");
 startTag.attributes().put(attribute);
 startTag.reset();
 assertEquals(0, startTag.getAttributes().size());
 }

 @Test
 public void testTagName() {
 StartTag startTag = new StartTag();
 startTag.name("Tag");
 assertEquals("Tag", startTag.name());
 }

 @Test
 public void testTagNormalName() {
 StartTag startTag = new StartTag();
 startTag.name("Tag");
 assertEquals("tag", startTag.normalName());
 }

 @Test
 public void testTagSelfClosing() {
 StartTag startTag = new StartTag();
 startTag.isSelfClosing(true);
 assertTrue(startTag.isSelfClosing());
 }

 @Test
 public void testTagSelfClosingReset() {
 StartTag startTag = new StartTag();
 startTag.isSelfClosing(true);
 startTag.reset();
 assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testTagToString() {
 StartTag startTag = new StartTag().name("tag");
 assertEquals("<tag>", startTag.toString());
 }

 @Test
 public void testEndTagToString() {
 EndTag endTag = new EndTag().name("tag");
 assertEquals("</tag>", endTag.toString());
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.getData().append("comment");
 comment.reset();
 assertEquals(null, comment.getData());
 }

 @Test
 public void testCommentString() {
 Comment comment = new Comment().getData().append("comment");
 assertEquals("<!--comment-->", comment.toString());
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character().data("character");
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testCharacterString() {
 Character character = new Character().data("character");
 assertEquals("character", character.toString());
 }

 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 assertTrue(eof.isEOF());
 }

 @Test
 public void testEOFString() {
 EOF eof = new EOF();
 assertEquals("", eof.toString());
 }

 @Test
 public void testTokenTypes() {
 Token token = new Doctype();
 assertEquals(TokenType.Doctype, token.type);
 token = new StartTag();
 assertEquals(TokenType.StartTag, token.type);
 token = new EndTag();
 assertEquals(TokenType.EndTag, token.type);
 token = new Comment();
 assertEquals(TokenType.Comment, token.type);
 token = new Character();
 assertEquals(TokenType.Character, token.type);
 token = new EOF();
 assertEquals(TokenType.EOF, token.type);
 }
}