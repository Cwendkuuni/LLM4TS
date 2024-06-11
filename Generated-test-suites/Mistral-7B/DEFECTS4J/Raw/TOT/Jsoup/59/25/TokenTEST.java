package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.pubSysKey = "DTD HTML 4.01";
 doctype.publicIdentifier.append("public \"-//W3C//DTD HTML 4.01//EN\"");
 doctype.systemIdentifier.append("/path/to/dtd/html.dtd");
 doctype.forceQuirks = true;

 Token expected = doctype;
 Token actual = doctype.reset();

 assertEquals(TokenType.Doctype, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(doctype.getName(), actual.getName());
 assertEquals(doctype.getPubSysKey(), actual.getPubSysKey());
 assertEquals(doctype.getPublicIdentifier(), actual.getPublicIdentifier());
 assertEquals(doctype.getSystemIdentifier(), actual.getSystemIdentifier());
 assertEquals(doctype.isForceQuirks(), actual.isForceQuirks());
 }

 @Test(expected = AssertionError.class)
 public void testResetNullDoctypeName() {
 Doctype doctype = new Doctype();
 doctype.name = null;
 doctype.reset();
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("html").attributes(new Attributes().put("class", "html"));

 Token expected = startTag;
 Token actual = startTag.reset();

 assertEquals(TokenType.StartTag, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(startTag.getName(), actual.getName());
 assertEquals(startTag.getAttributes(), actual.getAttributes());
 }

 @Test(expected = AssertionError.class)
 public void testResetNullStartTagName() {
 StartTag startTag = new StartTag();
 startTag.name = null;
 startTag.reset();
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 Token expected = endTag;
 Token actual = endTag.reset();

 assertEquals(TokenType.EndTag, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(endTag.getName(), actual.getName());
 }

 @Test(expected = AssertionError.class)
 public void testResetNullEndTagName() {
 EndTag endTag = new EndTag();
 endTag.name = null;
 endTag.reset();
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 Token expected = comment;
 Token actual = comment.reset();

 assertEquals(TokenType.Comment, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(comment.getData(), actual.getData());
 }

 @Test(expected = AssertionError.class)
 public void testResetNullCommentData() {
 Comment comment = new Comment();
 comment.data = null;
 comment.reset();
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&amp;");

 Token expected = character;
 Token actual = character.reset();

 assertEquals(TokenType.Character, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 assertEquals(character.getData(), actual.getData());
 }

 @Test(expected = AssertionError.class)
 public void testResetNullCharacterData() {
 Character character = new Character();
 character.data = null;
 character.reset();
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token expected = eof;
 Token actual = eof.reset();

 assertEquals(TokenType.EOF, actual.type);
 assertEquals(expected.getClass(), actual.getClass());
 }
}