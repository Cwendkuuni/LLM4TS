package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;

import static org.junit.Assert.*;

public class ParserTest {

 @Test
 public void testParseHtmlWithBaseUri() {
 String html = "<html><head><title>Test Page</title></head><body><h1>Welcome to JSoup!</h1></body></html>";
 Document doc = Parser.parse(html, "https://example.com/test.html");

 Element htmlNode = doc.head();
 Assert.assertEquals("html", htmlNode.tagName());

 Element titleNode = doc.head().title();
 Assert.assertEquals("Test Page", titleNode.text());

 Element h1Node = doc.body().get(0);
 Assert.assertEquals("h1", h1Node.tagName());
 Assert.assertEquals("Welcome to JSoup!", h1Node.text());
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<body><p>This is a test body fragment.</p></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "https://example.com/test.html");

 Assert.assertEquals(1, doc.numChildren());
 Element bodyNode = doc.body();
 Assert.assertEquals("body", bodyNode.tagName());

 Element pNode = bodyNode.get(0);
 Assert.assertEquals("p", pNode.tagName());
 Assert.assertEquals("This is a test body fragment.", pNode.text());
 }

 @Test
 public void testParseBodyFragmentRelaxed() {
 String bodyHtml = "<div><input type='text' name='username'></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "https://example.com/test.html");

 Element divNode = doc.body();
 Assert.assertEquals("div", divNode.tagName());

 Element inputNode = divNode.get(0);
 Assert.assertEquals("input", inputNode.tagName());
 Assert.assertTrue(inputNode.hasAttr("type"));
 Assert.assertEquals("text", inputNode.attr("type"));
 }

 @Test(expected =IllegalArgumentException.class)
 public void testParseNullHtml() {
 Parser.parse(null, "https://example.com/test.html");
 }

 @Test(expected =IllegalArgumentException.class)
 public void testParseEmptyHtml() {
 Parser.parse("", "https://example.com/test.html");
 }

 @Test(expected =IllegalArgumentException.class)
 public void testParseInvalidTag() {
 String invalidHtml = "<invalid>";
 Parser parser = new Parser(invalidHtml, "https://example.com/test.html", false);
 parser.parse(); // will cause exception to be thrown
 }

 @Test
 public void testParseXmlDeclaration() {
 String xmlHtml = "<?xml version='1.0' encoding='UTF-8'?><html><head><title>Test Page</title></head><body><h1>Welcome to JSoup!</h1></body></html>";
 Document doc = Parser.parse(xmlHtml, "https://example.com/test.html");

 Element xmlDeclaration = doc.body().get(0);
 Assert.assertTrue(xmlDeclaration instanceof XmlDeclaration);
 }
}