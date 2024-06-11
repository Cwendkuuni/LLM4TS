package org.jsoup.parser.test;

import static org.junit.Assert.*;
import org.jsoup.parser.Parser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser.*;

import java.util.LinkedList;

/**
 * Test for Parser class
 */
public class ParserTest {

 @ org.junit.BeforeClass static public void setup() {
 // Set up global variables or resources used by all tests in this class
 }

 /**
 * Test case for parse method with valid HTML
 */
 @ org.junit.Test public void testParseValidHtml() {
 String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
 Document doc = Parser.parse(html, "http://example.com");
 // Check the parsed document structure and elements
 Element htmlNode = doc.head();
 assertEquals(htmlTag, htmlNode.tag());
 assertEquals("html", htmlNode.tagName());
 assertEquals("http://example.com", htmlNode.baseUri());
 Element headNode = htmlNode.child(0);
 assertEquals(headTag, headNode.tag());
 assertEquals("head", headNode.tagName());
 Element titleNode = headNode.child(0);
 assertEquals(titleTag, titleNode.tag());
 assertEquals("Title", titleNode.text());
 Element bodyNode = doc.body();
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals("body", bodyNode.tagName());
 Element headingNode = bodyNode.child(0);
 assertEquals(tag("h1"), headingNode.tag());
 assertEquals("Heading", headingNode.text());
 }

 /**
 * Test case for parse method with invalid HTML (missing end tag)
 */
 @ org.junit.Test(expected = IllegalStateException.class) public void testParseInvalidHtmlMissingEndTag() {
 String html = "<html><head><title>Title</title>";
 Document doc = Parser.parse(html, "http://example.com");
 fail("Expected an IllegalStateException but no exception was thrown.");
 }

 /**
 * Test case for parseBodyFragment method with valid HTML
 */
 @ org.junit.Test public void testParseBodyFragmentValidHtml() {
 String bodyHtml = "<body><h1>Heading</h1></body>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 // Check the parsed document structure and elements
 Element bodyNode = doc.body();
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals("body", bodyNode.tagName());
 Element headingNode = bodyNode.child(0);
 assertEquals(tag("h1"), headingNode.tag());
 assertEquals("Heading", headingNode.text());
 }

 /**
 * Test case for parseBodyFragmentRelaxed method with valid HTML
 */
 @ org.junit.Test public void testParseBodyFragmentRelaxedValidHtml() {
 String bodyHtml = "<body><h1>Heading</h1>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 // Check the parsed document structure and elements
 Element bodyNode = doc.body();
 assertEquals(bodyTag, bodyNode.tag());
 assertEquals("body", bodyNode.tagName());
 Element headingNode = bodyNode.child(0);
 assertEquals(tag("h1"), headingNode.tag());
 assertEquals("Heading", headingNode.text());
 }

 /**
 * Test case for parse method with empty string
 */
 @ org.junit.Test public void testParseEmptyString() {
 String html = "";
 Document doc = Parser.parse(html, "http://example.com");
 // Check the parsed document is empty
 assertTrue(doc.toString().isEmpty());
 }

 /**
 * Test case for parse method with null input
 */
 @ org.junit.Test(expected = NullPointerException.class) public void testParseNullInput() {
 Parser.parse(null, "http://example.com");
 fail("Expected a NullPointerException but no exception was thrown.");
 }

 /**
 * Test case for parse method with invalid baseUri
 */
 @ org.junit.Test(expected = IllegalArgumentException.class) public void testParseInvalidBaseUri() {
 String html = "<html><head><title>Title</title></head><body><h1>Heading</h1></body></html>";
 Parser parser = new Parser(html, null, false); // invalid baseUri
 Document doc = parser.parse();
 fail("Expected an IllegalArgumentException but no exception was thrown.");
 }

 /**
 * Test case for parse method with isBodyFragment set to false and invalid HTML (missing start tag)
 */
 @ org.junit.Test(expected = IllegalStateException.class) public void testParseInvalidHtmlMissingStartTag() {
 String html = "</html>";
 Parser parser = new Parser(html, "http://example.com", false); // invalid HTML, missing start tag
 Document doc = parser.parse();
 fail("Expected an IllegalStateException but no exception was thrown.");
 }

 /**
 * Test case for parseBodyFragment method with empty string
 */
 @ org.junit.Test public void testParseBodyFragmentEmptyString() {
 String bodyHtml = "";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://example.com");
 // Check the parsed document is empty
 assertTrue(doc.toString().isEmpty());
 }

 /**
 * Test case for parseBodyFragment method with null input
 */
 @ org.junit.Test(expected = NullPointerException.class) public void testParseBodyFragmentNullInput() {
 Parser.parseBodyFragment(null, "http://example.com");
 fail("Expected a NullPointerException but no exception was thrown.");
 }

 /**
 * Test case for parseBodyFragmentRelaxed method with empty string
 */
 @ org.junit.Test public void testParseBodyFragmentRelaxedEmptyString() {
 String bodyHtml = "";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://example.com");
 // Check the parsed document is empty
 assertTrue(doc.toString().isEmpty());
 }

 /**
 * Test case for parseBodyFragmentRelaxed method with null input
 */
 @ org.junit.Test(expected = NullPointerException.class) public void testParseBodyFragmentRelaxedNullInput() {
 Parser.parseBodyFragmentRelaxed(null, "http://example.com");
 fail("Expected a NullPointerException but no exception was thrown.");
 }

}