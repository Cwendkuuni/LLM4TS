import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.DataNode;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        in.appendElement("div");
        in.appendText("Hello World!");

        Document out = w3CDom.fromJsoup(in);
        assertNotNull(out);
        Element root = out.getDocumentElement();
        assertEquals("div", root.getTagName());
        assertEquals("Hello World!", root.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element div = in.appendElement("div");
        div.appendText("Hello World!");

        Document out = w3CDom.convert(in);
        assertNotNull(out);
        Element root = out.getDocumentElement();
        assertEquals("div", root.getTagName());
        assertEquals("Hello World!", root.getTextContent());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("div");
        org.jsoup.nodes.TextNode textNode = new org.jsoup.nodes.TextNode("Hello World!");
        org.jsoup.nodes.Comment comment = new org.jsoup.nodes.Comment("This is a comment");
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("data");

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        w3CBuilder.head(source, 0);
        w3CBuilder.head(textNode, 1);
        w3CBuilder.head(comment, 2);
        w3CBuilder.head(dataNode, 3);
        w3CBuilder.tail(source, 0);

        org.w3c.dom.Node dest = w3CBuilder.getDest();

        assertNotNull(dest);
        assertEquals("div", dest.getNodeName());
        assertEquals("Hello World!", dest.getTextContent());

        org.w3c.dom.Node parent = dest.getParentNode();
        assertNull(parent);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("div");
        source.attr("attr1", "value1");
        source.attr("attr2", "value2");

        org.w3c.dom.Element el = null;
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        w3CBuilder.copyAttributes(source, el);

        assertNotNull(el);
        assertEquals("value1", el.getAttribute("attr1"));
        assertEquals("value2", el.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns", "namespace1");
        el.attr("xmlns:prefix", "namespace2");

        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(null);
        String prefix = w3CBuilder.updateNamespaces(el);

        assertEquals("prefix", prefix);
        assertTrue(w3CBuilder.getNamespaces().containsKey(""));
        assertTrue(w3CBuilder.getNamespaces().containsKey("prefix"));
        assertEquals("namespace1", w3CBuilder.getNamespaces().get(""));
        assertEquals("namespace2", w3CBuilder.getNamespaces().get("prefix"));
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document doc = null;
        String result = w3CDom.asString(doc);

        assertNotNull(result);
        assertEquals("", result);
    }

}