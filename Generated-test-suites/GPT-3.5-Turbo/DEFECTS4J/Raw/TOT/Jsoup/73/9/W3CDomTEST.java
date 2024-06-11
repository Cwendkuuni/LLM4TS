import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

public class W3CDomTest {

    W3CDom w3cDom;
    org.jsoup.nodes.Document jsoupDocument;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDocument = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup_validDocument() {
        org.jsoup.nodes.Element rootElement = jsoupDocument.appendElement("root");
        org.jsoup.nodes.Element childElement = rootElement.appendElement("child");
        childElement.attr("attribute", "value");

        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
        Node rootNode = w3cDocument.getFirstChild();
        Assert.assertNotNull(rootNode);
        Assert.assertEquals("root", rootNode.getNodeName());
        NodeList childNodes = rootNode.getChildNodes();
        Assert.assertEquals(1, childNodes.getLength());
        Node childNode = childNodes.item(0);
        Assert.assertEquals("child", childNode.getNodeName());
        Assert.assertEquals("value", childNode.getAttributes().getNamedItem("attribute").getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_nullDocument() {
        w3cDom.fromJsoup(null);
    }

    @Test
    public void testConvert_validDocument() {
        org.jsoup.nodes.Element rootElement = jsoupDocument.appendElement("root");
        org.jsoup.nodes.Element childElement = rootElement.appendElement("child");
        childElement.attr("attribute", "value");

        Document w3cDocument = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            w3cDocument = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            Assert.fail("Failed to create W3C Document");
        }

        w3cDom.convert(jsoupDocument, w3cDocument);

        Assert.assertNotNull(w3cDocument);
        Node rootNode = w3cDocument.getFirstChild();
        Assert.assertNotNull(rootNode);
        Assert.assertEquals("root", rootNode.getNodeName());
        NodeList childNodes = rootNode.getChildNodes();
        Assert.assertEquals(1, childNodes.getLength());
        Node childNode = childNodes.item(0);
        Assert.assertEquals("child", childNode.getNodeName());
        Assert.assertEquals("value", childNode.getAttributes().getNamedItem("attribute").getTextContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullJsoupDocument() {
        Document w3cDocument = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            w3cDocument = documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            Assert.fail("Failed to create W3C Document");
        }

        w3cDom.convert(null, w3cDocument);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvert_nullW3CDocument() {
        w3cDom.convert(jsoupDocument, null);
    }

    @Test
    public void testAsString_validDocument() {
        org.jsoup.nodes.Element rootElement = jsoupDocument.appendElement("root");
        org.jsoup.nodes.Element childElement = rootElement.appendElement("child");
        childElement.attr("attribute", "value");

        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        String w3cDocumentString = w3cDom.asString(w3cDocument);

        Assert.assertNotEquals("", w3cDocumentString);
        Assert.assertTrue(w3cDocumentString.contains("<root>"));
        Assert.assertTrue(w3cDocumentString.contains("<child attribute=\"value\">"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAsString_nullDocument() {
        w3cDom.asString(null);
    }

}