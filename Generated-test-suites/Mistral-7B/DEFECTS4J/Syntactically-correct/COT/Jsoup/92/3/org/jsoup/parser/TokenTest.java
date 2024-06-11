package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testTokenReset() {
 Token token = new Token.Doctype();
 token.reset();
 assertNull(token.getName());
 assertNull(token.getPubSysKey());
 assertNull(token.getPublicIdentifier());
 assertNull(token.getSystemIdentifier());
 assertFalse(token.isForceQuirks());
 }

 @Test
 public void testDoctypeNameReset() {
 Token token = new Token.Doctype();
 token.getName("html");
 token.reset();
 assertEquals("html", token.getName());
 }

 @Test
 public void testDoctypePublicIdentifierReset() {
 Token token = new Token.Doctype();
 token.getPublicIdentifier("public id");
 token.reset();
 assertEquals("public id", token.getPublicIdentifier());
 }

 @Test
 public void testDoctypeSystemIdentifierReset() {
 Token token = new Token.Doctype();
 token.getSystemIdentifier("system id");
 token.reset();
 assertEquals("system id", token.getSystemIdentifier());
 }

 @Test
 public void testDoctypeForceQuirksReset() {
 Token token = new Token.Doctype();
 token.setForceQuirks(true);
 token.reset();
 assertFalse(token.isForceQuirks());
 }

 @Test(expected = NullPointerException.class)
 public void testDoctypeNullName() {
 Token token = new Token.Doctype();
 token.name(null);
 }

 @Test(expected = NullPointerException.class)
 public void testDoctypeNullPublicIdentifier() {
 Token token = new Token.Doctype();
 token.getPublicIdentifier(null);
 }

 @Test(expected = NullPointerException.class)
 public void testDoctypeNullSystemIdentifier() {
 Token token = new Token.Doctype();
 token.getSystemIdentifier(null);
 }

 @Test
 public void testTagReset() {
 Token token = new Token.StartTag();
 token.reset();
 assertNull(token.tagName);
 assertNull(token.normalName);
 assertNull(token.getAttributes());
 assertFalse(token.isSelfClosing());
 }

 @Test
 public void testTagNameReset() {
 Token token = new Token.StartTag();
 token.name("tag");
 token.reset();
 assertEquals("tag", token.name());
 }

 @Test
 public void testTagNormalNameReset() {
 Token token = new Token.StartTag();
 token.name("TAG");
 token.reset();
 assertEquals("tag", token.normalName());
 }

 @Test
 public void testTagAttributesReset() {
 Token token = new Token.StartTag();
 token.newAttribute();
 token.newAttribute();
 token.attributes.put("attribute", "value");
 token.reset();
 assertNull(token.getAttributes());
 }

 @Test
 public void testTagSelfClosingReset() {
 Token token = new Token.StartTag();
 token.isSelfClosing(true);
 token.reset();
 assertFalse(token.isSelfClosing());
 }

 @Test(expected = NullPointerException.class)
 public void testTagNullTagName() {
 Token token = new Token.StartTag();
 token.name(null);
 }

 @Test
 public void testTagEmptyAttributeValue() {
 Token token = new Token.StartTag();
 token.newAttribute();
 token.setEmptyAttributeValue();
 assertTrue(token.hasEmptyAttributeValue);
 assertNull(token.getAttributes().get("attribute"));
 }

 @Test
 public void testTagStringAttributeValue() {
 Token token = new Token.StartTag();
 token.newAttribute();
 token.appendAttributeValue("attribute");
 token.appendAttributeValue("value");
 assertEquals("value", token.getAttributes().get("attribute"));
 }

 @Test
 public void testTagCharAttributeValue() {
 Token token = new Token.StartTag();
 token.newAttribute();
 token.appendAttributeValue('a');
 token.appendAttributeValue('b');
 assertEquals("ab", token.getAttributes().get("attribute"));
 }

 @Test
 public void testTagCharacter() {
 Token token = new Token.Character();
 token.data("test");
 assertEquals("test", token.getData());
 }

 @Test
 public void testTagComment() {
 Token token = new Token.Comment();
 token.data("comment");
 assertEquals("comment", token.getData());
 }

 @Test
 public void testTagCommentBogus() {
 Token token = new Token.Comment();
 token.bogus = true;
 assertTrue(token.isBogus());
 }

 @Test
 public void testTagStartTag() {
 Token token = new Token.StartTag();
 token.name("div");
 token.attributes.put("class", "class1 class2");
 assertTrue(token.isStartTag());
 assertEquals("div", token.name());
 assertEquals("class1 class2", token.getAttributes().toString());
 }

 @Test
 public void testTagEndTag() {
 Token token = new Token.EndTag();
 token.name("div");
 assertTrue(token.isEndTag());
 assertEquals("div", token.name());
 }

 @Test
 public void testTokenTypeDoctype() {
 Token token = new Token.Doctype();
 assertTrue(token.isDoctype());
 }

 @Test
 public void testTokenTypeStartTag() {
 Token token = new Token.StartTag();
 assertTrue(token.isStartTag());
 }

 @Test
 public void testTokenTypeEndTag() {
 Token token = new Token.EndTag();
 assertTrue(token.isEndTag());
 }

 @Test
 public void testTokenTypeComment() {
 Token token = new Token.Comment();
 assertTrue(token.isComment());
 }

 @Test
 public void testTokenTypeCharacter() {
 Token token = new Token.Character();
 assertTrue(token.isCharacter());
 }

 @Test
 public void testTokenTypeEOF() {
 Token token = new Token.EOF();
 assertTrue(token.isEOF());
 }

}