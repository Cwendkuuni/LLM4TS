package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.TreeBuilder;

import java.util.ArrayList;

import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;

 @Test
 public void testInitialiseParse() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }
  };

  String input = "test input";
  String baseUri = "http://test.com";
  treeBuilder.initialiseParse(input, baseUri, null);
  assertNotNull(treeBuilder.doc);
  assertNotNull(treeBuilder.reader);
  assertNotNull(treeBuilder.tokeniser);
  assertNotNull(treeBuilder.stack);
  assertNotNull(treeBuilder.baseUri);
  assertNull(treeBuilder.currentToken);
  assertNull(treeBuilder.errors);
 }

 @Test
 public void testParse() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    Element element = new Element("test");
    treeBuilder.stack.add(element);
    return true;
   }
  };

  String input = "<test></test>";
  String baseUri = "http://test.com";
  Document doc = treeBuilder.parse(input, baseUri);
  assertNotNull(doc);
  assertNotNull(doc.body());
  assertEquals(1, doc.body().childNodeSize());
  Element element = doc.body().child(0);
  assertNotNull(element.tagName());
  assertEquals("test", element.tagName());
 }

 @Test
 public void testParseWithErrors() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    treeBuilder.errors.add(new ParseError("Error message", token.getColumn(), token.getLineNumber()));
    return true;
   }
  };

  String input = "<test></test>";
  String baseUri = "http://test.com";
  ParseErrorList errors = new ParseErrorList();
  Document doc = treeBuilder.parse(input, baseUri, errors);
  assertNotNull(doc);
  assertFalse(errors.isEmpty());
 }

 @Test
 public void testProcessStartTag() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    return processStartTag(token.name(), token.attributes());
   }

   @Override
   protected boolean processStartTag(String name) {
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    return true;
   }

   @Override
   protected Element currentElement() {
    return null;
   }

   @Override
   protected boolean processStartTag(String name, Attributes attrs) {
    Element element = new Element(name);
    element.attributes().putAll(attrs);
    treeBuilder.stack.add(element);
    return true;
   }
  };

  String input = "<test attr1='value1' attr2='value2'></test>";
  String baseUri = "http://test.com";
  Document doc = treeBuilder.parse(input, baseUri);
  assertNotNull(doc);
  assertNotNull(doc.body());
  assertEquals(1, doc.body().childNodeSize());
  Element element = doc.body().child(0);
  assertNotNull(element.tagName());
  assertEquals("test", element.tagName());
  assertEquals("value1", element.attribute("attr1"));
  assertEquals("value2", element.attribute("attr2"));
 }

 @Test
 public void testProcessEndTag() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    Element parent = treeBuilder.currentElement();
    Element element = new Element(token.name());
    parent.appendChild(element);
    treeBuilder.stack.add(element);
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    Element element = treeBuilder.stack.remove(treeBuilder.stack.size() - 1);
    return true;
   }
  };

  String input = "<test><sub></sub></test>";
  String baseUri = "http://test.com";
  Document doc = treeBuilder.parse(input, baseUri);
  assertNotNull(doc);
  assertNotNull(doc.body());
  assertEquals(1, doc.body().childNodeSize());
  Element test = doc.body().child(0);
  assertNotNull(test.tagName());
  assertEquals("test", test.tagName());
  assertEquals(1, test.childNodeSize());
  Element sub = test.child(0);
  assertNotNull(sub.tagName());
  assertEquals("sub", sub.tagName());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessStartTagWithNullName() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    return processStartTag(token.name(), token.attributes());
   }

   @Override
   protected boolean processStartTag(String name) {
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    return true;
   }

   @Override
   protected Element currentElement() {
    return null;
   }

   @Override
   protected boolean processStartTag(String name, Attributes attrs) {
    if (name == null) {
     throw new IllegalStateException("Name cannot be null");
    }
    Element element = new Element(name);
    element.attributes().putAll(attrs);
    treeBuilder.stack.add(element);
    return true;
   }
  };

  String input = "<test></test>";
  String baseUri = "http://test.com";
  treeBuilder.parse(input, baseUri);
  fail("Expected an IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessStartTagWithEmptyName() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    return processStartTag(token.name(), token.attributes());
   }

   @Override
   protected boolean processStartTag(String name) {
    if (name.isEmpty()) {
     throw new IllegalArgumentException("Name cannot be empty");
    }
    Element element = new Element(name);
    element.attributes().putAll(token.attributes());
    treeBuilder.stack.add(element);
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    return true;
   }

   @Override
   protected Element currentElement() {
    return null;
   }
  };

  String input = "<test></test>";
  String baseUri = "http://test.com";
  treeBuilder.parse(input, baseUri);
  fail("Expected an IllegalArgumentException");
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithNullName() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    Element parent = treeBuilder.currentElement();
    Element element = new Element(token.name());
    parent.appendChild(element);
    treeBuilder.stack.add(element);
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    if (name == null) {
     throw new IllegalStateException("Name cannot be null");
    }
    Element element = treeBuilder.stack.remove(treeBuilder.stack.size() - 1);
    return true;
   }
  };

  String input = "<test><sub></sub></test>";
  String baseUri = "http://test.com";
  treeBuilder.parse(input, baseUri);
  fail("Expected an IllegalStateException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessEndTagWithEmptyName() {
  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(Token token) {
    Element parent = treeBuilder.currentElement();
    Element element = new Element(token.name());
    parent.appendChild(element);
    treeBuilder.stack.add(element);
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    if (name.isEmpty()) {
     throw new IllegalArgumentException("Name cannot be empty");
    }
    Element element = treeBuilder.stack.remove(treeBuilder.stack.size() - 1);
    return true;
   }

   @Override
   protected Element currentElement() {
    return null;
   }
  };

  String input = "<test><sub></sub></test>";
  String baseUri = "http://test.com";
  treeBuilder.parse(input, baseUri);
  fail("Expected an IllegalArgumentException");
 }
}