package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 doctype.setForceQuirks(true);
 doctype.getPublicIdentifier().append("public");
 doctype.getSystemIdentifier().append("system");
 Doctype resetDoctype = doctype.reset();
 assertEquals(doctype.getClass().getSimpleName(), resetDoctype.tokenType());
 assertNull(resetDoctype.getName());
 assertNull(resetDoctype.getPubSysKey());
 assertNull(resetDoctype.getPublicIdentifier());
 assertNull(resetDoctype.getSystemIdentifier());
 assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testTagReset() {
 Tag tag = new StartTag();
 tag.name("div");
 tag.attributes().put("class", "test");
 Tag resetTag = tag.reset();
 assertEquals(Tag.class.getSimpleName(), resetTag.tokenType());
 assertNull(resetTag.tagName);
 assertNull(resetTag.normalName);
 assertNull(resetTag.pendingAttributeName);
 assertNull(resetTag.pendingAttributeValue);
 assertNull(resetTag.pendingAttributeValueS);
 assertFalse(resetTag.isSelfClosing());
 assertNull(resetTag.getAttributes());
 }

 @Test
 public void testTagAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("div").attributes().put("class", "test1").put("id", "test2");
 assertEquals(2, startTag.getAttributes().size());
 assertEquals("test1", startTag.getAttributes().get("class"));
 assertEquals("test2", startTag.getAttributes().get("id"));
 }

 @Test
 public void testTagNameChange() {
 StartTag startTag = new StartTag();
 startTag.name("div").name("span");
 assertEquals("span", startTag.normalName());
 }

 @Test
 public void testTagSelfClosing() {
 StartTag startTag = new StartTag().selfClosing(true);
 assertTrue(startTag.isSelfClosing());
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.getData().append("This is a test comment");
 Comment resetComment = comment.reset();
 assertEquals(Comment.class.getSimpleName(), resetComment.tokenType());
 assertNull(resetComment.getData());
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character().data("A");
 Character resetCharacter = character.reset();
 assertEquals(Character.class.getSimpleName(), resetCharacter.tokenType());
 assertNull(resetCharacter.getData());
 }

 @Test
 public void testCDataReset() {
 CData cData = new CData().data("<![CDATA[This is a test CData]]>");
 CData resetCData = cData.reset();
 assertEquals(CData.class.getSimpleName(), resetCData.tokenType());
 assertNull(resetCData.getData());
 }

 @Test
 public void testEOFReset() {
 EOF eof = new EOF();
 EOF resetEof = eof.reset();
 assertEquals(EOF.class.getSimpleName(), resetEof.tokenType());
 }
}