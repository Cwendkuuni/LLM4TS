import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 assertNull(doctype.getName());
 }

 @Test
 public void testDoctypeGetName() {
 Doctype doctype = new Doctype();
 doctype.getName = new StringBuilder("HTML");
 assertEquals("doctype", doctype.tokenType());
 assertEquals("HTML", doctype.getName());
 }

 @Test
 public void testDoctypeForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks = true;
 assertTrue(doctype.isForceQuirks());
 doctype.reset();
 assertFalse(doctype.isForceQuirks());
 }

 // Test for StartTag

 @Test
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.name = "p";
 startTag.reset();
 assertNull(startTag.name);
 }

 @Test
 public void testStartTagName() {
 StartTag startTag = new StartTag();
 startTag.name = "p";
 assertEquals("StartTag", startTag.tokenType());
 assertEquals("p", startTag.name());
 }

 @Test
 public void testStartTagSelfClosing() {
 StartTag startTag = new StartTag();
 startTag.selfClosing = true;
 assertTrue(startTag.isSelfClosing());
 startTag.reset();
 assertFalse(startTag.isSelfClosing());
 }

 @Test
 public void testStartTagGetAttributes() {
 StartTag startTag = new StartTag();
 Attribute attribute1 = new Attribute("class", "class1");
 Attribute attribute2 = new Attribute("id", "id1");
 startTag.attributes.put(attribute1);
 startTag.attributes.put(attribute2);
 assertEquals("class:class1 id:id1", startTag.getAttributes().toString());
 }

 // Test for EndTag

 @Test
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.name = "p";
 endTag.reset();
 assertNull(endTag.name);
 }

 @Test
 public void testEndTagName() {
 EndTag endTag = new EndTag();
 endTag.name = "p";
 assertEquals("EndTag", endTag.tokenType());
 assertEquals("p", endTag.name());
 }

 // Test for Comment

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");
 comment.reset();
 assertNull(comment.getData());
 }

 @Test
 public void testCommentGetData() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 // Test for Character

 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.data = "A";
 character.reset();
 assertNull(character.getData());
 }

 @Test
 public void testCharactergetData() {
 Character character = new Character();
 character.data = "A";
 assertEquals("A", character.getData());
 }

 // Test for EOF

 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 assertNull(eof.getData());
 }

 @Test
 public void testEOFType() {
 EOF eof = new EOF();
 assertEquals(TokenType.EOF, eof.tokenType());
 }
}