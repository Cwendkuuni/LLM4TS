
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a JUnit 4 test class for the W3CDom class.
 */
public class W3CDomTest {

    private W3CDom W3CDOM;

    @Before
    public void setup() {
        W3CDOM = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");

        Document w3cDoc = W3CDOM.fromJsoup(jsoupDoc);

        // Verify the title tag
        Element titleElement = w3cDoc.getElementsByTagName("title").item(0);
        Node titleText = titleElement.getFirstChild();
        assert(titleText instanceof Text);
        assert(titleText.getNodeValue().equals("Test"));

        // Verify the body tag
        Element bodyElement = w3cDoc.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Node pText = pElement.getFirstChild();
        assert(pText instanceof Text);
        assert(pText.getNodeValue().equals("Hello, world!"));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body><p>Hello, world!</p></body></html>");

        Document w3cDoc = W3CDOM.fromJsoup(jsoupDoc);
        Document convertedDoc = W3CDOM.convert(jsoupDoc, w3cDoc);

        assert(convertedDoc == w3cDoc); // Check if the same document instance is returned

        // Verify the title tag
        Element titleElement = w3cDoc.getElementsByTagName("title").item(0);
        Node titleText = titleElement.getFirstChild();
        assert(titleText instanceof Text);
        assert(titleText.getNodeValue().equals("Test"));

        // Verify the body tag
        Element bodyElement = w3cDoc.getElementsByTagName("body").item(0);
        Element pElement = (Element) bodyElement.getElementsByTagName("p").item(0);
        Node pText = pElement.getFirstChild();
        assert(pText instanceof Text);
        assert(pText.getNodeValue().equals("Hello, world!"));
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        try {
            docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("root");
            doc.appendChild(root);

            String result = W3CDOM.asString(doc);

            assert(result.equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<root/>"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}

