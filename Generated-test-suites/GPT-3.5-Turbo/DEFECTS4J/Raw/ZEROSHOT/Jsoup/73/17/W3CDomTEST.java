import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.helper.W3CDom;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        Document out = new W3CDom().fromJsoup(in);

        assertNotNull(out);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = new org.jsoup.nodes.Document("");
        Document out = new W3CDom().fromJsoup(in);

        assertNotNull(out);

        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(in, out);

        // Check if the conversion was successful
        assertEquals(in.location(), out.getDocumentURI());

        Element rootElIn = in.child(0);
        Element rootElOut = out.getDocumentElement();
        assertEquals(rootElIn.tagName(), rootElOut.getTagName());

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(rootElIn, 0);
        w3cBuilder.tail(rootElIn, 0);

        // Check if the conversion handles Element correctly
        assertEquals(rootElOut, w3cBuilder.dest);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("div");
        element.attr("id", "example");
        element.attr("data-value", "123");

        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element el = doc.createElement("div");

            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(doc);
            w3cBuilder.copyAttributes(element, el);

            // Check if the attributes were correctly copied
            assertEquals("example", el.getAttribute("id"));
            assertEquals("123", el.getAttribute("data-value"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element element = new org.jsoup.nodes.Element("x:div");
        element.attr("xmlns", "http://www.example.com");
        element.attr("xmlns:prefix", "http://www.example.com/prefix");

        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        String prefix = w3cBuilder.updateNamespaces(element);

        // Check if the prefixes and namespaces were correctly updated
        assertEquals("", prefix);
        assertEquals("http://www.example.com", w3cBuilder.namespaces.get(""));
        assertEquals("http://www.example.com/prefix", w3cBuilder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("root");
            TextNode text = doc.createTextNode("This is a test");
            doc.appendChild(root);
            root.appendChild(text);

            String output = new W3CDom().asString(doc);

            // Check if the document was correctly serialized to a string
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root>This is a test</root>", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}