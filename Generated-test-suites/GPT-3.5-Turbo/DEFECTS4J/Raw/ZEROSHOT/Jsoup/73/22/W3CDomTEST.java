import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.W3CDom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.DataNode;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoupNotNull() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testFromJsoupConversion() {
        // Add test cases here
    }
    
    @Test
    public void testConvertLocation() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String location = jsoupDoc.location();
        assertEquals(location, w3cDoc.getDocumentURI());
    }
    
    @Test
    public void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element jsoupRoot = jsoupDoc.child(0);
        Element w3cRoot = w3cDoc.getDocumentElement();
        assertEquals(jsoupRoot.tagName(), w3cRoot.getTagName());
        // Add test cases here
    }
    
    @Test
    public void testW3CBuilderHeadElement() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "container");
        sourceElement.attr("id", "myDiv");
        sourceElement.attr("data-info", "sample data");

        w3cBuilder.head(sourceElement, 0);

        Element destElement = w3cBuilder.dest;
        assertEquals(destElement.getTagName(), "div");
        assertEquals(destElement.getAttribute("class"), "container");
        assertEquals(destElement.getAttribute("id"), "myDiv");
        assertEquals(destElement.getAttribute("data-info"), "sample data");
    }
    
    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        TextNode sourceTextNode = new TextNode("Hello, world!");

        w3cBuilder.head(sourceTextNode, 0);

        Comment destTextNode = (Comment) w3cBuilder.dest.getChildNodes().item(0);
        assertEquals(destTextNode.getTextContent(), "Hello, world!");
    }
    
    @Test
    public void testW3CBuilderHeadCommentNode() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        Comment sourceCommentNode = new Comment("This is a comment");

        w3cBuilder.head(sourceCommentNode, 0);

        Comment destCommentNode = (Comment) w3cBuilder.dest.getChildNodes().item(0);
        assertEquals(destCommentNode.getData(), "This is a comment");
    }
    
    @Test
    public void testW3CBuilderHeadDataNode() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        DataNode sourceDataNode = new DataNode("This is data");

        w3cBuilder.head(sourceDataNode, 0);

        Text destTextNode = (Text) w3cBuilder.dest.getChildNodes().item(0);
        assertEquals(destTextNode.getData(), "This is data");
    }
    
    @Test
    public void testW3CBuilderTail() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        Element sourceElement = new Element("div");
        w3cBuilder.head(sourceElement, 0);
        w3cBuilder.tail(sourceElement, 0);

        assertNull(w3cBuilder.dest.getParentNode());
    }
    
    @Test
    public void testCopyAttributes() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        Element sourceElement = new Element("div");
        sourceElement.attr("class", "container");
        sourceElement.attr("id", "myDiv");
        sourceElement.attr("data-info", "sample data");
        Element destElement = new Element("div");

        w3cBuilder.copyAttributes(sourceElement, destElement);

        assertEquals(destElement.attr("class"), "container");
        assertEquals(destElement.attr("id"), "myDiv");
        assertEquals(destElement.attr("data-info"), "sample data");
    }
    
    @Test
    public void testUpdateNamespaces() {
        W3CBuilder w3cBuilder = new W3CBuilder(jsoupDoc);
        Element sourceElement = new Element("div");
        sourceElement.attr("xmlns", "http://www.w3.org/2000/svg");
        sourceElement.attr("xmlns:example", "http://www.example.com");

        String prefix = w3cBuilder.updateNamespaces(sourceElement);

        assertEquals(w3cBuilder.namespaces.size(), 2);
        assertEquals(w3cBuilder.namespaces.get(""), "http://www.w3.org/2000/svg");
        assertEquals(w3cBuilder.namespaces.get("example"), "http://www.example.com");
        assertEquals(prefix, "");
    }
    
    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        // Add test cases here
    }
}