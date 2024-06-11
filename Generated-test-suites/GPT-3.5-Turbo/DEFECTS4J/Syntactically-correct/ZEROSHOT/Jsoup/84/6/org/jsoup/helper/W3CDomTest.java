import org.jsoup.nodes.*;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        
        // Create a jsoup document for testing
        Document doc = new Document("");
        Element root = new Element("root");
        Element child = new Element("child");
        child.appendChild(new Text("Hello World"));
        root.appendChild(child);
        doc.appendChild(root);
        
        // Convert jsoup document to W3C document
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        jsoupDoc.appendChild(root);
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        
        // Verify the conversion
        Assert.assertEquals("root", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals("Hello World", w3cDoc.getElementsByTagName("child").item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder();
        
        // Create a jsoup document for testing
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Element root = new Element("root");
        Element child = new Element("child");
        child.appendChild(new Text("Hello World"));
        root.appendChild(child);
        jsoupDoc.appendChild(root);
        
        // Create a W3C document for testing
        Document w3cDoc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            w3cDoc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        
        builder.convert(jsoupDoc, w3cDoc);
        
        // Verify the conversion
        Assert.assertEquals("root", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals("Hello World", w3cDoc.getElementsByTagName("child").item(0).getTextContent());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder();
        
        // Create a jsoup element for testing
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tag");
        jsoupEl.attr("attr1", "value1");
        jsoupEl.attr("attr2", "value2");
        
        // Create a W3C element for testing
        Document w3cDoc;
        Element w3cEl;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            w3cDoc = docBuilder.newDocument();
            w3cEl = w3cDoc.createElement("tag");
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        
        builder.copyAttributes(jsoupEl, w3cEl);
        
        // Verify the attributes copy
        Assert.assertEquals("value1", w3cEl.getAttribute("attr1"));
        Assert.assertEquals("value2", w3cEl.getAttribute("attr2"));
        Assert.assertEquals("", w3cEl.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder();
        
        // Create a jsoup element for testing
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("tag");
        jsoupEl.attr("xmlns", "http://www.example.com/xmlns");
        jsoupEl.attr("xmlns:prefix", "http://www.prefix.com/xmlns");
        
        // Create a W3C element for testing
        Document w3cDoc;
        Element w3cEl;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            w3cDoc = docBuilder.newDocument();
            w3cEl = w3cDoc.createElement("tag");
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        
        String prefix = builder.updateNamespaces(jsoupEl);
        
        // Verify the namespace update
        Assert.assertEquals("http://www.prefix.com/xmlns", builder.namespacesStack.peek().get("prefix"));
        Assert.assertEquals("prefix", prefix);
    }

    @Test
    public void testAsString() {
        W3CDom w3CDom = new W3CDom();

        // Create a W3C document for testing
        Document w3cDoc;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            w3cDoc = docBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }

        // Serialize the W3C document to a string
        String result = w3CDom.asString(w3cDoc);

        // Verify the string serialization
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<!DOCTYPE null>\n", result);
    }
}