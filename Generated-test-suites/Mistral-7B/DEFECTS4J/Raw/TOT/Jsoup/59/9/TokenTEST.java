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
 doctypeToken.pubSysKey = "DTD XHTML 1.0";
 doctypeToken.publicIdentifier.append("Public identifier");
 doctypeToken.systemIdentifier.append("/path/to/dtd/file.dtd");
 doctypeToken.forceQuirks = true;

 Doctype resetDoctypeToken = doctypeToken.reset();

 Assert.assertEquals("Doctype", resetDoctypeToken.tokenType());
 Assert.assertEquals("html", resetDoctypeToken.getName());
 Assert.assertNull(resetDoctypeToken.getPubSysKey());
 Assert.assertNull(resetDoctypeToken.getPublicIdentifier());
 Assert.assertNull(resetDoctypeToken.getSystemIdentifier());
 Assert.assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.nameAttr("html", new Attributes().put("class", " body"));

 StartTag resetStartTagToken = startTagToken.reset();

 Assert.assertEquals(TokenType.StartTag, resetStartTagToken.type);
 Assert.assertEquals("html", resetStartTagToken.name());
 Assert.assertEquals("html", resetStartTagToken.normalName());
 Assert.assertNull(resetStartTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("body");

 EndTag resetEndTagToken = endTagToken.reset();

 Assert.assertEquals(TokenType.EndTag, resetEndTagToken.type);
 Assert.assertEquals("body", resetEndTagToken.name());
 Assert.assertEquals("body", resetEndTagToken.normalName());
 Assert.assertNull(resetEndTagToken.getAttributes());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data.append("This is a comment");

 Comment resetCommentToken = commentToken.reset();

 Assert.assertEquals(TokenType.Comment, resetCommentToken.type);
 Assert.assertEquals("This is a comment", resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("&amp;");

 Character resetCharacterToken = characterToken.reset();

 Assert.assertEquals(TokenType.Character, resetCharacterToken.type);
 Assert.assertEquals("&amp;", resetCharacterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 EOF resetEOFToken = eofToken.reset();

 Assert.assertEquals(TokenType.EOF, resetEOFToken.type);
 }
}