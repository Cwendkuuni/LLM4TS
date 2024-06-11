package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
	
	private W3CDom w3cDom;
	private org.jsoup.nodes.Document jsoupDoc;
	private Document w3cDoc;
	private Element rootEl;
	
	@Before
	public void setUp() {
		w3cDom = new W3CDom();
		jsoupDoc = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello</h1><p>Example</p></body></html>");
		w3cDoc = w3cDom.fromJsoup(jsoupDoc);
		rootEl = w3cDoc.getDocumentElement();
	}
	
	@Test
	public void testFromJsoup() {
		assertNotNull(w3cDoc);
		assertEquals("html", rootEl.getTagName());
		assertEquals("Test", rootEl.getElementsByTagName("title").item(0).getTextContent());
	}
	
	@Test
	public void testConvert() {
		assertEquals("html", rootEl.getTagName());
		assertEquals("Hello", rootEl.getElementsByTagName("h1").item(0).getTextContent());
		assertEquals("Example", rootEl.getElementsByTagName("p").item(0).getTextContent());
	}
	
	@Test
	public void testW3CBuilder_head_Element() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.Element sourceEl = jsoupDoc.createElement("div").attr("id", "test");
		builder.head(sourceEl, 1);
		Element destEl = (Element)builder.dest;
		assertEquals("div", destEl.getTagName());
		assertEquals("test", destEl.getAttribute("id"));
	}
	
	@Test
	public void testW3CBuilder_head_TextNode() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Example", "");
		builder.head(sourceText, 1);
		Text destText = (Text)builder.dest.getLastChild();
		assertEquals("Example", destText.getTextContent());
	}
	
	@Test
	public void testW3CBuilder_head_Comment() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("This is a comment");
		builder.head(sourceComment, 1);
		org.w3c.dom.Comment destComment = (org.w3c.dom.Comment)builder.dest.getLastChild();
		assertEquals("This is a comment", destComment.getData());
	}
	
	@Test
	public void testW3CBuilder_head_DataNode() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Example", "");
		builder.head(sourceData, 1);
		Text destText = (Text)builder.dest.getLastChild();
		assertEquals("Example", destText.getData());
	}
	
	@Test
	public void testW3CBuilder_tail() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.Element sourceEl = jsoupDoc.createElement("div");
		builder.dest = w3cDoc.createElementNS("", "div");
		builder.tail(sourceEl, 1);
		assertEquals(w3cDoc.getDocumentElement(), builder.dest);
	}
	
	@Test
	public void testCopyAttributes() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.Element sourceEl = jsoupDoc.createElement("div").attr("class", "example");
		Element destEl = w3cDoc.createElementNS("", "div");
		builder.copyAttributes(sourceEl, destEl);
		assertEquals("example", destEl.getAttribute("class"));
	}
	
	@Test
	public void testUpdateNamespaces() {
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
		org.jsoup.nodes.Element el = jsoupDoc.createElement("div")
				.attr("xmlns", "http://www.w3.org/XML/1998/namespace")
				.attr("xmlns:prefix", "http://www.w3.org/XML/1998/namespace");
		String prefix = builder.updateNamespaces(el);
		assertEquals("prefix", prefix);
		assertEquals("http://www.w3.org/XML/1998/namespace", builder.namespaces.get(prefix));
	}
	
	@Test
	public void testAsString() {
		String xmlString = w3cDom.asString(w3cDoc);
		String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head><title>Test</title></head><body><h1>Hello</h1><p>Example</p></body></html>";
		assertEquals(expectedString, xmlString);
	}
}