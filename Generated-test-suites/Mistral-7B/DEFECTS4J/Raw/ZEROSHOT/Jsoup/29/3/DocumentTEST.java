package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DocumentTest {

 @Test
 public void testDocumentCreation() {
 Document doc = new Document("http://www.example.com");
 assertNotNull(doc);
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testCreateShell() {
 Document doc = Document.createShell("http://www.example.com");
 assertNotNull(doc);
 Element html = doc.head();
 assertEquals("html", html.nodeName());
 Element head = html.head();
 assertNotNull(head);
 Element body = html.body();
 assertNotNull(body);
 }

 @Test
 public void testGetHeadAndBody() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>", "http://www.example.com");
 Element head = doc.head();
 assertNotNull(head);
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://www.example.com");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testSetTitle() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body></body></html>", "http://www.example.com");
 doc.title("New Title");
 assertEquals("New Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Document doc = Jsoup.parse("<html></html>", "http://www.example.com");
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 assertEquals("a", newElement.nodeName());
 }

 @Test
 public void testNormalise() {
 Document doc = Jsoup.parse("<html><head><p>Test</p></head></html>", "http://www.example.com");
 doc.normalise();
 Element body = doc.body();
 List<Node> bodyChildren = new ArrayList<>();
 for (Node child : body.childNodes()) {
 bodyChildren.add(child);
 }
 assertEquals(1, bodyChildren.size());
 assertEquals("Test", bodyChildren.get(0).text());
 }

 @Test
 public void testNormaliseStructure() {
 Document doc = Jsoup.parse("<html><head><p>Test1</p></head><head><p>Test2</p></head></html>", "http://www.example.com");
 doc.normalise();
 Element body = doc.body();
 List<Node> bodyChildren = new ArrayList<>();
 for (Node child : body.childNodes()) {
 bodyChildren.add(child);
 }
 assertEquals(1, bodyChildren.size());
 assertEquals("Test1Test2", bodyChildren.get(0).text());
 }

 @Test
 public void testFindFirstElementByTagName() {
 Document doc = Jsoup.parse("<html><head><body><p>Test</p></body></head></html>", "http://www.example.com");
 Element head = doc.findFirstElementByTagName("head", doc);
 assertNotNull(head);
 Element body = doc.findFirstElementByTagName("body", doc);
 assertNotNull(body);
 }

 @Test
 public void testOuterHtml() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>", "http://www.example.com");
 String expected = "<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>";
 assertEquals(expected, doc.outerHtml());
 }

 @Test
 public void testText() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>", "http://www.example.com");
 doc.body().text("New Test Body");
 assertEquals("New Test Body", doc.body().text());
 }

 @Test
 public void testNodeName() {
 Document doc = Jsoup.parse("<html></html>", "http://www.example.com");
 assertEquals("#document", doc.nodeName());
 }

 @Test
 public void testClone() {
 Document doc = Jsoup.parse("<html><head><title>Test Title</title></head><body><p>Test Body</p></body></html>", "http://www.example.com");
 Document clone = doc.clone();
 Element title = doc.title();
 title.text("New Title");
 assertNotNull(title);
 assertNull(clone.title());
 }

 @Test
 public void testOutputSettings() {
 Document doc = Jsoup.parse("<html></html>", "http://www.example.com");
 OutputSettings outputSettings = doc.outputSettings();
 assertEquals(Charset.forName("UTF-8"), outputSettings.charset());
 }
}