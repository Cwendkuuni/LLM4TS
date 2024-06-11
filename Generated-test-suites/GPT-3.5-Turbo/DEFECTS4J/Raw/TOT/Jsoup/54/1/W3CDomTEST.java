import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = new Document("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom w3cDom = new W3CDom();

        org.w3c.dom.Document doc = w3cDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(doc);
        Assert.assertEquals("html", doc.getDocumentElement().getTagName());
        Assert.assertEquals("head", doc.getDocumentElement().getChildNodes().item(0).getNodeName());
        Assert.assertEquals("title", doc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        Assert.assertEquals("body", doc.getDocumentElement().getChildNodes().item(1).getNodeName());
        Assert.assertEquals("h1", doc.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0).getNodeName());
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = new Document("<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());

        org.w3c.dom.Document doc = factory.newDocumentBuilder().newDocument();
        w3cBuilder.convert(jsoupDoc, doc);

        Assert.assertNotNull(doc.getDocumentElement());
        Assert.assertEquals("html", doc.getDocumentElement().getTagName());
        Assert.assertEquals("head", doc.getDocumentElement().getChildNodes().item(0).getNodeName());
        Assert.assertEquals("title", doc.getDocumentElement().getChildNodes().item(0).getChildNodes().item(0).getNodeName());
        Assert.assertEquals("body", doc.getDocumentElement().getChildNodes().item(1).getNodeName());
        Assert.assertEquals("h1", doc.getDocumentElement().getChildNodes().item(1).getChildNodes().item(0).getNodeName());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        Element dest = w3cBuilder.doc.createElement("div");

        w3cBuilder.head(sourceEl, 0);

        Assert.assertEquals(dest.getNodeName(), w3cBuilder.dest.getNodeName());
        Assert.assertEquals(dest.getParentNode(), w3cBuilder.dest.getParentNode());
    }

    @Test
    public void testW3CBuilderTextElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Test Text");
        org.w3c.dom.Text text = w3cBuilder.doc.createTextNode("Test Text");

        w3cBuilder.head(sourceText, 0);

        Assert.assertEquals(text.getWholeText(), w3cBuilder.dest.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderCommentElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("Test Comment");
        org.w3c.dom.Comment comment = w3cBuilder.doc.createComment("Test Comment");

        w3cBuilder.head(sourceComment, 0);

        Assert.assertEquals(comment.getData(), w3cBuilder.dest.getChildNodes().item(0).getNodeValue());
    }

    @Test
    public void testW3CBuilderDataElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Test Data");
        org.w3c.dom.Text node = w3cBuilder.doc.createTextNode("Test Data");

        w3cBuilder.head(sourceData, 0);

        Assert.assertEquals(node.getWholeText(), w3cBuilder.dest.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        Element el = w3cBuilder.doc.createElement("div");

        source.attributes().put("class", "test-class");
        source.attributes().put("id", "test-id");

        w3cBuilder.copyAttributes(source, el);

        Assert.assertEquals("test-class", el.getAttribute("class"));
        Assert.assertEquals("test-id", el.getAttribute("id"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(factory.newDocumentBuilder().newDocument());
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");

        el.attributes().put("xmlns", "http://www.example.com");
        el.attributes().put("xmlns:prefix", "http://www.example.com/prefix");

        Assert.assertEquals("", w3cBuilder.updateNamespaces(el));

        Assert.assertEquals("http://www.example.com", w3cBuilder.namespaces.get(""));
        Assert.assertEquals("http://www.example.com/prefix", w3cBuilder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() throws TransformerException {
        W3CDom w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream("<div></div>".getBytes()));

            String result = w3cDom.asString(doc);

            Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><div/>", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}