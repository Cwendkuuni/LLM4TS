import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private org.w3c.dom.Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html></html>");
        w3cDoc = createW3CDocument();
    }

    private org.w3c.dom.Document createW3CDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document result = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(result);
        Assert.assertEquals(w3cDoc.getImplementation(), result.getImplementation());
        Assert.assertEquals(w3cDoc.getDocumentURI(), result.getDocumentURI());
    }

    @Test
    public void testConvert() {
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertNotNull(rootElement);
        Assert.assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testW3CBuilder() {
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(w3cDoc);
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        Element dest = w3cDoc.createElementNS("", "div");
        w3cBuilder.dest = dest;

        // Test head method
        w3cBuilder.head(sourceEl, 0);
        Element element = (Element) w3cBuilder.dest.getFirstChild();
        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.getTagName());

        // Test tail method
        w3cBuilder.tail(sourceEl, 0);
        Assert.assertNull(w3cBuilder.dest.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        Element dest = w3cDoc.createElementNS("", "div");
        w3cDom.copyAttributes(sourceEl, dest);
        Assert.assertEquals(sourceEl.attributes().size(), dest.getAttributes().getLength());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("div"), "");
        String result = w3cDom.updateNamespaces(el);
        Assert.assertEquals("", result);
    }

    @Test
    public void testAsString() {
        String result = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(result);
    }
}