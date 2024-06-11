package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Traversal;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;

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

    private static Document createJsoupDocument() {
        Document doc = new TextDocument();
        Element root = doc.createElement("root");
        root.appendChild(doc.createTextNode("Hello World!"));
        doc.appendChild(root);
        return doc;
    }

    @Test
    public void testFromJsoup() throws ParserConfigurationException, TransformerException {
        Document jsoupDoc = createJsoupDocument();
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        DocumentTraversal traversal = (DocumentTraversal) w3cDoc;
        NodeFilter textNodeFilter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node instanceof Text;
            }
        };

        Traversal textTraversal = traversal.getDocument().createTreeWalker(textNodeFilter);
        assertEquals("Hello World!", ((Text) textTraversal.nextNode()).getData());
    }

    @Test
    public void testConvert() throws ParserConfigurationException, TransformerException {
        Document jsoupDoc = createJsoupDocument();
        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        DocumentTraversal traversal = (DocumentTraversal) w3cDoc;
        NodeFilter textNodeFilter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node instanceof Text;
            }
        };

        Traversal textTraversal = traversal.getDocument().createTreeWalker(textNodeFilter);
        assertEquals("Hello World!", ((Text) textTraversal.nextNode()).getData());

        Element root = (Element) w3cDoc.getDocumentElement();
        assertEquals("root", root.getTagName());
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        Document jsoupDoc = createJsoupDocument();
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String xmlString = new W3CDom().asString(w3cDoc);

        String expectedXml = "<root>Hello World!</root>";
        assertEquals(expectedXml, xmlString);
    }

    @Test
    public void testCommentAndText() throws ParserConfigurationException, TransformerException {
        Document jsoupDoc = new TextDocument();
        Element root = jsoupDoc.createElement("root");
        root.appendChild(jsoupDoc.createTextNode("Hello World!"));
        root.appendChild(jsoupDoc.createComment("Hello Comment!"));
        jsoupDoc.appendChild(root);

        Document w3cDoc = new Document();
        new W3CDom().convert(jsoupDoc, w3cDoc);

        DocumentTraversal traversal = (DocumentTraversal) w3cDoc;
        NodeFilter textNodeFilter = new NodeFilter() {
            @Override
            public boolean acceptNode(Node node) {
                return node instanceof Text;
            }
        };

        Traversal textTraversal = traversal.getDocument().createTreeWalker(textNodeFilter);
        assertEquals("Hello World!", ((Text) textTraversal.nextNode()).getData());

        Node commentNode = traversal.getDocument().getDocumentElement().getElementsByTagName("comment")
                .item(0);
        assertEquals("Hello Comment!", ((Comment) commentNode).getData());
    }
}