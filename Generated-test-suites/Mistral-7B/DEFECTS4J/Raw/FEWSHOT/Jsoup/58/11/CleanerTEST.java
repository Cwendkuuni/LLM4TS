package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;
import org.junit.Test;

public class CleanerTest {

 private static final Whitelist WHITELIST = Whitelist.simple();

 @Test
 public void testCleanerCreation() {
 Cleaner cleaner = new Cleaner(WHITELIST);
 }

 @Test
 public void testCleanDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>foo</h1><script>alert('xss');</script></body></html>");
 Document cleanDocument = new Cleaner(WHITELIST).clean(dirtyDocument);

 assertEquals("<html><body><h1>foo</h1></body></html>", cleanDocument.body().html());
 assertFalse(cleanDocument.body().hasMetaCharset());
 assertFalse(cleanDocument.body().hasDoctype());
 }

 @Test
 public void testIsValidDocument() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>foo</h1></body></html>");
 assertTrue(new Cleaner(WHITELIST).isValid(dirtyDocument));

 Document invalidDocument = Jsoup.parse("<html><body><h1>foo</h1><script>alert('xss');</script></body></html>");
 assertFalse(new Cleaner(WHITELIST).isValid(invalidDocument));
 }

 @Test
 public void testCopySafeNodes() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1 id='foo' class='bar'><span>Hello</span></h1></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("div");

 int numDiscarded = new Cleaner(WHITELIST).copySafeNodes(root, destination);

 assertEquals(2, numDiscarded);

 Element h1 = new Cleaner(WHITELIST).clean(dirtyDocument).selectFirst("h1");
 assertSame(h1, destination.getElementsByTagName("h1").get(0));
 }

 @Test
 public void testCreateSafeElement() {
 Element sourceElement = Jsoup.parse("<html><body><h1 id='foo' class='bar' style='color:red'></h1></body></html>").selectFirst("h1");

 ElementMeta meta = new Cleaner(WHITELIST).createSafeElement(sourceElement);

 assertEquals("h1", meta.el.nodeName());
 assertEquals(3, meta.el.attributes().size());
 assertTrue(meta.el.hasAttr("id"));
 assertTrue(meta.el.hasAttr("class"));
 assertFalse(meta.el.hasAttr("style"));
 }

 @Test
 public void testCopySafeNodesWithDataNode() {
 Document dirtyDocument = Jsoup.parse("<html><body><script id='foo' src='bar.js'></script></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("div");

 int numDiscarded = new Cleaner(WHITELIST).copySafeNodes(root, destination);

 assertEquals(1, numDiscarded);

 DataNode dataNode = new Cleaner(WHITELIST).clean(dirtyDocument).selectFirst("#foo");
 assertSame(dataNode, destination.getElementsByTag("script").get(0));
 }

 @Test
 public void testCopySafeNodesWithTextNode() {
 Document dirtyDocument = Jsoup.parse("<html><body><h1>Hello <script>world</script></h1></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("div");

 int numDiscarded = new Cleaner(WHITELIST).copySafeNodes(root, destination);

 assertEquals(0, numDiscarded);

 Element h1 = new Cleaner(WHITELIST).clean(dirtyDocument).selectFirst("h1");
 Element textNode = h1.selectFirst("text()");
 Element textNodeDestination = destination.createTextNode(textNode.text());

 assertSame(textNodeDestination, destination.getElementsByTag("text").get(0));
 }

 @Test
 public void testCopySafeNodesWithComment() {
 Document dirtyDocument = Jsoup.parse("<html><body><!--comment--></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("div");

 int numDiscarded = new Cleaner(WHITELIST).copySafeNodes(root, destination);

 assertEquals(0, numDiscarded);

 assertTrue(destination.getElementsByTag("!comment").isEmpty());
 }

 @Test
 public void testCopySafeNodesWithProcessingInstruction() {
 Document dirtyDocument = Jsoup.parse("<html><body><xml-declaration systemId='foo'></xml-declaration></body></html>");
 Element root = dirtyDocument.body();
 Element destination = new Element("div");

 int numDiscarded = new Cleaner(WHITELIST).copySafeNodes(root, destination);

 assertEquals(0, numDiscarded);

 assertTrue(destination.getElementsByTag("xml-declaration").isEmpty());
 }
}