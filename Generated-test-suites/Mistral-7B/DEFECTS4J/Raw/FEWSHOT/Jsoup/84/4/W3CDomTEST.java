package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        String html = "<html><head></head><body><div id=\"root\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(new StringReader(html));
        Document w3cDoc = W3CDom.fromJsoup(jsoupDoc);

        assertEquals(html, W3CDom.asString(w3cDoc));
        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() throws ParserConfigurationException, JDOMException, TransformerException {
        String html = "<html xmlns:html='http://www.w3.org/1999/xhtml' xml:lang='en'><head><title>Test</title></head><body><div id=\"root\">Hello World</div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse(new StringReader(html));
        Document w3cDoc = W3CDom.createDocument();
        W3CDom.convert(jsoupDoc, w3cDoc);

        assertEquals(html, W3CDom.asString(w3cDoc));
        assertNotNull(w3cDoc.getDocumentElement());
        assertEquals("html:html", w3cDoc.getDocumentElement().getTagName());

        Element htmlEl = (Element) w3cDoc.getDocumentElement();
        assertEquals("en", htmlEl.getAttribute("xml:lang"));
        assertNull(htmlEl.getAttribute("xmlns"));

        Map<String, String> namespaceMap = new HashMap<>();
        namespaceMap.put("html", "http://www.w3.org/1999/xhtml");
        htmlEl.setNamespaceURI("http://www.w3.org/1999/xhtml");
        htmlEl.setPrefix("html");

        Node node = jsoupDoc.getElementById("root");
        Element w3cElement = (Element) node;
        String namespacePrefix = w3cElement.getPrefix();
        String namespaceUri = w3cElement.getNamespaceURI();

        assertEquals(namespacePrefix, "html");
        assertEquals(namespaceUri, "http://www.w3.org/1999/xhtml");

        Node w3cNode = w3cElement;
        StringWriter stringWriter = new StringWriter();
        XMLOutputter xmlOutputter = new XMLOutputter(new org.jdom2.output.Format("  ", true, new java.io.File("")));
        xmlOutputter.output(w3cDoc, stringWriter);
        String xmlString = stringWriter.toString();

        Document parsedXmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(xmlString.getBytes()));

        assertEquals(W3CDom.asString(parsedXmlDoc), W3CDom.asString(w3cDoc));
    }
}