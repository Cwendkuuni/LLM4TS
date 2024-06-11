package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;

import java.util.LinkedList;

public class ParserTest {

 @org.junit.BeforeClass
 static void setUpBeforeClass() throws Exception {
 }

 @org.junit.AfterClass
 static void tearDownAfterClass() throws Exception {
 }

 @org.junit.Before
 public void setUp() {
 }

 @org.junit.After
 public void tearDown() {
 }

 @org.junit.Test
 public void testParse() throws Exception {
 String html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
 Document doc = Parser.parse(html, "http://test.com/");
 Element titleElement = doc.select("title").first();
 Element h1Element = doc.body().select("h1").first();

 assertEquals("Test Page", titleElement.text());
 assertEquals("Welcome", h1Element.text());
 }

 @org.junit.Test
 public void testParseBodyFragment() throws Exception {
 String bodyHtml = "<h1>Test</h1>";
 Document doc = Parser.parseBodyFragment(bodyHtml, "http://test.com/");
 Element h1Element = doc.body().select("h1").first();

 assertEquals("Test", h1Element.text());
 }

 @org.junit.Test
 public void testParseBodyFragmentRelaxed() throws Exception {
 String bodyHtml = "<div><h1>Test</h1></div>";
 Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, "http://test.com/");
 Element h1Element = doc.body().select("h1").first();

 assertEquals("Test", h1Element.text());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithNullHtml() throws Exception {
 Parser.parse(null, "http://test.com/");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithNullBaseUri() throws Exception {
 Parser.parse("<html></html>", null);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithEmptyBaseUri() throws Exception {
 Parser.parse("<html></html>", "");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testParseWithInvalidBaseUri() throws Exception {
 Parser.parse("<html></html>", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M8AQwEgwHpZ2gpjcmVhdGVkIDMuMAAAD3R3d34lEAxMzYzSjQ2hyQxAAAAASUVORK5CYII=");
 }
}