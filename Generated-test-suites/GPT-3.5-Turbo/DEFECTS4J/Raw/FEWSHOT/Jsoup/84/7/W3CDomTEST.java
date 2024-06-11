package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

	@Test
	public void testFromJsoup() {
		Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
		W3CDom w3cDom = new W3CDom();

		Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

		assertNotNull(w3cDoc);
		Element element = w3cDoc.getDocumentElement();
		assertEquals("html", element.getTagName());
	}

	@Test
	public void testConvert() {
		Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
		W3CDom w3cDom = new W3CDom();
		org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

		Document out = new Document("");
		w3cDom.convert(jsoupDoc, out);

		assertNotNull(out);
		assertEquals("html", out.nodeName());
		Element body = out.body();
		assertNotNull(body);
		assertEquals("body", body.nodeName());
		Element h1 = body.getElementsByTag("h1").first();
		assertNotNull(h1);
		assertEquals("h1", h1.nodeName());
		assertEquals("Hello World", h1.text());
	}

	@Test
	public void testW3CBuilderHeadElement() {
		Document jsoupDoc = Jsoup.parse("<h1 class=\"title\">Hello World</h1>");
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		org.jsoup.nodes.Element source = jsoupDoc.body().getElementsByTag("h1").first();
		builder.head(source, 0);

		Document w3cDoc = builder.getDocument();
		assertNotNull(w3cDoc);
		Element rootElement = w3cDoc.getDocumentElement();
		assertNotNull(rootElement);
		Element h1 = (Element) rootElement.getElementsByTagName("h1").item(0);
		assertNotNull(h1);
		assertEquals("Hello World", h1.getTextContent());
		assertEquals("title", h1.getAttribute("class"));
	}

	@Test
	public void testW3CBuilderHeadTextNode() {
		TextNode textNode = new TextNode("Hello World");
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		builder.head(textNode, 0);

		Document w3cDoc = builder.getDocument();
		assertNotNull(w3cDoc);
		Element rootElement = w3cDoc.getDocumentElement();
		assertNotNull(rootElement);
		Text text = (Text) rootElement.getFirstChild();
		assertNotNull(text);
		assertEquals("Hello World", text.getData());
	}

	@Test
	public void testW3CBuilderHeadComment() {
		Comment comment = new Comment("This is a comment");
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		builder.head(comment, 0);

		Document w3cDoc = builder.getDocument();
		assertNotNull(w3cDoc);
		Element rootElement = w3cDoc.getDocumentElement();
		assertNotNull(rootElement);
		Comment c = (Comment) rootElement.getFirstChild();
		assertNotNull(c);
		assertEquals("This is a comment", c.getData());
	}

	@Test
	public void testW3CBuilderHeadDataNode() {
		DataNode dataNode = new DataNode("This is data");
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		builder.head(dataNode, 0);

		Document w3cDoc = builder.getDocument();
		assertNotNull(w3cDoc);
		Element rootElement = w3cDoc.getDocumentElement();
		assertNotNull(rootElement);
		Text text = (Text) rootElement.getFirstChild();
		assertNotNull(text);
		assertEquals("This is data", text.getData());
	}

	@Test
	public void testW3CBuilderTailElement() {
		Document w3cDoc = Document.createDocument();
		Element rootElement = w3cDoc.createElement("root");
		w3cDoc.appendChild(rootElement);

		Element element = w3cDoc.createElement("element");
		rootElement.appendChild(element);

		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

		builder.tail(element, 0);

		element = (Element) w3cDoc.getFirstChild();
		assertNotNull(element);
		assertEquals("root", element.getTagName());
	}

	@Test
	public void testCopyAttributes() {
		Document jsoupDoc = Jsoup.parse("<div id=\"test\" class=\"hello\"></div>");
		Element element = jsoupDoc.body().getElementsByTag("div").first();
		Element w3cElement = Document.createDocument().createElement("div");
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		builder.copyAttributes(element, w3cElement);

		assertTrue(w3cElement.hasAttribute("id"));
		assertEquals("test", w3cElement.getAttribute("id"));

		assertTrue(w3cElement.hasAttribute("class"));
		assertEquals("hello", w3cElement.getAttribute("class"));
	}

	@Test
	public void testUpdateNamespaces() {
		Document jsoupDoc = Jsoup.parse("<ns:div xmlns:ns=\"http://www.example.com\"></ns:div>");
		Element element = jsoupDoc.body().getElementsByTag("ns:div").first();
		W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);

		String prefix = builder.updateNamespaces(element);

		assertEquals("ns", prefix);
	}

	@Test
	public void testAsString() {
		Document w3cDoc = Document.createDocument();
		Element rootElement = w3cDoc.createElement("root");
		w3cDoc.appendChild(rootElement);
		Element childElement = w3cDoc.createElement("child");
		rootElement.appendChild(childElement);

		W3CDom w3cDom = new W3CDom();

		String xml = w3cDom.asString(w3cDoc);

		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root><child/></root>", xml);
	}
}