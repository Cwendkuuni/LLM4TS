package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TokenTest {

 Token doctypeToken = new Token.Doctype();
 Token startTagToken = new Token.StartTag("html");
 Token endTagToken = new Token.EndTag("html");
 Token commentToken = new Token.Comment();
 Token characterToken = new Token.Character("A");
 Token eofToken = new Token.EOF();

 @Test
 public void testTokenType() {
  assertEquals("Doctype", doctypeToken.tokenType());
  assertEquals("StartTag", startTagToken.tokenType());
  assertEquals("EndTag", endTagToken.tokenType());
  assertEquals("Comment", commentToken.tokenType());
  assertEquals("Character", characterToken.tokenType());
  assertEquals("EOF", eofToken.tokenType());
 }

 @Test
 public void testIsDoctype() {
  assertTrue(doctypeToken.isDoctype());
  assertFalse(startTagToken.isDoctype());
  assertFalse(endTagToken.isDoctype());
  assertFalse(commentToken.isDoctype());
  assertFalse(characterToken.isDoctype());
  assertFalse(eofToken.isDoctype());
 }

 @Test
 public void testIsStartTag() {
  assertFalse(doctypeToken.isStartTag());
  assertTrue(startTagToken.isStartTag());
  assertFalse(endTagToken.isStartTag());
  assertFalse(commentToken.isStartTag());
  assertFalse(characterToken.isStartTag());
  assertFalse(eofToken.isStartTag());
 }

 @Test
 public void testIsEndTag() {
  assertFalse(doctypeToken.isEndTag());
  assertFalse(startTagToken.isEndTag());
  assertTrue(endTagToken.isEndTag());
  assertFalse(commentToken.isEndTag());
  assertFalse(characterToken.isEndTag());
  assertFalse(eofToken.isEndTag());
 }

 @Test
 public void testIsComment() {
  assertFalse(doctypeToken.isComment());
  assertFalse(startTagToken.isComment());
  assertFalse(endTagToken.isComment());
  assertTrue(commentToken.isComment());
  assertFalse(characterToken.isComment());
  assertFalse(eofToken.isComment());
 }

 @Test
 public void testIsCharacter() {
  assertFalse(doctypeToken.isCharacter());
  assertFalse(startTagToken.isCharacter());
  assertFalse(endTagToken.isCharacter());
  assertFalse(commentToken.isCharacter());
  assertTrue(characterToken.isCharacter());
  assertFalse(eofToken.isCharacter());
 }

 @Test
 public void testIsEOF() {
  assertFalse(doctypeToken.isEOF());
  assertFalse(startTagToken.isEOF());
  assertFalse(endTagToken.isEOF());
  assertFalse(commentToken.isEOF());
  assertFalse(characterToken.isEOF());
  assertTrue(eofToken.isEOF());
 }

 @Test
 public void testAsDoctype() {
  Doctype doctype = (Doctype) doctypeToken;
  assertNull(doctype.getName());
  assertNull(doctype.getPublicIdentifier());
  assertNull(doctype.getSystemIdentifier());
  assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testAsStartTag() {
  StartTag startTag = (StartTag) startTagToken;
  assertEquals("html", startTag.name());
  assertFalse(startTag.isSelfClosing());
  assertNull(startTag.getAttributes());
 }

 @Test
 public void testAsEndTag() {
  EndTag endTag = (EndTag) endTagToken;
  assertEquals("html", endTag.name());
  assertFalse(endTag.isSelfClosing());
  assertNull(endTag.getAttributes());
 }

 @Test
 public void testAsComment() {
  Comment comment = (Comment) commentToken;
  assertTrue(comment.isComment());
  assertNull(comment.getData());
 }

 @Test
 public void testAsCharacter() {
  Character character = (Character) characterToken;
  assertEquals("A", character.getData());
 }

 @Test
 public void testNewAttribute() {
  StartTag startTag = new StartTag("div");
  startTag.newAttribute();
  startTag.appendAttributeName("class");
  startTag.appendAttributeValue("red");
  Attributes attributes = startTag.getAttributes();
  assertEquals("class", attributes.keySet().iterator().next());
  assertEquals("red", attributes.get("class").getValue());
 }

 @Test
 public void testFinaliseTag() {
  StartTag startTag = new StartTag("div");
  startTag.newAttribute();
  startTag.appendAttributeName("class");
  startTag.appendAttributeValue("red");
  startTag.finaliseTag();
  attributes = startTag.getAttributes();
  assertEquals("class", attributes.keySet().iterator().next());
  assertEquals("red", attributes.get("class").getValue());
 }
}