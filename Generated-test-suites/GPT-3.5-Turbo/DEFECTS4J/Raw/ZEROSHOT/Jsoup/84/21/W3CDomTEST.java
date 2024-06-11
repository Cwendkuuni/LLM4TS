import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document outputW3CDoc = w3cDom.convert(w3cDoc, new Document());

        assertNotNull(outputW3CDoc);
    }

    @Test
    public void testW3CBuilderHead() {
        Document doc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Node sourceNode = new org.jsoup.nodes.Element("div");

        w3cBuilder.head(sourceNode, 0);

        Element destElement = doc.getDocumentElement();
        assertNotNull(destElement);
        assertEquals("div", destElement.getTagName());
    }

    @Test
    public void testW3CBuilderTail() {
        Document doc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Node sourceNode = new org.jsoup.nodes.Element("div");

        w3cBuilder.head(sourceNode, 0);
        w3cBuilder.tail(sourceNode, 0);

        Element destElement = doc.getDocumentElement();
        assertNull(destElement);
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        Document doc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Node sourceNode = new org.jsoup.nodes.Element("div");
        sourceNode.attr("class", "test");

        Element destElement = doc.createElementNS("", "div");
        w3cBuilder.copyAttributes(sourceNode, destElement);

        String attributeValue = destElement.getAttribute("class");
        assertEquals("test", attributeValue);
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        Document doc = new Document();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
        org.jsoup.nodes.Element sourceElement = new org.jsoup.nodes.Element("div");
        sourceElement.attr("xmlns", "http://www.w3.org/2000/svg");

        String namespacePrefix = w3cBuilder.updateNamespaces(sourceElement);

        assertEquals("div", namespacePrefix);
        assertTrue(w3cBuilder.namespacesStack.peek().containsKey(""));
        assertEquals("http://www.w3.org/2000/svg", w3cBuilder.namespacesStack.peek().get(""));
    }

    @Test
    public void testAsString() {
        Document doc = new Document();
        Element element = doc.createElementNS("", "div");
        doc.appendChild(element);

        String output = w3cDom.asString(doc);

        assertNotNull(output);
        assertTrue(output.contains("<div></div>"));
    }
}