package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

 static class TestDoctype extends Token.Doctype {
 }

 static class TestStartTag extends Token.StartTag {
 }

 static class TestEndTag extends Token.EndTag {
 }

 static class TestComment extends Token.Comment {
 }

 static class TestCharacter extends Token.Character {
 }

 @Test
 public void testDoctypeReset() {
 TestDoctype token = new TestDoctype();
 token.getName().append("html");
 token.pubSysKey = "pubsys-key";
 token.publicIdentifier.append("public-id");
 token.systemIdentifier.append("sys-id");
 token.forceQuirks = true;
 TestDoctype resetToken = token.reset();
 assertNull(resetToken.getName());
 assertEquals(0, resetToken.pubSysKey.length());
 assertEquals(0, resetToken.publicIdentifier.length());
 assertEquals(0, resetToken.systemIdentifier.length());
 assertFalse(resetToken.isForceQuirks());
 }

 @Test
 public void testStartTagReset() {
 TestStartTag token = new TestStartTag();
 token.tagName = "div";
 token.attributes = new Attributes();
 TestStartTag resetToken = token.reset();
 assertNull(resetToken.tagName);
 assertNull(resetToken.normalName);
 assertNull(resetToken.pendingAttributeName);
 assertEquals(0, resetToken.pendingAttributeValue.length());
 assertEquals(0, resetToken.pendingAttributeValueS.length());
 assertFalse(resetToken.hasEmptyAttributeValue);
 assertFalse(resetToken.hasPendingAttributeValue);
 assertFalse(resetToken.selfClosing);
 assertNull(resetToken.attributes);
 }

 @Test
 public void testEndTagReset() {
 TestEndTag token = new TestEndTag();
 TestEndTag resetToken = token.reset();
 assertNull(resetToken.tagName);
 assertNull(resetToken.normalName);
 assertNull(resetToken.pendingAttributeName);
 assertEquals(0, resetToken.pendingAttributeValue.length());
 assertEquals(0, resetToken.pendingAttributeValueS.length());
 assertFalse(resetToken.hasEmptyAttributeValue);
 assertFalse(resetToken.hasPendingAttributeValue);
 assertFalse(resetToken.selfClosing);
 assertNull(resetToken.attributes);
 }

 @Test
 public void testCommentReset() {
 TestComment token = new TestComment();
 token.data.append("This is a comment");
 TestComment resetToken = token.reset();
 assertEquals("", resetToken.getData());
 }

 @Test
 public void testCharacterReset() {
 TestCharacter token = new TestCharacter();
 token.data("abc");
 TestCharacter resetToken = token.reset();
 assertNull(resetToken.getData());
 }
}