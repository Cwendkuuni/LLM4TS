import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element(null, "");
        jsoupDoc.appendChild(rootEl);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDoc = new Document();
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(sourceEl, 0);
        w3cBuilder.tail(sourceEl, 0);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("", "");
        Element el = new Element("", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new Document());
        w3cBuilder.copyAttributes(sourceEl, el);
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(new Document());
        String prefix = w3cBuilder.updateNamespaces(el);
        assertNotEquals("", prefix);
    }

    @Test
    public void testAsString() {
        Document doc = new Document();
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(doc);
        assertNotNull(result);
    }

}