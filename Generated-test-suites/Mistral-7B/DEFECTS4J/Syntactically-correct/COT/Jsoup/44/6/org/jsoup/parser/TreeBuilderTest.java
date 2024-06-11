package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;

 @Test
 public void testInitialiseParse() {
  String input = "some input";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }
  };

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
  String input = "some input";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected Document parse(String input, String baseUri, ParseErrorList errors) {
    return new Document(baseUri);
   }

   @Override
   protected void runParser() {
   }
  };

  Document doc = treeBuilder.parse(input, baseUri);

  assertNotNull(doc);
 }

 @Test
 public void testParseWithErrors() {
  String input = "some input";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected Document parse(String input, String baseUri, ParseErrorList errors) {
    return new Document(baseUri);
   }

   @Override
   protected void runParser() {
   }

   @Override
   protected ParseErrorList noTracking() {
    return new ParseErrorList();
   }
  };

  ParseErrorList errors = new ParseErrorList();

  treeBuilder.parse(input, baseUri, errors);

  assertTrue(errors.size() == 0);
 }

 @Test(expected = NullPointerException.class)
 public void testInitialiseParseWithNullBaseUri() {
  String input = "some input";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }
  };

  treeBuilder.initialiseParse(input, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseWithNullInput() {
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected Document parse(String input, String baseUri, ParseErrorList errors) {
    return new Document(baseUri);
   }

   @Override
   protected void runParser() {
   }
  };

  treeBuilder.parse(null, baseUri);
 }

 @Test
 public void testProcessStartTag() {
  String input = "<html><head></head><body></body></html>";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(String name) {
    return name.equals("html");
   }
  };

  treeBuilder.initialiseParse(input, baseUri, null);
  treeBuilder.runParser();

  Element html = treeBuilder.currentElement();
  assertNotNull(html);
 }

 @Test
 public void testProcessStartTagWithAttributes() {
  String input = "<html lang='en'>";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processStartTag(String name, Attributes attrs) {
    return name.equals("html");
    for (String attrName : attrs.keySet()) {
      String attrValue = attrs.value(attrName);
      if (attrName.equals("lang")) {
        return attrValue.equals("en");
      }
    }
    return false;
   }
  };

  treeBuilder.initialiseParse(input, baseUri, null);
  treeBuilder.runParser();

  Element html = treeBuilder.currentElement();
  assertNotNull(html);
  assertTrue(html.attributes().hasKey("lang"));
  assertTrue(html.attributes().get("lang").value().equals("en"));
 }

 @Test
 public void testProcessEndTag() {
  String input = "<html><head></head><body><p></p></body></html>";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    return name.equals("p");
   }
  };

  treeBuilder.initialiseParse(input, baseUri, null);
  treeBuilder.runParser();

  Element p = treeBuilder.currentElement();
  assertNotNull(p);
 }

 @Test
 public void testProcessEndTagWithEmptyName() {
  String input = "<html><head></head><body><p></p></body></html>";
  String baseUri = "baseUri";

  treeBuilder = new TreeBuilder() {
   @Override
   protected boolean process(Token token) {
    return true;
   }

   @Override
   protected boolean processEndTag(String name) {
    return name.isEmpty();
   }
  };

  treeBuilder.initialiseParse(input, baseUri, null);
  treeBuilder.runParser();

  Element p = treeBuilder.currentElement();
  assertNotNull(p);
 }
}