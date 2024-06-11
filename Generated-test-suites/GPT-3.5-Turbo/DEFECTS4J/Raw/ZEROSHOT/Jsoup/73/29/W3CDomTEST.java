import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = new Document("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
    
    @Test
    public void testConvert() {
        Document jsoupDoc = new Document("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document convertedDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, convertedDoc);
        assertNotNull(convertedDoc);
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Text text = w3cDom.new W3CBuilder().doc.createTextNode("Hello");
        Element el = w3cDom.new W3CBuilder().doc.createElement("div");
        w3cDom.new W3CBuilder().head(text, 0);
        w3cDom.new W3CBuilder().head(el, 0);
        assertNotNull(el);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("Hello");
        Text text = w3cDom.new W3CBuilder().doc.createTextNode(sourceText.getWholeText());
        assertNotNull(text);
    }

    @Test
    public void testW3CBuilderHeadCommentNode() {
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("Test Comment");
        Comment comment = w3cDom.new W3CBuilder().doc.createComment(sourceComment.getData());
        assertNotNull(comment);
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("Test Data");
        Text node = w3cDom.new W3CBuilder().doc.createTextNode(sourceData.getWholeData());
        assertNotNull(node);
    }

    @Test
    public void testW3CBuilderTail() {
        Element el = w3cDom.new W3CBuilder().doc.createElement("div");
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        w3cDom.new W3CBuilder().dest = el;
        w3cDom.new W3CBuilder().tail(source, 0);
        assertNull(w3cDom.new W3CBuilder().dest);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        source.attr("class", "test");
        org.w3c.dom.Element el = w3cDom.new W3CBuilder().doc.createElement("div");
        w3cDom.new W3CBuilder().copyAttributes(source, el);
        assertNotNull(el.getAttribute("class"));
        assertEquals("test", el.getAttribute("class"));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("div");
        el.attr("xmlns", "http://www.example.com");
        String prefix = w3cDom.new W3CBuilder().updateNamespaces(el);
        assertNotNull(prefix);
        assertEquals("", prefix);
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = new Document("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
    }
}