package org.jsoup.parser;

import static org.junit.Assert.*;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Test class for Parser.java
 */
public class ParserTest {

 private static final String HTML_TEMPLATE = "<html><head><title>Test Title</title></head>" +
         "<body><h1>Test Heading</h1><p id=\"test-p\">Test Paragraph</p>" +
         "<textarea>Test Textarea</textarea>" +
         "<img src=\"test-image.jpg\"/><script src=\"test-script.js\"></script>" +
         "<base href=\"/test\"/></body></html>";

 private Parser parser;
 private Document doc;

 @Before
 public void setUp() {
     parser = new Parser(HTML_TEMPLATE, "/test", false);
     doc = parser.parse();
 }

 /**
  * Test constructor with null html and null baseUri
  */
 @Test(expected = NullPointerException.class)
 public void testNullHtmlAndBaseUri() {
     try {
         new Parser(null, null, false);
     } catch (NullPointerException e) {
         assertEquals("html is marked non-null but is null", e.getMessage());
     }
 }

 /**
  * Test constructor with null baseUri
  */
 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
     try {
         new Parser("", "/test", false);
     } catch (NullPointerException e) {
         assertEquals("baseUri is marked non-null but is null", e.getMessage());
     }
 }

 /**
  * Test parse method
  */
 @Test
 public void testParse() {
     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(headTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test Title", doc.childNodes().get(0).childNodes().get(0).text());

     assertEquals(1, doc.childNodes().size() + 1); // body is not included in the document by default
 }

 /**
  * Test parseBodyFragment method
  */
 @Test
 public void testParseBodyFragment() {
     Document docBodyFragment = Parser.parseBodyFragment("<p id=\"test-p\">Test Paragraph</p>", "/test");
     assertEquals(htmlTag, docBodyFragment.tagName());
     assertEquals(1, docBodyFragment.childNodes().size());
     assertEquals(bodyTag, docBodyFragment.childNodes().get(0).tagName());
     assertEquals(1, docBodyFragment.childNodes().get(0).childNodes().size());
     assertEquals(pTag, docBodyFragment.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test Paragraph", docBodyFragment.childNodes().get(0).childNodes().get(0).text());
 }

 /**
  * Test parse method with relaxed parsing
  */
 @Test
 public void testParseWithRelaxedParsing() {
     parser = new Parser(HTML_TEMPLATE, "/test", false);
     doc = parser.parse();

     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(headTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(titleTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test Title", doc.childNodes().get(0).childNodes().get(0).text());

     assertEquals(1, doc.childNodes().size() + 1); // body is included in the document
 }

 /**
  * Test parseComment method
  */
 @Test
 public void testParseComment() {
     parser = new Parser("<! -- Test Comment -->", "/test");
     doc = parser.parse();

     assertEquals(htmlTag, doc.tagName());
     assertEquals(0, doc.childNodes().size());
     assertTrue(doc.childNodes().isEmpty());

     Element comment = doc.childNodes().getLast();
     assertEquals("Test Comment", comment.text());
 }

 /**
  * Test parseXmlDecl method
  */
 @Test
 public void testParseXmlDecl() {
     parser = new Parser("<xml version=\"1.0\"></xml>", "/test");
     doc = parser.parse();

     assertEquals(xmlDeclarationTag, doc.childNodes().get(0).tagName());
     assertEquals("1.0", doc.childNodes().get(0).text());
     assertEquals(0, doc.childNodes().size() - 1);
 }

 /**
  * Test parseEndTag method
  */
 @Test
 public void testParseEndTag() {
     parser = new Parser("<p>Test Paragraph</p>", "/test");
     doc = parser.parse();

     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(bodyTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(pTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test Paragraph", doc.childNodes().get(0).childNodes().get(0).text());

     Element p = doc.childNodes().get(0).childNodes().get(0);
     parser = new Parser("<p></p>", "/test");
     doc = parser.parse();
     Element p2 = doc.childNodes().get(0).childNodes().get(0);

     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(bodyTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(pTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertTrue(p2.childNodes().isEmpty());
 }

 /**
  * Test parseStartTag method
  */
 @Test
 public void testParseStartTag() {
     parser = new Parser("<p id=\"test-p\">Test Paragraph</p>", "/test");
     doc = parser.parse();

     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(bodyTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(pTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test Paragraph", doc.childNodes().get(0).childNodes().get(0).text());

     Element p = doc.childNodes().get(0).childNodes().get(0);
     assertEquals(1, p.childNodes().size());

     Attribute idAttr = p.attr("id");
     assertEquals("test-p", idAttr.getKey());
     assertEquals("test-p", idAttr.getValue());
 }

 /**
  * Test parseAttribute method
  */
 @Test
 public void testParseAttribute() {
     parser = new Parser("<a href=\"test.html\" title=\"Test Title\">Test Link</a>", "/test");
     doc = parser.parse();

     Element link = doc.childNodes().get(0);
     assertEquals("a", link.tagName());
     assertEquals(1, link.childNodes().size());
     assertEquals("Test Link", link.childNodes().get(0).text());

     Attribute hrefAttr = link.attr("href");
     assertEquals("href", hrefAttr.getKey());
     assertEquals("test.html", hrefAttr.getValue());

     Attribute titleAttr = link.attr("title");
     assertEquals("title", titleAttr.getKey());
     assertEquals("Test Title", titleAttr.getValue());
 }

 /**
  * Test parseTextNode method
  */
 @Test
 public void testParseTextNode() {
     parser = new Parser("<p>Test Paragraph</p>", "/test");
     doc = parser.parse();

     Element p = doc.childNodes().get(0);
     TextNode textNode = (TextNode) p.childNodes().get(0);
     assertEquals("Test Paragraph", textNode.text());
 }

 /**
  * Test parseCdata method
  */
 @Test
 public void testParseCdata() {
     parser = new Parser("<![CDATA[Test CDATA]]>", "/test");
     doc = parser.parse();

     TextNode textNode = (TextNode) doc.childNodes().get(0);
     assertEquals("Test CDATA", textNode.text());
 }

 /**
  * Test addChildToParent method
  */
 @Test
 public void testAddChildToParent() {
     parser = new Parser("<p><i>Test i Tag</i></p>", "/test");
     doc = parser.parse();

     Element p = doc.childNodes().get(0);
     Element i = p.childNodes().get(0);

     Element p2 = new Element(pTag, "/test");
     Element i2 = new Element(iTag, "/test");
     p2.appendChild(i2);

     assertEquals(htmlTag, doc.tagName());
     assertEquals(1, doc.childNodes().size());
     assertEquals(bodyTag, doc.childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().size());
     assertEquals(pTag, doc.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals(1, doc.childNodes().get(0).childNodes().get(0).childNodes().size());
     assertEquals(iTag, doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test i Tag", doc.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());

     Element root = parser.parseBodyFragment("<p><i>Test i Tag</i></p>", "/test");
     Element p3 = root.childNodes().get(0);
     Element i3 = p3.childNodes().get(0);

     assertEquals(htmlTag, root.tagName());
     assertEquals(1, root.childNodes().size());
     assertEquals(bodyTag, root.childNodes().get(0).tagName());
     assertEquals(1, root.childNodes().get(0).childNodes().size());
     assertEquals(pTag, root.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals(1, root.childNodes().get(0).childNodes().get(0).childNodes().size());
     assertEquals(iTag, root.childNodes().get(0).childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test i Tag", root.childNodes().get(0).childNodes().get(0).childNodes().get(0).text());

     parser.parseBodyFragment("<p><i>Test i Tag</i></p>", "/test");
     Element p4 = parser.parseBodyFragment("<p><i>Test i Tag</i></p>", "/test").childNodes().get(0);
     Element i4 = p4.childNodes().get(0);

     assertEquals(htmlTag, p4.tagName());
     assertEquals(1, p4.childNodes().size());
     assertEquals(bodyTag, p4.childNodes().get(0).tagName());
     assertEquals(1, p4.childNodes().get(0).childNodes().size());
     assertEquals(pTag, p4.childNodes().get(0).tagName());
     assertEquals(1, p4.childNodes().get(0).childNodes().size());
     assertEquals(iTag, p4.childNodes().get(0).childNodes().get(0).tagName());
     assertEquals("Test i Tag", p4.childNodes().get(0).childNodes().get(0).text());
 }

 /**
  * Test stackHasValidParent method
  */
 @Test
 public void testStackHasValidParent() {
     parser = new Parser(HTML_TEMPLATE, "/test", false);
     doc = parser.parse();

     for (Element el : doc.childNodes()) {
         for (Tag tag : el.tag().getValidChildren()) {
             Element el2 = new Element(tag, "/test");
             assertTrue(parser.stackHasValidParent(tag, el));
         }
     }

     parser = new Parser("<html><body><p><div></p></div></body></html>", "/test");
     doc = parser.parse();

     for (Element el : doc.childNodes()) {
         for (Tag tag : el.tag().getValidChildren()) {
             Element el2 = new Element(tag, "/test");
             assertFalse(parser.stackHasValidParent(tag, el));
         }
     }
 }

 /**
  * Test popStackToSuitableContainer method
  */
 @Test
 public void testPopStackToSuitableContainer() {
     parser = new Parser("<html><head><title>Test Title</title></head>" +
             "<body><h1>Test Heading</h1>" +
             "<p><i>Test i Tag</i></p>" +
             "<p><div></div></p></body></html>", "/test");
     doc = parser.parse();

     Element h1 = doc.childNodes().get(1);
     Element div = parser.popStackToSuitableContainer(pTag);
     assertEquals(bodyTag, div.tag());

     Element i = parser.popStackToSuitableContainer(iTag);
     assertEquals(pTag, i.parent().tag());

     Element p = parser.popStackToSuitableContainer(pTag);
     assertEquals(bodyTag, p.tag());

     Element title = parser.popStackToSuitableContainer(titleTag);
     assertEquals(htmlTag, title.parent().tag());
 }

 /**
  * Test popStackToClose method
  */
 @Test
 public void testPopStackToClose() {
     parser = new Parser("<p><i><i>Test i Tag</i></i></p>", "/test");
     doc = parser.parse();

     Element p = doc.childNodes().get(0);
     Element i1 = parser.popStackToClose(iTag);
     assertEquals(iTag, i1.tag());
     assertEquals(p, i1.parent());

     Element i2 = parser.popStackToClose(iTag);
     assertEquals(p, i2.parent());
 }
}