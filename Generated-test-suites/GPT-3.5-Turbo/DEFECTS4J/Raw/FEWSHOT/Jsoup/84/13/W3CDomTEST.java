
package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class W3CDomTest {
    private static final String HTML = "<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>";

    @Test
    public void testFromJsoup() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        
        // Convert to W3C Document
        W3CDom domConverter = new W3CDom();
        Document out = domConverter.fromJsoup(in);
        
        // Validate output
        Assert.assertNotNull(out);
        Assert.assertEquals("html", out.getDocumentElement().getTagName());
    }

    @Test
    public void testConvert() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        
        // Initialize W3C Document
        Document out = createW3CDocument();
        
        // Convert jsoup document to W3C Document
        W3CDom domConverter = new W3CDom();
        domConverter.convert(in, out);
        
        // Validate output
        Assert.assertEquals(1, out.getElementsByTagName("html").getLength());
        Assert.assertEquals(1, out.getElementsByTagName("head").getLength());
        Assert.assertEquals(1, out.getElementsByTagName("title").getLength());
        Assert.assertEquals(1, out.getElementsByTagName("body").getLength());
        Assert.assertEquals(1, out.getElementsByTagName("h1").getLength());
        Assert.assertEquals("Test", out.getElementsByTagName("title").item(0).getTextContent());
        Assert.assertEquals("Hello world!", out.getElementsByTagName("h1").item(0).getTextContent());
    }

    @Test
    public void testW3CBuilderHead() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.Element sourceEl = in.select("head").first();

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element dest = out.createElement("head");
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = dest;
        
        // Call head method
        builder.head(sourceEl, 0);
        
        // Validate output
        Assert.assertEquals(1, out.getElementsByTagName("head").getLength());
        Assert.assertTrue(out.getElementsByTagName("head").item(0) instanceof Element);
        Assert.assertEquals(out.getElementsByTagName("head").item(0), dest);
    }

    @Test
    public void testW3CBuilderTail() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.Element sourceEl = in.select("head").first();

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element dest = out.createElement("head");
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = dest;
        
        // Call tail method
        builder.tail(sourceEl, 0);
        
        // Validate output
        Assert.assertNull(builder.dest);
    }

    @Test
    public void testW3CBuilderHead_Element() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.Element sourceEl = in.select("title").first();

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element head = out.createElement("head");
        Element dest = out.createElement("title");
        head.appendChild(dest);
        out.appendChild(head);
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = dest;
        
        // Call head method
        builder.head(sourceEl, 0);
        
        // Validate output
        Assert.assertEquals(1, out.getElementsByTagName("title").getLength());
        Assert.assertTrue(out.getElementsByTagName("title").item(0) instanceof Element);
        Assert.assertEquals(out.getElementsByTagName("title").item(0), dest);
    }

    @Test
    public void testW3CBuilderHead_TextNode() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.TextNode sourceText = (org.jsoup.nodes.TextNode) in.select("h1").first().childNode(0);

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element body = out.createElement("body");
        Element h1 = out.createElement("h1");
        Text destText = out.createTextNode("");
        h1.appendChild(destText);
        body.appendChild(h1);
        out.appendChild(body);
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = destText;
        
        // Call head method
        builder.head(sourceText, 0);
        
        // Validate output
        Assert.assertEquals("Hello world!", destText.getData());
    }

    @Test
    public void testW3CBuilderHead_Comment() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.Comment sourceComment = (org.jsoup.nodes.Comment) in.select("h1").first().childNode(0);

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element body = out.createElement("body");
        Element h1 = out.createElement("h1");
        Comment destComment = out.createComment("");
        h1.appendChild(destComment);
        body.appendChild(h1);
        out.appendChild(body);
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = destComment;
        
        // Call head method
        builder.head(sourceComment, 0);
        
        // Validate output
        Assert.assertEquals("Hello world!", destComment.getData());
    }

    @Test
    public void testW3CBuilderHead_DataNode() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.DataNode sourceData = (org.jsoup.nodes.DataNode) in.select("h1").first().childNode(0);

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element body = out.createElement("body");
        Element h1 = out.createElement("h1");
        Text destData = out.createTextNode("");
        h1.appendChild(destData);
        body.appendChild(h1);
        out.appendChild(body);
        
        // Initialize W3CBuilder
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.dest = destData;
        
        // Call head method
        builder.head(sourceData, 0);
        
        // Validate output
        Assert.assertEquals("Hello world!", destData.getData());
    }

    @Test
    public void testCopyAttributes() {
        // Initialize input
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse(HTML);
        org.jsoup.nodes.Element sourceEl = in.select("h1").first();

        // Initialize W3C Document
        Document out = createW3CDocument();
        Element dest = out.createElement("h1");
        
        // Call copyAttributes method
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        builder.copyAttributes(sourceEl, dest);
        
        // Validate output
        Assert.assertEquals(0, dest.getAttributes().getLength());
    }

    @Test
    public void testUpdateNamespaces() {
        // Initialize input
        org.jsoup.nodes.Element el = org.jsoup.Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>").select("html").first();

        // Initialize W3C Document
        Document out = createW3CDocument();
        
        // Call updateNamespaces method
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(out);
        String prefix = builder.updateNamespaces(el);
        
        // Validate output
        Assert.assertNull(prefix);
    }

    @Test
    public void testAsString() {
        // Initialize W3C Document
        Document doc = createW3CDocument();
        Element html = doc.createElement("html");
        Element head = doc.createElement("head");
        Element title = doc.createElement("title");
        Text titleText = doc.createTextNode("Test");
        Element body = doc.createElement("body");
        Element h1 = doc.createElement("h1");
        Text h1Text = doc.createTextNode("Hello world!");
        
        doc.appendChild(html);
        html.appendChild(head);
        head.appendChild(title);
        title.appendChild(titleText);
        html.appendChild(body);
        body.appendChild(h1);
        h1.appendChild(h1Text);
        
        // Convert W3C Document to String
        W3CDom domConverter = new W3CDom();
        String result = domConverter.asString(doc);
        
        // Validate output
        Assert.assertEquals("<html><head><title>Test</title></head><body><h1>Hello world!</h1></body></html>", result);
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
