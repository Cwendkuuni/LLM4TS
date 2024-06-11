import org.jsoup.helper.W3CDom;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        w3cDom.convert(jsoupDoc, w3cDoc);

        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilderHead() {
        Document doc = null;
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("tag");
        int depth = 0;
        w3cBuilder.head(source, depth);
        Element dest = w3cBuilder.dest;

        Assert.assertNotNull(dest);
    }

    @Test
    public void testW3CBuilderTail() {
        Document doc = null;
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("tag");
        int depth = 0;
        w3cBuilder.head(source, depth);
        w3cBuilder.tail(source, depth);
        Element dest = w3cBuilder.dest;

        Assert.assertNull(dest);
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        Document doc = null;
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("tag");
        Element el = doc.createElement("tag");
        w3cBuilder.copyAttributes(source, el);

        Assert.assertNotNull(el);
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        Document doc = null;
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag");
        String prefix = w3cBuilder.updateNamespaces(el);

        Assert.assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document doc = null;
        String result = w3cDom.asString(doc);

        Assert.assertNotNull(result);
    }
}