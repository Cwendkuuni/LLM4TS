
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();

        Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testFromJsoup_NullInput() {
        W3CDom w3cDom = new W3CDom();

        Document w3cDocument = w3cDom.fromJsoup(null);

        Assert.assertNull(w3cDocument);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("testElement");
        jsoupDocument.appendChild(jsoupElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        w3cBuilder.convert(jsoupDocument, w3cBuilder.doc);

        Assert.assertEquals(1, w3cBuilder.doc.getChildNodes().getLength());
        Assert.assertEquals("testElement", w3cBuilder.doc.getChildNodes().item(0).getNodeName());
    }

    @Test
    public void testConvert_BlankLocation() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        jsoupDocument.location("http://example.com");
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("testElement");
        jsoupDocument.appendChild(jsoupElement);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        w3cBuilder.convert(jsoupDocument, w3cBuilder.doc);

        Assert.assertEquals("http://example.com", w3cBuilder.doc.getDocumentURI());
    }

    @Test
    public void testConvert_TextNode() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("Hello World");
        jsoupDocument.appendChild(jsoupTextNode);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        w3cBuilder.convert(jsoupDocument, w3cBuilder.doc);

        Node w3cNode = w3cBuilder.doc.getChildNodes().item(0);
        Assert.assertTrue(w3cNode instanceof Text);
        Assert.assertEquals("Hello World", w3cNode.getTextContent());
    }

    @Test
    public void testConvert_Comment() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("This is a comment");
        jsoupDocument.appendChild(jsoupComment);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        w3cBuilder.convert(jsoupDocument, w3cBuilder.doc);

        Node w3cNode = w3cBuilder.doc.getChildNodes().item(0);
        Assert.assertTrue(w3cNode instanceof Comment);
        Assert.assertEquals("This is a comment", w3cNode.getNodeValue());
    }

    @Test
    public void testConvert_DataNode() {
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("This is data");
        jsoupDocument.appendChild(jsoupDataNode);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

        w3cBuilder.convert(jsoupDocument, w3cBuilder.doc);

        Node w3cNode = w3cBuilder.doc.getChildNodes().item(0);
        Assert.assertTrue(w3cNode instanceof Text);
        Assert.assertEquals("This is data", w3cNode.getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cElement = w3cDocument.createElement("testElement");
        w3cDocument.appendChild(w3cElement);
        W3CDom w3cDom = new W3CDom();

        String xmlString = w3cDom.asString(w3cDocument);

        Assert.assertNotNull(xmlString);
        Assert.assertEquals("<testElement/>", xmlString);
    }

    @Test
    public void testAsString_Exception() {
        Document w3cDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element w3cElement = w3cDocument.createElement("testElement");
        w3cDocument.appendChild(w3cElement);
        W3CDom w3cDom = new W3CDom();

        try {
            String xmlString = w3cDom.asString(null);
        } catch (IllegalStateException e) {
            Assert.assertEquals("java.lang.IllegalStateException: javax.xml.transform.TransformerException: java.lang.NullPointerException", e.toString());
        }
    }
}

