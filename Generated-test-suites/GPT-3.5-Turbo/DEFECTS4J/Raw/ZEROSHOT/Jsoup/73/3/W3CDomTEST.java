import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Text;
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
        Document w3cDoc = null;
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("");
        int depth = 0;
        Document doc = null;
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.head(source, depth);
        // add assertion here
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("");
        int depth = 0;
        Document doc = null;
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);
        builder.tail(source, depth);
        // add assertion here
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Node source = new org.jsoup.nodes.Element("");
        Element el = null;
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        builder.copyAttributes(source, el);
        // add assertion here
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        String prefix = builder.updateNamespaces(el);
        // add assertion here
    }

    @Test
    public void testAsString() {
        Document doc = null;
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(doc);
        // add assertion here
    }
}