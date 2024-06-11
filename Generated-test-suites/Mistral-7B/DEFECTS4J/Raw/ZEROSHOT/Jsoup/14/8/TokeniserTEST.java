package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private CharacterReader createCharacterReader(String input) {
 char[] chars = input.toCharArray();
 ByteArrayInputStream stream = new ByteArrayInputStream(chars);
 return new CharacterReader(stream);
 }

 @org.junit.Test
 public void testNullReader() {
 Tokeniser tokeniser = new Tokeniser(null);
 assertNull(tokeniser.read());
 }

 @org.junit.Test
 public void testNewTokeniser() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 assertEquals(Token.TokenType.Eof, tokeniser.read().type);
 }

 @org.junit.Test
 public void testReadCharacter() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("a"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);
 }

 @org.junit.Test
 public void testReadCharacterEof() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("ab"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("a", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test
 public void testReadMultipleCharacters() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("ab"));
 Token token1 = tokeniser.read();
 assertEquals(Token.TokenType.Character, token1.type);
 assertEquals("a", token1.data);
 Token token2 = tokeniser.read();
 assertEquals(Token.TokenType.Character, token2.type);
 assertEquals("b", token2.data);
 }

 @org.junit.Test
 public void testReadSelfClosingTag() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<img src=\"image.png\" />"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertTrue(token.selfClosing);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test
 public void testReadStartTag() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<div id=\"id\"></div>"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Attribute, token.type);
 assertEquals("id", token.attributeName);
 assertEquals("id", token.attributeValue);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test
 public void testReadEndTag() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<div><p></p></div>"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("p", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("p", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 assertEquals("div", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test
 public void testReadComment() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<!-- comment -->"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 assertEquals("comment", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("-->", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testReadNonCharacterTokenBeforeCharacter() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 Token token1 = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token1.type);
 Token token2 = tokeniser.read();
 assertEquals(Token.TokenType.Character, token2.type);
 }

 @org.junit.Test
 public void testReadCharacterReference() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("&#123;"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("\u007b", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(";", token.data);
 }

 @org.junit.Test
 public void testReadCharacterReferenceInvalid() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("&#xabcd;"));
 Token token = tokeniser.read();
 assertNull(token);
 }

 @org.junit.Test
 public void testReadCharacterReferenceNamed() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("&copy;"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("\u00A9", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(";", token.data);
 }

 @org.junit.Test
 public void testReadCharacterReferenceNamedInvalid() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("&abcd;"));
 Token token = tokeniser.read();
 assertNull(token);
 }

 @org.junit.Test
 public void testReadCharacterReferenceNamedWithAttribute() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<div class=\"class\" title=\"title\">&nbsp;</div>"));
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 assertEquals("div", token.tagName);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Attribute, token.type);
 assertEquals("class", token.attributeName);
 assertEquals("class", token.attributeValue);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(" ", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Attribute, token.type);
 assertEquals("title", token.attributeName);
 assertEquals("title", token.attributeValue);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(" ", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.CharacterReference, token.type);
 assertEquals("\u00A0", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(";", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(" ", token.data);
 token = tokeniser.read();
 assertEquals(Token.TokenType.Eof, token.type);
 }

 @org.junit.Test
 public void testTransitionStates() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 tokeniser.transition(TokeniserState.Script);
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.transition(TokeniserState.Style);
 tokeniser.transition(TokeniserState.Script);
 }

 @org.junit.Test
 public void testCreateTagPending() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 Token tagPending = tokeniser.createTagPending(true);
 Validate.isInstanceOf(Token.StartTag.class, tagPending);
 tagPending = tokeniser.createTagPending(false);
 Validate.isInstanceOf(Token.EndTag.class, tagPending);
 }

 @org.junit.Test
 public void testEmitTagPending() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 Token tagPending = tokeniser.createTagPending(true);
 tokeniser.emitTagPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.StartTag, token.type);
 tagPending = tokeniser.createTagPending(false);
 tokeniser.emitTagPending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.EndTag, token.type);
 }

 @org.junit.Test
 public void testCreateCommentPending() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader(""));
 tokeniser.createCommentPending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Comment, token.type);
 tokeniser.emitCommentPending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals("-->", token.data);
 }

 @org.junit.Test
 public void testCreateDoctypePending() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<!DOCTYPE html>"));
 tokeniser.createDoctypePending();
 Token token = tokeniser.read();
 assertEquals(Token.TokenType.Doctype, token.type);
 tokeniser.emitDoctypePending();
 token = tokeniser.read();
 assertEquals(Token.TokenType.Character, token.type);
 assertEquals(">", token.data);
 }

 @org.junit.Test
 public void testIsAppropriateEndTagToken() {
 Tokeniser tokeniser = new Tokeniser(createCharacterReader("<div><p></p></div>"));
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser = new Tokeniser(createCharacterReader("<div><p></p><p></p></div>"));
 assertTrue(tokeniser.isAppropriateEndTagToken());
 tokeniser = new Tokeniser(createCharacterReader("<div><p></p></div><p></p>"));
 assertFalse(tokeniser.isAppropriateEndTagToken());
 }
}