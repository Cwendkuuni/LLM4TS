package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.pubSysKey = "DTD HTML401";
 doctypeToken.publicIdentifier.append("public");
 doctypeToken.systemIdentifier.append("system.dtd");
 doctypeToken.forceQuirks = true;

 Token expectedDoctypeToken = new Doctype();
 expectedDoctypeToken.name.append("html");
 expectedDoctypeToken.pubSysKey = "DTD HTML401";
 expectedDoctypeToken.publicIdentifier.append("public");
 expectedDoctypeToken.systemIdentifier.append("system.dtd");
 expectedDoctypeToken.forceQuirks = true;

 Assert.assertEquals("Expected doctype tokens to be equal", expectedDoctypeToken, doctypeToken);
 }

 @Test
 public void testResetDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name.append("html");
 doctypeToken.pubSysKey = "DTD HTML401";
 doctypeToken.publicIdentifier.append("public");
 doctypeToken.systemIdentifier.append("system.dtd");
 doctypeToken.forceQuirks = true;

 doctypeToken.reset();

 Assert.assertNull("Expected name to be null after reset", doctypeToken.name.toString());
 Assert.assertNull("Expected pubSysKey to be null after reset", doctypeToken.pubSysKey);
 Assert.assertNull("Expected publicIdentifier to be null after reset", doctypeToken.publicIdentifier.toString());
 Assert.assertFalse("Expected forceQuirks to be false after reset", doctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.nameAttr("p", new Attributes());

 Token expectedStartTagToken = new StartTag();
 expectedStartTagToken.nameAttr("p", new Attributes());

 Assert.assertEquals("Expected start tag tokens to be equal", expectedStartTagToken, startTagToken);
 }

 @Test
 public void testResetStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.nameAttr("p", new Attributes());

 startTagToken.reset();

 Assert.assertNull("Expected tagName to be null after reset", startTagToken.tagName);
 Assert.assertNull("Expected normalName to be null after reset", startTagToken.normalName);
 Assert.assertNull("Expected attributes to be null after reset", startTagToken.attributes);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.nameAttr("p", new Attributes());

 Token expectedEndTagToken = new EndTag();
 expectedEndTagToken.nameAttr("p", new Attributes());

 Assert.assertEquals("Expected end tag tokens to be equal", expectedEndTagToken, endTagToken);
 }

 @Test
 public void testResetEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.nameAttr("p", new Attributes());

 endTagToken.reset();

 Assert.assertNull("Expected tagName to be null after reset", endTagToken.tagName);
 Assert.assertNull("Expected normalName to be null after reset", endTagToken.normalName);
 Assert.assertNull("Expected attributes to be null after reset", endTagToken.attributes);
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 Token expectedCommentToken = new Comment();
 expectedCommentToken.data.append("This is a comment");

 Assert.assertEquals("Expected comment tokens to be equal", expectedCommentToken, commentToken);
 }

 @Test
 public void testResetCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 commentToken.reset();

 Assert.assertNull("Expected data to be null after reset", commentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 Token expectedCharacterToken = new Character();
 expectedCharacterToken.data("&amp;");

 Assert.assertEquals("Expected character tokens to be equal", expectedCharacterToken, characterToken);
 }

 @Test
 public void testResetCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 characterToken.reset();

 Assert.assertNull("Expected data to be null after reset", characterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 Token expectedEOFToken = new EOF();

 Assert.assertEquals("Expected EOF tokens to be equal", expectedEOFToken, eofToken);
 }

 @Test
 public void testResetEOFToken() {
 EOF eofToken = new EOF();

 eofToken.reset();

 Assert.assertNull("Expected EOF token to be null after reset", eofToken);
 }
}