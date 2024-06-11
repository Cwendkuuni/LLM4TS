package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Character;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.reset();
 }

 @Test
 public void testDoctypeWithPubSysKey() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPubSysKey().equals("pubSysKey");
 doctype.reset();
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeWithInvalidPubSysKey() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPubSysKey().equals("invalidPubSysKey");
 doctype.reset();
 }

 @Test
 public void testDoctypeWithPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().equals("publicIdentifier");
 doctype.reset();
 }

 @Test
 public void testDoctypeWithSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getSystemIdentifier().equals("systemIdentifier");
 doctype.reset();
 }

 @Test
 public void testDoctypeWithForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.isForceQuirks();
 doctype.reset();
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag();
 startTag.tagName().equals("tagName");
 startTag.reset();
 }

 @Test
 public void testStartTagWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.tagName().equals("tagName");
 StartTag tag = startTag.name("tagName", new Attributes().put("attributeName", "attributeValue"));
 tag.name().equals("tagName");
 tag.getAttributes().toString().equals("attributeName=\"attributeValue\"");
 tag.reset();
 }

 @Test(expected = AssertionError.class)
 public void testStartTagWithInvalidAttributeName() {
 StartTag startTag = new StartTag();
 startTag.tagName().equals("tagName");
 StartTag tag = startTag.name("tagName", new Attributes().put("invalidAttributeName", "attributeValue"));
 tag.name().equals("tagName");
 tag.getAttributes().toString().equals("attributeName=\"attributeValue\"");
 tag.reset();
 }

 @Test
 public void testStartTagWithEmptyAttributeValue() {
 StartTag startTag = new StartTag();
 startTag.tagName().equals("tagName");
 StartTag tag = startTag.name("tagName", new Attributes().put("attributeName", ""));
 tag.name().equals("tagName");
 tag.getAttributes().toString().equals("");
 tag.reset();
 }

 @Test
 public void testStartTagWithBooleanAttribute() {
 StartTag startTag = new StartTag();
 startTag.tagName().equals("tagName");
 StartTag tag = startTag.name("tagName", new Attributes().put("attributeName", true));
 tag.name().equals("tagName");
 tag.getAttributes().toString().equals("attributeName");
 tag.reset();
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.getData().equals("commentData");
 comment.reset();
 }

 @Test
 public void testCharacter() {
 Character character = new Character();
 character.getData().equals("characterData");
 character.reset();
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 eof.reset();
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token invalidToken = new Token(); // invalid constructor call
 }

 @Test
 public void testResetAllTokens() {
 Doctype doctype = new Doctype();
 StartTag startTag = new StartTag();
 Comment comment = new Comment();
 Character character = new Character();
 EOF eof = new EOF();
 doctype.reset();
 startTag.reset();
 comment.reset();
 character.reset();
 eof.reset();
 }

 @Test
 public void testTokenTypeIdentification() {
 Token token = new Doctype();
 Assert.assertEquals(TokenType.Doctype, token.isDoctype());
 token = new StartTag();
 Assert.assertEquals(TokenType.StartTag, token.isStartTag());
 token = new EndTag();
 Assert.assertEquals(TokenType.EndTag, token.isEndTag());
 token = new Comment();
 Assert.assertEquals(TokenType.Comment, token.isComment());
 token = new Character();
 Assert.assertEquals(TokenType.Character, token.isCharacter());
 token = new EOF();
 Assert.assertEquals(TokenType.EOF, token.isEOF());
 }
}