package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTest {

 private Document document;

 @Before
 public void setup() {
 document = new Document("").createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document doc = new Document("http://example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://example.com");
 assertEquals("#document", doc.nodeName());
 Element html = doc.head();
 assertEquals("http://example.com", html.baseUri());
 }

 @Test
 public void testGetHeadElement() {
 document.title("Test Title");
 Element head = document.head();
 Element title = head.getElementsByTag("title").first();
 assertEquals("Test Title", title.text());
 }

 @Test
 public void testGetBodyElement() {
 Element body = document.body();
 assertTrue(body.children().isEmpty());
 }

 @Test
 public void testGetTitle() {
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testSetTitle() {
 document.title("");
 document.title("Test Title");
 assertEquals("Test Title", document.title());
 }

 @Test
 public void testCreateElement() {
 Element p = document.createElement("p");
 assertEquals("p", p.nodeName());
 assertEquals("http://example.com", p.baseUri());
 }

 @Test
 public void testNormalize() {
 document.title("Test Title");
 document.appendText(" Test text ");
 document.normalize();
 Element body = document.body();
 assertEquals(2, body.childNodes().size());
 TextNode titleNode = document.getElementsByTag("title").first().childNodes().get(0);
 assertEquals("Test Title", titleNode.text());
 TextNode textNode = body.childNodes().get(0);
 assertEquals(" Test text ", textNode.text());
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 new Document(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyBaseUri() {
 new Document(" ");
 }
}