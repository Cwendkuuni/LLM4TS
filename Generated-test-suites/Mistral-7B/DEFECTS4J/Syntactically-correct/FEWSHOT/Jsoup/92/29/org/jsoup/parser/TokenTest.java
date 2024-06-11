package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class TokenTest {

 abstract static class BaseTokenTest<T extends Token> {
 @Test
 public void testReset() {
 T token = new T();
 assertNull(token.tagName);
 assertNull(token.normalName);
 assertNull(token.pendingAttributeName);
 assertNull(token.pendingAttributeValue);
 assertNull(token.pendingAttributeValueS);
 assertFalse(token.hasEmptyAttributeValue);
 assertFalse(token.hasPendingAttributeValue);
 assertFalse(token.selfClosing);
 assertNull(token.attributes);
 }

 @Test
 public void testType() {
 T token = new T();
 assertEquals(token.getClass().getSimpleName(), token.tokenType());
 }
}

 @Test
 public static class DoctypeTest extends BaseTokenTest<Token.Doctype> {
 @Test
 public void testGetName() {
 Doctype token = new Doctype();
 token.name("html");
 assertEquals("html", token.getName());
 }

 @Test
 public void testPubSysKey() {
 Doctype token = new Doctype();
 token.pubSysKey = "<?xml";
 assertEquals("<?xml", token.getPubSysKey());
 }

 @Test
 public void testPublicIdentifier() {
 Doctype token = new Doctype();
 token.publicIdentifier.append("public -1");
 assertEquals("public -1", token.getPublicIdentifier());
 }

 @Test
 public void testSystemIdentifier() {
 Doctype token = new Doctype();
 token.systemIdentifier.append("/a/b/c.html");
 assertEquals("/a/b/c.html", token.getSystemIdentifier());
 }

 @Test
 public void testIsForceQuirks() {
 Doctype token = new Doctype();
 token.forceQuirks = true;
 assertTrue(token.isForceQuirks());
 }
}

 @Test
 public static class TagTest extends BaseTokenTest<Token.Tag> {
 @Test
 public void testName() {
 Token.Tag token = new Token.Tag();
 token.name("div");
 assertEquals("div", token.name());
 }

 @Test
 public void testNormalName() {
 Token.Tag token = new Token.Tag();
 token.name("DIV");
 assertEquals("div", token.normalName());
 }

 @Test
 public void testNewAttribute() {
 Token.Tag token = new Token.Tag();
 token.newAttribute();
 assertNull(token.attributes);
 }

 @Test
 public void testFinaliseTag() {
 Token.Tag token = new Token.Tag();
 token.finaliseTag();
 assertEquals(0, token.attributes.size());
 }

 @Test
 public void testToString() {
 Token.Tag token = new Token.Tag();
 token.name("p");
 assertEquals("<p>", token.toString());
 }

 @Test
 public void testIsSelfClosing() {
 Token.Tag token = new Token.Tag();
 token.selfClosing = true;
 assertTrue(token.isSelfClosing());
 }

 @Test
 public void testGetAttributes() {
 Token.Tag token = new Token.Tag();
 Attributes attributes = new Attributes();
 token.attributes = attributes;
 assertNotNull(token.getAttributes());
 }

 @Test
 public void testAppendTagName() {
 Token.Tag token = new Token.Tag();
 token.appendTagName("html");
 assertEquals("html", token.tagName);
 }

 @Test
 public void testAppendTagNameChar() {
 Token.Tag token = new Token.Tag();
 token.appendTagName('h');
 assertEquals("h", token.tagName);
 }

 @Test
 public void testAppendAttributeName() {
 Token.Tag token = new Token.Tag();
 token.appendAttributeName("class");
 assertEquals("class", token.pendingAttributeName);
 }

 @Test
 public void testAppendAttributeNameChar() {
 Token.Tag token = new Token.Tag();
 token.appendAttributeName('c');
 assertEquals("c", token.pendingAttributeName);
 }

 @Test
 public void testAppendAttributeValue() {
 Token.Tag token = new Token.Tag();
 token.appendAttributeValue("class='red'");
 assertEquals("class='red'", token.pendingAttributeValue.toString());
 }

 @Test
 public void testAppendAttributeValueChar() {
 Token.Tag token = new Token.Tag();
 token.appendAttributeValue('=');
 assertEquals("=", token.pendingAttributeValue.charAt(0));
 }

 @Test
 public void testAppendAttributeValueCharArray() {
 Token.Tag token = new Token.Tag();
 char[] chars = {'=', 'c', 'l', 'a', 's', 's', '=', '"', 'r', 'e', 'd', '"'};
 token.appendAttributeValue(chars);
 assertEquals("class='red'", token.pendingAttributeValue.toString());
 }

 @Test
 public void testAppendAttributeValueIntArray() {
 Token.Tag token = new Token.Tag();
 int[] codepoints = {123, 32, 63, 100, 115, 115, 101, 114, 114, 111, 119, 105, 111, 44, 32, 101, 116, 116, 111, 110, 34};
 token.appendAttributeValue(codepoints);
 assertEquals("class='red'", token.pendingAttributeValue.toString());
 }

 @Test
 public void testSetEmptyAttributeValue() {
 Token.Tag token = new Token.Tag();
 token.setEmptyAttributeValue();
 assertTrue(token.hasEmptyAttributeValue);
 }
}

 @Test
 public static class StartTagTest extends TokenTest {
 @Test
 public void testNameAttr() {
 StartTag token = new StartTag();
 token.nameAttr("p", null);
 assertEquals("p", token.tagName);
 assertNull(token.attributes);
 }

 @Test
 public void testNameAttrWithAttributes() {
 StartTag token = new StartTag();
 Attributes attributes = new Attributes();
 token.nameAttr("p", attributes);
 assertEquals("p", token.tagName);
 assertEquals("p", token.normalName());
 assertNotNull(token.attributes);
 assertEquals(0, token.attributes.size());
 }

 @Test
 public void testToStringWithAttributes() {
 StartTag token = new StartTag();
 Attributes attributes = new Attributes();
 token.nameAttr("p", attributes);
 attributes.put("class", "red");
 assertEquals("<p class='red'>", token.toString());
 }
}

 @Test
 public static class EndTagTest extends TokenTest {
 @Test
 public void testToString() {
 EndTag token = new EndTag();
 assertEquals("</p>", token.toString());
 }
}

 @Test
 public static class CommentTest extends BaseTokenTest<Token.Comment> {
 @Test
 public void testGetData() {
 Comment token = new Comment();
 token.data.append("This is a comment");
 assertEquals("This is a comment", token.getData());
 }

 @Test
 public void testBogus() {
 Comment token = new Comment();
 token.bogus = true;
 assertTrue(token.isBogus());
 }
}

 @Test
 public static class CharacterTest extends BaseTokenTest<Token.Character> {
 @Test
 public void testData() {
 Character token = new Character();
 token.data("!");
 assertEquals("!", token.getData());
 }

 @Test
 public void testCData() {
 CData token = new CData("Hello <world>");
 assertEquals("<![CDATA[Hello <world>]]>", token.toString());
 }
}

 @Test
 public static class EOFTest extends BaseTokenTest<Token.EOF> {
 // EOF has no methods to test
}

}