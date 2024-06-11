package org.jsoup.helper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml. lanzas.jaxp.JAXP;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoupToW3CAndBack() {
 Document jsoupDocument = JAXP.newDocumentBuilder().newDocument();
 Element root = jsoupDocument.getDocumentElement();
 root.setTagName("html");
 root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");

 Element head = jsoupDocument.createElement("head");
 Element title = jsoupDocument.createElement("title");
 title.setTextContent("Test Page");
 head.appendChild(title);
 root.appendChild(head);

 Element body = jsoupDocument.createElement("body");
 Element p = jsoupDocument.createElement("p");
 p.setTextContent("This is a test.");
 body.appendChild(p);
 root.appendChild(body);

 W3CDom w3CDom = new W3CDom();
 Document w3cDocument = w3CDom.fromJsoup(jsoupDocument);

 String w3cXml = w3CDom.asString(w3cDocument);
 Document parsedW3cDocument = JAXP.newDocumentBuilder().parse(new ByteArrayInputStream(w3cXml.getBytes()));

 assertEquals(jsoupDocument.getDocumentElement().getTagName(), parsedW3cDocument.getDocumentElement().getTagName());
 assertEquals(jsoupDocument.getDocumentElement().getAttribute("xmlns"), parsedW3cDocument.getDocumentElement().getAttribute("xmlns"));

 // Check head and title elements
 assertEquals(head.getTextContent(), parsedW3cDocument.getElementsByTagName("head").item(0).getTextContent());
 assertEquals(title.getTextContent(), parsedW3cDocument.getElementsByTagName("title").item(0).getTextContent());

 // Check body and p elements
 assertEquals(body.getTextContent(), parsedW3cDocument.getElementsByTagName("body").item(0).getTextContent());
 assertEquals(p.getTextContent(), parsedW3cDocument.getElementsByTagName("p").item(0).getTextContent());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullJsoupDocument() {
 W3CDom w3CDom = new W3CDom();
 Document jsoupDocument = null;
 try {
 w3CDom.fromJsoup(jsoupDocument);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

}