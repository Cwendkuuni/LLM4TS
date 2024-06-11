import static org.junit.Assert.*;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom converter = new W3CDom();
        Document jsoupDocument = createJsoupDocument(); // create a jsoup document for testing

        org.w3c.dom.Document w3cDocument = converter.fromJsoup(jsoupDocument);

        assertNotNull(w3cDocument);
        assertEquals(jsoupDocument.location(), w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvert() {
        W3CDom converter = new W3CDom();
        Document jsoupDocument = createJsoupDocument(); // create a jsoup document for testing
        org.w3c.dom.Document w3cDocument = createW3CDocument();
        Element rootElement = jsoupDocument.child(0);

        converter.convert(jsoupDocument, w3cDocument);

        assertEquals(rootElement.tagName(), w3cDocument.getDocumentElement().getTagName());
        assertEquals(rootElement.child(0).tagName(), w3cDocument.getDocumentElement().getChildNodes().item(0).getNodeName());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder builder = createW3CBuilder();
        org.jsoup.nodes.Node sourceElement = createSourceNode();

        builder.head(sourceElement, 0);

        Node w3cElement = builder.doc.getDocumentElement();
        assertEquals(sourceElement.child(0).tagName(), w3cElement.getChildNodes().item(0).getNodeName());

        // test with TextNode
        org.jsoup.nodes.Node sourceTextNode = createSourceTextNode();
        builder.head(sourceTextNode, 1);

        Node w3cTextNode = w3cElement.getChildNodes().item(1);
        assertEquals(sourceTextNode.outerHtml(), w3cTextNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder builder = createW3CBuilder();
        org.jsoup.nodes.Node sourceElement = createSourceNode();

        builder.head(sourceElement, 0);
        builder.tail(sourceElement, 0);

        assertNull(builder.dest.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = createW3CBuilder();
        org.jsoup.nodes.Node sourceElement = createSourceNode();
        Element w3cElement = createW3CElement();
  
        builder.copyAttributes(sourceElement, w3cElement);

        assertEquals(sourceElement.attributes().size(), w3cElement.getAttributes().getLength());
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = createW3CBuilder();
        org.jsoup.nodes.Element sourceElement = createSourceElement();

        String prefix = builder.updateNamespaces(sourceElement);
        
        assertNotNull(prefix);
        assertEquals(sourceElement.tagName().substring(0, sourceElement.tagName().indexOf(":")), prefix);
    }

    @Test
    public void testAsString() {
        W3CDom converter = new W3CDom();
        org.w3c.dom.Document w3cDocument = createW3CDocument();

        String xmlString = converter.asString(w3cDocument);

        assertNotNull(xmlString);
        assertTrue(xmlString.contains(w3cDocument.getDocumentElement().getTagName()));
    }

    // Helper methods to create test data

    private Document createJsoupDocument() {
        Document doc = new Document("");
        Element root = new Element("#root");
        Element element = new Element("div");
        element.attr("id", "test");
        element.text("Hello World");
        root.appendChild(element);
        doc.appendChild(root);
        return doc;
    }

    private org.w3c.dom.Document createW3CDocument() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private W3CDom.W3CBuilder createW3CBuilder() {
        Document w3cDocument = createW3CDocument();
        return new W3CDom.W3CBuilder(w3cDocument);
    }

    private org.jsoup.nodes.Node createSourceNode() {
        Document doc = createJsoupDocument();
        return doc.child(0).child(0);
    }

    private org.jsoup.nodes.TextNode createSourceTextNode() {
        Document doc = createJsoupDocument();
        return (org.jsoup.nodes.TextNode) doc.child(0).childNode(0);
    }

    private org.jsoup.nodes.Element createSourceElement() {
        Document doc = createJsoupDocument();
        return doc.child(0).child(0);
    }

    private Element createW3CElement() {
        org.w3c.dom.Document w3cDocument = createW3CDocument();
        return w3cDocument.createElement("test");
    }
}