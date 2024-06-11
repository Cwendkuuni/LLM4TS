package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
  
  private W3CDom w3cDom;
  private org.jsoup.nodes.Document jsoupDoc;
  
  @Before
  public void setup() throws ParserConfigurationException {
    w3cDom = new W3CDom();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    jsoupDoc = new org.jsoup.nodes.Document("");
    jsoupDoc.appendChild(new org.jsoup.nodes.Element("html"));
  }

  @Test
  public void testFromJsoup() {
    Document result = w3cDom.fromJsoup(jsoupDoc);
    assertNotNull(result);
  }

  @Test
  public void testConvert() {
    Document out = null;
    w3cDom.convert(jsoupDoc, out);
    assertNotNull(out);
  }

  @Test
  public void testW3CBuilder() {
    Document doc = null;
    W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
    org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("div");
    w3cBuilder.head(jsoupElement, 0);
    w3cBuilder.tail(jsoupElement, 0);
  }

  @Test
  public void testCopyAttributes() {
    Element el = new Element("div");
    org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
    source.dataset().put("test", "1");
    source.attr("testAttr", "2");
    org.jsoup.nodes.Attribute attribute = new org.jsoup.nodes.Attribute("class", "testClass");
    source.attributes().put(attribute.getKey(), attribute);
    w3cDom.copyAttributes(source, el);

    Attr result = el.getAttributeNode("class");
    assertNotNull(result);
    assertEquals("testClass", result.getValue());
  }

  @Test
  public void testUpdateNamespaces() {
    org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
    el.attr("xmlns:html", "http://www.w3.org/1999/xhtml");
    el.attr("xmlns:foo", "http://www.example.com/foo");
    String result = w3cDom.updateNamespaces(el);
    assertNotNull(result);
    assertEquals("", result);
  }

  @Test
  public void testAsString() throws TransformerException {
    Document doc = null;
    String result = w3cDom.asString(doc);
    assertNotNull(result);
  }

}