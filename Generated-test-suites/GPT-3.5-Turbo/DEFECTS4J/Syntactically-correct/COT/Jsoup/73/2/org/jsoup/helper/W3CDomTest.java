import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Entity;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Notation;
import org.w3c.dom.EntityReference;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div>Hello World!</div></body></html>");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document expectedW3cDoc = builder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(expectedW3cDoc);
            org.jsoup.nodes.Element rootEl = jsoupDoc.child(0);
            NodeTraversor.traverse(w3cBuilder, rootEl);

            Assert.assertEquals(expectedW3cDoc, w3cDoc);
        } catch (ParserConfigurationException e) {
            Assert.fail("Unexpected exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testAsString() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head></head><body><div>Hello World!</div></body></html>";
        String result = w3cDom.asString(w3cDoc);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        w3cDom.fromJsoup(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupParserConfigurationException() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(null); // Triggers ParserConfigurationException
            Document doc = builder.newDocument();
            w3cDom.fromJsoup(jsoupDoc); // Throws IllegalStateException
        } catch (ParserConfigurationException e) {
            Assert.fail("Unexpected ParserConfigurationException occurred: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        w3cDom.convert(null, w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullOutput() {
        w3cDom.convert(jsoupDoc, null);
    }

    @Test
    public void testW3CBuilderHeadElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("id", "elementId");
        element.attr("class", "elementClass");
        TextNode textNode = new TextNode("Example text");
        element.appendChild(textNode);

        int depth = 0;
        w3cBuilder.head(element, depth);

        Element w3cElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("div", w3cElement.getTagName());
        Assert.assertEquals("Example text", w3cElement.getTextContent());
        Assert.assertTrue(w3cElement.getAttribute("id").equals("elementId"));
        Assert.assertTrue(w3cElement.getAttribute("class").equals("elementClass"));
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Example text");

        int depth = 0;
        w3cBuilder.head(textNode, depth);

        Element w3cElement = w3cDoc.getDocumentElement();
        Text w3cTextNode = (Text) w3cElement.getFirstChild();
        Assert.assertEquals("Example text", w3cTextNode.getData());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("Example comment");

        int depth = 0;
        w3cBuilder.head(comment, depth);

        Element w3cElement = w3cDoc.getDocumentElement();
        Comment w3cComment = (Comment) w3cElement.getFirstChild();
        Assert.assertEquals("Example comment", w3cComment.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("Example data");

        int depth = 0;
        w3cBuilder.head(dataNode, depth);

        Element w3cElement = w3cDoc.getDocumentElement();
        Text w3cDataNode = (Text) w3cElement.getFirstChild();
        Assert.assertEquals("Example data", w3cDataNode.getData());
    }

    @Test
    public void testW3CBuilderTailElement() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");

        int depth = 0;
        w3cBuilder.tail(element, depth);

        Element w3cElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("div", w3cElement.getTagName());
        Assert.assertNull(w3cElement.getNextSibling());
    }
}