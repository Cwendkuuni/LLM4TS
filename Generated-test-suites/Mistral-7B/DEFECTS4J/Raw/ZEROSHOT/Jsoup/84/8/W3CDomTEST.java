package org.jsoup.helper;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
import org.jsoup.nodes.Document;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Test;
import static org.junit.Assert.*;

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
import java.util.Stack;

/**
 * Test class for W3CDom.java
 */
public class W3CDomTest {

 @Test
 public void testFromJsoup() throws ParserConfigurationException {
 Document jsoupDoc = Document.fromUrl("http://www.example.com");
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 assertEquals("http://www.example.com", w3cDoc.getDocumentURI());
 }

 @Test
 public void testConvert() throws ParserConfigurationException {
 Document jsoupDoc = Document.fromUrl("http://www.example.com");
 Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
 new W3CDom().convert(jsoupDoc, w3cDoc);
 assertEquals("http://www.example.com", w3cDoc.getDocumentURI());
 }

 @Test
 public void testAsString() throws ParserConfigurationException, TransformerException {
 Document jsoupDoc = Document.fromUrl("http://www.example.com");
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 String w3cString = new W3CDom().asString(w3cDoc);
 // Perform some assertions on the result string
 }

 @Test
 public void testW3CBuilder() throws ParserConfigurationException {
 Document jsoupDoc = DocumentBuilderFactory.newInstance().newDocument();
 Element root = jsoupDoc.createElement("root");
 root.appendChild(jsoupDoc.createTextNode("Hello World"));
 jsoupDoc.appendChild(root);
 Document w3cDoc = DocumentBuilderFactory.newInstance().newDocument();
 new W3CDom().convert(jsoupDoc, w3cDoc);

 NodeTraversor traversor = new NodeTraversor();
 traversor.addVisitor(new W3CBuilderChecker());
 traversor.traverse(w3cDoc.getDocumentElement());
 }

 /**
 * Visitor class for testing Element conversion
 */
 public static class W3CBuilderChecker implements NodeVisitor {

 @Override
 public void head(Node node, int depth) {
 if (node instanceof Element) {
 Element element = (Element) node;
 String nodeName = element.getNodeName();
 String nodeText = element.getTextContent();
 // Perform some assertions on the nodeName and nodeText
 }
 }

 @Override
 public void tail(Node node, int depth) {
 // No action required
 }
 }
}