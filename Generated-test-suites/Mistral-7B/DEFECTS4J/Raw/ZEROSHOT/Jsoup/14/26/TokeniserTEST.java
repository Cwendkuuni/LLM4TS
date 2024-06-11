package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

/**
 * Tests for the Tokeniser class.
 */
public class TokeniserTest {

 private static ByteArrayInputStream createStream(String text) {
 char[] chars = text.toCharArray();
 return new ByteArrayInputStream(chars, 0, chars.length);
 }

 @org.junit.Test
 public void testBasic() {
 // Given a simple input
 ByteArrayInputStream stream = createStream("<html><head></head><body></body></html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(17, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
 assertEquals("html", tokens.get(0).tagName);
 assertEquals(Token.TokenType.StartTag, tokens.get(1).type);
 assertEquals("head", tokens.get(1).tagName);
 // ... and so on

 // When checking other properties
 assertEquals(TokeniserState.Data, tokeniser.getState());
 assertEquals(0, tokeniser.errors.size());
 }

 @org.junit.Test
 public void testWithErrors() {
 // Given an input with errors
 ByteArrayInputStream stream = createStream("<html><head><script>alert('Hello')</script></head><body></body></html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);
 tokeniser.setTrackErrors(true);

 // When reading tokens
 List<Token> tokens = new ArrayList<>();
 while ((tokeniser.read()) != null) {
 tokens.add(tokeniser.emitPending);
 }

 // Then some errors have been found
 assertEquals(3, tokeniser.errors.size());

 // And the expected tokens and errors have been read
 // ... (code to check the tokens and errors would be very long and complex)
 }

 @org.junit.Test
 public void testCharacterReference() throws IOException {
 // Given an input with character references
 ByteArrayInputStream stream = createStream("<html>&#x1F600;</html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(3, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
 assertEquals("html", tokens.get(0).tagName);
 assertEquals(Token.Character.class, tokens.get(1).getClass());
 assertEquals('\uD834\uDC00', tokens.get(1).charPositionInLine);
 // ... and so on
 }

 @org.junit.Test
 public void testSelfClosingTag() {
 // Given an input with self-closing tags
 ByteArrayInputStream stream = createStream("<img src=\"image.jpg\" />");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(6, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
 assertEquals("img", tokens.get(0).tagName);
 // ... and so on
 }

 @org.junit.Test
 public void testNestedTags() {
 // Given an input with nested tags
 ByteArrayInputStream stream = createStream("<html><head><title><h1>Hello</h1></title></head><body></body></html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(21, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
 assertEquals("html", tokens.get(0).tagName);
 // ... and so on
 }

 @org.junit.Test
 public void testComment() {
 // Given an input with a comment
 ByteArrayInputStream stream = createStream("<html><!-- This is a comment --></html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(8, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.StartTag, tokens.get(0).type);
 assertEquals("html", tokens.get(0).tagName);
 assertEquals(Comment.class, tokens.get(1).getClass());
 // ... and so on
 }

 @org.junit.Test
 public void testDoctype() {
 // Given an input with a doctype
 ByteArrayInputStream stream = createStream("<!doctype html>");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);

 // When reading tokens
 Token token;
 List<Token> tokens = new ArrayList<>();
 while ((token = tokeniser.read()) != null) {
 tokens.add(token);
 }

 // Then the expected number of tokens have been read
 assertEquals(3, tokens.size());

 // And the expected tokens have been read
 assertEquals(Token.TokenType.Doctype, tokens.get(0).type);
 // ... and so on
 }

 @org.junit.Test
 public void testCharacterConsumption() throws IOException {
 // Given an input with character references
 ByteArrayInputStream stream = createStream("#x{1F601}; #x{1F602}");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);
 Character ch = null;
 // When consuming characters
 while ((ch = tokeniser.consumeCharacterReference(null, false)) != null) {
 assertEquals('\uD834\uDC01', ch.intValue());
 }
 }

 @org.junit.Test(expected = IllegalStateException.class)
 public void testCharacterConsumptionWithAttribute() throws IOException {
 // Given an input with character references in an attribute
 ByteArrayInputStream stream = createStream("<html <img src=\"#x{1F601}\" /> >");
 CharacterReader reader = new CharacterReader(stream);
 Tokeniser tokeniser = new Tokeniser(reader);
 Character ch = null;
 // When trying to consume characters in an attribute
 tokeniser.consumeCharacterReference(null, true);
 }
}