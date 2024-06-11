package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Tag.StartTag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

 // Testing Doctype Methods
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.reset();
 assertNull(doctype.getName());
 assertNull(doctype.getPubSysKey());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 public void testDoctypeSetName() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 assertEquals("html", doctype.getName());
 }

 public void testDoctypeSetForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 // Testing Tag Methods
 public void testTagReset() {
 Tag tag = new Tag();
 tag.reset();
 assertNull(tag.tagName);
 assertNull(tag.normalName);
 assertNull(tag.pendingAttributeName);
 assertNull(tag.pendingAttributeValue);
 assertNull(tag.pendingAttributeValueS);
 assertFalse(tag.hasEmptyAttributeValue);
 assertFalse(tag.hasPendingAttributeValue);
 assertFalse(tag.selfClosing);
 assertNull(tag.attributes);
 }

 public void testTagNewAttribute() {
 Tag tag = new Tag();
 tag.newAttribute();
 assertNotNull(tag.attributes);
 }

 public void testTagFinaliseTag() {
 Tag tag = new Tag();
 tag.newAttribute();
 tag.finaliseTag();
 assertNotNull(tag.attributes);
 }

 public void testTagName() {
 Tag tag = new Tag();
 tag.name("div");
 assertEquals("div", tag.name());
 }

 public void testTagNormalName() {
 Tag tag = new Tag();
 tag.name("DIV");
 assertEquals("div", tag.normalName());
 }

 public void testTagIsSelfClosing() {
 Tag tag = new Tag();
 tag.isSelfClosing(true);
 assertTrue(tag.isSelfClosing());
 }

 // Testing StartTag Methods
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.reset();
 assertNull(startTag.tagName);
 assertNull(startTag.normalName);
 assertNull(startTag.pendingAttributeName);
 assertNull(startTag.pendingAttributeValue);
 assertNull(startTag.pendingAttributeValueS);
 assertFalse(startTag.hasEmptyAttributeValue);
 assertFalse(startTag.hasPendingAttributeValue);
 assertFalse(startTag.selfClosing);
 assertNull(startTag.attributes);
 }

 public void testStartTagToString() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 assertEquals("<div>", startTag.toString());
 }

 public void testStartTagNewAttribute() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 startTag.newAttribute().name("class").value("test");
 assertEquals("<div class=\"test\">", startTag.toString());
 }

 // Testing EndTag Methods
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.reset();
 assertNull(endTag.tagName);
 assertNull(endTag.normalName);
 assertFalse(endTag.selfClosing);
 }

 public void testEndTagToString() {
 EndTag endTag = new EndTag();
 endTag.name("div");
 assertEquals("</div>", endTag.toString());
 }

 // Testing Comment Methods
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.reset();
 assertNull(comment.data);
 assertFalse(comment.bogus);
 }

 public void testCommentgetData() {
 Comment comment = new Comment();
 comment.data("This is a comment");
 assertEquals("This is a comment", comment.getData());
 }

 public void testCommentToString() {
 Comment comment = new Comment();
 comment.data("<!-- This is a comment -->");
 assertEquals("<!--This is a comment-->", comment.toString());
 }

 // Testing CharacterMethods
 public void testCharacterReset() {
 Character character = new Character();
 character.reset();
 assertNull(character.data);
 }

 public void testCharacterData() {
 Character character = new Character();
 character.data("a");
 assertEquals("a", character.getData());
 }

 public void testCharacterToString() {
 Character character = new Character();
 character.data("a");
 assertEquals("a", character.toString());
 }

 // Testing CDataMethods
 public void testCDataReset() {
 CData cData = new CData("abc");
 cData.reset();
 assertNull(cData.data);
 }

 public void testCDataData() {
 CData cData = new CData("abc");
 assertEquals("abc", cData.getData());
 }

 public void testCDataToString() {
 CData cData = new CData("<!--ABC-->");
 assertEquals("<![CDATA[abc]]>", cData.toString());
 }

 // Testing EOFMethods
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 assertNull(eof.data);
 }

 // Edge cases and exception handling test cases
 public void testTagWithEmptyName() {
 Tag tag = new Tag();
 tag.name(" ");
 assertNotNull(tag.name());
 }

 public void testTagWithNullName() {
 Tag tag = new Tag();
 tag.name(null);
 assertNull(tag.name());
 }

 public void testTagWithLongName() {
 Tag tag = new Tag();
 tag.name("tagNameWithVeryVeryVeryVeryVeryVeryVeryVeryLongName");
 assertEquals("tagNameWithVeryVeryVeryVeryVeryVeryVeryVeryLongName", tag.name());
 }

 public void testTagWithEmptyAttributeValue() {
 Tag tag = new Tag();
 tag.newAttribute().name("class").value("");
 assertEquals("<div class=\"\">", tag.toString());
 }

 public void testTagWithNullAttributeValue() {
 Tag tag = new Tag();
 tag.newAttribute().name("class").value(null);
 assertEquals("<div>", tag.toString());
 }

 public void testTagWithMultipleAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 startTag.newAttribute().name("class").value("test");
 startTag.newAttribute().name("id").value("myid");
 assertEquals("<div class=\"test\" id=\"myid\">", startTag.toString());
 }

 public void testTagWithAttributesAndValueInOneString() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 startTag.newAttribute().name("class").value("test id=\"myid\"");
 assertEquals("<div class=\"test id=\"myid\"\">", startTag.toString());
 }

 public void testTagWithAttributesAndValueInBuilder() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 startTag.newAttribute().name("class").value("test");
 startTag.newAttribute().name("id").value("myid");
 assertEquals("<div class=\"test\" id=\"myid\">", startTag.toString());
 }

 public void testTagWithSelfClosingAndNoAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("img").isSelfClosing(true);
 assertEquals("<img />", startTag.toString());
 }

 public void testTagWithSelfClosingAndAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("img").isSelfClosing(true).newAttribute().name("src").value("image.jpg");
 assertEquals("<img src=\"image.jpg\" />", startTag.toString());
 }

 public void testTagWithInvalidXMLName() {
 StartTag startTag = new StartTag();
 startTag.name("&tag");
 assertEquals("<tag>", startTag.toString());
 }

 public void testTagWithInvalidXMLAttributeName() {
 StartTag startTag = new StartTag();
 startTag.name("div").newAttribute().name("&attr").value("value");
 assertEquals("<div attr=\"value\">", startTag.toString());
 }

 public void testTagWithInvalidXMLAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.name("div").newAttribute().name("src").value("&value");
 assertEquals("<div src=\"&value\">", startTag.toString());
 }

 public void testDoctypeWithEmptyName() {
 Doctype doctype = new Doctype();
 doctype.name("");
 assertNotNull(doctype.getName());
 }

 public void testDoctypeWithNullName() {
 Doctype doctype = new Doctype();
 doctype.name(null);
 assertNull(doctype.getName());
 }

 public void testDoctypeWithEmptyPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier("");
 assertNotNull(doctype.getPublicIdentifier());
 }

 public void testDoctypeWithNullPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier(null);
 assertNull(doctype.getPublicIdentifier());
 }

 public void testDoctypeWithEmptySystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getSystemIdentifier("");
 assertNotNull(doctype.getSystemIdentifier());
 }

 public void testDoctypeWithNullSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getSystemIdentifier(null);
 assertNull(doctype.getSystemIdentifier());
 }

 public void testDoctypeWithEmptyForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(true);
 assertTrue(doctype.isForceQuirks());
 }

 public void testCommentWithLongData() {
 Comment comment = new Comment();
 comment.data("This is a comment with very very very very very very very very long data");
 assertEquals("<!--This is a comment with very very very very very very very very long data-->", comment.toString());
 }

 public void testCommentWithBogus() {
 Comment comment = new Comment();
 comment.bogus(true);
 assertTrue(comment.bogus);
 }

 public void testCharacterWithEmptyData() {
 Character character = new Character();
 character.data("");
 assertNull(character.data);
 }

 public void testCharacterWithNullData() {
 Character character = new Character();
 character.data(null);
 assertNull(character.data);
 }

}