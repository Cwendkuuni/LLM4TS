package org.jsoup.helper;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    private static org.jsoup.nodes.Document jsoupDoc;
    private static org.w3c.dom.Document w3cDoc;
    private static W3CDom w3cDomConverter;

    @BeforeClass
    public static void setUp() {
        String html = "<html><head></head><body><div><p>Hello, World!</p></div></body></html>";
        jsoupDoc = org.jsoup.Jsoup.parse(html);
        w3cDomConverter = new W3CDom();
        w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        // check if the converted W3C document is not null
        assertEquals(true, w3cDoc != null);
    }

    @Test
    public void testConvert() {
        // check if the W3C document URI is set correctly
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvert_tagName() {
        // check if the root element of W3C document is converted correctly
        assertEquals(jsoupDoc.child(0).tagName(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert_textNode() {
        // check if TextNodes are converted correctly in the W3C document
        Elements jsoupTextNodes = jsoupDoc.select("html body div p").first().textNodes();
        NodeList w3cTextNodes = w3cDoc.getElementsByTagName("p").item(0).getChildNodes();

        assertEquals(jsoupTextNodes.size(), w3cTextNodes.getLength());
        for (int i = 0; i < jsoupTextNodes.size(); i++) {
            assertEquals(jsoupTextNodes.get(i).text(), w3cTextNodes.item(i).getTextContent());
        }
    }

    @Test
    public void testConvert_comment() {
        // check if Comments are converted correctly in the W3C document
        org.jsoup.nodes.Comment jsoupComment = jsoupDoc.select("html body div p").first().parent().previousSibling();
        Node w3cComment = w3cDoc.getElementsByTagName("body").item(0).getPreviousSibling();

        assertEquals(jsoupComment.getData(), w3cComment.getNodeValue());
    }

    @Test
    public void testConvert_dataNode() {
        // check if DataNodes are converted correctly in the W3C document
        org.jsoup.nodes.DataNode jsoupDataNode = jsoupDoc.select("html body div p").first().previousSibling();
        Node w3cDataNode = w3cDoc.getElementsByTagName("div").item(0).getPreviousSibling();

        assertEquals(jsoupDataNode.getWholeData(), w3cDataNode.getTextContent());
    }

    @Test
    public void testAsString() {
        // check if the serialized W3C document matches the input HTML string
        String w3cString = w3cDomConverter.asString(w3cDoc);
        assertEquals(jsoupDoc.html(), w3cString);
    }

    @Test
    public void testInvalidDocumentBuilder() {
        // simulate ParserConfigurationException by providing invalid configuration
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true); // invalid option
        W3CDom w3cDomConverter = new W3CDom(factory);

        try {
            org.w3c.dom.Document w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);
        } catch (IllegalStateException e) {
            assertEquals("java.lang.IllegalStateException: javax.xml.parsers.FactoryConfigurationError: " +
                    "Unable to validate using org.xml.sax.SAXNotRecognizedException: feature " +
                    "'http://xml.org/sax/features/validation' not recognized", e.toString());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testAsNullString() {
        // try to serialize a null W3C document
        w3cDomConverter.asString(null);
    }
}