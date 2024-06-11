package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Attribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterReader(new StringReader(""));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testNullReader() {
 Validate.isNull(new Tokeniser(null, errors));
 }

 @Test
 public void testNullErrors() {
 Validate.isNull(new Tokeniser(reader, null));
 }

 @Test(expected = NullPointerException.class)
 public void testNullState() {
 tokeniser.transition(null);
 }

 @Test
 public void testInitialState() {
 assertEquals(TokeniserState.Data, tokeniser.getState());
 }

 @Test
 public void testReadEmpty() {
 Token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacter() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('A');
 tokeniser.advanceTransition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals("A", token.getValue());
 }

 @Test
 public void testReadSingleCharacterWithError() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('B');
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.error(TokeniserState.Data);
 token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacterWithConsumedError() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('B');
 tokeniser.advanceTransition(TokeniserState.Data);
 tokeniser.consumeCharacterReference(null, false);
 token token = tokeniser.read();
 assertNull(token);
 }

 @Test
 public void testReadSingleCharacterWithCharacterReference() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('#');
 tokeniser.transition(TokeniserState.CharacterRef);
 tokeniser.consumeCharacterReference('x', false);
 tokeniser.advanceTransition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals("x", token.getValue());
 }

 @Test
 public void testReadCharacterReferenceWithOutOfRangeCharacter() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('#');
 tokeniser.transition(TokeniserState.CharacterRef);
 tokeniser.consumeCharacterReference('z', false);
 tokeniser.advanceTransition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals(Character.replacementChar, token.getValue());
 }

 @Test
 public void testReadCharacterReferenceWithHexMode() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);tokeniser.transition(TokeniserState.CharacterRef);
 tokeniser.consumeCharacterReference('#x', false);
 tokeniser.consumeHexSequence("FF");
 tokeniser.advanceTransition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals('\u00FF', token.getValue().charAt(0));
 }

 @Test
 public void testReadString() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('A');
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit("ABC");
 tokeniser.transition(TokeniserState.Text);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Character.class, token.getClass());
 assertEquals("ABC", token.getValue());
 }

 @Test
 public void testReadAttributeName() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideStartTag);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("name");
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(StartTag.class, token.getClass());
 assertEquals("name", token.getValue().getAttributeName());
 }

 @Test
 public void testReadAttributeValue() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideStartTag);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("name");
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.consumeCharacterReference('&', true);
 tokeniser.consumeLetterSequence("apos");
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(StartTag.class, token.getClass());
 assertEquals("apos", token.getValue().getAttributeValue("apos"));
 }

 @Test
 public void testReadTagName() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideStartTag);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(StartTag.class, token.getClass());
 assertEquals("div", token.getValue().getTagName());
 }

 @Test
 public void testReadEndTag() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideStartTag);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(EndTag.class, token.getClass());
 assertEquals("div", token.getValue().getTagName());
 }

 @Test
 public void testReadComment() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('*');
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.consumeCharacterReference('--', false);
 tokeniser.consumeAnyCharacter(15);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Comment.class, token.getClass());
 assertEquals("--", token.getValue().getCommentText());
 }

 @Test
 public void testReadDoctype() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.consumeLetterSequence("DOCTYPE");
 tokeniser.consumeAnyCharacter(1);
 tokeniser.consumeLetterSequence("html");
 tokeniser.consumeAnyCharacter(1);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 assertEquals(Doctype.class, token.getClass());
 assertEquals("html", token.getValue().getDoctypeName());
 }

 @Test
 public void testReadStartTag() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emit(' ');
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.consumeLetterSequence("class");
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.consumeLetterSequence("test");
 tokeniser.transition(TokeniserState.AttributeNameEnd);
 tokeniser.consumeCharacterReference('>', false);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 StartTag startTag = (StartTag) token;
 assertEquals("div", startTag.getTagName());
 assertEquals("test", startTag.getAttributeValue("class"));
 }

 @Test
 public void testReadSelfClosingTag() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("img");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.SelfClosingTag);
 tokeniser.consumeCharacterReference('/', false);
 tokeniser.consumeLetterSequence("img");
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 StartTag startTag = (StartTag) token;
 assertEquals("img", startTag.getTagName());
 assertEquals(true, startTag.isSelfClosing());
 }

 @Test
 public void testReadEndTagWithAppropriateStartTag() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 EndTag endTag = (EndTag) token;
 assertEquals("div", endTag.getTagName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadEndTagWithoutAppropriateStartTag() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("p");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.Data);
 tokeniser.read();
 tokeniser.read();
 token token = tokeniser.read();
 }

 @Test
 public void testReadEndTagWithAppropriateStartTagAndAttributes() {
 tokeniser.emit("A");
 tokeniser.transition(TokeniserState.InsideChar);
 tokeniser.emit('<');
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.TagNameEnd);
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.consumeLetterSequence("/");
 tokeniser.consumeLetterSequence("div");
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.consumeLetterSequence("class");
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.consumeLetterSequence("test");
 tokeniser.transition(TokeniserState.AttributeNameEnd);
 tokeniser.transition(TokeniserState.Data);
 token token = tokeniser.read();
 EndTag endTag = (EndTag) token;
 assertEquals("div", endTag.getTagName());
 assertEquals("test", endTag.getAttributeValue("class"));
 }
}