package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Attributes;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

 @org.junit.Test
 public void testDoctypeCreation() {
 Doctype doctype = new Doctype();
 assertEquals("Doctype", doctype.tokenType());
 }

 @org.junit.Test
 public void testDoctypeNameSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 assertEquals("Doctype{name='html', publicIdentifier='', systemIdentifier='', forceQuirks=false}", doctype.toString());
 }

 @org.junit.Test
 public void testDoctypePublicIdentifierSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.publicIdentifier("_public");
 assertEquals("Doctype{name='', publicIdentifier='_public', systemIdentifier='', forceQuirks=false}", doctype.toString());
 }

 @org.junit.Test
 public void testDoctypeSystemIdentifierSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.systemIdentifier("_system");
 assertEquals("Doctype{name='', publicIdentifier='', systemIdentifier='_system', forceQuirks=false}", doctype.toString());
 }

 @org.junit.Test
 public void testDoctypeForceQuirksSetterAndGetter() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks(true);
 assertEquals("Doctype{name='', publicIdentifier='', systemIdentifier='', forceQuirks=true}", doctype.toString());
 }

 @org.junit.Test
 public void testTagCreation() {
 Tag tag = new Tag();
 assertEquals("Tag", tag.tokenType());
 }

 @org.junit.Test
 public void testTagNameSetterAndGetter() {
 Tag tag = new Tag();
 tag.name("tagName");
 assertEquals("Tag{name='tagName', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=null}", tag.toString());
 }

 @org.junit.Test
 public void testTagSelfClosingSetterAndGetter() {
 Tag tag = new Tag();
 tag.selfClosing(true);
 assertEquals("Tag{name='', pendingAttributeName=null, pendingAttributeValue='', selfClosing=true, attributes=null}", tag.toString());
 }

 @org.junit.Test
 public void testTagNewAttributeMethod() {
 Tag tag = new Tag();
 tag.newAttribute();
 assertEquals("Tag{name='', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=Attributes{}}", tag.toString());
 }

 @org.junit.Test
 public void testTagFinaliseTagMethod() {
 Tag tag = new Tag();
 tag.newAttribute();
 tag.finaliseTag();
 assertEquals("Tag{name='', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=Attributes{}}", tag.toString());
 }

 @org.junit.Test
 public void testStartTagCreation() {
 StartTag startTag = new StartTag();
 assertEquals("StartTag", startTag.tokenType());
 }

 @org.junit.Test
 public void testStartTagNameSetterAndGetter() {
 StartTag startTag = new StartTag();
 startTag.name("startTagName");
 assertEquals("StartTag{name='startTagName', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=Attributes{}}", startTag.toString());
 }

 @org.junit.Test
 public void testStartTagAttributesSetterAndGetter() {
 StartTag startTag = new StartTag();
 Attribute attribute = new Attribute("attributeName", "attributeValue");
 Attributes attributes = new Attributes();
 attributes.put(attribute);
 startTag.attributes = attributes;
 assertEquals("StartTag{name='', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=Attributes{attributeName=attributeValue}}", startTag.toString());
 }

 @org.junit.Test
 public void testStartTagToStringMethod() {
 StartTag startTag = new StartTag();
 startTag.name("startTagName");
 Attribute attribute = new Attribute("attributeName", "attributeValue");
 Attributes attributes = new Attributes();
 attributes.put(attribute);
 startTag.attributes = attributes;
 assertEquals("<startTagName attributeName='attributeValue'>", startTag.toString());
 }

 @org.junit.Test
 public void testEndTagCreation() {
 EndTag endTag = new EndTag();
 assertEquals("EndTag", endTag.tokenType());
 }

 @org.junit.Test
 public void testEndTagNameSetterAndGetter() {
 EndTag endTag = new EndTag();
 endTag.name("endTagName");
 assertEquals("EndTag{name='endTagName', pendingAttributeName=null, pendingAttributeValue='', selfClosing=false, attributes=null}", endTag.toString());
 }

 @org.junit.Test
 public void testEndTagToStringMethod() {
 EndTag endTag = new EndTag();
 endTag.name("endTagName");
 assertEquals("</endTagName>", endTag.toString());
 }

 @org.junit.Test
 public void testCommentCreation() {
 Comment comment = new Comment();
 assertEquals("Comment", comment.tokenType());
 }

 @org.junit.Test
 public void testCommentDataAppenderAndGetter() {
 Comment comment = new Comment();
 comment.appendAttributeValue("data");
 assertEquals("Comment{data='data',}", comment.toString());
 }

 @org.junit.Test
 public void testCommentToStringMethod() {
 Comment comment = new Comment();
 comment.appendAttributeValue("<!--commentData-->");
 assertEquals("<!--commentData-->", comment.toString());
 }

 @org.junit.Test
 public void testCharacterCreation() {
 Character character = new Character("characterData");
 assertEquals("Character", character.tokenType());
 }

 @org.junit.Test
 public void testCharacterDataAppenderAndGetter() {
 Character character = new Character("characterData");
 character.appendAttributeValue("data");
 assertEquals("Character{data='datadata'}", character.toString());
 }

 @org.junit.Test
 public void testCharacterToStringMethod() {
 Character character = new Character("characterData");
 character.appendAttributeValue("data");
 assertEquals("datadata", character.getData());
 }

 @org.junit.Test
 public void testEOFCreation() {
 EOF eof = new EOF();
 assertEquals("EOF", eof.tokenType());
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testDoctypeForceQuirksSetterWithInvalidArgument() {
 Doctype doctype = new Doctype();
 doctype.forceQuirks(-1);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagSelfClosingSetterWithInvalidArgument() {
 Tag tag = new Tag();
 tag.selfClosing(-1);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagNameSetterWithEmptyString() {
 Tag tag = new Tag();
 tag.name("");
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagNameSetterWithNull() {
 Tag tag = new Tag();
 tag.name(null);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagAttributesSetterWithNull() {
 Tag tag = new Tag();
 tag.attributes = null;
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagSelfClosingSetterWithNull() {
 Tag tag = new Tag();
 tag.selfClosing = null;
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testTagNameSetterWithLengthGreaterThanNameLengthLimit() {
 Tag tag = new Tag();
 tag.name("Name".repeat(129));
 }

 @org.junit.Test
 public void testTokenTypes() {
 TokenType[] expected = TokenType.values();
 TokenType[] actual = Token.TokenType.values();
 assertArrayEquals(expected, actual);
 }

 @org.junit.Test
 public void testTokenTypeEqualsMethod() {
 TokenType expected = TokenType.Doctype;
 TokenType actual = Token.TokenType.Doctype;
 assertEquals(expected, actual);
 }

 @org.junit.Test
 public void testTokenTypeHashCodeMethod() {
 TokenType expected = TokenType.Doctype;
 TokenType actual = Token.TokenType.Doctype;
 assertEquals(expected.hashCode(), actual.hashCode());
 }
}