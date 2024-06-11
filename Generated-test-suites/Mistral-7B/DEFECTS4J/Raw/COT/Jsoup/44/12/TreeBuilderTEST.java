package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Token;
import org.jsoup.parser.TreeBuilder;
import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder builder;
 private Document doc;
 private ParseErrorList errors;

 @Test
 public void testInitializeParse() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = "Test Input";
  String baseUri = "http://test.com";
  errors = new ParseErrorList();
  builder.initialiseParse(input, baseUri, errors);
  Validate.notNull(builder.doc, "Document should not be null after initialiseParse");
  Validate.assertEquals(baseUri, builder.baseUri, "Base Uri should be set correctly");
  assertNull(builder.currentToken, "currentToken should be null after initialiseParse");
  assertSame(errors, builder.errors, "errors should be set correctly");
 }

 @Test(expected = NullPointerException.class)
 public void testInitializeParseNullInput() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = null;
  String baseUri = "http://test.com";
  errors = new ParseErrorList();
  builder.initialiseParse(input, baseUri, errors);
 }

 @Test(expected = NullPointerException.class)
 public void testInitializeParseNullBaseUri() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = "Test Input";
  String baseUri = null;
  errors = new ParseErrorList();
  builder.initialiseParse(input, baseUri, errors);
 }

 @Test
 public void testParse() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = "Test Input";
  String baseUri = "http://test.com";
  errors = new ParseErrorList();
  doc = builder.parse(input, baseUri);
  assertNotNull(doc, "Document should not be null after parse");
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = null;
  String baseUri = "http://test.com";
  errors = new ParseErrorList();
  builder.parse(input, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullBaseUri() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = "Test Input";
  String baseUri = null;
  errors = new ParseErrorList();
  builder.parse(input, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullErrors() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  String input = "Test Input";
  String baseUri = "http://test.com";
  ParseErrorList errors = null;
  builder.parse(input, baseUri, errors);
 }

 @Test
 public void testRunParser() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  ArrayList<Token> tokens = new ArrayList<>();
  tokens.add(new Token.StartTag("html"));
  tokens.add(new Token.StartTag("head"));
  tokens.add(new Token.StartTag("title"));
  tokens.add(new Token.Text("Test Title"));
  tokens.add(new Token.EndTag("title"));
  tokens.add(new Token.EndTag("head"));
  tokens.add(new Token.StartTag("body"));
  tokens.add(new Token.EndTag("body"));
  tokens.add(new Token.EndTag("html"));
  for (Token token : tokens) {
   builder.tokeniser.read();
   builder.process(token);
  }
  assertNull(builder.doc, "Document should be null after processing invalid input");
 }

 @Test
 public void testProcessStartTag() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token startTagToken = new Token.StartTag("div");
  startTagToken.nameAttr("id", "testDiv");
  Element element = new Element("div");
  element.setAttribute("id", "testDiv");
  builder.processStartTag(startTagToken.name);
  Element currentElement = builder.currentElement();
  assertEquals(element, currentElement, "Current element should match the processed start tag");
 }

 @Test
 public void testProcessStartTagWithAttributes() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token startTagToken = new Token.StartTag("div");
  startTagToken.nameAttr("id", "testDiv");
  Attributes attributes = new Attributes();
  attributes.put("class", "testClass");
  builder.processStartTag(startTagToken.name, attributes);
  Element currentElement = builder.currentElement();
  Element expectedElement = new Element("div");
  expectedElement.setAttribute("id", "testDiv");
  expectedElement.addClass("testClass");
  assertEquals(expectedElement, currentElement, "Current element should match the processed start tag with attributes");
 }

 @Test
 public void testProcessEndTag() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token endTagToken = new Token.EndTag("div");
  builder.processEndTag(endTagToken.name);
  Element currentElement = builder.currentElement();
  assertEquals(null, currentElement, "Current element should be null after processing an end tag");
 }

 @Test
 public void testProcessInvalidToken() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token invalidToken = new Token("Test", Token.TokenType.INVALID);
  builder.process(invalidToken);
  Element currentElement = builder.currentElement();
  assertNull(currentElement, "Current element should be null after processing an invalid token");
 }

 @Test
 public void testProcessEndTagWithoutOpeningTag() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token endTagToken = new Token.EndTag("div");
  builder.processEndTag(endTagToken.name);
  Element currentElement = builder.currentElement();
  assertNull(currentElement, "Current element should be null after processing an end tag without an opening tag");
 }

 @Test
 public void testProcessStartTagWithInvalidName() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token startTagToken = new Token.StartTag("&div");
  builder.processStartTag(startTagToken.name);
  Element currentElement = builder.currentElement();
  assertNull(currentElement, "Current element should be null after processing a start tag with an invalid name");
 }

 @Test
 public void testProcessStartTagWithEmptyName() {
  builder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return false;
   }
  };
  builder.initialiseParse("Test Input", "http://test.com", new ParseErrorList());
  Token startTagToken = new Token.StartTag("");
  builder.processStartTag(startTagToken.name);
  Element currentElement = builder.currentElement();
  assertNull(currentElement, "Current element should be null after processing a start tag with an empty name");
 }

}