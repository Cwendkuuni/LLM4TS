package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

 private static final String HTML = "<html><head><title>Test</title></head><body><script>function test(){}</script></body></html>";

 @org.junit.Before
 public void setUp() {
 ParseErrorList errors = new ParseErrorList();
 CharacterReader reader = new CharacterReader(new ByteArrayInputStream(HTML.getBytes()));
 this.tokeniser = new Tokeniser(reader, errors);
 }

 @org.junit.Test
 public void testReadAndEmitCharacter() {
 assertEquals("", tokeniser.read().toString());
 tokeniser.emit('A');
 assertEquals("A", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitString() {
 tokeniser.emit("Hello");
 assertEquals("Hello", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitStartTag() {
 tokeniser.transition(TokeniserState.StartTag);
 tokeniser.emit(new StartTag("div"));
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", ((StartTag) tokeniser.read()).tagName);
 }

 @org.junit.Test
 public void testReadAndEmitEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emit(new EndTag("div"));
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("div", ((EndTag) tokeniser.read()).tagName);
 }

 @org.junit.Test
 public void testReadAndEmitComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emit(new Comment("This is a comment"));
 assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 assertEquals("--This is a comment--", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emit(new Doctype("HTML"));
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("HTML", ((Doctype) tokeniser.read()).doctype);
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#', "a");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("a", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReferenceHex() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#', "x", "&#x61;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("a", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReferenceInvalid() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#');
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReferenceInvalidNamed() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#', "Invalid");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReferenceInvalidNamedWithSemicolon() { // named with semicolon
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference("#", "Invalid;");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @org.junit.Test
 public void testReadAndEmitCharacterReferenceInvalidNamedWithAttributes() { // named with semicolon and attributes
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference("#", "Invalid; class=\"class\" id=\"id\"");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 }

 @org.junit.Test
 public void testReadAndEmitSingleCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#', "a");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("a", tokeniser.read().toString());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals(replacementChar, tokeniser.read().toString().charAt(0));
 }

 @org.junit.Test
 public void testReadAndEmitNamedCharacterReference() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference('#', "amp;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("&", tokeniser.read().toString());
 assertEquals("amp;", ((Character) tokeniser.read()).data);
 }

 @org.junit.Test
 public void testReadAndEmitNamedCharacterReferenceWithAttributes() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference("#", "amp; class=\"class\" id=\"id\"");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("&", tokeniser.read().toString());
 assertEquals("amp;", ((Character) tokeniser.read()).data);
 assertEquals(Token.TokenType.Attribute, tokeniser.read().getType());
 assertEquals("class", ((Attribute) tokeniser.read()).name);
 assertEquals("class", ((Attribute) tokeniser.read()).name);
 assertEquals("class=\"class\" id=\"id\"", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitNamedCharacterReferenceWithAttributesAndSemicolon() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.emitCharacterReference("#", "amp; class=\"class\" id=\"id\" ;");
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals("&", tokeniser.read().toString());
 assertEquals("amp;", ((Character) tokeniser.read()).data);
 assertEquals(Token.TokenType.Attribute, tokeniser.read().getType());
 assertEquals("class", ((Attribute) tokeniser.read()).name);
 assertEquals("class", ((Attribute) tokeniser.read()).name);
 assertEquals("class=\"class\" id=\"id\"", tokeniser.read().toString());
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals(';', tokeniser.read().toString().charAt(0));
 }

 @org.junit.Test
 public void testReadAndEmitStartTagWithAttributes() {
 tokeniser.transition(TokeniserState.StartTag);
 List<Attribute> attrs = new ArrayList<>();
 attrs.add(new Attribute("class", "class"));
 attrs.add(new Attribute("id", "id"));
 tokeniser.emitStartTag("div", attrs);
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", ((StartTag) tokeniser.read()).tagName);
 assertEquals(attrs, ((StartTag) tokeniser.read()).attributes);
 }

 @org.junit.Test
 public void testReadAndEmitStartTagWithAttributesAndSelfClosing() {
 tokeniser.transition(TokeniserState.StartTag);
 List<Attribute> attrs = new ArrayList<>();
 attrs.add(new Attribute("class", "class"));
 attrs.add(new Attribute("id", "id"));
 tokeniser.emitStartTag("img", attrs, true);
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("img", ((StartTag) tokeniser.read()).tagName);
 assertEquals(attrs, ((StartTag) tokeniser.read()).attributes);
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("img", ((EndTag) tokeniser.read()).tagName);
 }

 @org.junit.Test
 public void testReadAndEmitStartTagWithAttributesAndNoSlash() {
 tokeniser.transition(TokeniserState.Data);
 List<Attribute> attrs = new ArrayList<>();
 attrs.add(new Attribute("class", "class"));
 attrs.add(new Attribute("id", "id"));
 tokeniser.emitStartTag("div", attrs);
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("div", ((StartTag) tokeniser.read()).tagName);
 assertEquals(attrs, ((StartTag) tokeniser.read()).attributes);
 assertEquals(Token.TokenType.Character, tokeniser.read().getType());
 assertEquals(" ", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitEndTag() {
 tokeniser.transition(TokeniserState.EndTag);
 tokeniser.emitEndTag("div");
 assertEquals(Token.TokenType.EndTag, tokeniser.read().getType());
 assertEquals("div", ((EndTag) tokeniser.read()).tagName);
 }

 @org.junit.Test
 public void testReadAndEmitComment() {
 tokeniser.transition(TokeniserState.Comment);
 tokeniser.emitComment("This is a comment");
 assertEquals(Token.TokenType.Comment, tokeniser.read().getType());
 assertEquals("--This is a comment--", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testReadAndEmitDoctype() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctype("html", null, null);
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("html", ((Doctype) tokeniser.read()).doctype);
 }

 @org.junit.Test
 public void testReadAndEmitDoctypeWithPublicIdentifier() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctype("html", "public", "test");
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("html", ((Doctype) tokeniser.read()).doctype);
 assertEquals("test", ((Doctype) tokeniser.read()).publicIdentifier);
 }

 @org.junit.Test
 public void testReadAndEmitDoctypeWithSystemIdentifier() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctype("html", null, "test.html");
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("html", ((Doctype) tokeniser.read()).doctype);
 assertEquals("test.html", ((Doctype) tokeniser.read()).systemIdentifier);
 }

 @org.junit.Test
 public void testReadAndEmitDoctypeWithPublicAndSystemIdentifier() {
 tokeniser.transition(TokeniserState.Doctype);
 tokeniser.emitDoctype("html", "public", "test", "test.html");
 assertEquals(Token.TokenType.Doctype, tokeniser.read().getType());
 assertEquals("html", ((Doctype) tokeniser.read()).doctype);
 assertEquals("test", ((Doctype) tokeniser.read()).publicIdentifier);
 assertEquals("test.html", ((Doctype) tokeniser.read()).systemIdentifier);
 }

 @org.junit.Test
 public void testReadAndEmitScriptTag() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emitScriptTag();
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", ((StartTag) tokeniser.read()).tagName);
 }

 @org.junit.Test
 public void testReadAndEmitScriptTagWithSource() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emitScriptTag("test.js");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", ((StartTag) tokeniser.read()).tagName);
 assertEquals("test.js", ((StartTag) tokeniser.read()).source);
 }

 @org.junit.Test
 public void testReadAndEmitScriptTagWithType() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emitScriptTag("test.js", "text/javascript");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", ((StartTag) tokeniser.read()).tagName);
 assertEquals("text/javascript", ((StartTag) tokeniser.read()).type);
 }

 @org.junit.Test
 public void testReadAndEmitScriptTagWithTypeAndSource() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emitScriptTag("test.js", "text/javascript", "other.js");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", ((StartTag) tokeniser.read()).tagName);
 assertEquals("text/javascript", ((StartTag) tokeniser.read()).type);
 assertEquals("other.js", ((StartTag) tokeniser.read()).source);
 }

 @org.junit.Test
 public void testReadAndEmitScriptTagWithTypeAndSourceAndCharset() {
 tokeniser.transition(TokeniserState.Script);
 tokeniser.emitScriptTag("test.js", "text/javascript", "other.js", "UTF-8");
 assertEquals(Token.TokenType.StartTag, tokeniser.read().getType());
 assertEquals("script", ((StartTag) tokeniser.read()).tagName);
 assertEquals("text/javascript", ((StartTag) tokeniser.read()).type);
 assertEquals("other.js", ((StartTag) tokeniser.read()).source);
 assertEquals("UTF-8", ((StartTag) tokeniser.read()).charset);
 }

 @org.junit.Test
 public void testErrorHandling() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.error("Unexpected character '%s' in input state [Data]", ">");
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Unexpected character '>' in input state [Data]", tokeniser.read().toString());
 }

 @org.junit.Test
 public void testErrorHandlingEof() {
 tokeniser.transition(TokeniserState.Data);
 tokeniser.eofError();
 assertEquals(Token.TokenType.Error, tokeniser.read().getType());
 assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", tokeniser.read().toString());
 }

 private Tokeniser tokeniser;
}