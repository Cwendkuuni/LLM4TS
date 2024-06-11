import org.jsoup.helper.*;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;
import org.jsoup.helper.W3CDom;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;

import org.junit.Assert;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = createW3CDocument();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        Assert.assertTrue(w3cDoc.getDocumentElement() != null);
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);
        org.w3c.dom.Element w3cElement = w3cDoc.getDocumentElement();
        Assert.assertEquals(jsoupElement.tagName(), w3cElement.getTagName());
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);
        builder.tail(jsoupElement, 0);
        org.w3c.dom.Element w3cElement = w3cDoc.getDocumentElement();
        Assert.assertNull(w3cElement.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.attr("attr1", "value1");
        jsoupElement.attr("attr2", "value2");
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        org.w3c.dom.Element w3cElement = w3cDoc.createElementNS("", jsoupElement.tagName());
        builder.copyAttributes(jsoupElement, w3cElement);
        Assert.assertEquals(jsoupElement.attr("attr1"), w3cElement.getAttribute("attr1"));
        Assert.assertEquals(jsoupElement.attr("attr2"), w3cElement.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        jsoupElement.attr("xmlns", "urn:test");
        jsoupElement.attr("xmlns:prefix", "urn:prefix");
        Document w3cDoc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        String prefix = builder.updateNamespaces(jsoupElement);
        Assert.assertEquals("", prefix);
        Assert.assertEquals("urn:test", builder.namespaces.get(""));
        Assert.assertEquals("urn:prefix", builder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createW3CDocument();
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(result);
    }
    
    private Document createW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}