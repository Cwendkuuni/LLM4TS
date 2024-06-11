package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name.append("HTML");
 doctype.publicIdentifier.append("publicIdentifier");
 doctype.systemIdentifier.append("systemIdentifier");
 doctype.forceQuirks = true;

 Doctype resetDoctype = doctype.reset();

 Assert.assertEquals(Doctype.class.getSimpleName(), resetDoctype.tokenType());
 Assert.assertEquals("HTML", resetDoctype.getName());
 Assert.assertEquals("publicIdentifier", resetDoctype.getPublicIdentifier());
 Assert.assertEquals("systemIdentifier", resetDoctype.getSystemIdentifier());
 Assert.assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testResetStringBuilder() {
 StringBuilder sb = new StringBuilder("initialValue");
 Token.reset(sb);
 Assert.assertEquals(0, sb.length());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.attributes = new Attributes();
 startTag.attributes.put(new Attribute("class", "classValue"));

 StartTag resetStartTag = startTag.reset();

 Assert.assertEquals(StartTag.class.getSimpleName(), resetStartTag.tokenType());
 Assert.assertEquals("html", resetStartTag.name());
 Assert.assertNull(resetStartTag.attributes);
 }

 @Test
 public void testStartTagWithAttributes() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 startTag.attributes = new Attributes();
 startTag.attributes.put(new Attribute("class", "classValue"));

 StartTag resetStartTag = startTag.reset();

 Assert.assertEquals(StartTag.class.getSimpleName(), resetStartTag.tokenType());
 Assert.assertEquals("html", resetStartTag.name());
 Assert.assertNull(resetStartTag.attributes);

 StartTag newAttribute = resetStartTag.newAttribute();
 newAttribute.appendAttributeName("id");
 newAttribute.appendAttributeValue("idValue");
 newAttribute.finaliseTag();

 Assert.assertEquals(1, resetStartTag.getAttributes().size());
 Attribute attribute = resetStartTag.getAttributes().get("class");
 Assert.assertEquals("classValue", attribute.getValue());
 Attribute idAttribute = resetStartTag.getAttributes().get("id");
 Assert.assertEquals("idValue", idAttribute.getValue());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag();
 endTag.name("html");

 EndTag resetEndTag = endTag.reset();

 Assert.assertEquals(EndTag.class.getSimpleName(), resetEndTag.tokenType());
 Assert.assertEquals("html", resetEndTag.name());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.data.append("This is a comment");

 Comment resetComment = comment.reset();

 Assert.assertEquals(Comment.class.getSimpleName(), resetComment.tokenType());
 Assert.assertEquals("This is a comment", resetComment.getData());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidStartTag() {
 StartTag startTag = new StartTag();
 startTag.tagName = null;
 startTag.finaliseTag();
 }

 @Test
 public void testCharacter() {
 Character character = new Character();
 character.data("This is a character token");

 Character resetCharacter = character.reset();

 Assert.assertEquals(Character.class.getSimpleName(), resetCharacter.tokenType());
 Assert.assertEquals("This is a character token", resetCharacter.getData());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();

 EOF resetEOF = eof.reset();

 Assert.assertEquals(EOF.class.getSimpleName(), resetEOF.tokenType());
 }

 @Test
 public void testTokenTypes() {
 Token token = new Doctype();
 Token doctype = token.isDoctype() ? token : null;
 Token startTag = token.isStartTag() ? token : null;
 Token endTag = token.isEndTag() ? token : null;
 Token comment = token.isComment() ? token : null;
 Token character = token.isCharacter() ? token : null;
 Token eof = token.isEOF() ? token : null;

 Assert.assertNotNull(doctype);
 Assert.assertNull(startTag);
 Assert.assertNull(endTag);
 Assert.assertNull(comment);
 Assert.assertNull(character);
 Assert.assertNull(eof);
 }

}