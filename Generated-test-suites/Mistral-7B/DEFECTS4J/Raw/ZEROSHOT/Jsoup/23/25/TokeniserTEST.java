package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.CharacterInput;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.ParseError;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.TokeniserState;

public class TokeniserTest {

 private CharacterReader reader;
 private ParseErrorList errors;
 private Tokeniser tokeniser;

 @Before
 public void setUp() {
 reader = new CharacterInput(new ByteArrayInputStream("test data".getBytes()));
 errors = new ParseErrorList();
 tokeniser = new Tokeniser(reader, errors);
 }

 @Test
 public void testRead() throws IOException {
 Token token;
 // Test reading normal characters
 for (char c = 'a'; c <= 'z'; c++) {
 token = tokeniser.read();
 Validate.isInstanceOf(Character.class, token);
 assertEquals(String.valueOf(c), token.getData());
 }

 // Test reading whitespace
 token = tokeniser.read();
 assertEquals(Token.Whitespace.class, token.getClass());

 // Test reading end tag
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 Validate.isInstanceOf(EndTag.class, token);
 assertEquals("div", token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading start tag
 tokeniser.transition(TokeniserState.StartTag);
 token = tokeniser.read();
 Validate.isInstanceOf(StartTag.class, token);
 assertEquals("script", token.getData());
 lastStartTag = (StartTag) token;

 // Test reading end tag for appropriate tag
 tokeniser.createCommentPending();
 tokeniser.emitCommentPending();
 tokeniser.transition(TokeniserState.EndTag);
 token = tokeniser.read();
 Validate.isInstanceOf(EndTag.class, token);
 assertEquals("script", token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading comment
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 Validate.isInstanceOf(Comment.class, token);
 assertEquals("<!-- test comment -->", token.getData());

 // Test reading doctype
 tokeniser.createDoctypePending();
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 Validate.isInstanceOf(Doctype.class, token);
 assertEquals("<!doctype html>", token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading self closing tag
 tokeniser.transition(TokeniserState.SelfClosingTag);
 token = tokeniser.read();
 Validate.isInstanceOf(StartTag.class, token);
 assertEquals("img", token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 token = tokeniser.consumeCharacterReference(null, false);
 assertEquals(replacementChar, token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 tokeniser.transition(TokeniserState.CharacterReference);
 token = tokeniser.consumeCharacterReference('x', false);
 assertEquals('x', token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference("ampersand", false);
 assertNull(tokeniser.read());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading named character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference("amp", false);
 assertEquals('&', tokeniser.read().getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading invalid character reference
 tokeniser.transition(TokeniserState.CharacterReference);
 tokeniser.consumeCharacterReference(" invalid", false);
 assertNull(tokeniser.read());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading tag name
 tokeniser.transition(TokeniserState.TagName);
 tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 Validate.isInstanceOf(StartTag.class, token);
 assertEquals("div", token.getData());
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading attribute name
 tokeniser.transition(TokeniserState.AttributeName);
 tokeniser.createTempBuffer();
 tokeniser.consumeCharacter('a', true);
 tokeniser.consumeCharacter('t', true);
 tokeniser.consumeCharacter('t', true);
 tokeniser.consumeCharacter('=', true);
 tokeniser.consumeCharacter('v', true);
 tokeniser.consumeCharacter('a', true);
 tokeniser.consumeCharacter('l', true);
 tokeniser.consumeCharacter('u', true);
 tokeniser.consumeCharacter('e', true);
 tokeniser.consumeCharacter('>', true);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals("attribute", tokeniser.dataBuffer.toString());
 tokeniser.createTempBuffer();
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading attribute value
 tokeniser.transition(TokeniserState.AttributeValue);
 tokeniser.createTempBuffer();
 tokeniser.consumeCharacter('"', true);
 tokeniser.consumeCharacter('t', true);
 tokeniser.consumeCharacter('e', true);
 tokeniser.consumeCharacter('s', true);
 tokeniser.consumeCharacter('t', true);
 tokeniser.consumeCharacter(' ', true);
 tokeniser.consumeCharacter('d', true);
 tokeniser.consumeCharacter('a', true);
 tokeniser.consumeCharacter('t', true);
 tokeniser.consumeCharacter('a', true);
 tokeniser.consumeCharacter(' ', true);
 tokeniser.consumeCharacter('o', true);
 tokeniser.consumeCharacter('k', true);
 tokeniser.consumeCharacter(' ', true);
 tokeniser.consumeCharacter('v', true);
 tokeniser.consumeCharacter('a', true);
 tokeniser.consumeCharacter('l', true);
 tokeniser.consumeCharacter('u', true);
 tokeniser.consumeCharacter('e', true);
 tokeniser.consumeCharacter('"', true);
 tokeniser.acknowledgeSelfClosingFlag();
 assertEquals("test data", tokeniser.dataBuffer.toString());
 tokeniser.createTempBuffer();
 tokeniser.acknowledgeSelfClosingFlag();

 // Test reading end of file
 tokeniser.transition(TokeniserState.EOF);
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 tokeniser.read();
 assertTrue(tokeniser.errors.size() > 0);
 }

 @After
 public void tearDown() {
 errors.clear();
 reader.close();
 }
}